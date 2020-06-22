package com.example.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubFolder extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;
    private int folderNumber;

    private TextView folderName;

    private ExpandableVideoLoader listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String,List<DataLister>> listDataChild;

    private String tempFolderName;
    private int counter;

    private final String JSON_URL = "https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject folder;
    private String folder_name;
    private String audio_url;
    private String audio_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_folder);

        bundle = getIntent().getExtras();

        folderNumber = bundle.getInt("number");

        folderName = findViewById(R.id.folderName);

        listView = findViewById(R.id.listView);

        if(folderNumber ==  0)
        {
            folderName.setText("Thinam oru nal vaarthai thirumaalai patri");

            prepareThirumalaiList(getApplicationContext());
        }
        else
        {
            folderName.setText("Thinam oru nal vaarthai");

            prepareList(getApplicationContext());
        }

        listAdapter = new ExpandableVideoLoader(SubFolder.this,listDataHeader,listDataChild);
        listView.setAdapter(listAdapter);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                DataLister dataLister = (DataLister) listAdapter.getChild(groupPosition,childPosition);

                intent = new Intent(SubFolder.this,VideoPlayer.class);

                bundle = new Bundle();
                bundle.putString("list",dataLister.getName());
                bundle.putString("url",dataLister.getUrl());

                intent.putExtras(bundle);
                startActivity(intent);

                return false;
            }
        });
    }

    private void prepareList(Context applicationContext) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<DataLister>>();

        counter = 0;

        queue = Volley.newRequestQueue(applicationContext);
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("video");

                            for (int i = 0; i < jsonArray.length() ; i++ )
                            {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");
                                if( ! (listDataHeader.contains(folder_name)) )
                                {
                                    listDataHeader.add(folder_name);
                                    counter++;

                                    List<DataLister> list = new ArrayList<DataLister>();
                                    audio_name = folder.getString("name");
                                    list.add(new DataLister(audio_name,folder.getString("url")));

                                    for (int j = 0 ; j < jsonArray.length() ; j++ )
                                    {
                                        if( i != j )
                                        {
                                            folder = jsonArray.getJSONObject(j);
                                            tempFolderName = folder.getString("folder");

                                            if(tempFolderName.equals(folder_name))
                                            {
                                                audio_name = folder.getString("name");
                                                list.add(new DataLister(audio_name,folder.getString("url")));
                                            }
                                        }
                                    }
                                    listDataChild.put(listDataHeader.get(counter-1),list);
                                    listAdapter.notifyDataSetChanged();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SubFolder.this,"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    private void prepareThirumalaiList(Context applicationContext) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<DataLister>>();

        counter = 0;

        queue = Volley.newRequestQueue(applicationContext);
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("thirumalai");

                            for (int i = 0; i < jsonArray.length() ; i++ )
                            {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");
                                if( ! (listDataHeader.contains(folder_name)) )
                                {
                                    listDataHeader.add(folder_name);
                                    counter++;

                                    List<DataLister> list = new ArrayList<DataLister>();
                                    audio_name = folder.getString("name");
                                    list.add(new DataLister(audio_name,folder.getString("url")));

                                    for (int j = 0 ; j < jsonArray.length() ; j++ )
                                    {
                                        if( i != j )
                                        {
                                            folder = jsonArray.getJSONObject(j);
                                            tempFolderName = folder.getString("folder");

                                            if(tempFolderName.equals(folder_name))
                                            {
                                                audio_name = folder.getString("name");
                                                list.add(new DataLister(audio_name,folder.getString("url")));
                                            }
                                        }
                                    }
                                    listDataChild.put(listDataHeader.get(counter-1),list);
                                    listAdapter.notifyDataSetChanged();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SubFolder.this,"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        queue.add(request);
    }
}
