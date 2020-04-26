package com.example.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class VideoPlayer extends AppCompatActivity {

    private TextView Playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Playing = findViewById(R.id.playing);
        Playing.setSelected(true);
    }
}
