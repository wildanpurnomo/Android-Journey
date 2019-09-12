package com.example.asus.securenotesapplication.user_sign;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.securenotesapplication.MainActivity;
import com.example.asus.securenotesapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextInputLayout inputEmail, inputPassword;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button)findViewById(R.id.btn_login);
        inputEmail = (TextInputLayout)findViewById(R.id.input_log_email);
        inputPassword = (TextInputLayout)findViewById(R.id.input_log_password);

        fAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String umail = inputEmail.getEditText().getText().toString().trim();
                String upassword = inputPassword.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(umail) && !TextUtils.isEmpty(upassword))
                {
                    login(umail,upassword);
                }
            }
        });
    }

    private void login(String email, String password)
    {
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    Toast.makeText(LoginActivity.this, "Berhasil Masuk!", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(LoginActivity.this, "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
