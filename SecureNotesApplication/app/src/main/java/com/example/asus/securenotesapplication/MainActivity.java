package com.example.asus.securenotesapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private TextView txtCreateFile, txtCheckFiles, txtLogout;
    private FirebaseAuth fAuth;
    private DatabaseReference fNotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null)
        {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }

        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

        txtCreateFile = (TextView)findViewById(R.id.text_create_file);
        txtCheckFiles = (TextView)findViewById(R.id.text_check_files);
        txtLogout = (TextView)findViewById(R.id.text_logout);

        txtCreateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MainActivity.this, CreateNotesActivity.class);
                startActivity(createIntent);
                finish();
            }
        });

        txtCheckFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkIntent = new Intent(MainActivity.this, CheckFIlesActivity.class);
                startActivity(checkIntent);
                finish();
            }
        });
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent toHomeIntent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(toHomeIntent);
                finish();
            }
        });
    }

    private void updateUI()
    {
        if(fAuth.getCurrentUser() != null)
        {
            Log.i("MainActivity", "fAuth != null");
        }
        else
        {
            Intent startIntent = new Intent (MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity", "fAuth == null");
        }
    }
}
