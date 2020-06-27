package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class VideoPlayer extends AppCompatActivity {

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;
    private Bundle bundle;

    private SimpleExoPlayer simpleExoPlayerView;
    private ProgressBar progressBar;
    private ImageView fullscreen;
    private PlayerView playerView;
    private LinearLayout layout;
    private TextView CurrentlyPlaying;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bundle = getIntent().getExtras();

        String playingNow = bundle.getString("list");
        String url = bundle.getString("url");
        //int flag = bundle.getInt("flag",0);
        //flag = bundle.getBoolean("flag",false);

        playerView = findViewById(R.id.player);
        progressBar = findViewById(R.id.progress);
        fullscreen = findViewById(R.id.fullscreen);
        //layout = findViewById(R.id.extraSegment);
        CurrentlyPlaying = findViewById(R.id.playingInfo);

        CurrentlyPlaying.setText(playingNow);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Uri videoUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/database-97af9.appspot.com/o/Jothidam%2Fmoodiya%20aalayanga%20yendru%20thirakkum.mp4?alt=media&token=2451ae2b-0912-4929-9ef2-4848e941edca");
        Uri videoUrl = Uri.parse(url);
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayerView = ExoPlayerFactory.newSimpleInstance(VideoPlayer.this,trackSelector,loadControl);

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

                if (flag)
                {
                    //layout.setVisibility(View.VISIBLE);
                    fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag = false;
                }
                else {
                    //ayout.setVisibility(View.GONE);
                    fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.smallscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;

                }

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
