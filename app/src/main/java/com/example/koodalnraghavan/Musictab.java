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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class Musictab extends Fragment {

    private LinearLayout folder1;
    private LinearLayout folder2;

    private Intent subFolderActivity;
    private Bundle bundle;

    public Musictab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_musictab, container, false);

        folder1 = view.findViewById(R.id.folder1);
        folder2 = view.findViewById(R.id.folder2);

        folder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(),"Folder 1",Toast.LENGTH_SHORT).show();

                subFolderActivity = new Intent(view.getContext(),SubFolder.class);

                bundle = new Bundle();
                bundle.putInt("number",0);

                subFolderActivity.putExtras(bundle);
                startActivity(subFolderActivity);
            }
        });

        folder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(),"Folder 2",Toast.LENGTH_SHORT).show();

                subFolderActivity = new Intent(view.getContext(),SubFolder.class);

                bundle = new Bundle();
                bundle.putInt("number",1);

                subFolderActivity.putExtras(bundle);
                startActivity(subFolderActivity);
            }
        });

        return view;
    }


}
