package com.example.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideoPlayer extends AppCompatActivity {

    private TextView Playing;
    private Button download;
    private VideoView videoView;
    private ProgressBar progressBar;

    private MediaController mediaController;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        bundle = getIntent().getExtras();

        String playingNow = bundle.getString("list");
        String url = bundle.getString("url");

        Uri uri = Uri.parse(url);
        mediaController = new MediaController(this);

        progressBar = findViewById(R.id.progress);

        videoView = findViewById(R.id.videoplayer);
        videoView.setMediaController(mediaController);

        Playing = findViewById(R.id.playing);
        Playing.setText("Currently Playing : " + playingNow);
        Playing.setSelected(true);

        progressBar.setVisibility(View.GONE);
        videoView.setVideoURI(uri);
        videoView.start();

        //progressBar.setVisibility(View.VISIBLE);

        download = findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadManager downloadManager = (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                Context context = v.getContext();
                String filename = playingNow;
                String fileExtension = ".mp4";

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtension);

                downloadManager.enqueue(request);

            }
        });
    }
}
