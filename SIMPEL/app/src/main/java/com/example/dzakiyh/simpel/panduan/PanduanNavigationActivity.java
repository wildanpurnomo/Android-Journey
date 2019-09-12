package com.example.dzakiyh.simpel.panduan;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;

public class PanduanNavigationActivity extends AppCompatActivity {

    private TextView TV1, TV2, TV3, TV4, TV5, TV6, TV7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panduan_navigation);

        initComponents();

        underlineAllTextViews();
    }

    private void initComponents() {
        TV1 = findViewById(R.id.TV_Panduan_1);
        TV2 = findViewById(R.id.TV_Panduan_2);
        TV3 = findViewById(R.id.TV_Panduan_3);
        TV4 = findViewById(R.id.TV_Panduan_4);
        TV5 = findViewById(R.id.TV_Panduan_5);
        TV6 = findViewById(R.id.TV_Panduan_6);
        TV7 = findViewById(R.id.TV_Panduan_7);
    }

    private void underlineAllTextViews(){
        TV1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TV2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TV3.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TV4.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TV5.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TV6.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TV7.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

}
