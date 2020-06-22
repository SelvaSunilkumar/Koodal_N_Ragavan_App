package com.example.koodalnraghavan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class Videotab extends Fragment {

    private ProgressBar progressBar;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String,List<DataLister>> listDataChild;
    private WebView webView;


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

    private MediaPlayer mediaPlayer;

    private Dialog music;
    private ImageView playAudio;
    private ImageView downloadAudio;
    private TextView Info;
    private ProgressBar progressBar1;
    private boolean flag = false;

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        webView.destroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videotab, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);
        webView = view.findViewById(R.id.web);

        prepareListData(view);



        listAdapter = new ExpandableListAdapter(view.getContext(),listDataHeader,listDataChild);
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

        mediaPlayer = new MediaPlayer();
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                DataLister dataLister = (DataLister) listAdapter.getChild(groupPosition,childPosition);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(dataLister.getUrl());

                /*music = new Dialog(view.getContext());
                music.setContentView(R.layout.music_player);
                music.show();

                playAudio = music.findViewById(R.id.pausePlay);
                downloadAudio = music.findViewById(R.id.download);
                downloadAudio.setVisibility(View.GONE);
                progressBar1 = music.findViewById(R.id.progress);
                Info = music.findViewById(R.id.playingNow);

                Info.setText(dataLister.getName());
                Info.setSelected(true);

                final String portalUrl = dataLister.getUrl();

                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(portalUrl);
                    mediaPlayer.prepare();
                    Toast.makeText(view.getContext(),"Play",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                        {
                            progressBar1.setVisibility(View.VISIBLE);
                        }
                        else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                        {
                            progressBar1.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
                playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!flag)
                        {
                            playAudio.setImageDrawable(getResources().getDrawable(R.drawable.play_er));
                            flag = true;
                            mediaPlayer.pause();
                        }
                        else
                        {
                            playAudio.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                            flag = false;
                            mediaPlayer.start();
                        }
                    }
                });


                music.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mediaPlayer.stop();
                    }
                });*/
                return false;
            }
        });

        //---------------------------------------------------------------------------------
        return view;
    }

    private void prepareListData(View view) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<DataLister>>();

        counter = 0;

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("audio");

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
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        queue.add(request);
    }
}
