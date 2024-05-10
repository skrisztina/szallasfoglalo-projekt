package com.example.szallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FoglalasokActivity extends AppCompatActivity implements FoglalasAdapter.OnButtonClickListener{

    private static final String LOG_TAG = FoglalasokActivity.class.getName();
    private FirebaseUser user;
    private String userEmail;
    private FirebaseFirestore mfirestore;
    private CollectionReference foglalasokcolref;
    private RecyclerView recyclerView;
    private ArrayList<Foglalas> foglalasok;
    private FoglalasAdapter adapter;

    private final int gridNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foglalasok);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.i(LOG_TAG, "Regisztrált felhasználó: " + user.getEmail());
        } else {
            Log.i(LOG_TAG, "Nem regisztrált felhasználó.");
            finish();
        }
        userEmail = user.getEmail();
        mfirestore = FirebaseFirestore.getInstance();
        foglalasokcolref= mfirestore.collection("Foglalasok");
        recyclerView = findViewById(R.id.foglalasokRV);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNum));
        foglalasok = new ArrayList<>();

        foglalasokcolref.whereEqualTo("foglaloEmail", userEmail).limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Foglalas foglalas = document.toObject(Foglalas.class);
                        foglalasok.add(foglalas);
                        Log.i(LOG_TAG, "Foglalasok +" + foglalas.getSzallasName());
                    }
                    if(foglalasok.size() == 0){
                        Toast.makeText(FoglalasokActivity.this, "Még nincs foglalásod!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        adapter = new FoglalasAdapter(FoglalasokActivity.this, foglalasok);
                        adapter.setOnButtonClickListener(FoglalasokActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Log.e(LOG_TAG, "Hiba a foglalások betöltésekor: " + task.getException());
                }
            }
        });

    }

    public void logout(View view) {
        finish();
    }

    public void onButtonClick(Foglalas clickedFoglalas){
        String clickedname = clickedFoglalas.getSzallasName();
        foglalasokcolref.whereEqualTo("foglaloEmail", user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    String szal = (String) document.getData().get("szallasName");
                    if(szal.equals(clickedname)){
                        document.getReference().delete();
                        Log.i(LOG_TAG, clickedname + "foglalasa torlodott");
                        finish();
                    }
                }
            }
        });
    }

}