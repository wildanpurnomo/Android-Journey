package com.example.remindtetitb.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.remindtetitb.R;
import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvInfoAkademik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final InfoAdapter infoAdapter = new InfoAdapter();
        infoAdapter.setContext(this);

        InfoViewModel infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        infoViewModel.setListInfo();

        rvInfoAkademik = findViewById(R.id.rv_main_allinfo);
        rvInfoAkademik.setLayoutManager(new LinearLayoutManager(this));

        infoViewModel.getListInfo().observe(this, new Observer<ArrayList<Info>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Info> infos) {
                infoAdapter.setListInfo(infos);
                rvInfoAkademik.setAdapter(infoAdapter);
                rvInfoAkademik.addItemDecoration(new InfoDecoration(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin)));
            }
        });
    }
}
