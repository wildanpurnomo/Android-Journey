package com.example.eiga.ui.favorite;


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
public class FavoriteNavigation extends Fragment {
    private static final String EXTRA_STATE = "extra_state";

    private FavoriteDisplayer favoriteMovDisplayer = new FavoriteDisplayer();
    private FavoriteDisplayer favoriteTVDisplayer = new FavoriteDisplayer();
    private Fragment activeFragment;

    private int indexPage;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_movie:
                    indexPage = 0;
                    getChildFragmentManager().beginTransaction()
                            .hide(activeFragment)
                            .show(favoriteMovDisplayer)
                            .commit();
                    activeFragment = favoriteMovDisplayer;
                    return true;

                case R.id.navigation_tv:
                    indexPage = 1;
                    getChildFragmentManager().beginTransaction()
                            .hide(activeFragment)
                            .show(favoriteTVDisplayer)
                            .commit();
                    activeFragment = favoriteTVDisplayer;
                    return true;
            }
            return false;
        }
    };


    public FavoriteNavigation() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        BottomNavigationView navigationView = root.findViewById(R.id.nav_mov_tv);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            indexPage = savedInstanceState.getInt(EXTRA_STATE, 0);

            favoriteMovDisplayer = (FavoriteDisplayer) getChildFragmentManager().getFragment(savedInstanceState, "movFav");
            favoriteTVDisplayer = (FavoriteDisplayer) getChildFragmentManager().getFragment(savedInstanceState, "tvFav");

            if (indexPage == 0) {
                activeFragment = favoriteMovDisplayer;
            } else {
                activeFragment = favoriteTVDisplayer;
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

        outState.putInt(EXTRA_STATE, indexPage);

        getChildFragmentManager().putFragment(outState, "movFav", favoriteMovDisplayer);
        getChildFragmentManager().putFragment(outState, "tvFav", favoriteTVDisplayer);
    }

    private void initFragment() {
        Bundle bundleMov = new Bundle();
        Bundle bundleTV = new Bundle();

        bundleMov.putInt(FavoriteDisplayer.ARG_SECTION_NUMBER, 0);
        bundleTV.putInt(FavoriteDisplayer.ARG_SECTION_NUMBER, 1);

        favoriteMovDisplayer.setArguments(bundleMov);
        favoriteTVDisplayer.setArguments(bundleTV);

        indexPage = 0;

        getChildFragmentManager().beginTransaction()
                .add(R.id.fl_fav, favoriteTVDisplayer, "tvFav")
                .hide(favoriteTVDisplayer)
                .add(R.id.fl_fav, favoriteMovDisplayer, "movFav")
                .commit();
        activeFragment = favoriteMovDisplayer;
    }
}
