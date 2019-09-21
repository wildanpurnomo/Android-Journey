package com.example.remindtetitb.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.remindtetitb.R;
import com.example.remindtetitb.helper.SharedPrefManager;
import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{
    private static String FILTER_MODE_KEY = "filter_mode";
    private static String SAVED_LIST = "saved_list";

    private SharedPrefManager sharedPrefManager;

    private RecyclerView rvInfoAkademik;
    private InfoAdapter infoAdapter = new InfoAdapter();
    private Button btnFilterKuliah, btnFilterAkademik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);
        infoAdapter.setContext(this);

        rvInfoAkademik = findViewById(R.id.rv_main_allinfo);
        rvInfoAkademik.setLayoutManager(new LinearLayoutManager(this));
        rvInfoAkademik.addItemDecoration(new InfoDecoration(getResources().getDimensionPixelSize(R.dimen.list_spacing)));

        btnFilterKuliah = findViewById(R.id.btn_main_kuliah_filter);
        btnFilterKuliah.setOnClickListener(this);
        btnFilterAkademik = findViewById(R.id.btn_main_akademik_filter);
        btnFilterAkademik.setOnClickListener(this);

        final String filterMode;
        if (savedInstanceState != null) {
            filterMode = savedInstanceState.getString(FILTER_MODE_KEY);
            infoAdapter.setListInfo(savedInstanceState.<Info>getParcelableArrayList(SAVED_LIST));
        } else{
            filterMode = sharedPrefManager.filterInfo();
        }

        assert filterMode != null;
        if(filterMode.equals("kuliah")){
            btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));
        } else if(filterMode.equals("akademik")){
            btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));
        }

        InfoViewModel infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        infoViewModel.setListInfo();
        infoViewModel.getListInfo().observe(this, new Observer<ArrayList<Info>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Info> infos) {
                infoAdapter.setListInfo(infos);

                if(filterMode.equals("kuliah")){
                    infoAdapter.getFilter().filter("kuliah");
                } else if(filterMode.equals("akademik")){
                    infoAdapter.getFilter().filter("akademik");
                }

                rvInfoAkademik.setAdapter(infoAdapter);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILTER_MODE_KEY, sharedPrefManager.filterInfo());
        outState.putParcelableArrayList(SAVED_LIST, infoAdapter.getListInfo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_main_kuliah_filter:
                if(sharedPrefManager.filterInfo().equals("all")){
                    // Mode: Akademik
                    sharedPrefManager.setFilterInfo("akademik");
                    btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));

                    infoAdapter.getFilter().filter("akademik");
                } else if(sharedPrefManager.filterInfo().equals("akademik")){
                    // Mode: Akademik + Kuliah
                    sharedPrefManager.setFilterInfo("all");
                    btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_kuliah, null));

                    infoAdapter.getFilter().filter("all");
                }
                break;

            case R.id.btn_main_akademik_filter:
                if(sharedPrefManager.filterInfo().equals("all")){
                    // Mode: Kuliah
                    sharedPrefManager.setFilterInfo("kuliah");
                    btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));

                    infoAdapter.getFilter().filter("kuliah");
                } else if(sharedPrefManager.filterInfo().equals("kuliah")){
                    // Mode: Kuliah + Akademik
                    sharedPrefManager.setFilterInfo("all");
                    btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_akademik, null));

                    infoAdapter.getFilter().filter("all");
                }
                break;
        }
    }
}
