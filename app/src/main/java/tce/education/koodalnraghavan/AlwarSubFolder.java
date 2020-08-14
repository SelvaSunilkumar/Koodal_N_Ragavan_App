package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.util.Map;

import es.dmoral.toasty.Toasty;
import tce.education.koodalnraghavan.adapter.AlwarMusicAdapter;
import tce.education.koodalnraghavan.classes.AlwarMusicDetails;

public class AlwarSubFolder extends AppCompatActivity implements AlwarMusicAdapter.OnItemClickListener {

    private TextView texView;

    private String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    private ArrayList<AlwarMusicDetails> details;
    private AlwarMusicAdapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<DataPriceLister> arrayList;

    private Bundle bundle;
    private String folderName;
    private int index;

    private AlvargalinManamDatabaseHelper databaseHelper;

    private String Name;
    private String UPI_Id;
    private String Amount;
    private String Note;
    private String URL_Music;
    private LinearLayout upiPayment;
    private TextView NameTextView;
    private TextView UPI_id_TextView;
    private Button PayNow;
    private TextView AmountToPay;
    private TextView albumInfo;

    private Dialog payementSelector;
    private Dialog GooglePayProcessor;

    //private AlwarAdapter adapter;

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
    private ProgressBar progressBar;
    private Button DownloadMusic;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Runnable runnable;
    private Handler handler;

    private final String USERNAME = "tpvsuser1";
    private final String PASSWORD = "tpvs@userONE";
    private final String CREDENTIALS = USERNAME + ":" + PASSWORD;
    private final String AUTHENTICATOR = "Basic " + Base64.encodeToString(CREDENTIALS.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
    private Map<String,String> headers;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alwar_sub_folder);

