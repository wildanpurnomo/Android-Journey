package com.example.dzakiyh.simpel.pembuatan_sim_baru;

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

public class JadwalUjianActivity extends AppCompatActivity {

    private TextView keteranganJadwalUjian;

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
        setContentView(R.layout.activity_jadwal_ujian);

        initComponents();

        changeUI();
    }

    private void initComponents() {
        keteranganJadwalUjian = findViewById(R.id.TV_JadwalUjian_Keterangan);
    }

    private void changeUI(){
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String jadwalUjian = dataSnapshot.child("jadwalUjian").getValue().toString();

                if(!jadwalUjian.equals("NULL")){
                    keteranganJadwalUjian.setText("Submisi telah disetujui. Jadwal ujian Anda adalah " + jadwalUjian + ".");
                    keteranganJadwalUjian.setTextColor(Color.parseColor("#20C456"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
