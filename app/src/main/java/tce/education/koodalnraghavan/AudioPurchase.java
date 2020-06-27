package tce.education.koodalnraghavan;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koodalnraghavan.R;

import java.util.ArrayList;

public class AudioPurchase extends Fragment {

    private ListView listView;
    private LinearLayout linearLayout;
    private TextView textView;
    private ProgressBar progressBar;
    private ImageView Stop;
    private ImageView Pause;
    private Button Download;
    private TextView PlayinSong;

    private WebView webView;


    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private AlvargalinManamDatabaseHelper databaseHelper;
    private Cursor data;
    private int pausePlayCounter;
    int icons[] = {R.drawable.pause_icon , R.drawable.play_icon};
    private MediaPlayer mediaPlayer;

    @Override
    public void onPause() {

        webView.goBack();
        super.onPause();
    }

    @Override
    public void onDestroyView() {

        if (webView != null)
        {
            webView.removeAllViews();
            super.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        if(webView != null)
        {
            webView.destroy();
        }
        super.onDestroy();
    }

    public AudioPurchase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_purchase, container, false);

        listView = view.findViewById(R.id.listView);

        textView = view.findViewById(R.id.warner);
        progressBar = view.findViewById(R.id.progress);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.videoinfo,R.id.portal,list);

        databaseHelper = new AlvargalinManamDatabaseHelper(view.getContext());
        data = databaseHelper.fetchSongList();

        mediaPlayer = new MediaPlayer();

        webView = view.findViewById(R.id.web);


        if(data.getCount() == 0)
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Purchases so for");
            Toast.makeText(view.getContext(),"No Purchases so Far",Toast.LENGTH_SHORT).show();
            //linearLayout.setVisibility(View.GONE);
        }
        else {

            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            textView.setVisibility(View.GONE);
            //linearLayout.setVisibility(View.VISIBLE);

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
                    //Toast.makeText(view.getContext(),list.get(position) + url.get(position),Toast.LENGTH_SHORT).show();

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setHttpAuthUsernamePassword("https://tpvs.tce.edu/restricted/","myrealm","tpvsuser1","tpvs@userONE");

                    webView.setWebViewClient(new WebViewClient()
                    {
                        @Override
                        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                            handler.proceed("tpvsuser1","tpvs@userONE");
                        }

                        @Override
                        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                            handler.proceed();
                        }
                    });


                    webView.loadUrl(url.get(position));

                    /*try {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        //mediaPlayer.setDataSource(view.getContext(), Uri.parse(url.get(position)));
                        mediaPlayer.setDataSource(getActivity(),Uri.parse(url.get(position)));
                        mediaPlayer.prepare();
                        Toast.makeText(view.getContext(),"Play",Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();

                        pausePlayCounter = 0;

                        PlayinSong.setText("Playing now : " + list.get(position));
                        PlayinSong.setSelected(true);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(view.getContext(),"Player couldn't Start",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            pausePlayCounter++;

                            if(pausePlayCounter%2 == 0)
                            {
                                mediaPlayer.start();
                                Pause.setBackgroundResource(icons[0]);
                            }
                            else
                            {
                                mediaPlayer.pause();
                                Pause.setBackgroundResource(icons[1]);
                            }
                        }
                    });

                    Stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mediaPlayer.stop();

                            PlayinSong.setText("Stopped : " + list.get(position));
                            PlayinSong.setSelected(true);
                            return;
                        }
                    });*/
                }
            });
        }
        return view;
    }
}
