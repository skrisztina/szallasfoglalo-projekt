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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int KEY = 64;

    private FirebaseAuth auth;

    EditText userEmail;
    EditText passwdEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.UserEmail);
        passwdEt = findViewById(R.id.PasswordEt);

    }

    public void bejelentkezes(View view) {
        String email = userEmail.getText().toString();
        String password = passwdEt.getText().toString();

        //logolás:
        Log.i(LOG_TAG, "Bejelentkezett: " + email + ", jelszó: " + password);

        if(email.equals("") || password.equals("")){
            Toast.makeText(MainActivity.this, "A mezők kitöltése kötelező!", Toast.LENGTH_LONG).show();
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sikeres bejelentkezés! ", Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "Sikeres bejelentkezés");
                    startSzallasok();
                } else {
                    Log.i(LOG_TAG, "A bejelentkezés nem volt sikeres.");
                    Toast.makeText(MainActivity.this, "A bejelentkezés nem volt sikeres: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void regisztracio(View view) {
        Intent i = new Intent(this, RegistrationActivity.class);
        i.putExtra("KEY", KEY);
        startActivity(i);
    }

    public void bejelentkezes_guest(View view) {
        auth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG, "Sikeres anonim bejelentkezés");
                    startSzallasok();
                } else {
                    Log.i(LOG_TAG, "Az anonim bejelentkezés nem volt sikeres");
                    Toast.makeText(MainActivity.this, "Az anonim bejelentkezés nem volt sikeres: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void startSzallasok(){
        Intent intent = new Intent(this, Szallasok.class);
        startActivity(intent);
    }
}