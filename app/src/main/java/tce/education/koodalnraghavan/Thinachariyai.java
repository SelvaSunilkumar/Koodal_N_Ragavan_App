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
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Thinachariyai extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TextView dateSelector;
    private TextView DescriptionText;
    private TextView Description;
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
    private TextView linkOne;
    private ImageView pointerOne;
    private TextView linkTwo;
    private ImageView pointerTwo;
    private View seperator;

    private String dateSelected;
    private final String LoaderString = "Loading...";
    private final String nullSetter = "-";
    private final String message = "available soon...";

    private Intent nextActivity;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject object;
    private boolean flag;
    private String getDate;

    private MediaPlayer mediaPlayer;

    private LinearLayout layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private ToggleButton toggleButton;
    private LinearLayout informLayout;
    private BottomSheetBehavior informBottomSheetBehaviour;
    private ToggleButton informToggleButton;
    private ListView informListView;
    private ArrayList<String> infoList;
    private ArrayList<String> infoUrl;
    private ArrayAdapter<String> informAdapter;

    private LinearLayout musicPlayerLayout;
    private ImageView topPlayPause;
    private ImageView PlayPause;
    private ImageView fastForward;
    private ImageView fastRewind;
    private TextView currentTime;
    private TextView totalTime;
    private SeekBar seekBar;
    private ProgressBar musicProgress;
    private TextView playingNow;
    private Handler handler;
    private Runnable runnable;
    private boolean isPlaying = false;

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
        setContentView(R.layout.activity_thinachariyai);

        layout = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);
        toggleButton = findViewById(R.id.toogleButton);
        informLayout = findViewById(R.id.InformbottomSheet);
        informBottomSheetBehaviour = BottomSheetBehavior.from(informLayout);
        informToggleButton = findViewById(R.id.toogleInformation);
        informListView = findViewById(R.id.infoLister);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        TitleTootlbar = findViewById(R.id.titleId);
        TitleTootlbar.setText("Dhinacharyai");
        TitleTootlbar.setSelected(true);

        dateSelector = findViewById(R.id.selectorInfo);
        DescriptionText = findViewById(R.id.descriptionText);
        Description = findViewById(R.id.description);
        DateText = findViewById(R.id.dater);
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
        linkOne = findViewById(R.id.linkOne);
        pointerOne = findViewById(R.id.pointerOne);
        linkTwo = findViewById(R.id.linkTwo);
        pointerTwo = findViewById(R.id.pointerTwo);
        seperator = findViewById(R.id.seperator);

        topPlayPause = findViewById(R.id.topPlayPause);
        PlayPause = findViewById(R.id.playPause);
        fastForward = findViewById(R.id.forwardId);
        fastRewind = findViewById(R.id.rewindId);
        seekBar = findViewById(R.id.seekBar);
        musicProgress = findViewById(R.id.musicProgress);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        musicPlayerLayout = findViewById(R.id.musicController);
        playingNow = findViewById(R.id.audioName);
        handler = new Handler();

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
        linkOne.setVisibility(View.GONE);
        pointerOne.setVisibility(View.GONE);
        linkTwo.setVisibility(View.GONE);
        pointerTwo.setVisibility(View.GONE);
        seperator.setVisibility(View.GONE);
        musicPlayerLayout.setVisibility(View.GONE);
        musicProgress.setVisibility(View.GONE);

        mediaPlayer = new MediaPlayer();

        queue = Volley.newRequestQueue(Thinachariyai.this);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int sheetState) {

                if (sheetState == BottomSheetBehavior.STATE_EXPANDED) {
                    toggleButton.setChecked(true);
                }
                if (sheetState == BottomSheetBehavior.STATE_COLLAPSED) {
                    toggleButton.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        informToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    informBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    informBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        informBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int layIndicator) {
                if (layIndicator == BottomSheetBehavior.STATE_EXPANDED)
                {
                    informToggleButton.setChecked(true);
                }
                if (layIndicator == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    informToggleButton.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        infoList = new ArrayList<>();
        infoUrl = new ArrayList<>();

        infoList.add("Dhinasaryay - English");
        infoList.add("Dhinasaryay - Tamil");
        infoUrl.add("https://tpvs.tce.edu/unrestricted/description/Dhinasaryay%20Front%20Page-Eng.pdf");
        infoUrl.add("https://tpvs.tce.edu/unrestricted/description/Dinasaryay%20Front%20Page-Tam.pdf");

        informAdapter = new ArrayAdapter<>(Thinachariyai.this,R.layout.pdfinfo,R.id.portal,infoList);

        informListView.setAdapter(informAdapter);
        informListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Thinachariyai.this,PdfViewer.class);
                Bundle bundle = new Bundle();

                bundle.putString("url",infoUrl.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


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
                musicProgress.setVisibility(View.GONE);

                Description.setText(LoaderString);
                dayText.setText(LoaderString);
                thidhi.setText(LoaderString);
                star.setText(LoaderString);
                event.setText(LoaderString);

                flag = false;

                request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
                            @SuppressLint("ResourceAsColor")
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

                                            String portalUrl = object.getString("url_one");
                                            String portalUrlOne = object.getString("url_two");
                                            String portalUrlTwo = object.getString("url_three");
                                            flag = true;

                                            if (!portalUrl.equals("null"))
                                            {
                                                musicProgress.setVisibility(View.VISIBLE);
                                                String titleOne = object.getString("title_one");
                                                playingNow.setVisibility(View.VISIBLE);
                                                musicPlayerLayout.setVisibility(View.VISIBLE);
                                                link.setVisibility(View.VISIBLE);
                                                pointer.setVisibility(View.VISIBLE);
                                                link.setText(titleOne);
                                                link.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        PlayAudio(portalUrl,titleOne);
                                                    }
                                                });
                                            }
                                            else {
                                                musicPlayerLayout.setVisibility(View.GONE);
                                                link.setVisibility(View.GONE);
                                                pointer.setVisibility(View.GONE);
                                            }
                                            if (!portalUrlOne.equals("null")) {
                                                musicProgress.setVisibility(View.VISIBLE);
                                                String titleTwo = object.getString("title_two");
                                                linkOne.setVisibility(View.VISIBLE);
                                                pointerOne.setVisibility(View.VISIBLE);
                                                linkOne.setText(titleTwo);
                                                linkOne.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        PlayAudio(portalUrlOne,titleTwo);
                                                    }
                                                });
                                            }
                                            else {
                                                playingNow.setVisibility(View.GONE);
                                                linkOne.setVisibility(View.GONE);
                                                pointerOne.setVisibility(View.GONE);
                                            }

                                            if (!portalUrlTwo.equals("null"))
                                            {
                                                musicProgress.setVisibility(View.VISIBLE);
                                                String titleThree = object.getString("title_three");
                                                linkTwo.setVisibility(View.VISIBLE);
                                                pointerTwo.setVisibility(View.VISIBLE);
                                                linkTwo.setText(titleThree);
                                                linkTwo.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        PlayAudio(portalUrlTwo,titleThree);
                                                    }
                                                });
                                            }
                                            else {
                                                linkTwo.setVisibility(View.GONE);
                                                pointerTwo.setVisibility(View.GONE);
                                            }
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
            case R.id.events:
                nextActivity = new Intent(this,Events.class);
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

    private void PlayAudio(String url,String audioName)
    {
        playingNow.setText(audioName);
        isPlaying = false;
        seekBar.setMax(100);
        PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
        topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(Thinachariyai.this,Uri.parse(url));
            //mediaPlayer.prepare();
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = mediaPlayer.getDuration();
                totalTime.setText(milliSecondsToTimer(duration));
                seekBar.setMax(duration);
                mediaPlayer.start();
                changeSeek();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
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

                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    musicProgress.setVisibility(View.VISIBLE);
                }
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    musicProgress.setVisibility(View.GONE);
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
                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                    mediaPlayer.start();
                    changeSeek();
                    isPlaying = false;
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
                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                    mediaPlayer.start();
                    changeSeek();
                    isPlaying = false;
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

    private void changeSeek() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currentPosition);
        currentTime.setText(milliSecondsToTimer(currentPosition));
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

    private String milliSecondsToTimer(long milliSeconds)
    {
        String timerString = "";
        String secondsString;

        int hours = (int) (milliSeconds / (1000*60*60));
        int minutes = (int) (milliSeconds % (1000 * 60*60)) / (1000*60);
        int seconds = (int) ((milliSeconds % (1000*60*60)) % (1000*60)/1000);

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
}
