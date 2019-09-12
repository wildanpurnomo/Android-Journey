package com.example.projectfuelfree.purchase_activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfuelfree.R;
import com.example.projectfuelfree.UserActivity;
import com.example.projectfuelfree.user_register_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AfterPurchaseActivity extends AppCompatActivity {

    public final static String EXTRA_PROD_NAME = "";
    public final static String EXTRA_DATE = "";
    public final static String EXTRA_HPL = "";
    public final static String EXTRA_NOMINAL= "";
    public final static String EXTRA_TOKEN = "";
    public final static String EXTRA_TEMP="";

    private TextView notaProductBought, notaDateBought, notaHargaPerLiter, notaNominalBought, notaLiterGet, notaTokenGet;
    private Button backToMainMenuButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fNotaDatabase;

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
        setContentView(R.layout.activity_after_purchase);

        backToMainMenuButton = findViewById(R.id.BTNbackToUserActivity);
        notaProductBought = findViewById(R.id.TVnotaProductBought);
        notaDateBought = findViewById(R.id.TVnotaDateOfTransaction);
        notaHargaPerLiter = findViewById(R.id.TVnotaHargaPerLiter);
        notaNominalBought = findViewById(R.id.TVnotaNominalBought);
        notaLiterGet = findViewById(R.id.TVnotaLiterGet);
        notaTokenGet = findViewById(R.id.TVnotaToken);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String tokenPassed = extras.getString("EXTRA_TOKEN");
        String prodName = extras.getString("EXTRA_PROD_NAME");
        String dateBght = extras.getString("EXTRA_DATE");
        String nominalBght = extras.getString("EXTRA_NOMINAL");
        String literGet = extras.getString("EXTRA_HPL");
        String temp = extras.getString("EXTRA_TEMP");

//        fNotaDatabase = FirebaseDatabase.getInstance().getReference().child("Nota").child(firebaseAuth.getCurrentUser().getUid()).child(tokenPassed);


        notaProductBought.setText(prodName);
        notaDateBought.setText(dateBght);
        notaHargaPerLiter.setText("Harga satu liter : " + temp);
        notaNominalBought.setText("Nominal yang dibeli : " + nominalBght);
        notaLiterGet.setText("Liter yang didapat : " + literGet);
        notaTokenGet.setText(tokenPassed);


        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AfterPurchaseActivity.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void updateUI() {
        if(firebaseAuth.getCurrentUser() == null){
            Intent i = new Intent(AfterPurchaseActivity.this, LoginActivity.class);
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Log.d("User Auth", "is Null");
        }

        else{

            Log.d("User Auth", "not Null");
        }
    }
}
