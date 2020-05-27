package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MusicLister extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private ImageView Stop;
    private ImageView Pause;
    private Button Download;
    private TextView PlayinSong;
    private LinearLayout upiPayment;
    private TextView NameTextView;
    private TextView UPI_id_TextView;
    private Button PayNow;
    private TextView AmountToPay;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayList<String> price;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    //private Azhwarmusic1 azhwarmusic;
    private NamePriceList priceList;
    private AlvargalinManamDatabaseHelper databaseHelper;

    private String folderReference;
    private int pausePlayCounter;
    int icons[] = {R.drawable.pause_icon , R.drawable.play_icon};
    private String Name;
    private String UPI_Id;
    private String Amount;
    private String Note;
    private String URL_Music;

    private MediaPlayer mediaPlayer;
    private Dialog payementSelector;
    private Dialog GooglePayProcessor;

    private Bundle bundle;

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lister);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progress);
        Stop = findViewById(R.id.stop);
        Pause = findViewById(R.id.pause);
        Download = findViewById(R.id.download);
        PlayinSong = findViewById(R.id.playinginfo);

        PlayinSong.setSelected(true);
        Download.setEnabled(false);
        Download.setSelected(true);
        Download.setVisibility(View.GONE);

        bundle = getIntent().getExtras();

        folderReference = bundle.getString("foldername");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("AlvargalinManam").child("Audio").child(folderReference);

        list = new ArrayList<String >();
        url = new ArrayList<String>();
        price = new ArrayList<String>();
        databaseHelper = new AlvargalinManamDatabaseHelper(this);

        mediaPlayer = new MediaPlayer();

        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.videoinfo,R.id.portal,list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //azhwarmusic = new Azhwarmusic1();
                priceList = new NamePriceList();

                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    progressBar.setVisibility(View.VISIBLE);
                    priceList = ds.getValue(NamePriceList.class);
                    list.add(String.valueOf(priceList.getPortal()));
                    url.add(String.valueOf(priceList.getUrl()));
                    price.add(String.valueOf(priceList.getValue()));
                }

                progressBar.setVisibility(View.GONE);
                listView.setAdapter(adapter);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0 || isAudioAvailable(list.get(position)))
                        {
                            String MusicUrl = url.get(position);

                            //Toast.makeText(getApplicationContext(),"Song Url : " + MusicUrl,Toast.LENGTH_SHORT).show();

                            try{
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(MusicUrl);
                                mediaPlayer.prepare();
                                Toast.makeText(getApplicationContext(),"Play",Toast.LENGTH_SHORT).show();
                                mediaPlayer.start();
                                pausePlayCounter = 0;

                                PlayinSong.setText("Playing now : " + list.get(position));
                                PlayinSong.setSelected(true);
                                Download.setEnabled(true);
                                Download.setText("Download");
                                Download.setSelected(true);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Mediaplayer couln't start",Toast.LENGTH_SHORT).show();
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
                            });

                            /*Download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(),"Downloading : " + list.get(position),Toast.LENGTH_SHORT).show();

                                    Uri uri = Uri.parse(MusicUrl);

                                    DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                    DownloadManager.Request request = new DownloadManager.Request(uri);

                                    Context context = getApplicationContext();
                                    String fileName = list.get(position);
                                    String fileExtension = ".mp3";

                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,fileName + fileExtension);

                                    downloadManager.enqueue(request);
                                }
                            });*/
                        }
                        else {
                            // payment gateway

                            payementSelector = new Dialog(MusicLister.this);
                            payementSelector.setContentView(R.layout.payment_selector);

                            upiPayment = payementSelector.findViewById(R.id.googlePlayButton);

                            upiPayment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    GooglePayProcessor = new Dialog(MusicLister.this);
                                    GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                                    PayNow = GooglePayProcessor.findViewById(R.id.paynow);
                                    AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                                    AmountToPay.setText(price.get(position));

                                    PayNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                                            UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                                            //Storing the data from layout to the sting variables
                                            Name = NameTextView.getText().toString();
                                            UPI_Id = UPI_id_TextView.getText().toString();
                                            Amount = price.get(position);

                                            Note = list.get(position);

                                            URL_Music = url.get(position);

                                            payUsingUpi(Amount,UPI_Id,Name,Note,URL_Music);

                                        }
                                    });

                                    GooglePayProcessor.show();

                                }
                            });

                            payementSelector.show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void payUsingUpi(String amount,String upiId,String name,String note,String bookUrl)
    {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay with");

        if(null != chooser.resolveActivity(getPackageManager()))
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
                if((RESULT_OK == resultCode) || (resultCode == 11))
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

    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if(isConnectionAvailable(MusicLister.this))
        {
            String str = data.get(0);
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

            if(status.equals("success"))
            {
                isSongUpdated(Note);
                Toast.makeText(MusicLister.this,"Transaction Successful \n Press the song to play",Toast.LENGTH_SHORT).show();
                Log.d("UPI","responseStr : " + approvalRefNo);

                GooglePayProcessor.dismiss();
                payementSelector.dismiss();

                /*Uri uri_book = Uri.parse(URL_Music);

                DownloadManager downloadManager; //(DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri_book);

                Context context = getApplicationContext();
                String filename = Note;
                String fileExtention = ".mp3";

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtention);

                Toast.makeText(getApplicationContext(),"Downloading : " + Note,Toast.LENGTH_SHORT).show();

                downloadManager.enqueue(request);*/

            }
            else if ("Payment cancelled by user.".equals(paymentCancel))
            {
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();
                Toast.makeText(MusicLister.this,"Payment cancelled by the user" ,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MusicLister.this,"Transaction failed.Please try again later",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MusicLister.this,"Internet connection not Available",Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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

    public void isSongUpdated(String songName)
    {
        boolean isInsertDataSuccessful = databaseHelper.insertAudio(songName);

        if(isInsertDataSuccessful)
        {
            Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Failed to insert Data",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isAudioAvailable(String audioName)
    {
        Cursor data = databaseHelper.fetchSongList();

        while(data.moveToNext())
        {
            if(audioName.equals(data.getString(0)))
            {
                return true;
            }
        }
        return false;
    }
}
