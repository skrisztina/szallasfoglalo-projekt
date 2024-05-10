package com.example.szallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfilActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProfilActivity.class.getName();

    private EditText profilName;
    private EditText profilEmail;
    private EditText profilPhone;
    private EditText profilHome;

    private FirebaseUser user;
    private FirebaseFirestore mfirestore;
    private CollectionReference userscolref;
    private CollectionReference foglalasokref;

    private String userEmail;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mfirestore = FirebaseFirestore.getInstance();
        userscolref = mfirestore.collection("Users");
        foglalasokref = mfirestore.collection("Foglalasok");

        profilName = findViewById(R.id.profilName);
        profilEmail = findViewById(R.id.profilEmail);
        profilPhone = findViewById(R.id.profilPhone);
        profilHome = findViewById(R.id.profilHome);

        userEmail = user.getEmail();

        userscolref.whereEqualTo("email", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        id = (String) document.getId();
                        String name = (String) document.getData().get("nev");
                        String email = (String) document.getData().get("email");
                        String home = (String) document.getData().get("lakcim");
                        String phone = (String) document.getData().get("phone_num");

                        profilName.setText(name);
                        profilEmail.setText(email);
                        profilHome.setText(home);
                        profilPhone.setText(phone);

                        Animation slide_up = AnimationUtils.loadAnimation(ProfilActivity.this, R.anim.slide_up);

                        profilName.startAnimation(slide_up);
                        profilEmail.startAnimation(slide_up);
                        profilHome.startAnimation(slide_up);
                        profilPhone.startAnimation(slide_up);
                    }
                }
            }
        });

    }

    public void back(View view) {
        finish();
    }

    public void modositas(View view) {
        String name = profilName.getText().toString();
        String email = profilEmail.getText().toString();
        String home = profilHome.getText().toString();
        String phone = profilPhone.getText().toString();


        if(name.equals("") || email.equals("") || home.equals("") || phone.equals("")){
            Toast.makeText(ProfilActivity.this, "Nem maradhat mező üresen!", Toast.LENGTH_LONG).show();
        } else {
            userscolref.document(id).update("email", email, "lakcim", home, "nev", name, "phone_num", phone)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ProfilActivity.this, "Sikeres módosítás!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilActivity.this, "A módosítás nem volt sikeres.", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void torles(View view) {
        foglalasokref.whereEqualTo("foglaloEmail", user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    document.getReference().delete();
                }
            }
        });
        userscolref.whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    document.getReference().delete();
                }
            }
        });


        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfilActivity.this, "Felhasználó sikeresen törölve!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(LOG_TAG, "Nem sikerült a felhasználó törlése.");
                }
            }
        });
    }
}