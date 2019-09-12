package com.example.dzakiyh.simpel.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.login_and_register.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    private TextView namaLengkapDisplayer, usernameDisplayer;
    private Button signOut;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fUserDatabase;


    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firebaseAuth = FirebaseAuth.getInstance();

        return inflater.inflate(R.layout.fragment_my_account, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        initComponents();

        updateDisplayers();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

    }

    private void initComponents() {

        namaLengkapDisplayer = getView().findViewById(R.id.TV_AccFragment_DisplayNamaLengkap);
        usernameDisplayer = getView().findViewById(R.id.TV_AccFragment_DisplayUsername);
        signOut = getView().findViewById(R.id.BTN_AccFragment_SignOut);
    }

    private void updateDisplayers(){
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comotNamaLengkap = dataSnapshot.child("namaLengkap").getValue().toString();
                String comotUsername = dataSnapshot.child("username").getValue().toString();

                namaLengkapDisplayer.setText(comotNamaLengkap);
                usernameDisplayer.setText("Username : " + comotUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
