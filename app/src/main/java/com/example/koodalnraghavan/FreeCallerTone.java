package com.example.koodalnraghavan;

import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.spec.ECField;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class FreeCallerTone extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private ImageView Pause;
    private ImageView Stop;
    private Button DownloadButton;
    private TextView CurrentSong;

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
        CurrentSong = view.findViewById(R.id.playinginfo);
        CurrentSong.setSelected(true);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("FreeCallerTune");

        list = new ArrayList<String>();
        url = new ArrayList<String>();

        mediaPlayer = new MediaPlayer();

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
                Pause.setEnabled(false);
                Stop.setEnabled(false);
                listView.setAdapter(adapter);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                        final String portalUrl = url.get(position);

                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(portalUrl);
                            mediaPlayer.prepare();
                            Toast.makeText(view.getContext(),"Play",Toast.LENGTH_SHORT).show();
                            mediaPlayer.start();
                            playpauseCounter=0;
                            DownloadButton.setEnabled(true);
                            Pause.setEnabled(true);
                            Stop.setEnabled(true);
                            CurrentSong.setText("Playing now : " + list.get(position));
                            CurrentSong.setSelected(true);
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
                                Pause.setEnabled(false);
                                return;
                            }
                        });

                        DownloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(view.getContext(),"Clicked :"+list.get(position),Toast.LENGTH_SHORT).show();

                                Uri uri = Uri.parse(portalUrl);

                                DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                DownloadManager.Request request = new DownloadManager.Request(uri);

                                Context context = view.getContext();
                                String filename = list.get(position).toString();
                                String fileExtension = ".mp3";

                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtension);

                                downloadManager.enqueue(request);
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
