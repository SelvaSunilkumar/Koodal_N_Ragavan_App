package com.example.koodalnraghavan;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

public class AudioPurchase extends Fragment {

    private ListView listView;
    private LinearLayout linearLayout;
    private TextView textView;
    private ProgressBar progressBar;
    private ImageView Stop;
    private ImageView Pause;
    private Button Download;
    private TextView PlayinSong;


    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private AlvargalinManamDatabaseHelper databaseHelper;
    private Cursor data;
    private int pausePlayCounter;
    int icons[] = {R.drawable.pause_icon , R.drawable.play_icon};
    private MediaPlayer mediaPlayer;

    @Override
    public void onStop() {
        super.onStop();
        //mediaPlayer.release();
    }

    public AudioPurchase() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_purchase, container, false);

        listView = view.findViewById(R.id.listView);
        linearLayout = view.findViewById(R.id.controller);
        textView = view.findViewById(R.id.warner);
        progressBar = view.findViewById(R.id.progress);
        Stop = view.findViewById(R.id.stop);
        Pause = view.findViewById(R.id.pause);
        PlayinSong = view.findViewById(R.id.playinginfo);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.videoinfo,R.id.portal,list);

        databaseHelper = new AlvargalinManamDatabaseHelper(view.getContext());
        data = databaseHelper.fetchSongList();

        mediaPlayer = new MediaPlayer();

        if(data.getCount() == 0)
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Purchases so for");
            Toast.makeText(view.getContext(),"No Purchases so Far",Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
        }
        else {
            PlayinSong.setText("Select a Song to Play...");
            PlayinSong.setSelected(true);

            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            textView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);

            while (data.moveToNext())
            {
                list.add(String.valueOf(data.getString(0)));
                url.add(String.valueOf(data.getString(1)));
            }
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(view.getContext(),list.get(position) + url.get(position),Toast.LENGTH_SHORT).show();

                    try {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        //mediaPlayer.setDataSource(view.getContext(), Uri.parse(url.get(position)));
                        mediaPlayer.setDataSource(getActivity(),Uri.parse(url.get(position)));
                        mediaPlayer.prepare();
                        Toast.makeText(view.getContext(),"Play",Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();

                        pausePlayCounter = 0;

                        PlayinSong.setText("Playing now : " + list.get(position));
                        PlayinSong.setSelected(true);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(view.getContext(),"Player couldn't Start",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            pausePlayCounter++;

                            if(pausePlayCounter%2 == 0)
                            {
                                mediaPlayer.start();
                                Pause.setBackgroundResource(icons[0]);
                            }
                            else
                            {
                                mediaPlayer.pause();
                                Pause.setBackgroundResource(icons[1]);
                            }
                        }
                    });

                    Stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mediaPlayer.stop();

                            PlayinSong.setText("Stopped : " + list.get(position));
                            PlayinSong.setSelected(true);
                            return;
                        }
                    });
                }
            });
        }
        return view;
    }
}
