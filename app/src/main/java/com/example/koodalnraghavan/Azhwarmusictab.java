package com.example.koodalnraghavan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Azhwarmusictab extends Fragment  {

    private ListView listView;
    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    Azhwaraudios azhwaraudios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_azhwarmusictab,container,false);
        azhwaraudios = new Azhwaraudios();
        listView = view.findViewById(R.id.listView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("folders");
        list = new ArrayList<>();

        url = new ArrayList<>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.videoinfo,R.id.portal,list);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    azhwaraudios = ds.getValue(Azhwaraudios.class);
                    list.add(String.valueOf(azhwaraudios.getName()));
                    // bundle.putString("url",String.valueOf(azhwaraudios.getName()));
                }

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            Intent intent = new Intent(view.getContext(),Azhwaraudiopart1.class);
                            startActivity(intent);
                        }
                        if(position == 1){
                            Intent intent = new Intent(view.getContext(), Azhwaraudiopart2.class);
                            startActivity(intent);
                        }
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
