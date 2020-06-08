package com.example.koodalnraghavan;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BoyBabyName extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    //private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private BabyNamer namer;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public BoyBabyName() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boy_baby_name, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        list = new ArrayList<String>();
        //url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.baby_names,R.id.name,list);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("BabyNames").child("Boys");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                namer = new BabyNamer();
                progressBar.setVisibility(View.VISIBLE);

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    namer = ds.getValue(BabyNamer.class);

                    list.add(String.valueOf(namer.getName_tml()));
                }

                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
