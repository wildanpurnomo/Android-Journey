package com.example.dzakiyh.simpel.perpanjangan_sim;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.perpanjangan_sim.kirim_berkas.SudahUploadBerkasPerpanjanganActivity;
import com.example.dzakiyh.simpel.perpanjangan_sim.kirim_berkas.UploadBerkasPerpanjanganActivity;
import com.example.dzakiyh.simpel.perpanjangan_sim.pengisian_formulir.FormulirPerpanjanganSIMActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigasiPerpanjanganSIMActivity extends AppCompatActivity {

    private CardView containerDataDiri, containerUploadBerkas, containerVerifikasiBerkas, containerJadwalAmbil;

    private TextView dataDiri1, dataDiri2, berkas1, berkas2, verifBerkas1, verifBerkas2, jadwalUjian1, jadwalUjian2;

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
        setContentView(R.layout.activity_navigasi_perpanjangan_sim);

        initComponents();

        changeUI();

        containerDataDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiPerpanjanganSIMActivity.this, FormulirPerpanjanganSIMActivity.class);
                startActivity(i);
            }
        });

        containerUploadBerkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiPerpanjanganSIMActivity.this, UploadBerkasPerpanjanganActivity.class);
                startActivity(i);
            }
        });

        containerVerifikasiBerkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiPerpanjanganSIMActivity.this, SudahUploadBerkasPerpanjanganActivity.class);
                startActivity(i);
            }
        });

        containerJadwalAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiPerpanjanganSIMActivity.this, JadwalPengambilanActivity.class);
                startActivity(i);
            }
        });
    }

    private void initComponents() {

        containerDataDiri = findViewById(R.id.CVPerpanjanganSIM_contDataDiri);
        containerUploadBerkas = findViewById(R.id.CVPerpanjanganSIM_contBerkas);
        containerVerifikasiBerkas = findViewById(R.id.CVPerpanjanganSIM_contVerifikasiBerkas);
        containerJadwalAmbil = findViewById(R.id.CVPerpanjanganSIM_contJadwalUjian);

        dataDiri1 = findViewById(R.id.TV_DataDiriPerpanj);
        dataDiri2 = findViewById(R.id.TV_DataDiriPerpanj2);
        berkas1 = findViewById(R.id.TV_BerkasPerpanj);
        berkas2 = findViewById(R.id.TV_BerkasPerpanj2);
        verifBerkas1 = findViewById(R.id.TV_VerifikasiBerkasPerpanj);
        verifBerkas2 = findViewById(R.id.TV_VerifikasiBerkasPerpanj2);
        jadwalUjian1 = findViewById(R.id.TV_JadwalUjianPerpanj);
        jadwalUjian2 = findViewById(R.id.TV_JadwalUjianPerpanj2);
    }

    private void changeUI(){
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sudahFormBaru = dataSnapshot.child("sudahFormPerpanjangan").getValue().toString();
                String sudahUploadKK = dataSnapshot.child("sudahKKPerpanj").getValue().toString();
                String sudahUploadKTP = dataSnapshot.child("sudahKTPPerpanj").getValue().toString();
                String sudahUploadSKSehat = dataSnapshot.child("sudahSuratSehatPerpanj").getValue().toString();
                String jadwalUjian = dataSnapshot.child("jadwalAmbil").getValue().toString();

                if(sudahFormBaru.equals("TRUE")){
                    containerDataDiri.setCardBackgroundColor(Color.parseColor("#20C456"));
                    dataDiri1.setTextColor(Color.parseColor("#20C456"));
                    dataDiri2.setTextColor(Color.parseColor("#20C456"));
                }

                if(sudahUploadKK.equals("TRUE") && sudahUploadKTP.equals("TRUE") && sudahUploadSKSehat.equals("TRUE")){
                    containerUploadBerkas.setCardBackgroundColor(Color.parseColor("#20C456"));
                    berkas1.setTextColor(Color.parseColor("#20C456"));
                    berkas2.setTextColor(Color.parseColor("#20C456"));
                }

                if(sudahFormBaru.equals("TRUE") && sudahUploadKK.equals("TRUE") && sudahUploadKTP.equals("TRUE") && sudahUploadSKSehat.equals("TRUE") && jadwalUjian.equals("NULL")) {
                    containerVerifikasiBerkas.setCardBackgroundColor(Color.parseColor("#FFE600"));
                    verifBerkas1.setTextColor(Color.parseColor("#FFE600"));
                    verifBerkas2.setTextColor(Color.parseColor("#FFE600"));

                    containerJadwalAmbil.setCardBackgroundColor(Color.parseColor("#FFE600"));
                    jadwalUjian1.setTextColor(Color.parseColor("#FFE600"));
                    jadwalUjian2.setTextColor(Color.parseColor("#FFE600"));
                }

                if(sudahFormBaru.equals("TRUE") && sudahUploadKK.equals("TRUE") && sudahUploadKTP.equals("TRUE") && sudahUploadSKSehat.equals("TRUE") && !jadwalUjian.equals("NULL")) {
                    containerVerifikasiBerkas.setCardBackgroundColor(Color.parseColor("#20C456"));
                    verifBerkas1.setTextColor(Color.parseColor("#20C456"));
                    verifBerkas2.setTextColor(Color.parseColor("#20C456"));

                    containerJadwalAmbil.setCardBackgroundColor(Color.parseColor("#20C456"));
                    jadwalUjian1.setTextColor(Color.parseColor("#20C456"));
                    jadwalUjian2.setTextColor(Color.parseColor("#20C456"));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
