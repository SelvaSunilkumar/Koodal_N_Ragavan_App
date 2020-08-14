package tce.education.koodalnraghavan;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Map;


public class Videotab extends Fragment {

    private ProgressBar progressBar;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String,List<DataLister>> listDataChild;

    private String tempFolderName;
    private int counter;

    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject folder;
    private String folder_name;
    private String audio_name;

    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Handler handler;
    private TextView songTitle;
    private TextView currentTime;
    private TextView totalTime;
    private SeekBar seekBar;
    private ProgressBar songProgressBar;
    private ImageView topPlayPause;
    private ImageView PlayPause;
    private ImageView fastForward;
    private ImageView fastRewind;
    private ToggleButton musicExpander;
    private Button DownloadMusic;
    private LinearLayout musicPlayer;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean isPlaying = false;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videotab, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        handler = new Handler();
        songTitle = view.findViewById(R.id.playingInfo);
        currentTime = view.findViewById(R.id.currentTime);
        totalTime = view.findViewById(R.id.totalTime);
        seekBar = view.findViewById(R.id.seekBar);
        songProgressBar = view.findViewById(R.id.musicProgress);
        topPlayPause = view.findViewById(R.id.topPlayPause);
        PlayPause = view.findViewById(R.id.playPause);
        fastForward = view.findViewById(R.id.forwardId);
        fastRewind = view.findViewById(R.id.rewindId);
        musicExpander = view.findViewById(R.id.toggleMusic);
        musicPlayer = view.findViewById(R.id.MusicPlayerSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(musicPlayer);
        DownloadMusic = view.findViewById(R.id.downloadId);

        prepareListData(view);
        DownloadMusic.setVisibility(View.GONE);

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
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    musicExpander.setChecked(false);
                }
                if (i == BottomSheetBehavior.STATE_EXPANDED)
                {
                    musicExpander.setChecked(true);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        listAdapter = new ExpandableListAdapter(view.getContext(),listDataHeader,listDataChild);
        listView.setAdapter(listAdapter);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        mediaPlayer = new MediaPlayer();
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                DataLister dataLister = (DataLister) listAdapter.getChild(groupPosition,childPosition);

                isPlaying = false;
                seekBar.setMax(100);
                PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                songTitle.setText(dataLister.getName());
                mediaPlayer.reset();

                try {
                    mediaPlayer.setDataSource(view.getContext(), Uri.parse(dataLister.getUrl()));
                    //mediaPlayer.prepare();
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        int duration = mediaPlayer.getDuration();
                        totalTime.setText(milliSecondsToTime(duration));
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

                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                        {
                            songProgressBar.setVisibility(View.GONE);
                        }
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                        {
                            songProgressBar.setVisibility(View.VISIBLE);
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

                return false;
            }
        });

        //---------------------------------------------------------------------------------
        return view;
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
    private void prepareListData(View view) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<DataLister>>();

        counter = 0;

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("audio");

                            for (int i = 0; i < jsonArray.length() ; i++ )
                            {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");
                                if( ! (listDataHeader.contains(folder_name)) )
                                {
                                    listDataHeader.add(folder_name);
                                    counter++;

                                    List<DataLister> list = new ArrayList<DataLister>();
                                    audio_name = folder.getString("name");
                                    list.add(new DataLister(audio_name,folder.getString("url")));

                                    for (int j = 0 ; j < jsonArray.length() ; j++ )
                                    {
                                        if( i != j )
                                        {
                                            folder = jsonArray.getJSONObject(j);
                                            tempFolderName = folder.getString("folder");

                                            if(tempFolderName.equals(folder_name))
                                            {
                                                audio_name = folder.getString("name");
                                                list.add(new DataLister(audio_name,folder.getString("url")));
                                            }
                                        }
                                    }
                                    listDataChild.put(listDataHeader.get(counter-1),list);
                                    listAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
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
}
