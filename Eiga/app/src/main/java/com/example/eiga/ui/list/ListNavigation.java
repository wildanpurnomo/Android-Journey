package com.example.eiga.ui.list;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.eiga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListNavigation extends Fragment {
    private static final String STATE_HELPER = "state_helper";

    private ListDisplayer listDisplayerMovie = new ListDisplayer();
    private ListDisplayer listDisplayerTV = new ListDisplayer();
    private Fragment activeFragment;

    private int indexNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.navigation_movie:
                    indexNavigation = 0;
                    getChildFragmentManager().beginTransaction()
                            .hide(activeFragment).show(listDisplayerMovie)
                            .commit();
                    activeFragment = listDisplayerMovie;
                    return true;

                case R.id.navigation_tv:
                    indexNavigation = 1;
                    getChildFragmentManager().beginTransaction()
                            .hide(activeFragment).show(listDisplayerTV)
                            .commit();
                    activeFragment = listDisplayerTV;
                    return true;
            }
            return false;
        }
    };

    public ListNavigation() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        BottomNavigationView navigationView = root.findViewById(R.id.nav_mov_tv);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            int savedIndex = savedInstanceState.getInt(STATE_HELPER, 0);
            listDisplayerMovie = (ListDisplayer) getChildFragmentManager().getFragment(savedInstanceState, "mov");
            listDisplayerTV = (ListDisplayer) getChildFragmentManager().getFragment(savedInstanceState, "tv");

            if (savedIndex == 0) {
                activeFragment = listDisplayerMovie;
            } else {
                activeFragment = listDisplayerTV;
            }
        } else {
            if (activeFragment == null) {
                initFragment();
            }
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_HELPER, indexNavigation);

        getChildFragmentManager().putFragment(outState, "mov", listDisplayerMovie);
        getChildFragmentManager().putFragment(outState, "tv", listDisplayerTV);
    }

    private void initFragment() {
        Bundle bundleMov = new Bundle();
        Bundle bundleTV = new Bundle();

        bundleMov.putInt(ListDisplayer.ARG_SECTION_NUMBER, 0);
        bundleTV.putInt(ListDisplayer.ARG_SECTION_NUMBER, 1);

        listDisplayerMovie.setArguments(bundleMov);
        listDisplayerTV.setArguments(bundleTV);

        indexNavigation = 0;
        getChildFragmentManager().beginTransaction()
                .add(R.id.fl_list, listDisplayerTV, "tv")
                .hide(listDisplayerTV)
                .add(R.id.fl_list, listDisplayerMovie, "mov")
                .commit();

        activeFragment = listDisplayerMovie;
    }
}
