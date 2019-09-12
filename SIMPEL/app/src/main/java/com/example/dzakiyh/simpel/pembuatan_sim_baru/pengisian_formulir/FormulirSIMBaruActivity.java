package com.example.dzakiyh.simpel.pembuatan_sim_baru.pengisian_formulir;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.model.FormulirSIMBaru;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormulirSIMBaruActivity extends AppCompatActivity {

    private EditText namaLengkap, alamatPribadi, noTeleponPribadi, namaOrtu, noTeleponOrtu;
    private Button btnLanjut;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fUserDatabase, fFormulirSIMBaruDatabase, fFormUmumDatabase;

    private AlertDialog.Builder alertDialogBuilder;

    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_formulir_simbaru);

        initComponents();

        setNamaOnForm();

        validateUI();

        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dari formulir
                String instNamaLengkap = namaLengkap.getText().toString();
                String instAlamatPribadi = alamatPribadi.getText().toString();
                String instNoTelpPribadi = noTeleponPribadi.getText().toString();
                String instNamaOrtu = namaOrtu.getText().toString();
                String instNoTelpOrtu = noTeleponOrtu.getText().toString();

                //idPengirim
                String instIDPengirim = getIDPengirim();

                //tanggal kirim
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String instDate = dateFormat.format(date);

                //instantiate objek formulir
                final FormulirSIMBaru form = new FormulirSIMBaru("Formulir SIM Baru", instDate, instIDPengirim, instNamaLengkap, instAlamatPribadi, instNoTelpPribadi,instNamaOrtu,instNoTelpOrtu);

                //set alertdialog
                alertDialogBuilder.setMessage("Anda akan mengirimkan formulir. Apakah data anda sudah benar?");
                alertDialogBuilder.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        kirimFormulirKeDatabase(form);
                    }
                });

                alertDialogBuilder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).removeEventListener(valueEventListener);

    }

    private void initComponents() {
        namaLengkap = findViewById(R.id.ET_formSIMBaruNamaLengkap);
        alamatPribadi = findViewById(R.id.ET_formSIMBaruAlamat);
        noTeleponPribadi = findViewById(R.id.ET_formSIMBaruNomorTelp);
        namaOrtu = findViewById(R.id.ET_formSIMBaruOrtuWali);
        noTeleponOrtu = findViewById(R.id.ET_formSIMBaruNoTelpOrtuWali);

        btnLanjut = findViewById(R.id.BTN_formSIMBaruLanjut);

        alertDialogBuilder = new AlertDialog.Builder(this);
    }

    private void setNamaOnForm() {

        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comotNamaLengkap = dataSnapshot.child("namaLengkap").getValue().toString();
                namaLengkap.setText(comotNamaLengkap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getIDPengirim(){

        return firebaseAuth.getCurrentUser().getUid();
    }

    private void kirimFormulirKeDatabase(FormulirSIMBaru form){
        fFormulirSIMBaruDatabase = FirebaseDatabase.getInstance().getReference().child("FormulirSIMBaru").child(form.getTanggalKirim());
        fFormulirSIMBaruDatabase.setValue(form);

        fFormUmumDatabase = FirebaseDatabase.getInstance().getReference().child("FormUntukHistory").child(firebaseAuth.getCurrentUser().getUid()).child("FormSIMBaru");
        fFormUmumDatabase.setValue(form);

        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("sudahFormBaru");
        fUserDatabase.setValue("TRUE");
    }

    private void validateUI(){

        Log.d("Halo dari formulirBaru1", "Kalau baca ini error");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Halo dari formulirBaru2", "Kalau baca ini error");
                String bahanEval = dataSnapshot.child("sudahFormBaru").getValue().toString();
                Log.d("Bahan Eval Value is ", bahanEval);

                if(bahanEval.equals("TRUE")){

                    Intent i = new Intent(FormulirSIMBaruActivity.this, SudahIsiFormSIMBaruActivity.class);
                    startActivity(i);
                    finish();
                }

                else{
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        fUserDatabase.addValueEventListener(valueEventListener);


//        fUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}
