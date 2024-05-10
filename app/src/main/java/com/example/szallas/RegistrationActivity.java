package com.example.szallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF = RegistrationActivity.class.getPackage().toString();
    private static final int KEY = 64;

    EditText nameET;
    EditText emailET;
    EditText passwdET;
    EditText passwdagainET;
    EditText phoneET;
    EditText homeET;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CollectionReference collectionref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwdET = findViewById(R.id.passwdET);
        passwdagainET = findViewById(R.id.passwdagainET);
        phoneET = findViewById(R.id.phoneET);
        homeET = findViewById(R.id.homeET);

        int key = getIntent().getIntExtra("KEY", 0);

       if(key != 64){
            finish();
       }

       auth = FirebaseAuth.getInstance();
       firestore = FirebaseFirestore.getInstance();
       collectionref = firestore.collection("Users");
    }

    public void vissza(View view) {
        finish();
    }

    public void regisztracio(View view) {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();
        String passwd = passwdET.getText().toString();
        String passwd_conf = passwdagainET.getText().toString();
        String phone = phoneET.getText().toString();
        String home = homeET.getText().toString();


        if(!passwd.equals(passwd_conf)){
            Toast.makeText(RegistrationActivity.this, "A jelszó megerősítése nem volt sikeres.", Toast.LENGTH_LONG).show();
        }

        if(name.equals("") || email.equals("") || phone.equals("") || home.equals("") || passwd.equals("")){
            Toast.makeText(RegistrationActivity.this, "Üres mező!", Toast.LENGTH_LONG).show();
        }

        Log.i(LOG_TAG, "Regisztrált: " + name + ", e-mail: " + email);

        auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG, "A regisztráció sikeres volt!");
                    collectionref.add(new User(name,email,phone,home));
                    startSzallasok();
                } else {
                    Log.i(LOG_TAG, "A regisztráció sikertelen volt.");
                    Toast.makeText(RegistrationActivity.this, "A regisztráció nem volt sikeres: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void startSzallasok(){
        Intent intent = new Intent(this, Szallasok.class);
        startActivity(intent);
    }

}