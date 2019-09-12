package com.example.projectfuelfree.purchase_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.model.Bensin;
import com.example.projectfuelfree.model.Nota;
import com.example.projectfuelfree.user_register_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PurchaseProductActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_NAME = "";

    private TextView oktanNumberDisplayerTextView, hargaPerLiterDisplayerTextView, literGetHintTextView;
    private EditText nominalBoughtEditText;
    private Button confirmPurchaseButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fProductDatabase, fNotaDatabase, fQuickCastNota, fUserDatabase;

    private AlertDialog.Builder alertDialogBuilder;

    private Double literGet;
    private int nominalBought, saldoUser;
    private String hintLiterDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Purchase");

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_purchase_product);

        oktanNumberDisplayerTextView = findViewById(R.id.TVdisplayOktanVal);
        hargaPerLiterDisplayerTextView = findViewById(R.id.TVdisplayHargaPerLiter);
        confirmPurchaseButton = findViewById(R.id.BTNpurchaseProceed);
        literGetHintTextView = findViewById(R.id.TVliterGetHint);
        nominalBoughtEditText = findViewById(R.id.ETnominalBought);

        alertDialogBuilder = new AlertDialog.Builder(this);

        String key = getIntent().getStringExtra(EXTRA_PRODUCT_NAME);

        //get saldo
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saldoUser = Integer.parseInt(dataSnapshot.child("saldo").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fProductDatabase = FirebaseDatabase.getInstance().getReference().child("Bensin").child(key);
        fProductDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String oktanNumber = (String)dataSnapshot.child("Oktan").getValue();
                String hargaPerLiter = (String)dataSnapshot.child("HargaPerLiter").getValue();
                oktanNumberDisplayerTextView.setText(oktanNumber);
                hargaPerLiterDisplayerTextView.setText(hargaPerLiter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nominalBoughtEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                literGetHintTextView.setText("Anda membeli 0 liter.");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                literGetHintTextView.setText("Anda membeli 0 liter.");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){return;}
                double nominal =  Double.parseDouble(nominalBoughtEditText.getText().toString());
                double harga = Double.parseDouble(hargaPerLiterDisplayerTextView.getText().toString());

                literGet = nominal / harga;

                DecimalFormat df = new DecimalFormat("#.##");
                hintLiterDisplayed = df.format(literGet);

                literGetHintTextView.setText("Anda membeli " + hintLiterDisplayed + " liter.");


            }
        });

        confirmPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nominalBought = Integer.parseInt(nominalBoughtEditText.getText().toString());

                if((saldoUser - nominalBought) < 0){
                    Toast.makeText(PurchaseProductActivity.this, Integer.toString(saldoUser-nominalBought), Toast.LENGTH_SHORT).show();
                    return;
                }

                fUserDatabase.child("saldo").setValue(Integer.toString(saldoUser - nominalBought));


                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                DecimalFormat df = new DecimalFormat("#.##");
                final String temp = df.format(literGet);

                final String instToken = getToken(4) + "-" + getToken(4) + "-" + getToken(4) + "-" + getToken(4);
                final String instDate = dateFormat.format(date);
                final String instProductBought = getIntent().getStringExtra(EXTRA_PRODUCT_NAME);
                final String instNominalBought = nominalBoughtEditText.getText().toString();

                final Nota instNota = new Nota(instToken, instDate, instProductBought, instNominalBought, temp);

                fNotaDatabase = FirebaseDatabase.getInstance().getReference().child("Nota").child(firebaseAuth.getCurrentUser().getUid()).child(instToken);
                fQuickCastNota = FirebaseDatabase.getInstance().getReference().child("Quick Cast Nota").child(instToken);

                alertDialogBuilder.setMessage("Anda akan membeli " + instProductBought + " sejumlah Rp " + instNominalBought + " ,-");
                alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fNotaDatabase.setValue(instNota);
                        fQuickCastNota.setValue(instNota);

                        Intent i = new Intent(PurchaseProductActivity.this, AfterPurchaseActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("EXTRA_PROD_NAME", instProductBought);
                        extras.putString("EXTRA_DATE", instDate);
                        extras.putString("EXTRA_HPL", temp);
                        extras.putString("EXTRA_NOMINAL", instNominalBought);
                        extras.putString("EXTRA_TOKEN", instToken);
                        extras.putString("EXTRA_TEMP", hargaPerLiterDisplayerTextView.getText().toString());
                        i.putExtras(extras);
                        startActivity(i);
                        finish();

                    }
                });

                alertDialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
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

    private void updateUI(){
        if(firebaseAuth.getCurrentUser() == null){
            Intent i = new Intent(PurchaseProductActivity.this, LoginActivity.class);
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("User Auth", "is Null");
        }

        else{

            Log.d("User Auth", "not Null");
        }
    }

    private String getToken(int length){
        Random random = new Random();
        String CHARS = "1234567890";

        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return token.toString();
    }
}
