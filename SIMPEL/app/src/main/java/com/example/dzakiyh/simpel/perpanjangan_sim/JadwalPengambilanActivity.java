package com.example.dzakiyh.simpel.perpanjangan_sim;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JadwalPengambilanActivity extends AppCompatActivity {

    private TextView keteranganJadwalPengambilan;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_jadwal_pengambilan);

        initComponents();

        changeUI();
    }

    private void initComponents() {

        keteranganJadwalPengambilan = findViewById(R.id.TV_JadwalPengambilan_Keterangan);
    }

    private void changeUI() {

        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String jadwalAmbil = dataSnapshot.child("jadwalAmbil").getValue().toString();

                if(!jadwalAmbil.equals("NULL")){
                    keteranganJadwalPengambilan.setText("Submisi telah disetujui. Jadwal pengambilan Anda adalah " + jadwalAmbil + ".");
                    keteranganJadwalPengambilan.setTextColor(Color.parseColor("#20C456"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
