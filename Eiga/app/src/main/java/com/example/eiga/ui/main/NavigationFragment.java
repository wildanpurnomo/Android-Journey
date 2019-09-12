package com.example.eiga.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eiga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment {


    public NavigationFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_navigation, container, false);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
        final CustomViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabs = root.findViewById(R.id.tabs_main);
        tabs.setupWithViewPager(viewPager);

        return root;
    }

}
