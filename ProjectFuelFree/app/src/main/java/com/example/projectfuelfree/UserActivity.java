package com.example.projectfuelfree;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfuelfree.check_history_activity.CheckHistoryActivity;
import com.example.projectfuelfree.purchase_activity.DisplayProductsActivity;
import com.example.projectfuelfree.top_up_info_activity.TopUpInfoActivity;
import com.example.projectfuelfree.user_register_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    private TextView welcomeUserTV;
    private Button toPurchaseActivityButton, toHistoryActivityButton, toTopUpInfoActivityButton, logOutButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference fUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main Menu");

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        }

        updateUI();


    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_user);

        welcomeUserTV = findViewById(R.id.TVwelcomeUser);
        toPurchaseActivityButton = findViewById(R.id.BTNtoPurchaseActivity);
        toHistoryActivityButton = findViewById(R.id.BTNtoHistoryActivity);
        toTopUpInfoActivityButton = findViewById(R.id.BTNtoTopUpInfoActivity);
        logOutButton = findViewById(R.id.BTNlogOut);

        fUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                welcomeUserTV.setText("Welcome! " + dataSnapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toPurchaseActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, DisplayProductsActivity.class);
                startActivity(i);
            }
        });

        toHistoryActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, CheckHistoryActivity.class);
                startActivity(i);
            }
        });

        toTopUpInfoActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, TopUpInfoActivity.class);
                startActivity(i);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(UserActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void updateUI(){
        if(firebaseAuth.getCurrentUser() == null){
            Intent i = new Intent(UserActivity.this, LoginActivity.class);
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("User Auth", "is Null");
        }

        else{

            Log.d("User Auth", "not Null");
        }
    }
}
