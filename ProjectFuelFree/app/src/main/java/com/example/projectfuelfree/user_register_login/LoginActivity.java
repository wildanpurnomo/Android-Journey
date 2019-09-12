package com.example.projectfuelfree.user_register_login;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private Button loginButtonProceed;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();

        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_login);
        setTitle("Login");
        setTitleColor(Color.parseColor("#003663"));

        loginEmailEditText = findViewById(R.id.ETloginEmail);
        loginPasswordEditText = findViewById(R.id.ETloginPassword);
        loginButtonProceed = findViewById(R.id.BTNloginProceed);

        loginButtonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instEmail = loginEmailEditText.getText().toString();
                String instPassword = loginPasswordEditText.getText().toString();

                loginUser(instEmail, instPassword);
            }
        });

    }

    private void loginUser(String instEmail, String instPassword) {
        if(instEmail.isEmpty()){
            loginEmailEditText.setError("Mohon tuliskan Email");
            loginEmailEditText.requestFocus();
        }

        else if(instPassword.isEmpty()){
            loginPasswordEditText.setError("Mohon tuliskan password");
            loginPasswordEditText.requestFocus();
        }

        else if(!(instEmail.isEmpty() && instPassword.isEmpty())){
            firebaseAuth.signInWithEmailAndPassword(instEmail, instPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Intent i = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(i);
                    }
                }
            });
        }

        else{
            Toast.makeText(this, "Kesalahan terjadi!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("User Auth", "is Null");
        }

        else{
            Intent i = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(i);
            finish();

            Log.d("User Auth", "not Null");
        }
    }

}
