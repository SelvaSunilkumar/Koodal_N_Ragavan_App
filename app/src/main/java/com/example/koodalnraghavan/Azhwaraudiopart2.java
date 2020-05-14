package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;

public class Azhwaraudiopart2 extends AppCompatActivity {
    private ListView listView;
    private ImageView stop;
    private ImageView pause;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private MediaPlayer mediaPlayer;
    int pauseplaycounter;
    private Button Download;
    private ProgressBar progressBar;
    private TextView PlayingSong;

    Azhwarmusic1 azhwarmusic1;

    int icons[] = {R.drawable.play_icon, R.drawable.pause_icon};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azhwaraudiopart2);

        listView = findViewById(R.id.lv2);
        stop = findViewById(R.id.stop);
        pause = findViewById(R.id.pause);

        //PlayingSong.setSelected(true);
        //Download.setEnabled(false);
        //Download.setSelected(true);

        pauseplaycounter = 0;

        mediaPlayer = new MediaPlayer();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("part2");
        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.videoinfo, R.id.portal, list);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                azhwarmusic1 = new Azhwarmusic1();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    azhwarmusic1 = ds.getValue(Azhwarmusic1.class);
                    list.add(String.valueOf(azhwarmusic1.getTitle()));
                    url.add(String.valueOf(azhwarmusic1.getUrl()));
                }
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String portalUrl = url.get(position);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(portalUrl);
                            mediaPlayer.prepare();
                            Toast.makeText(Azhwaraudiopart2.this, "Play", Toast.LENGTH_SHORT).show();
                            mediaPlayer.start();
                            pauseplaycounter = 0;

                            /*PlayingSong.setText("Playing now : " +list.get(position));
                            PlayingSong.setSelected(true);
                            Download.setEnabled(true);
                            Download.setText("Download");
                            Download.setSelected(true);*/
                        } catch (Exception e) {
                            System.out.println("Exception Caught : " + e);
                        }
                        pause.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(View v) {
                                pauseplaycounter++;

                                if (pauseplaycounter % 2 == 0) {
                                    mediaPlayer.start();
                                    pause.setBackgroundResource(icons[0]);
                                } else {
                                    mediaPlayer.pause();
                                    pause.setBackgroundResource(icons[1]);

                                }
                            }
                        });
                        stop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer.stop();
                                return;
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

