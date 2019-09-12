package com.example.dzakiyh.simpel.login_and_register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dzakiyh.simpel.HomeActivity;
import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DaftarActivity extends AppCompatActivity {

    private EditText registerNamaLengkap, registerNoKTP, registerUsername, registerEmail, registerPassword;
    private Button registerProceedButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        updateUI();

    }



    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_daftar);

        initComponents();

        registerProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instNamaLengkap = registerNamaLengkap.getText().toString();
                String instNoKTP = registerNoKTP.getText().toString();
                String instUsername = registerUsername.getText().toString();
                String instEmail = registerEmail.getText().toString();
                String instPassword = registerPassword.getText().toString();

                registerUser(instNamaLengkap, instNoKTP, instUsername, instEmail, instPassword);
            }
        });
    }

    private void registerUser(final String namaLengkap, final String noKTP, final String username, final String email, final String password){
        if(namaLengkap.isEmpty()){
            registerNamaLengkap.setError("Mohon tuliskan nama lengkap Anda.");
            registerNamaLengkap.requestFocus();
        }

        else if(noKTP.isEmpty()){
            registerNoKTP.setError("Mohon tuliskan No KTP.");
            registerNoKTP.requestFocus();
        }

        else if(username.isEmpty()){
            registerUsername.setError("Mohon tuliskan username");
            registerUsername.requestFocus();
        }

        else if(email.isEmpty()){
            registerEmail.setError("Mohon tuliskan email");
            registerEmail.requestFocus();
        }

        else if(password.isEmpty()){
            registerPassword.setError("Mohon tuliskan password");
            registerPassword.requestFocus();
        }

        else if(!(email.isEmpty() && password.isEmpty())){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DaftarActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(DaftarActivity.this, "Register gagal : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Users instUser = new Users(username, email, password, namaLengkap, noKTP, "FALSE", "FALSE","FALSE","FALSE","FALSE","FALSE","FALSE","FALSE","NULL","NULL");

                        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
                        fUsersDatabase.setValue(instUser);

                        Intent i = new Intent(DaftarActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }

        else{
            Toast.makeText(this, "Kesalahan terjadi!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponents() {

        registerNamaLengkap = findViewById(R.id.ET_registerNamaLengkap);
        registerNoKTP = findViewById(R.id.ET_registerNoKTP);
        registerUsername = findViewById(R.id.ET_registerUsername);
        registerEmail = findViewById(R.id.ET_registerEmail);
        registerPassword = findViewById(R.id.ET_registerPassword);
        registerProceedButton = findViewById(R.id.BTN_registerButton);
    }

    private void updateUI() {
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("from updateUI in Main", "User is null");
        }

        else{
            Intent i = new Intent(DaftarActivity.this, HomeActivity.class);
            startActivity(i);
            finish();

            Log.d("from updateUI in Daftar", "User is not null");
        }
    }
}
