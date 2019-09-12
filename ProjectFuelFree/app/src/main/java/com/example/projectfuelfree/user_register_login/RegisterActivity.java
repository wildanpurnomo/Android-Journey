package com.example.projectfuelfree.user_register_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.UserActivity;
import com.example.projectfuelfree.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerUsernameEditText, registerEmailEditText, registerPasswordEditText;
    private Button registerProceedButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference fUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        firebaseAuth = FirebaseAuth.getInstance();

        registerUsernameEditText = findViewById(R.id.ETregisterUsername);
        registerEmailEditText = findViewById(R.id.ETregisterEmail);
        registerPasswordEditText = findViewById(R.id.ETregisterPassword);
        registerProceedButton = findViewById(R.id.BTNregisterButton);

        registerProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instUsername = registerUsernameEditText.getText().toString();
                String instEmail = registerEmailEditText.getText().toString();
                String instPassword = registerPasswordEditText.getText().toString();

                registerUser(instUsername, instEmail, instPassword);


            }
        });
    }

    private void registerUser(final String instUsername, final String instEmail, final String instPassword) {
        if(instEmail.isEmpty()){
            registerEmailEditText.setError("Mohon tuliskan Email");
            registerEmailEditText.requestFocus();
        }

        else if(instPassword.isEmpty()){
            registerPasswordEditText.setError("Mohon tuliskan password");
            registerPasswordEditText.requestFocus();
        }

        else if(instUsername.isEmpty()){
            registerUsernameEditText.setError("Mohon tuliskan username");
            registerUsernameEditText.requestFocus();
        }


        else if(!(instEmail.isEmpty() && instPassword.isEmpty())){
            firebaseAuth.createUserWithEmailAndPassword(instEmail, instPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Register gagal : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Users instUser = new Users(instUsername, instEmail, "100000");

                        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
                        fUsersDatabase.setValue(instUser);

                        Intent i = new Intent(RegisterActivity.this, UserActivity.class);
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


}
