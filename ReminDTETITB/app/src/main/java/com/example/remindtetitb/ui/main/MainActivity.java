package com.example.remindtetitb.ui.main;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.remindtetitb.R;
import com.example.remindtetitb.helper.SharedPrefManager;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.ui.adapter.InfoAdapter;
import com.example.remindtetitb.ui.adapter.SearchAdapter;
import com.example.remindtetitb.ui.custom.InfoDecoration;
import com.example.remindtetitb.ui.custom.StatefulRecyclerView;
import com.example.remindtetitb.viewmodels.InfoViewModel;
import com.example.remindtetitb.viewmodels.SearchViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener {
    private static String SAVED_LIST = "saved_list";
    private static String SAVED_SEARCH = "saved_searched";
    private static String SAVED_QUERY = "saved_query";
    private static String SAVED_STATE = "saved_state";

    private SharedPrefManager sharedPrefManager;
    private SearchViewModel searchViewModel;

    private StatefulRecyclerView rvInfoAkademik, rvSearchedInfo;
    private ProgressBar pbLoading;
    private InfoAdapter infoAdapter = new InfoAdapter();
    private SearchAdapter searchAdapter = new SearchAdapter();
    private Button btnFilterKuliah, btnFilterAkademik;
    private SearchView searchView;

    private MenuItem itemSearch;

    private boolean isSearching;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);
        InfoViewModel infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        infoAdapter.setContext(this);
        searchAdapter.setContext(this);

        if (savedInstanceState != null) {
            infoAdapter.setListInfo(savedInstanceState.<Info>getParcelableArrayList(SAVED_LIST));
            searchAdapter.setSearchedInfo(savedInstanceState.<Info>getParcelableArrayList(SAVED_SEARCH));
            searchAdapter.getFilter().filter(sharedPrefManager.filterInfo());
            searchQuery = savedInstanceState.getString(SAVED_QUERY);
            isSearching = savedInstanceState.getBoolean(SAVED_STATE);
        }

        pbLoading = findViewById(R.id.pb_main_loading);

        Toolbar toolbarSearch = findViewById(R.id.toolbar_search);
        toolbarSearch.setOnClickListener(this);
        toolbarSearch.inflateMenu(R.menu.search_menu);
        toolbarSearch.setTitle("Cari berdasarkan matkul, dosen, dsb.");

        itemSearch = toolbarSearch.getMenu().getItem(0);
        itemSearch.setOnActionExpandListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView = (SearchView) itemSearch.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);

            if(searchQuery != null && !searchQuery.isEmpty()){
                itemSearch.expandActionView();
                searchView.setQuery(searchQuery, false);
                searchView.clearFocus();
            }
        }

        rvInfoAkademik = findViewById(R.id.rv_main_allinfo);
        rvInfoAkademik.setLayoutManager(new LinearLayoutManager(this));
        rvInfoAkademik.addItemDecoration(new InfoDecoration(getResources().getDimensionPixelSize(R.dimen.list_spacing)));
        rvInfoAkademik.scrollToPosition(savedInstanceState == null ? 0 : savedInstanceState.getInt("text"));

        rvSearchedInfo = findViewById(R.id.rv_main_search);
        rvSearchedInfo.setLayoutManager(new LinearLayoutManager(this));
        rvSearchedInfo.addItemDecoration(new InfoDecoration(getResources().getDimensionPixelSize(R.dimen.list_spacing)));
        if(isSearching){
            rvSearchedInfo.setAdapter(searchAdapter);
        }

        btnFilterKuliah = findViewById(R.id.btn_main_kuliah_filter);
        btnFilterKuliah.setOnClickListener(this);
        btnFilterAkademik = findViewById(R.id.btn_main_akademik_filter);
        btnFilterAkademik.setOnClickListener(this);

        if(sharedPrefManager.filterInfo().equals("kuliah")){
            btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));
        } else if(sharedPrefManager.filterInfo().equals("akademik")){
            btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));
        }

        infoViewModel.setListInfo();
        displayLoading(true);
        infoViewModel.getListInfo().observe(this, new Observer<ArrayList<Info>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Info> infos) {
                infoAdapter.setListInfo(infos);
                infoAdapter.getFilter().filter(sharedPrefManager.filterInfo());
                rvInfoAkademik.setAdapter(infoAdapter);

                displayLoading(false);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_LIST, infoAdapter.getListInfo());
        outState.putParcelableArrayList(SAVED_SEARCH, searchAdapter.getSearchedInfo());
        outState.putString(SAVED_QUERY, searchQuery);
        outState.putBoolean(SAVED_STATE, isSearching);
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

                    if(isSearching){
                        searchAdapter.getFilter().filter("akademik");
                    }
                } else if(sharedPrefManager.filterInfo().equals("akademik")){
                    // Mode: Akademik + Kuliah
                    sharedPrefManager.setFilterInfo("all");
                    btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_kuliah, null));

                    infoAdapter.getFilter().filter("all");

                    if(isSearching){
                        searchAdapter.getFilter().filter("all");
                    }
                }
                break;

            case R.id.btn_main_akademik_filter:
                if(sharedPrefManager.filterInfo().equals("all")){
                    // Mode: Kuliah
                    sharedPrefManager.setFilterInfo("kuliah");
                    btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));

                    infoAdapter.getFilter().filter("kuliah");

                    if(isSearching){
                        searchAdapter.getFilter().filter("kuliah");
                    }
                } else if(sharedPrefManager.filterInfo().equals("kuliah")){
                    // Mode: Kuliah + Akademik
                    sharedPrefManager.setFilterInfo("all");
                    btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_akademik, null));

                    infoAdapter.getFilter().filter("all");

                    if(isSearching){
                        searchAdapter.getFilter().filter("all");
                    }
                }
                break;

            case R.id.toolbar_search:
                itemSearch.expandActionView();
                break;
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        if(isSearching){
            isSearching = false;

            searchViewModel.getSearchResults().removeObservers(this);
            searchQuery = "";

            rvSearchedInfo.setVisibility(View.GONE);
            rvInfoAkademik.setVisibility(View.VISIBLE);
        }
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        isSearching = true;

        searchQuery = s.toLowerCase().trim().replaceAll("\\s", "+");
        searchViewModel.setSearchResults(searchQuery);
        displayLoading(true);
        searchViewModel.getSearchResults().observe(this, new Observer<ArrayList<Info>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Info> infos) {
                searchAdapter.setSearchedInfo(infos);
                searchAdapter.getFilter().filter(sharedPrefManager.filterInfo());
                rvSearchedInfo.setAdapter(searchAdapter);

                displayLoading(false);
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchQuery = s;
        return true;
    }

    private void displayLoading(boolean isLoading){
        if (isLoading) {
            pbLoading.setVisibility(View.VISIBLE);
            rvInfoAkademik.setVisibility(View.GONE);
            rvSearchedInfo.setVisibility(View.GONE);
        } else{
            pbLoading.setVisibility(View.GONE);
            if (isSearching) {
                rvSearchedInfo.setVisibility(View.VISIBLE);
            } else{
                rvInfoAkademik.setVisibility(View.VISIBLE);
            }
        }
    }


}
