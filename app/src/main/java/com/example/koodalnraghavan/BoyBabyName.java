package com.example.koodalnraghavan;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class BoyBabyName extends Fragment {

    private ListView listView;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private AzhwarVideoDatabaseHelper databaseHelper;
    private Cursor data;

    public BoyBabyName() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boy_baby_name, container, false);

        listView = view.findViewById(R.id.listView);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.musicinfo,R.id.portal,list);

        databaseHelper = new AzhwarVideoDatabaseHelper(view.getContext());
        data = databaseHelper.fetchVideoList();

        if(data.getCount() == 0)
        {
            Toast.makeText(view.getContext(),"No purchases so far",Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(data.moveToNext())
            {
                list.add(String.valueOf(data.getString(0)));
                url.add(String.valueOf(data.getString(1)));
            }
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });
        }

        return view;
    }
}
