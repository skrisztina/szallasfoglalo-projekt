package com.example.szallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Szallasok extends AppCompatActivity implements SzallasItemAdapter.OnButtonClickListener{
    private static final String LOG_TAG = Szallasok.class.getName();

    private FirebaseUser user;

    private RecyclerView recyclerview;
    private ArrayList<SzallasItem> szallasok;
    private SzallasItemAdapter adapter;

    private final int gridNumber = 1;

    private FirebaseFirestore mfirestore;
    private CollectionReference collectionref;

    private Spinner helyspinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szallasok);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Log.i(LOG_TAG, "Regisztrált felhasználó.");
        } else {
            Log.i(LOG_TAG, "Nem regisztrált felhasználó.");
            finish();
        }

        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new GridLayoutManager(this, gridNumber));
        szallasok = new ArrayList<>();
        adapter = new SzallasItemAdapter(this, szallasok);
        recyclerview.setAdapter(adapter);
        adapter.setOnButtonClickListener(this);

        mfirestore = FirebaseFirestore.getInstance();
        collectionref = mfirestore.collection("Szallasok");


        helyspinner = (Spinner) findViewById(R.id.helySpinner);
        List<String> helyek = new ArrayList<>();
        ArrayAdapter<String> helyadapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, helyek);
        helyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        helyspinner.setAdapter(helyadapter);
        helyek.add("Válassz várost!");

        collectionref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String hely = document.getString("place");
                        helyek.add(hely);
                    }
                    helyadapter.notifyDataSetChanged();
                }
            }
        });

        helyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedhely = parent.getItemAtPosition(position).toString();
                if(!selectedhely.equals("Válassz várost!")){
                    if(adapter != null){
                        adapter.getFilter().filter(selectedhely);
                        Log.i(LOG_TAG, "Szűrés: " + selectedhely);
                    }
                } else {
                    if(adapter!= null){
                        adapter.getFilter().filter("");
                        Log.i(LOG_TAG, "Szűrés: " + "Válassz várost!");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        queryData();
    }

    public void onButtonClick(SzallasItem clickedSzallas){
        String userEmail = user.getEmail();
        Log.i(LOG_TAG, "Email: " + userEmail);
        String clickedName = clickedSzallas.getName();

        if(user == null || user.isAnonymous()){
            Toast.makeText(Szallasok.this, "Ehhez a funkcióhoz regisztrálni kell!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Szallasok.this, SzallasFoglalasActivity.class);
            intent.putExtra("szallas", clickedName);
            startActivity(intent);
        }

    }

    public void queryData(){
        szallasok.clear();

        collectionref.orderBy("foglalasDb", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {

            for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                SzallasItem szallas = document.toObject(SzallasItem.class);
                szallasok.add(szallas);
            }

            if(szallasok.size() == 0){
                initializeData();
                queryData();
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {

        String[] szallasList = getResources().getStringArray(R.array.szallas_names);
        String[] szallasPlace = getResources().getStringArray(R.array.szallas_places);
        String[] szallasInfo = getResources().getStringArray(R.array.szallas_info);
        String[] szallasPrice = getResources().getStringArray(R.array.szallas_price);
        TypedArray szallasImageRes = getResources().obtainTypedArray(R.array.szallas_images);
        TypedArray szallasRating = getResources().obtainTypedArray(R.array.szallas_rate);

        szallasok.clear();

        for(int j = 0; j < szallasList.length; j++){
            collectionref.add(new SzallasItem(szallasList[j], szallasPlace[j], szallasInfo[j], szallasPrice[j], szallasRating.getFloat(j, 0), szallasImageRes.getResourceId(j, 0), 0));
        }

        szallasImageRes.recycle();

    }

    public void logout(View view) {
        Toast.makeText(Szallasok.this, "Kijelentkezés...", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void startProfil(View view) {
        if(user == null || user.isAnonymous()){
            Toast.makeText(Szallasok.this, "Ehhez a funkcióhoz regisztrálni kell!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Szallasok.this,ProfilActivity.class);
            startActivity(intent);
        }
    }

    public void startFoglaltak(View view) {
        if(user == null || user.isAnonymous()){
            Toast.makeText(Szallasok.this, "Figyelem! Regisztáció nélkül nem tudod megtekinteni a foglalásaidat!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Szallasok.this, FoglalasokActivity.class);
            startActivity(intent);
        }
    }

}