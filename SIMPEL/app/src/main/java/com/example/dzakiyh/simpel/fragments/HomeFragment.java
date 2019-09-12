package com.example.dzakiyh.simpel.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.latihan_soal.LatihanSoalActivity;
import com.example.dzakiyh.simpel.panduan.PanduanNavigationActivity;
import com.example.dzakiyh.simpel.pembuatan_sim_baru.NavigasiDaftarActivity;
import com.example.dzakiyh.simpel.perpanjangan_sim.NavigasiPerpanjanganSIMActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private CardView containerSIMBaru, containerPerpanjanganSIM, containerPanduan, containerLatihanSoal;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initComponent();

        containerSIMBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NavigasiDaftarActivity.class);
                startActivity(i);
            }
        });

        containerPerpanjanganSIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NavigasiPerpanjanganSIMActivity.class);
                startActivity(i);
            }
        });

        containerPanduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PanduanNavigationActivity.class);
                startActivity(i);
            }
        });

        containerLatihanSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LatihanSoalActivity.class);
                startActivity(i);
            }
        });
    }

    private void initComponent() {

        containerSIMBaru = getView().findViewById(R.id.containerSIMBaruMenu);
        containerPerpanjanganSIM = getView().findViewById(R.id.containerPerpanjanganSIM);
        containerPanduan = getView().findViewById(R.id.containerPanduan);
        containerLatihanSoal = getView().findViewById(R.id.containerLatihanSoal);
    }
}
