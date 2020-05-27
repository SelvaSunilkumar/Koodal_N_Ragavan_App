package com.example.koodalnraghavan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideoPlayer extends AppCompatActivity {

    private TextView Playing;
    private Button download;
    //private VideoView videoView;
    private ProgressBar progressBar;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;
    private boolean flag;
    //private MediaController mediaController;
    private Bundle bundle;

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bundle = getIntent().getExtras();

        String playingNow = bundle.getString("list");
        String url = bundle.getString("url");
        //int flag = bundle.getInt("flag",0);
        flag = bundle.getBoolean("flag",false);

        System.out.println("Playing Now : " +playingNow);
        System.out.println("Flag : " + flag);

        Uri uri = Uri.parse(url);
        //mediaController = new MediaController(this);

        progressBar = findViewById(R.id.progress);
        exoPlayerView = findViewById(R.id.exoVideoPlayer);

        //videoView = findViewById(R.id.videoplayer);
        //videoView.setMediaController(mediaController);

        Playing = findViewById(R.id.playing);
        Playing.setText("Currently Playing : " + playingNow);
        Playing.setSelected(true);

        progressBar.setVisibility(View.VISIBLE);

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

            DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory(playingNow);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource source = new ExtractorMediaSource(uri,defaultHttpDataSourceFactory,extractorsFactory,null,null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(source);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if(playWhenReady && playbackState == ExoPlayer.STATE_READY)
                        progressBar.setVisibility(View.GONE);
                    if(playWhenReady && playbackState == ExoPlayer.STATE_BUFFERING)
                        progressBar.setVisibility(View.VISIBLE);
                    if(playWhenReady && playbackState == ExoPlayer.STATE_ENDED)
                        progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                    //Toast.makeText(getApplicationContext(),"Player Error",Toast.LENGTH_SHORT).show();
                    dialog = new AlertDialog.Builder(VideoPlayer.this);
                    dialog.setMessage("Couldn't play video");
                    dialog.setTitle("Player Error");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    alertDialog = dialog.create();
                    alertDialog.show();
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        //videoView.setVideoURI(uri);
        //videoView.start();
        //progressBar.setVisibility(View.GONE);

        /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                progressBar.setVisibility(View.GONE);
                if(videoView.isPlaying())
                    progressBar.setVisibility(View.GONE);
                if(!videoView.isPlaying())
                    progressBar.setVisibility(View.VISIBLE);
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                            progressBar.setVisibility(View.VISIBLE);
                        if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                });
            }
        });*/

        //progressBar.setVisibility(View.VISIBLE);

        download = findViewById(R.id.download);
        download.setVisibility(View.GONE);
        if(flag)
        {
            download.setVisibility(View.GONE);
        }
        else {
            download.setVisibility(View.VISIBLE);
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
}
