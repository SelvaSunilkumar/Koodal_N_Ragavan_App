package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.koodalnraghavan.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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
import com.google.android.exoplayer2.ui.PlayerView;
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
    private TextView ContactUs;
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

    private SimpleExoPlayer simpleExoPlayerView;
    //private ProgressBar progressBar;
    private ImageView fullscreen;
    private PlayerView playerView;
    LinearLayout layout;

    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jodhidam_contact_video_p_layer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //videoView = findViewById(R.id.videoplayer);
        DownloadVideo = findViewById(R.id.download);
        progressBar = findViewById(R.id.progress);
        ContactUs = findViewById(R.id.contact);
        currentlyPlaying = findViewById(R.id.playingInfo);

        ContactNumber = "9894063660";
        WebsiteUrl = "http://www.kavignakoodalnraghavan.com";

        bundle = getIntent().getExtras();

        videoLink = bundle.getString("url");
        videoName = bundle.getString("list");

        playerView = findViewById(R.id.player);
        progressBar = findViewById(R.id.progress);
        fullscreen = findViewById(R.id.fullscreen);
        layout = findViewById(R.id.customer);

        currentlyPlaying.setText(videoName);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Uri videoUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/database-97af9.appspot.com/o/Jothidam%2Fmoodiya%20aalayanga%20yendru%20thirakkum.mp4?alt=media&token=2451ae2b-0912-4929-9ef2-4848e941edca");

        Uri videoUrl = Uri.parse(videoLink);

        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayerView = ExoPlayerFactory.newSimpleInstance(this,trackSelector,loadControl);

        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(videoUrl,factory,extractorsFactory,null,null);

        playerView.setPlayer(simpleExoPlayerView);

        playerView.setKeepScreenOn(true);
        simpleExoPlayerView.prepare(mediaSource);
        simpleExoPlayerView.setPlayWhenReady(true);

        simpleExoPlayerView.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if(playbackState == Player.STATE_BUFFERING)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if(playbackState == Player.STATE_READY)
                {
                    progressBar.setVisibility(View.GONE);
                }
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

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {

                if(flag)
                {
                    layout.setVisibility(View.VISIBLE);
                    fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag = false;
                }
                else
                {
                    layout.setVisibility(View.GONE);
                    fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.smallscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;
                }
            }
        });


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

        ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(JodhidamContactVideoPLayer.this, ContactUs.class);
                startActivity(nextActivity);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayerView.setPlayWhenReady(false);
        simpleExoPlayerView.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        simpleExoPlayerView.setPlayWhenReady(true);
        simpleExoPlayerView.getPlaybackState();
    }
}
