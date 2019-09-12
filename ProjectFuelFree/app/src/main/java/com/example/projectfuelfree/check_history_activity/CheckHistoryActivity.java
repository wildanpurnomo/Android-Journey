package com.example.projectfuelfree.check_history_activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.model.Bensin;
import com.example.projectfuelfree.model.Nota;
import com.example.projectfuelfree.purchase_activity.DisplayProductsActivity;
import com.example.projectfuelfree.purchase_activity.DisplayProductsAdapter;
import com.example.projectfuelfree.user_register_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckHistoryActivity extends AppCompatActivity {
    private RecyclerView mRvHistoryDisplayer;
    private CheckHistoryAdapter checkHistoryAdapter;
    private DatabaseReference fNotaDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Nota> listNota = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("History");

        firebaseAuth = FirebaseAuth.getInstance();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_check_history);

        mRvHistoryDisplayer = findViewById(R.id.RVhistoryCheck);
        mRvHistoryDisplayer.setLayoutManager(new LinearLayoutManager(this));

        fNotaDatabase = FirebaseDatabase.getInstance().getReference().child("Nota").child(firebaseAuth.getCurrentUser().getUid());
        fNotaDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNota.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String instToken = (String)snapshot.child("token").getValue();
                    String instDate = (String)snapshot.child("dateOfTransaction").getValue();
                    String instProdName = (String)snapshot.child("productBought").getValue();
                    String instNominal = (String)snapshot.child("nominalBought").getValue();
                    String instLiterGet = (String)snapshot.child("literGet").getValue();

                    Nota instNota = new Nota(instToken, instDate, instProdName, instNominal, instLiterGet);

                    listNota.add(instNota);
                }

                checkHistoryAdapter = new CheckHistoryAdapter(CheckHistoryActivity.this, listNota);
                mRvHistoryDisplayer.setAdapter(checkHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void updateUI() {
        if(firebaseAuth.getCurrentUser() == null){
            Intent i = new Intent(CheckHistoryActivity.this, LoginActivity.class);
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("User Auth", "is Null");
        }

        else{

            Log.d("User Auth", "not Null");
        }
    }
}
