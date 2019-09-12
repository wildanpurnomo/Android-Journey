package com.example.dzakiyh.simpel.pembuatan_sim_baru;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.pembuatan_sim_baru.kirim_berkas.SudahUploadBerkasActivity;
import com.example.dzakiyh.simpel.pembuatan_sim_baru.kirim_berkas.UploadBerkasActivity;
import com.example.dzakiyh.simpel.pembuatan_sim_baru.pengisian_formulir.FormulirSIMBaruActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigasiDaftarActivity extends AppCompatActivity {

    private CardView containerDataDiri, containerUploadBerkas, containerVerifikasiBerkas, containerJadwalUjian;

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
        setContentView(R.layout.activity_navigasi_daftar);

        initComponents();

        changeUI();

        containerDataDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiDaftarActivity.this, FormulirSIMBaruActivity.class);
                startActivity(i);
            }
        });

        containerUploadBerkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiDaftarActivity.this, UploadBerkasActivity.class);
                startActivity(i);
            }
        });

        containerVerifikasiBerkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiDaftarActivity.this, SudahUploadBerkasActivity.class);
                startActivity(i);
            }
        });

        containerJadwalUjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigasiDaftarActivity.this, JadwalUjianActivity.class);
                startActivity(i);

            }
        });
    }

    private void initComponents() {

        containerDataDiri = findViewById(R.id.CVSIMBaru_contDataDiri);
        containerUploadBerkas = findViewById(R.id.CVSIMBaru_contBerkas);
        containerVerifikasiBerkas = findViewById(R.id.CVSIMBaru_contVerifikasiBerkas);
        containerJadwalUjian = findViewById(R.id.CVSIMBaru_contJadwalUjian);

        dataDiri1 = findViewById(R.id.TV_DataDiri);
        dataDiri2 = findViewById(R.id.TV_DataDiri2);
        berkas1 = findViewById(R.id.TV_Berkas);
        berkas2 =findViewById(R.id.TV_Berkas2);
        verifBerkas1 = findViewById(R.id.TV_VerifikasiBerkas);
        verifBerkas2 = findViewById(R.id.TV_VerifikasiBerkas2);
        jadwalUjian1 = findViewById(R.id.TV_JadwalUjian);
        jadwalUjian2 = findViewById(R.id.TV_JadwalUjian2);
    }

    private void changeUI(){
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sudahFormBaru = dataSnapshot.child("sudahFormBaru").getValue().toString();
                String sudahUploadKK = dataSnapshot.child("sudahUploadKK").getValue().toString();
                String sudahUploadKTP = dataSnapshot.child("sudahUploadKTP").getValue().toString();
                String sudahUploadSKSehat = dataSnapshot.child("sudahUploadSuratSehat").getValue().toString();
                String jadwalUjian = dataSnapshot.child("jadwalUjian").getValue().toString();

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

                    containerJadwalUjian.setCardBackgroundColor(Color.parseColor("#FFE600"));
                    jadwalUjian1.setTextColor(Color.parseColor("#FFE600"));
                    jadwalUjian2.setTextColor(Color.parseColor("#FFE600"));
                }

                if(sudahFormBaru.equals("TRUE") && sudahUploadKK.equals("TRUE") && sudahUploadKTP.equals("TRUE") && sudahUploadSKSehat.equals("TRUE") && !jadwalUjian.equals("NULL")) {
                    containerVerifikasiBerkas.setCardBackgroundColor(Color.parseColor("#20C456"));
                    verifBerkas1.setTextColor(Color.parseColor("#20C456"));
                    verifBerkas2.setTextColor(Color.parseColor("#20C456"));

                    containerJadwalUjian.setCardBackgroundColor(Color.parseColor("#20C456"));
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
