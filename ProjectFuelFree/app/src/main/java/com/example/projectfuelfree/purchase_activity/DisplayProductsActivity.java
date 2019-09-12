package com.example.projectfuelfree.purchase_activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.UserActivity;
import com.example.projectfuelfree.model.Bensin;
import com.example.projectfuelfree.user_register_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayProductsActivity extends AppCompatActivity {
    private RecyclerView mRvProductsDisplayer;
    private DisplayProductsAdapter displayProductsAdapter;
    private DatabaseReference fProductsDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Bensin> listBensin = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Purchase");

        firebaseAuth = FirebaseAuth.getInstance();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_display_products);

        mRvProductsDisplayer = findViewById(R.id.RVdisplayProducts);
        mRvProductsDisplayer.setLayoutManager(new LinearLayoutManager(this));

        fProductsDatabase = FirebaseDatabase.getInstance().getReference().child("Bensin");
        fProductsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBensin.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String instNamaBensin = snapshot.getKey();
                    String instHarga = (String)snapshot.child("HargaPerLiter").getValue();

                    Bensin instBensin = new Bensin(instNamaBensin, instHarga);
                    listBensin.add(instBensin);
                }
                displayProductsAdapter = new DisplayProductsAdapter(DisplayProductsActivity.this, listBensin);
                mRvProductsDisplayer.setAdapter(displayProductsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void updateUI() {
        if(firebaseAuth.getCurrentUser() == null){
            Intent i = new Intent(DisplayProductsActivity.this, LoginActivity.class);
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("User Auth", "is Null");
        }

        else{

            Log.d("User Auth", "not Null");
        }
    }


}
