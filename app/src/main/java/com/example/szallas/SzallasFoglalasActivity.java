package com.example.szallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SzallasFoglalasActivity extends AppCompatActivity {
    private static final String LOG_TAG = SzallasFoglalasActivity.class.getName();

    private String szallasId;
    private String szallasName;
    private String userEmail;
    private FirebaseUser user;
    private FirebaseFirestore mfirestore;
    private CollectionReference szallasokcolref;
    private CollectionReference foglalasokcolref;

    private EditText kezdoDatum;
    private EditText vegDatum;
    private SzallasItem szallas;
    private TextView foglName;
    private ImageView foglImage;
    private TextView foglHely;
    private RatingBar foglRating;
    private TextView foglPrice;
    private TextView foglInfo;
    private Button foglBtn;

    private NotificationHandler notifhandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szallas_foglalas);
        szallasName = getIntent().getStringExtra("szallas");
        mfirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();
        szallasokcolref = mfirestore.collection("Szallasok");
        foglalasokcolref = mfirestore.collection("Foglalasok");

        kezdoDatum = findViewById(R.id.foglKezdet);
        vegDatum = findViewById(R.id.foglVeg);
        foglName = findViewById(R.id.foglName);
        foglHely = findViewById(R.id.foglHely);
        foglPrice = findViewById(R.id.foglPrice);
        foglInfo = findViewById(R.id.foglInfo);
        foglImage = findViewById(R.id.foglImage);
        foglRating = findViewById(R.id.foglRating);
        foglBtn = findViewById(R.id.foglBtn);

        notifhandler = new NotificationHandler(this);

        if(!userEmail.equals("")){
            foglalasokcolref.whereEqualTo("foglaloEmail", userEmail).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                                if(document.getString("szallasName").equals(szallasName)){
                                    foglBtn.setEnabled(false);
                                    foglBtn.setText("Foglalva!");
                                }
                            }
                        }
                    });
        }

        szallasokcolref.whereEqualTo("name", szallasName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            szallasId = document.getId();
                            szallas = document.toObject(SzallasItem.class);
                            foglName.setText(szallas.getName());
                            foglHely.setText(szallas.getPlace());
                            foglPrice.setText(szallas.getPrice());
                            foglInfo.setText(szallas.getInfo());
                            foglRating.setRating(szallas.getRating());
                            Context context = SzallasFoglalasActivity.this;
                            Glide.with(context).load(szallas.getImageRes()).into(foglImage);
                        }
                    }
                });
    }

    public void vissza(View view) {
        finish();
    }

    public void foglalas(View view) {
        String kezdodatum = kezdoDatum.getText().toString();
        String vegdatum = vegDatum.getText().toString();

        if(userEmail.equals("")){
            Toast.makeText(this, "A foglaláshoz regisztrálnod kell!", Toast.LENGTH_LONG).show();
        } else {

            if(kezdodatum.equals("") || vegdatum.equals("")){
                Toast.makeText(this, "Add meg a kezdő és végdátumot!", Toast.LENGTH_LONG).show();
            } else {
                Foglalas foglalas = new Foglalas(userEmail, szallas.getName(), szallas.getPlace(), szallas.getInfo(), szallas.getPrice(), szallas.getRating(), szallas.getImageRes(), kezdodatum, vegdatum);

                foglalasokcolref.add(foglalas).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SzallasFoglalasActivity.this, "Sikeres foglalás!", Toast.LENGTH_LONG).show();
                        notifhandler.send("Sikeresen lefoglaltad a " + szallas.getName() + "-t!");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SzallasFoglalasActivity.this, "A foglalás nem volt sikeres!", Toast.LENGTH_LONG).show();
                    }
                });

                int fogldb = szallas.getFoglalasDb()+1;
                szallasokcolref.document(szallasId).update("foglalasDb", fogldb).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(LOG_TAG, "Szallas FoglalasDb++ sikeres");
                    }
                });
            }
        }

    }
}