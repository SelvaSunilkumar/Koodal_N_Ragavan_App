package com.example.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class SongViewer extends AppCompatActivity {

    private Bundle bundle;
    private ImageView stop;
    private ImageView pause;
    int pauseplaycounter;
    private MediaPlayer mediaPlayer;

    int icons[]={R.drawable.play_icon,R.drawable.pause_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_viewer);

        stop = findViewById(R.id.stop);
        pause = findViewById(R.id.pause);

        mediaPlayer = new MediaPlayer();

        pauseplaycounter=0;

        bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try{
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            Toast.makeText(SongViewer.this,"Play",Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            pauseplaycounter=0;
        }
        catch(Exception e){
            System.out.println("Exception Caught : " + e);
        }

        pause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                pauseplaycounter++;

                if(pauseplaycounter%2==0)
                {
                    mediaPlayer.start();
                    pause.setBackgroundResource(icons[0]);
                }
                else
                {
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
}
