package tce.education.koodalnraghavan;

import android.database.Cursor;
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


import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AudioPurchase extends Fragment {

    private ListView listView;
    private TextView textView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private AlvargalinManamDatabaseHelper databaseHelper;
    private Cursor data;
    private MediaPlayer mediaPlayer;

    private LinearLayout musicPlayerLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private ToggleButton layoutExpander;
    private TextView songInfo;
    private TextView TotalSongTime;
    private TextView currentPlayTime;
    private ImageView topPlayPause;
    private ImageView PlayPause;
    private SeekBar seekBar;
    private ImageView fastForward;
    private ImageView fastRewind;
    private ProgressBar musicProgressBar;
    private Button DownloadMusic;
    private boolean isPlaying = false;
    private Runnable runnable;
    private Handler handler;

    private final String USERNAME = "tpvsuser1";
    private final String PASSWORD = "tpvs@userONE";
    private final String CREDENTIALS = USERNAME + ":" + PASSWORD;
    private final String AUTHENTICATOR = "Basic " + Base64.encodeToString(CREDENTIALS.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
    private Map<String,String> headers;

    public AudioPurchase() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_purchase, container, false);

        listView = view.findViewById(R.id.listView);

        textView = view.findViewById(R.id.warner);
        progressBar = view.findViewById(R.id.progress);

        musicPlayerLayout = view.findViewById(R.id.MusicPlayerSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(musicPlayerLayout);
        layoutExpander = view.findViewById(R.id.toggleMusic);
        songInfo = view.findViewById(R.id.playingInfo);
        topPlayPause = view.findViewById(R.id.topPlayPause);
        PlayPause = view.findViewById(R.id.playPause);
        musicProgressBar = view.findViewById(R.id.musicProgress);
        TotalSongTime = view.findViewById(R.id.totalTime);
        currentPlayTime = view.findViewById(R.id.currentTime);
        seekBar = view.findViewById(R.id.seekBar);
        fastForward = view.findViewById(R.id.forwardId);
        fastRewind = view.findViewById(R.id.rewindId);
        DownloadMusic = view.findViewById(R.id.downloadId);

        handler = new Handler();
        headers = new HashMap<>();
        headers.put("Authorization",AUTHENTICATOR);
        DownloadMusic.setVisibility(View.GONE);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.videoinfo,R.id.portal,list);

        databaseHelper = new AlvargalinManamDatabaseHelper(view.getContext());
        data = databaseHelper.fetchSongList();

        layoutExpander.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    layoutExpander.setChecked(true);
                }
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    layoutExpander.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        mediaPlayer = new MediaPlayer();

        if(data.getCount() == 0)
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Purchases so for");
            Toast.makeText(view.getContext(),"No Purchases so Far",Toast.LENGTH_SHORT).show();
        }
        else {

            textView.setVisibility(View.GONE);

            while (data.moveToNext())
            {
                list.add(String.valueOf(data.getString(0)));
                url.add(String.valueOf(data.getString(1)));
            }
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    isPlaying = false;
                    seekBar.setMax(100);
                    PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                    topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                    songInfo.setText(list.get(position));

                    try {
                        mediaPlayer.setDataSource(view.getContext(), Uri.parse(url.get(position)),headers);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int duration = mediaPlayer.getDuration();
                            TotalSongTime.setText(milliSecondsToTimer(duration));
                            seekBar.setMax(duration);
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
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            {
                                progressBar.setVisibility(View.GONE);
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
        }
        return view;
    }

    private void changeSeek() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currentPosition);
        currentPlayTime.setText(milliSecondsToTimer(currentPosition));
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
