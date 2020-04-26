package com.example.koodalnraghavan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class Musictab extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    public PdfLoader pdfLoader;

    private Intent nextActivity;

    public Musictab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_musictab, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DailyVideo");

        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String >(view.getContext(),R.layout.musicinfo,R.id.portal,list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    pdfLoader = ds.getValue(PdfLoader.class);
                    list.add(String.valueOf(pdfLoader.getPortal()));
                    url.add(String.valueOf(pdfLoader.getUrl()));
                }
                progressBar.setVisibility(View.GONE);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        nextActivity = new Intent(getContext(),VideoPlayer.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("list",list.get(position));
                        bundle.putString("url",url.get(position));
                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
