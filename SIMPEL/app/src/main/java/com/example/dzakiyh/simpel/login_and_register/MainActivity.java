package com.example.dzakiyh.simpel.login_and_register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dzakiyh.simpel.HomeActivity;
import com.example.dzakiyh.simpel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private TextView toRegisterPageTextView;
    private Button loginProceed;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Hello from onCreate", "Created Main Activity!");

        firebaseAuth = FirebaseAuth.getInstance();

        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        initComponents();

        loginProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instEmail = loginEmail.getText().toString();
                String instPassword = loginPassword.getText().toString();

                loginUser(instEmail, instPassword);
            }
        });

        toRegisterPageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterPageIntent = new Intent(MainActivity.this, DaftarActivity.class);
                startActivity(toRegisterPageIntent);
            }
        });
    }

    private void loginUser(String instEmail, String instPassword) {
        if(instEmail.isEmpty()){
            loginEmail.setError("Mohon isikan email.");
            loginEmail.requestFocus();
        }

        else if(instPassword.isEmpty()){
            loginPassword.setError("Mohon isikan password");
            loginPassword.requestFocus();
        }

        else if(!(instEmail.isEmpty() && instPassword.isEmpty())){
            firebaseAuth.signInWithEmailAndPassword(instEmail, instPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(i);
                    }

                    else{
                        Toast.makeText(MainActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void initComponents() {
        loginEmail = findViewById(R.id.ET_loginEmail);
        loginPassword = findViewById(R.id.ET_loginPassword);
        loginProceed = findViewById(R.id.BTN_loginProceed);
        toRegisterPageTextView = findViewById(R.id.TV_toRegisterTextView);
    }

    private void updateUI() {
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("from updateUI in Main", "User is null");
        }

        else{
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
            finish();

            Log.d("from updateUI in Main", "User is not null");
        }
    }
}
