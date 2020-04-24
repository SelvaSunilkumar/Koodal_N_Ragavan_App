package com.example.koodalnraghavan;

import android.app.DownloadManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.spec.ECField;
import java.util.ArrayList;


public class FreeCallerTone extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private ImageView Pause;
    private ImageView Stop;
    private Button DownloadButton;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private MediaPlayer mediaPlayer;

    public PdfLoader pdfLoader;

    private int playpauseCounter;

    int icons[] = {R.drawable.pause_icon,R.drawable.play_icon};


    public FreeCallerTone() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_caller_tone, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);
        Pause = view.findViewById(R.id.pause);
        Stop = view.findViewById(R.id.stop);
        DownloadButton = view.findViewById(R.id.download);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("FreeCallerTune");

        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String >(view.getContext(),R.layout.callertuneinfo,R.id.portal,list);

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
                DownloadButton.setEnabled(false);
                listView.setAdapter(adapter);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                        String portalUrl = url.get(position);

                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(portalUrl);
                            mediaPlayer.prepare();
                            Toast.makeText(view.getContext(),"Play",Toast.LENGTH_SHORT).show();
                            mediaPlayer.start();
                            playpauseCounter=0;
                            DownloadButton.setEnabled(true);
                        }
                        catch (Exception e)
                        {
                            System.out.print("Exception Caught : " + e);
                        }

                        Pause.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                playpauseCounter++;

                                if(playpauseCounter%2 ==0)
                                {
                                    mediaPlayer.start();
                                    Pause.setBackgroundResource(icons[0]);
                                }
                                else {
                                    mediaPlayer.pause();
                                    Pause.setBackgroundResource(icons[1]);
                                }
                            }
                        });

                        Stop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer.stop();
                                return;
                            }
                        });

                        DownloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(view.getContext(),"Clicked :"+list.get(position),Toast.LENGTH_SHORT).show();
                            }
                        });
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
