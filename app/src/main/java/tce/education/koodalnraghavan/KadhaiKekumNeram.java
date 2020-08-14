package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import tce.education.koodalnraghavan.adapter.StoryAdapter;
import tce.education.koodalnraghavan.classes.storyDataLoader;

public class KadhaiKekumNeram extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TextView TitleTootlbar;
    private Button Donation;
    private ListView listView;
    private ProgressBar progressBar;

    private Intent nextActivity;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private ArrayList<storyDataLoader> list;
    private StoryAdapter adapter;
    private MediaPlayer mediaPlayer;

    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject song;

    //music player Code
    private int currentMediaPosition;
    private boolean isPlaying = false;
    private Handler handler;
    private Runnable runnable;
    private TextView SongTitle;
    private ImageView topPlayPause;
    private ImageView PlayPause;
    private ImageView fastForward;
    private ImageView fastRewind;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView TotalSongTime;
    private ProgressBar musicProgressBar;
    private ImageView songDisplayPicture;
    private ToggleButton sheetExpander;
    private Button downloadMusic;
    private LinearLayout musicPlayer;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kadhai_kekum_neram);

        TitleTootlbar = findViewById(R.id.titleId);
        TitleTootlbar.setText(R.string.kadhai);
        TitleTootlbar.setSelected(true);

        handler = new Handler();
        SongTitle = findViewById(R.id.playingInfo);
        topPlayPause = findViewById(R.id.topPlayPause);
        PlayPause = findViewById(R.id.playPause);
        fastForward = findViewById(R.id.forwardId);
        fastRewind = findViewById(R.id.rewindId);
        seekBar = findViewById(R.id.seekBar);
        currentTime = findViewById(R.id.currentTime);
        TotalSongTime = findViewById(R.id.totalTime);
        musicProgressBar = findViewById(R.id.musicProgress);
        songDisplayPicture = findViewById(R.id.imageView);
        sheetExpander =  findViewById(R.id.toggleMusic);
        musicPlayer = findViewById(R.id.MusicPlayerSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(musicPlayer);
        downloadMusic = findViewById(R.id.downloadId);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progress);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        Donation = findViewById(R.id.donation);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KadhaiKekumNeram.this,Sambavanai.class);
                startActivity(intent);
            }
        });

        downloadMusic.setVisibility(View.GONE);

        sheetExpander.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                if (i == BottomSheetBehavior.STATE_EXPANDED)
                {
                    sheetExpander.setChecked(true);
                }
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    sheetExpander.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        list = new ArrayList<>();
        mediaPlayer = new MediaPlayer();

        queue = Volley.newRequestQueue(KadhaiKekumNeram.this);
        /**/
        new StoryLoader().execute(JSON_URL);
    }

    private class StoryLoader extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            request = new JsonObjectRequest(Request.Method.GET, strings[0], null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                jsonArray = response.getJSONArray("story");
                                progressBar.setVisibility(View.VISIBLE);
                                for(int iterator = 0; iterator < jsonArray.length() ; iterator++)
                                {
                                    song = jsonArray.getJSONObject(iterator);

                                    list.add(new storyDataLoader(song.getString("name"),song.getString("url"),song.getString("image")));
                                }
                                adapter = new StoryAdapter(getApplicationContext(),R.layout.videoinfo,list);
                                progressBar.setVisibility(View.GONE);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //code for music player
                                        storyDataLoader dataLoader = list.get(position);
                                        isPlaying = false;
                                        seekBar.setMax(100);
                                        PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                        topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));

                                        SongTitle.setText(dataLoader.getName());
                                        Picasso.get().load(dataLoader.getImage()).into(songDisplayPicture);

                                        try {
                                            mediaPlayer.setDataSource(KadhaiKekumNeram.this,Uri.parse(dataLoader.getUrl()));
                                            mediaPlayer.prepareAsync();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                currentMediaPosition = mediaPlayer.getDuration();
                                                TotalSongTime.setText(milliSecondsToTime(currentMediaPosition));
                                                seekBar.setMax(currentMediaPosition);
                                                mediaPlayer.start();
                                                changeSeek();
                                            }
                                        });

                                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                            @Override
                                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                if (fromUser)
                                                {
                                                    mediaPlayer.seekTo(progress);
                                                }
                                            }

                                            @Override
                                            public void onStartTrackingTouch(SeekBar seekBar) {

                                            }

                                            @Override
                                            public void onStopTrackingTouch(SeekBar seekBar) {

                                            }
                                        });

                                        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                            @Override
                                            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                                                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                                {
                                                    musicProgressBar.setVisibility(View.VISIBLE);
                                                }
                                                else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                                {
                                                    musicProgressBar.setVisibility(View.GONE);
                                                }
                                                return false;
                                            }
                                        });

                                        topPlayPause.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!isPlaying)
                                                {
                                                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.play_er));
                                                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.play_er));
                                                    isPlaying = true;
                                                    mediaPlayer.pause();
                                                }
                                                else {
                                                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                                    isPlaying = false;
                                                    mediaPlayer.start();
                                                    changeSeek();
                                                }
                                            }
                                        });

                                        PlayPause.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!isPlaying)
                                                {
                                                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.play_er));
                                                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.play_er));
                                                    isPlaying = true;
                                                    mediaPlayer.pause();
                                                }
                                                else {
                                                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                                    isPlaying = false;
                                                    mediaPlayer.start();
                                                    changeSeek();
                                                }
                                            }
                                        });

                                        fastForward.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                                            }
                                        });

                                        fastRewind.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                                            }
                                        });
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Please try again Later",Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> headers = new HashMap<>();
                    String username = "tpvsuser1";
                    String password = "tpvs@userONE";
                    String credentials = username + ":" + password;
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
                    headers.put("authorization",auth);
                    return headers;
                }
            };

            queue.add(request);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void changeSeek() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currentPosition);
        currentTime.setText(milliSecondsToTime(currentPosition));
        if (mediaPlayer.isPlaying())
        {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeek();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    private String milliSecondsToTime(long duration) {
        String timerString = "";
        String secondsString;

        int hours = (int) (duration / (1000*60*60));
        int minutes = (int) (duration % (1000 * 60*60)) / (1000*60);
        int seconds = (int) ((duration % (1000*60*60)) % (1000*60)/1000);

        if (hours > 0)
        {
            timerString = hours + ":";
        }
        if (seconds < 10)
        {
            secondsString = "0" + seconds;
        }
        else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;
        return timerString;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.home:
                nextActivity = new Intent(this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                nextActivity = new Intent(this,AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.events:
                nextActivity = new Intent(this,Events.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                nextActivity = new Intent(this,GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                nextActivity = new Intent(this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                nextActivity = new Intent(this, ContactUs.class);
                startActivity(nextActivity);
                break;
            case R.id.google:
                dialog = new AlertDialog.Builder(this);
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
                dialog = new AlertDialog.Builder(this);
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
                dialog = new AlertDialog.Builder(this);
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
                dialog = new AlertDialog.Builder(this);
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
