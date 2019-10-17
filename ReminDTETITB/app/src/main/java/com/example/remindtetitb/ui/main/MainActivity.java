package com.example.remindtetitb.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.remindtetitb.R;
import com.example.remindtetitb.helper.SharedPrefManager;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.ui.adapter.InfoAdapter;
import com.example.remindtetitb.ui.custom.InfoDecoration;
import com.example.remindtetitb.viewmodels.InfoViewModel;
import com.example.remindtetitb.viewmodels.SearchViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private SharedPrefManager sharedPrefManager;
    private InfoViewModel infoViewModel;
    private SearchViewModel searchViewModel;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvInfoAkademik;
    private ProgressBar pbLoading;
    private InfoAdapter infoAdapter = new InfoAdapter();
    private Button btnFilterKuliah, btnFilterAkademik;
    private SearchView searchView;

    private MenuItem itemSearch;

    private ArrayList<Info> defaultInfo = new ArrayList<>();
    private boolean isSearching, isConnectedInternet;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefManager = new SharedPrefManager(this);
        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        infoAdapter.setContext(this);

        refreshLayout = findViewById(R.id.swiperefresh_main_info_container);
        refreshLayout.setOnRefreshListener(this);
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
        }

        rvInfoAkademik = findViewById(R.id.rv_main_allinfo);
        rvInfoAkademik.setLayoutManager(new LinearLayoutManager(this));
        rvInfoAkademik.addItemDecoration(new InfoDecoration(getResources().getDimensionPixelSize(R.dimen.list_spacing)));
        rvInfoAkademik.setAdapter(infoAdapter);

        btnFilterKuliah = findViewById(R.id.btn_main_kuliah_filter);
        btnFilterKuliah.setOnClickListener(this);
        btnFilterAkademik = findViewById(R.id.btn_main_akademik_filter);
        btnFilterAkademik.setOnClickListener(this);

        if (sharedPrefManager.filterInfo().equals("kuliah")) {
            btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));
        } else if (sharedPrefManager.filterInfo().equals("akademik")) {
            btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));
        }

        final TextView tvSwipeHint = findViewById(R.id.tv_main_swipe_hint);

        infoViewModel.setListInfo();
        displayLoading(true);
        infoViewModel.getListInfo().observe(this, new Observer<ArrayList<Info>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Info> infos) {
                if (infos != null) {
                    isConnectedInternet = true;
                    defaultInfo = infos;
                    infoAdapter.setListInfo(infos);
                    infoAdapter.getFilter().filter(sharedPrefManager.filterInfo());
                    tvSwipeHint.setVisibility(View.GONE);
                } else {
                    isConnectedInternet = false;
                    tvSwipeHint.setVisibility(View.VISIBLE);
                }

                displayLoading(false);
            }
        });

        searchViewModel.getSearchResults().observe(this, new Observer<ArrayList<Info>>() {
            @Override
            public void onChanged(ArrayList<Info> infos) {
                if (infos != null) {
                    isConnectedInternet = true;
                    infoAdapter.setListInfo(infos);
                    infoAdapter.getFilter().filter(sharedPrefManager.filterInfo());
                    tvSwipeHint.setVisibility(View.GONE);
                } else {
                    isConnectedInternet = false;
                    tvSwipeHint.setVisibility(View.VISIBLE);
                }

                displayLoading(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_kuliah_filter:
                if (sharedPrefManager.filterInfo().equals("all")) {
                    // Mode: Akademik
                    sharedPrefManager.setFilterInfo("akademik");
                    btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));

                    infoAdapter.getFilter().filter("akademik");

                    if (isSearching) {
                        infoAdapter.getFilter().filter("akademik");
                    }
                } else if (sharedPrefManager.filterInfo().equals("akademik")) {
                    // Mode: Akademik + Kuliah
                    sharedPrefManager.setFilterInfo("all");
                    btnFilterKuliah.setBackground(getResources().getDrawable(R.drawable.bg_tag_kuliah, null));

                    infoAdapter.getFilter().filter("all");

                    if (isSearching) {
                        infoAdapter.getFilter().filter("all");
                    }
                }
                break;

            case R.id.btn_main_akademik_filter:
                if (sharedPrefManager.filterInfo().equals("all")) {
                    // Mode: Kuliah
                    sharedPrefManager.setFilterInfo("kuliah");
                    btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_disabled, null));

                    infoAdapter.getFilter().filter("kuliah");

                    if (isSearching) {
                        infoAdapter.getFilter().filter("kuliah");
                    }
                } else if (sharedPrefManager.filterInfo().equals("kuliah")) {
                    // Mode: Kuliah + Akademik
                    sharedPrefManager.setFilterInfo("all");
                    btnFilterAkademik.setBackground(getResources().getDrawable(R.drawable.bg_tag_akademik, null));

                    infoAdapter.getFilter().filter("all");

                    if (isSearching) {
                        infoAdapter.getFilter().filter("all");
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
        if (isSearching) {
            isSearching = false;

            infoAdapter.setListInfo(defaultInfo);
            infoAdapter.getFilter().filter(sharedPrefManager.filterInfo());

            searchQuery = "";
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

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onRefresh() {
        if (isSearching) {
            searchViewModel.setSearchResults(searchQuery);
        } else {
            infoViewModel.setListInfo();
        }
    }

    private void displayLoading(boolean isLoading) {
        if (isLoading) {
            pbLoading.setVisibility(View.VISIBLE);
            rvInfoAkademik.setVisibility(View.GONE);
        } else {
            pbLoading.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            if (isConnectedInternet) {
                rvInfoAkademik.setVisibility(View.VISIBLE);
            } else {
                rvInfoAkademik.setVisibility(View.GONE);
            }
        }
    }
}
