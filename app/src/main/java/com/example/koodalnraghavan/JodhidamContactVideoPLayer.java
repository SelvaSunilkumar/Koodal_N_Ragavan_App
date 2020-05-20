package com.example.koodalnraghavan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class JodhidamContactVideoPLayer extends AppCompatActivity {

    //private VideoView videoView;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private TextView currentlyPlaying;
    private Button DownloadVideo;
    private TextView phoneNumber;
    private TextView websiteLink;
    private ProgressBar progressBar;

    private Intent intent;
    private Bundle bundle;
    //private MediaController mediaController;
    private Uri uri;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private String videoLink;
    private String videoName;
    private String ContactNumber;
    private String WebsiteUrl;

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jodhidam_contact_video_p_layer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //videoView = findViewById(R.id.videoplayer);
        exoPlayerView = findViewById(R.id.exoVideoPlayer);
        currentlyPlaying = findViewById(R.id.playingNow);
        DownloadVideo = findViewById(R.id.download);
        phoneNumber = findViewById(R.id.phoneNumber);
        websiteLink = findViewById(R.id.websiteLink);
        progressBar = findViewById(R.id.progress);

        ContactNumber = "9894063660";
        WebsiteUrl = "http://www.kavignakoodalnraghavan.com";

        bundle = getIntent().getExtras();

        videoLink = bundle.getString("url");
        videoName = bundle.getString("list");

        currentlyPlaying.setText("Currently Playing : " + videoName);
        currentlyPlaying.setSelected(true);

        uri = Uri.parse(videoLink);

        progressBar.setVisibility(View.VISIBLE);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

            DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory(videoName);
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

        /*mediaController = new MediaController(this);

        currentlyPlaying.setText("Playing Now : " + videoName);
        currentlyPlaying.setSelected(true);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        progressBar.setVisibility(View.VISIBLE);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                progressBar.setVisibility(View.GONE);

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                            progressBar.setVisibility(View.VISIBLE);
                        if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            progressBar.setVisibility(View.GONE);
                        if(videoView.isPlaying())
                            progressBar.setVisibility(View.GONE);
                        if(!videoView.isPlaying())
                            progressBar.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
            }
        });*/

        DownloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                Context context = v.getContext();
                String filename = videoName;
                String fileExtension = ".mp4";

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtension);

                downloadManager.enqueue(request);
            }
        });

        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(JodhidamContactVideoPLayer.this);
                dialog.setMessage("do you wish to call : " + ContactNumber);
                dialog.setTitle("Redirecting ...");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callThem = new Intent(Intent.ACTION_DIAL);
                        callThem.setData(Uri.parse("tel:" + ContactNumber));
                        startActivity(callThem);
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(JodhidamContactVideoPLayer.this);
                dialog.setMessage("Website : " + WebsiteUrl );
                dialog.setTitle("Redirecting ...");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(WebsiteUrl);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(launchBrowser);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }
}
