package tce.education.koodalnraghavan;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class PrasadhamAudio extends Fragment {

    private String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";

    private ProgressBar progressBar;
    private ListView listView;
    private View view;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray array;
    private JSONObject object;

    private MediaPlayer mediaPlayer;
    private TextView songTitle;
    private ToggleButton musicExpander;
    private ImageView topPlayPause;
    private ImageView PlayPause;
    private ImageView fastForward;
    private ImageView fastRewind;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView totalTime;
    private ProgressBar musicProgressBar;
    private Button musicDownload;
    private LinearLayout musicPlayer;
    private BottomSheetBehavior bottomSheetBehavior;
    private Runnable runnable;
    private Handler handler;
    private boolean isPlaying = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    public PrasadhamAudio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_prasadham_audio, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);
        songTitle = view.findViewById(R.id.playingInfo);
        musicExpander = view.findViewById(R.id.toggleMusic);
        topPlayPause = view.findViewById(R.id.topPlayPause);
        PlayPause = view.findViewById(R.id.playPause);
        fastForward = view.findViewById(R.id.forwardId);
        fastRewind = view.findViewById(R.id.rewindId);
        seekBar = view.findViewById(R.id.seekBar);
        currentTime = view.findViewById(R.id.currentTime);
        totalTime = view.findViewById(R.id.totalTime);
        musicProgressBar = view.findViewById(R.id.musicProgress);
        musicDownload = view.findViewById(R.id.downloadId);
        musicPlayer = view.findViewById(R.id.MusicPlayerSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(musicPlayer);

        musicDownload.setVisibility(View.GONE);
        list = new ArrayList<>();
        url = new ArrayList<>();
        adapter = new ArrayAdapter<>(view.getContext(),R.layout.videoinfo,R.id.portal,list);
        mediaPlayer = new MediaPlayer();
        handler = new Handler();

        musicExpander.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    musicExpander.setChecked(true);
                }
                if (i ==  BottomSheetBehavior.STATE_COLLAPSED)
                {
                    musicExpander.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        parseJson();

        return view;
    }

    private void parseJson() {

        queue = Volley.newRequestQueue(view.getContext());

        request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            array = response.getJSONArray("prasadhamaudio");

                            progressBar.setVisibility(View.VISIBLE);
                            for (int iterator = 0 ; iterator < array.length() ; iterator++)
                            {
                                object = array.getJSONObject(iterator);

                                list.add(object.getString("name"));
                                url.add(object.getString("url"));
                            }
                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    isPlaying = false;
                                    seekBar.setMax(100);
                                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));

                                    songTitle.setText(list.get(position));

                                    try {
                                        mediaPlayer.reset();
                                        mediaPlayer.setDataSource(view.getContext(),Uri.parse(url.get(position)));
                                        //mediaPlayer.prepare();
                                        mediaPlayer.prepareAsync();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            int Duration = mediaPlayer.getDuration();
                                            totalTime.setText(milliSecondsToTimer(Duration));
                                            seekBar.setMax(Duration);
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
                                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
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
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(view.getContext(),"Please try again Later",Toast.LENGTH_SHORT).show();
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

    private String milliSecondsToTimer(long duration) {
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
}
