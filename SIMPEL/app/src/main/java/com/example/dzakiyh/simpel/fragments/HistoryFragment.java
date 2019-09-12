package com.example.dzakiyh.simpel.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.check_history.CheckHistoryAdapter;
import com.example.dzakiyh.simpel.model.FormulirSIMBaru;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView mRVHistoryDIsplayer;
    private CheckHistoryAdapter checkHistoryAdapter;

    private DatabaseReference fFormDatabase;
    private FirebaseAuth firebaseAuth;

    private List<FormulirSIMBaru> listForm = new ArrayList<>();


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mRVHistoryDIsplayer = getView().findViewById(R.id.RV_History);
        mRVHistoryDIsplayer.setLayoutManager(new LinearLayoutManager(getActivity()));

        fFormDatabase = FirebaseDatabase.getInstance().getReference().child("FormUntukHistory").child(firebaseAuth.getCurrentUser().getUid());
        fFormDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listForm.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String comotTanggal = (String)snapshot.child("tanggalKirim").getValue();
                    Log.d("Halo dari history + ", "tanggal adalah " + comotTanggal);
                    String comotJenis = (String)snapshot.child("jenisFormulir").getValue();
                    Log.d("Halo dari history + ", "jenis adalah " + comotJenis);

                    FormulirSIMBaru instFormHistory = new FormulirSIMBaru();
                    instFormHistory.setTanggalKirim(comotTanggal);
                    instFormHistory.setJenisFormulir(comotJenis);

                    listForm.add(instFormHistory);
                }

                checkHistoryAdapter = new CheckHistoryAdapter(getActivity(), listForm);
                mRVHistoryDIsplayer.setAdapter(checkHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
