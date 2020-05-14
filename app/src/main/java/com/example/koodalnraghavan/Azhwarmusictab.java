package com.example.koodalnraghavan;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Azhwarmusictab extends Fragment  {

    private ListView listView;
    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private ImageView logo;
    private ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    Azhwaraudios azhwaraudios;

    int background = R.drawable.folder;

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
        progressBar = view.findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("AlvargalinManam").child("Audio").child("Folders");
        list = new ArrayList<>();

        url = new ArrayList<>();

        View view1 = inflater.inflate(R.layout.videoinfo,container,false);
        view1.findViewById(R.id.imageIcon).setBackgroundResource(R.drawable.folder);
        //logo.setBackgroundResource(R.drawable.folder);
        //logo.setBackground(background);
        //logo = view.findViewById(R.id.imageIcon);
        //logo.setBackgroundResource(R.drawable.folder);
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.folder_info,R.id.portal,list);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    progressBar.setVisibility(View.VISIBLE);
                    azhwaraudios = ds.getValue(Azhwaraudios.class);
                    list.add(String.valueOf(azhwaraudios.getName()));
                    // bundle.putString("url",String.valueOf(azhwaraudios.getName()));
                }

                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(getContext(),"Folder : " + list.get(position),Toast.LENGTH_SHORT).show();

                        Intent nextActivity = new Intent(view.getContext(),MusicLister.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("foldername",list.get(position));
                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);



                        /*if(position == 0){
                            //code has to modified here
                            Intent intent = new Intent(view.getContext(),Azhwaraudiopart1.class);
                            startActivity(intent);
                        }
                        if(position == 1){
                            Intent intent = new Intent(view.getContext(), Azhwaraudiopart2.class);
                            startActivity(intent);
                        }*/
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
