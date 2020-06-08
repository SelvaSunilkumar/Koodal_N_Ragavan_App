package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Thinachariyai extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TextView dateSelector;
    private TextView DescriptionText;
    private TextView Description;
    private TextView videoText;
    private TextView video;
    private TextView DateText;
    private TextView TitleTootlbar;
    private CalendarView calendarView;
    private Button Donation;
    private ProgressBar progressBar;

    private TextView dayTextView;
    private TextView dayText;
    private TextView thidhiTextView;
    private TextView thidhi;
    private TextView starTextView;
    private TextView star;
    private TextView eventTextView;
    private TextView event;

    private String dateSelected;
    private final String LoaderString = "Loading...";
    private final String nullSetter = "-";
    private final String message = "available soon...";

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference databaseReference;
    private EventDescriptionLoader descriptionLoader;

    private Intent nextActivity;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;
    private Dialog EventVideoPlayer;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thinachariyai);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        TitleTootlbar = findViewById(R.id.titleId);
        TitleTootlbar.setText("Dhinacharyai");
        TitleTootlbar.setSelected(true);

        dateSelector = findViewById(R.id.selectorInfo);
        DescriptionText = findViewById(R.id.descriptionText);
        Description = findViewById(R.id.description);
        DateText = findViewById(R.id.dateText);
        calendarView = findViewById(R.id.calenderView);
        dayTextView = findViewById(R.id.day);
        dayText = findViewById(R.id.dayText);
        thidhiTextView = findViewById(R.id.thidhi);
        thidhi = findViewById(R.id.thidhiText);
        starTextView = findViewById(R.id.star);
        star = findViewById(R.id.startText);
        eventTextView = findViewById(R.id.event);
        event = findViewById(R.id.eventText);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle(" About us");
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Description.setVisibility(View.GONE);
        DescriptionText.setVisibility(View.GONE);
        //video.setVisibility(View.GONE);
        //videoText.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDate = simpleDateFormat.format(date);
        DateText.setText(currentDate);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Events");

        Donation = findViewById(R.id.donation);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thinachariyai.this,Sambavanai.class);
                startActivity(intent);
            }
        });

        DescriptionText.setVisibility(View.GONE);
        Description.setVisibility(View.GONE);
        dayTextView.setVisibility(View.GONE);
        dayText.setVisibility(View.GONE);
        thidhiTextView.setVisibility(View.GONE);
        thidhi.setVisibility(View.GONE);
        star.setVisibility(View.GONE);
        starTextView.setVisibility(View.GONE);
        eventTextView.setVisibility(View.GONE);
        event.setVisibility(View.GONE);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                /*
                private TextView DescriptionText;
                private TextView Description;
                private TextView dayTextView;
                private TextView dayText;
                private TextView thidhiTextView;
                private TextView thidhi;
                private TextView starTextView;
                private TextView star;
                private TextView eventTextView;
                private TextView event;*/

                dateSelected = dayOfMonth + "-" + (month + 1) + "-" + year;
                DateText.setText(dateSelected);

                DescriptionText.setVisibility(View.VISIBLE);
                Description.setVisibility(View.VISIBLE);
                dayTextView.setVisibility(View.VISIBLE);
                dayText.setVisibility(View.VISIBLE);
                thidhiTextView.setVisibility(View.VISIBLE);
                thidhi.setVisibility(View.VISIBLE);
                star.setVisibility(View.VISIBLE);
                starTextView.setVisibility(View.VISIBLE);
                eventTextView.setVisibility(View.VISIBLE);
                event.setVisibility(View.VISIBLE);

                Description.setText(LoaderString);
                dayText.setText(LoaderString);
                thidhi.setText(LoaderString);
                star.setText(LoaderString);
                event.setText(LoaderString);



                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dateSelector.setVisibility(View.GONE);

                        if(dataSnapshot.hasChild(dateSelected))
                        {
                            databaseReference = reference.child(dateSelected);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    descriptionLoader = new EventDescriptionLoader();

                                    descriptionLoader = dataSnapshot.getValue(EventDescriptionLoader.class);

                                    DescriptionText.setText(descriptionLoader.getMonth() + " : ");
                                    Description.setText(String.valueOf(descriptionLoader.getMonth_tml()));
                                    dayText.setText(descriptionLoader.getDay());
                                    thidhi.setText(descriptionLoader.getThidhi());
                                    //event.setText(descriptionLoader.getEvent());
                                    star.setText(descriptionLoader.getStar());

                                    if(dataSnapshot.hasChild("event"))
                                    {
                                       event.setText(descriptionLoader.getEvent());
                                    }
                                    else
                                    {
                                        event.setText(nullSetter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            Description.setText(message);
                            dayText.setText(message);
                            thidhi.setText(message);
                            star.setText(message);
                            event.setText(message);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                /*reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(dateSelected))
                        {
                            //content for the date is present
                            databaseReference = reference.child(dateSelected);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    descriptionLoader = new EventDescriptionLoader();
                                    descriptionLoader = dataSnapshot.getValue(EventDescriptionLoader.class);
                                    Description.setText(descriptionLoader.getDescription());
                                    video.setText(descriptionLoader.getName());

                                    if(descriptionLoader.getUrl() == null)
                                    {
                                        video.setClickable(false);
                                    }
                                    else {
                                        video.setClickable(true);
                                        video.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //Toast.makeText(getApplicationContext(),"Url loading",Toast.LENGTH_SHORT).show();
                                                EventVideoPlayer = new Dialog(Thinachariyai.this);
                                                EventVideoPlayer.setContentView(R.layout.calendar_videoplayer);

                                                Uri uri = Uri.parse(descriptionLoader.getUrl());

                                                progressBar = EventVideoPlayer.findViewById(R.id.progress);
                                                exoPlayerView = EventVideoPlayer.findViewById(R.id.videoplayer);
                                                progressBar.setVisibility(View.VISIBLE);


                                                try {
                                                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                                                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                                                    exoPlayer = ExoPlayerFactory.newSimpleInstance(Thinachariyai.this,trackSelector);

                                                    DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory(descriptionLoader.getName());
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
                                                            dialog = new AlertDialog.Builder(Thinachariyai.this);
                                                            dialog.setMessage("Couldn't play video");
                                                            dialog.setTitle("Player Error");

                                                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    EventVideoPlayer.dismiss();
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

                                                EventVideoPlayer.show();
                                                EventVideoPlayer.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        exoPlayer.stop();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            Description.setText("Description Not Available");
                            video.setText("Video Not Available");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.home:
                //Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                //Toast.makeText(getApplicationContext(),"About Us",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.others:
                nextActivity = new Intent(this,Others.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                //Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                //Toast.makeText(getApplicationContext(),"Free Downloads",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                //Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,ContactUs.class);
                startActivity(nextActivity);
                break;
            case R.id.google:
                dialog = new AlertDialog.Builder(Thinachariyai.this);
                dialog.setMessage("Taking you to Google");
                dialog.setTitle("Google");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("http://www.kavignakoodalnraghavan.com");
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
                break;
            case R.id.facebook:
                dialog = new AlertDialog.Builder(Thinachariyai.this);
                dialog.setMessage("Taking you to Facebook");
                dialog.setTitle("Facebook");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://www.facebook.com/kavignakoodal.n.raghavan");
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
                break;
            case R.id.twitter:
                dialog = new AlertDialog.Builder(Thinachariyai.this);
                dialog.setMessage("Taking you to Twitter");
                dialog.setTitle("Twitter");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://twitter.com/koodalraghavan?lang=en");
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
                break;
            case R.id.youtube:
                dialog = new AlertDialog.Builder(Thinachariyai.this);
                dialog.setMessage("Taking you to Youtube");
                dialog.setTitle("Youtube");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://www.youtube.com/user/RANGASRI4");
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
                break;
            case R.id.settings:
                nextActivity = new Intent(this,Settings.class);
                startActivity(nextActivity);
                break;
        }
        return false;
    }
}
