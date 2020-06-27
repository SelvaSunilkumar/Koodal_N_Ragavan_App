package tce.education.koodalnraghavan;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class OtherAudio extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private WebView webView;

    private Dialog music;
    private ImageView playAudio;
    private ImageView downloadAudio;
    private TextView Info;
    private ProgressBar progressBar1;
    private boolean flag = false;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private MediaPlayer mediaPlayer;

    private String JSON_URl = "https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject audio;
    private String audio_name;
    private String audio_url;

    public OtherAudio() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        webView.destroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_audio, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);
        webView = view.findViewById(R.id.web);

        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.videoinfo,R.id.portal,list);
        mediaPlayer = new MediaPlayer();

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("otheraudio");

                            progressBar.setVisibility(View.VISIBLE);
                            for(int iterator = 0 ; iterator < jsonArray.length() ; iterator++)
                            {
                                audio = jsonArray.getJSONObject(iterator);

                                audio_name = audio.getString("name");
                                audio_url = audio.getString("url");

                                list.add(audio_name);
                                url.add(audio_url);
                            }
                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    //webView.getSettings().setJavaScriptEnabled(true);
                                    //webView.loadUrl(url.get(position));
                                    music = new Dialog(view.getContext());
                                    music.setContentView(R.layout.music_player);
                                    music.show();

                                    playAudio = music.findViewById(R.id.pausePlay);
                                    downloadAudio = music.findViewById(R.id.download);
                                    progressBar1 = music.findViewById(R.id.progress);
                                    Info = music.findViewById(R.id.playingNow);

                                    Info.setText(list.get(position));
                                    Info.setSelected(true);

                                    final String portalUrl = url.get(position);

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
                                                flag = true;
                                                mediaPlayer.pause();
                                            }
                                            else
                                            {
                                                playAudio.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
                                                flag = false;
                                                mediaPlayer.start();
                                            }
                                        }
                                    });

                                    downloadAudio.setOnClickListener(new View.OnClickListener() {
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
                                    });

                                    music.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            mediaPlayer.stop();
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
                Toast.makeText(view.getContext(),"Please try again later ",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        return view;
    }
}
