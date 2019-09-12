package com.example.eiga.ui.list;


import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.eiga.R;
import com.example.eiga.model.Show;
import com.example.eiga.ui.search.SearchAdapter;
import com.example.eiga.ui.search.SearchViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDisplayer extends Fragment implements SearchView.OnQueryTextListener {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String OUTSTATE = "outstate";

    private RecyclerView rvTrending;
    private RecyclerView rvPopular;
    private SearchAdapter searchAdapter = new SearchAdapter();

    private RelativeLayout containerPopular, containerTrending, containerSearch;
    private ProgressBar progressBar;
    private SearchView searchView;
    private Toolbar toolbarSearch;

    private ListViewModel listViewModel;
    private SearchViewModel searchViewModel;

    private int indexPage;
    private boolean isSearching;
    private String searchQuery;

    public ListDisplayer() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        if (getArguments() != null) {
            indexPage = getArguments().getInt(ARG_SECTION_NUMBER, 0);
        }
        listViewModel.setListPopular(indexPage);
        listViewModel.setListTrending(indexPage);

        if (savedInstanceState != null) {
            isSearching = savedInstanceState.getBoolean(OUTSTATE);
            searchQuery = savedInstanceState.getString(OUTSTATE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_displayer, container, false);

        toolbarSearch = root.findViewById(R.id.toolbar_search);
        rvTrending = root.findViewById(R.id.rv_trending);
        rvPopular = root.findViewById(R.id.rv_popular);
        RecyclerView rvSearch = root.findViewById(R.id.rv_search);
        containerPopular = root.findViewById(R.id.rl_container_popular);
        containerTrending = root.findViewById(R.id.rl_container_trending);
        containerSearch = root.findViewById(R.id.rl_container_search);
        progressBar = root.findViewById(R.id.pb_list);

        initToolbarSearch();

        searchAdapter.setActivity(getActivity());
        searchAdapter.setIndexHelper(indexPage);
        rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSearch.setAdapter(searchAdapter);

        final ListItemDecoration listItemDecoration = new ListItemDecoration(getResources().getDimensionPixelSize(R.dimen.grid_spacing));

        displayLoading(true);

        listViewModel.getListPopular().observe(getViewLifecycleOwner(), new Observer<ArrayList<Show>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Show> shows) {
                ListAdapter listAdapter = new ListAdapter(getActivity(), shows, indexPage);
                rvPopular.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                rvPopular.setAdapter(listAdapter);
                rvPopular.addItemDecoration(listItemDecoration);
                listAdapter.notifyDataSetChanged();
                displayLoading(false);
            }
        });

        listViewModel.getListTrending().observe(getViewLifecycleOwner(), new Observer<ArrayList<Show>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Show> shows) {
                ListAdapter listAdapter = new ListAdapter(getActivity(), shows, indexPage);
                rvTrending.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                rvTrending.setAdapter(listAdapter);
                rvTrending.addItemDecoration(listItemDecoration);
                listAdapter.notifyDataSetChanged();
                displayLoading(false);
            }
        });

        return root;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        displayLoading(true);

        searchQuery = s.toLowerCase().trim().replaceAll("\\s", "-");
        searchViewModel.setSearchResults(indexPage, searchQuery);
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<ArrayList<Show>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Show> shows) {
                if (!searchViewModel.isLoading) {
                    searchAdapter.setListSearch(shows);
                    displayLoading(false);
                }
            }
        });
        searchView.clearFocus();
        isSearching = true;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchQuery = s;
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (searchView != null) {
            outState.putString(OUTSTATE, searchQuery);
            outState.putBoolean(OUTSTATE, isSearching);
        }
    }

    private void initToolbarSearch() {
        toolbarSearch.inflateMenu(R.menu.search_menu);
        if (indexPage == 0) {
            toolbarSearch.setTitle(getResources().getString(R.string.search_mov));
        } else {
            toolbarSearch.setTitle(getResources().getString(R.string.search_tv));
        }

        final MenuItem itemSearch = toolbarSearch.getMenu().getItem(0);
        itemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (isSearching) {
                    containerSearch.setVisibility(View.GONE);
                    containerPopular.setVisibility(View.VISIBLE);
                    containerTrending.setVisibility(View.VISIBLE);

                    searchViewModel.getSearchResults().removeObservers(getViewLifecycleOwner());
                    searchQuery = "";
                    isSearching = false;
                }
                searchView.clearFocus();
                return true;
            }
        });

        toolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSearch.expandActionView();
            }
        });

        if (getActivity() != null) {
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            if (searchManager != null) {
                searchView = (SearchView) itemSearch.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setOnQueryTextListener(this);

                if (searchQuery != null && !searchQuery.isEmpty()) {
                    itemSearch.expandActionView();
                    searchView.setQuery(searchQuery, false);
                    searchView.clearFocus();
                }
            }
        }
    }

    private void displayLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            containerTrending.setVisibility(View.GONE);
            containerPopular.setVisibility(View.GONE);
            containerSearch.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            if (isSearching) {
                containerSearch.setVisibility(View.VISIBLE);
            } else {
                containerTrending.setVisibility(View.VISIBLE);
                containerPopular.setVisibility(View.VISIBLE);
            }
        }
    }
}
