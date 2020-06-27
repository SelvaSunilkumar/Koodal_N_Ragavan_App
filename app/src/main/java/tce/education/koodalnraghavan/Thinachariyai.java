package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.koodalnraghavan.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private TextView link;
    private ImageView pointer;
    private View seperator;

    private String dateSelected;
    private final String LoaderString = "Loading...";
    private final String nullSetter = "-";
    private final String message = "available soon...";

    private Intent nextActivity;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;
    private Dialog EventVideoPlayer;

    private final String JSON_URL = "https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject object;
    private boolean flag;
    private String getDate;

    private Dialog music;
    private ImageView playAudio;
    private ImageView downloadAudio;
    private TextView Info;
    private ProgressBar progressBar1;
    private boolean flagPlayer = false;
    private MediaPlayer mediaPlayer;


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
        link = findViewById(R.id.link);
        pointer = findViewById(R.id.pointer);
        seperator = findViewById(R.id.seperator);

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
        link.setVisibility(View.GONE);
        pointer.setVisibility(View.GONE);
        seperator.setVisibility(View.GONE);

        mediaPlayer = new MediaPlayer();

        queue = Volley.newRequestQueue(Thinachariyai.this);

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

                //dateSelector.setVisibility(View.GONE);
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
                seperator.setVisibility(View.VISIBLE);

                Description.setText(LoaderString);
                dayText.setText(LoaderString);
                thidhi.setText(LoaderString);
                star.setText(LoaderString);
                event.setText(LoaderString);

                flag = false;

                request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    jsonArray = response.getJSONArray("dhinachariyai");

                                    for (int iterator = 0; iterator < jsonArray.length() ; iterator++)
                                    {
                                        object = jsonArray.getJSONObject(iterator);

                                        getDate = object.getString("date");
                                        if(getDate.equals(dateSelected))
                                        {
                                            dateSelector.setText(object.getString("heading"));
                                            DescriptionText.setText(object.getString("tml_month") + " : ");
                                            Description.setText(String.valueOf(object.getInt("tml_day")));
                                            dayText.setText(object.getString("day"));
                                            thidhi.setText(object.getString("thidhi"));
                                            event.setText(object.getString("event"));
                                            star.setText(object.getString("star"));

                                            String portalUrl = object.getString("url");

                                            if (!object.getString("url").equals("null"))
                                            {
                                                pointer.setVisibility(View.VISIBLE);
                                                link.setVisibility(View.VISIBLE);

                                                link.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Toast.makeText(getApplicationContext(),"Please wait",Toast.LENGTH_SHORT).show();

                                                        music = new Dialog(view.getContext());
                                                        music.setContentView(R.layout.music_player);
                                                        music.show();

                                                        playAudio = music.findViewById(R.id.pausePlay);
                                                        downloadAudio = music.findViewById(R.id.download);
                                                        progressBar1 = music.findViewById(R.id.progress);
                                                        Info = music.findViewById(R.id.playingNow);

                                                       /* Info.setText(object.getString());
                                                        Info.setSelected(true);*/
                                                        Info.setVisibility(View.GONE);



                                                        try {
                                                            mediaPlayer.stop();
                                                            mediaPlayer.reset();
                                                            mediaPlayer.setDataSource(portalUrl);
                                                            mediaPlayer.prepare();
                                                            Toast.makeText(view.getContext(),"Play",Toast.LENGTH_SHORT).show();
                                                            mediaPlayer.start();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                                            @Override
                                                            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                                                                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                                                {
                                                                    progressBar1.setVisibility(View.VISIBLE);
                                                                }
                                                                else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                                                {
                                                                    progressBar1.setVisibility(View.GONE);
                                                                }
                                                                return false;
                                                            }
                                                        });
                                                        playAudio.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if(!flag)
                                                                {
                                                                    playAudio.setImageDrawable(getResources().getDrawable(R.drawable.play_er));
                                                                    flagPlayer = true;
                                                                    mediaPlayer.pause();
                                                                }
                                                                else
                                                                {
                                                                    playAudio.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                                                    flagPlayer = false;
                                                                    mediaPlayer.start();
                                                                }
                                                            }
                                                        });

                                                        downloadAudio.setVisibility(View.GONE);

                                                        /*downloadAudio.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Toast.makeText(getContext(),"Downloading : " + list.get(position),Toast.LENGTH_SHORT).show();

                                                                Uri uri = Uri.parse(portalUrl);

                                                                DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                                                DownloadManager.Request request = new DownloadManager.Request(uri);

                                                                Context context = view.getContext();
                                                                String fileename = list.get(position);
                                                                String fileExtension = ".mp3";

                                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                                                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,fileename + fileExtension);

                                                                downloadManager.enqueue(request);
                                                            }
                                                        });*/

                                                        music.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialog) {
                                                                mediaPlayer.stop();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                            else {
                                                pointer.setVisibility(View.GONE);
                                                link.setVisibility(View.GONE);
                                            }

                                            flag = true;

                                        }
                                    }

                                    if(!flag)
                                    {
                                        dateSelector.setText("");
                                        Description.setText(message);
                                        dayText.setText(message);
                                        thidhi.setText(message);
                                        star.setText(message);
                                        event.setText(message);
                                        pointer.setVisibility(View.GONE);
                                        link.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(request);

                /*reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    Description.setText(descriptionLoader.getMonth_tml());
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