        texView = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(AlwarSubFolder.this));

        musicPlayerLayout = findViewById(R.id.MusicPlayerSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(musicPlayerLayout);
        layoutExpander = findViewById(R.id.toggleMusic);
        songInfo = findViewById(R.id.playingInfo);
        topPlayPause = findViewById(R.id.topPlayPause);
        PlayPause = findViewById(R.id.playPause);
        progressBar = findViewById(R.id.musicProgress);
        TotalSongTime = findViewById(R.id.totalTime);
        currentPlayTime = findViewById(R.id.currentTime);
        seekBar = findViewById(R.id.seekBar);
        fastForward = findViewById(R.id.forwardId);
        fastRewind = findViewById(R.id.rewindId);
        DownloadMusic = findViewById(R.id.downloadId);

        handler = new Handler();
        DownloadMusic.setVisibility(View.GONE);
        bundle = getIntent().getExtras();
        folderName = bundle.getString("foldername");
        index = bundle.getInt("index");

        databaseHelper = new AlvargalinManamDatabaseHelper(getApplicationContext());
        headers = new HashMap<>();
        headers.put("Authorization",AUTHENTICATOR);
        progressBar.setVisibility(View.GONE);

        arrayList = new ArrayList<>();
        details = new ArrayList<>();
        adapter = new AlwarMusicAdapter(getApplicationContext(),details);

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

        new ClassAsy().execute();

    }

    @Override
    public void onItemClick(int position) {

        AlwarMusicDetails musicDetails = details.get(position);
        progressBar.setVisibility(View.VISIBLE);
        isPlaying = false;
        seekBar.setMax(100);
        PlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
        topPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_music));
        if (isAudioAvailable(musicDetails.getAudioName()) || musicDetails.getFormat().equals("pdf") || musicDetails.getAudioName().equals("Sample"))
        {
            if (musicDetails.getFormat().equals("pdf"))
            {
                Intent intent = new Intent(AlwarSubFolder.this,PdfViewAuthentication.class);

                bundle = new Bundle();
                bundle.putString("url",musicDetails.getAudioUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else {
                //code for webview

                songInfo.setText(musicDetails.getAudioName());
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(AlwarSubFolder.this,Uri.parse(musicDetails.getAudioUrl()),headers);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        progressBar.setVisibility(View.GONE);
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
        }
        else
        {
            payementSelector = new Dialog(AlwarSubFolder.this);
            payementSelector.setContentView(R.layout.payment_selector);

            upiPayment = payementSelector.findViewById(R.id.googlePlayButton);
            albumInfo = payementSelector.findViewById(R.id.albumInfo);
            albumInfo.setVisibility(View.VISIBLE);

            upiPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GooglePayProcessor = new Dialog(AlwarSubFolder.this);
                    GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                    PayNow = GooglePayProcessor.findViewById(R.id.paynow);
                    AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                    AmountToPay.setText(musicDetails.getPrice());

                    PayNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                            UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                            //Storing the data from layout to the sting variables
                            Name = NameTextView.getText().toString();
                            UPI_Id = UPI_id_TextView.getText().toString();
                            Amount = musicDetails.getPrice();

                            Note = folderName;

                            URL_Music = musicDetails.getAudioUrl();

                            payUsingUpi(Amount,UPI_Id,Name,Note);
                        }
                    });

                    GooglePayProcessor.show();
                }
            });

            payementSelector.show();
        }
    }

    class ClassAsy extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            parseJson();
            return null;
        }
    }

    private void parseJson() {
        queue = Volley.newRequestQueue(AlwarSubFolder.this);
        request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("alwaraudio");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);
                                if (folderName.equals(jsonObject.getString("folder")))
                                {
                                    String audioName = jsonObject.getString("name");
                                    String audioUrl = jsonObject.getString("url");
                                    String audioPrice = jsonObject.getString("price");
                                    String audioFormat = jsonObject.getString("format");
                                    details.add(new AlwarMusicDetails(audioName,audioUrl,audioFormat,audioPrice));
                                }
                            }
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(AlwarSubFolder.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Please try again later",Toast.LENGTH_SHORT).show();
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


    private void payUsingUpi(String amount, String upi_id, String name,String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upi_id)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay with");

        if(null != chooser.resolveActivity(getApplicationContext().getPackageManager()))
        {
            startActivityForResult(chooser,0);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No UPI found, please install one of the upi",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 0:
                if((Activity.RESULT_OK == resultCode) || (resultCode == 11))
                {
                    if(data != null)
                    {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI","onActivityResult : " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    }
                    else
                    {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }
                else
                {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> dataList) {
        if(isConnectionAvailable(getApplicationContext()))
        {
            String str = dataList.get(0);
            Log.d("UPIPAY","upiPaymentDataOperation : " + str);
            String paymentCancel = "";

            if(str == null)
            {
                str = "discard";
            }
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");

            for(int i=0;i<response.length;i++)
            {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if(status.equals("success")) {
                //isSongUpdated(Note);
                isSongUpdated(details);
                Toasty.success(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr : " + approvalRefNo);
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();

                Intent nextActivity = new Intent(getApplicationContext(), VideoPlayer.class);

                Bundle bundle = new Bundle();
                bundle.putString("list", Note);
                bundle.putString("url", URL_Music);
                //bundle.putInt("flag",1);
                bundle.putBoolean("flag", true);

                nextActivity.putExtras(bundle);
                startActivity(nextActivity);
            }
            else if ("Payment cancelled by user.".equals(paymentCancel))
            {
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();
                Toasty.warning(getApplicationContext(),"Payment cancelled by the user" ,Toast.LENGTH_SHORT).show();
            }
            else {
                Toasty.error(getApplicationContext(),"Transaction failed.Please try again later",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toasty.info(getApplicationContext(),"Internet connection not Available",Toast.LENGTH_SHORT).show();
        }
    }

    private void isSongUpdated(ArrayList<AlwarMusicDetails> arrayList) {

        AlwarMusicDetails alwarMusicDetails;
        boolean flag = false;
        for (int i=0;i<arrayList.size();i++)
        {
            alwarMusicDetails = arrayList.get(i);
            if (!databaseHelper.insertAudio(alwarMusicDetails.getAudioName(),alwarMusicDetails.getAudioUrl()) && !alwarMusicDetails.getFormat().equals("pdf"))
            {
                flag = true;
            }
        }
    }

    private boolean isConnectionAvailable(Context applicationContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable())
            {
                return true;
            }
        }
        return false;
    }

    private boolean isAudioAvailable(String s) {

        Cursor data = databaseHelper.fetchSongList();

        while (data.moveToNext())
        {
            if (s.equals(data.getString(0)))
            {
                return true;
            }
        }
        return false;
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
