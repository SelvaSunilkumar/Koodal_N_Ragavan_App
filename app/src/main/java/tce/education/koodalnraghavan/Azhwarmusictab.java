package tce.education.koodalnraghavan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Azhwarmusictab extends Fragment  {

    private Expander listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String,List<DataPriceLister>> listDataChild;

    private String tempFolderName;
    private int counter;

    private final String JSON_URL = "https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject folder;
    private String folder_name;
    private String audio_url;
    private String audio_name;

    private MediaPlayer mediaPlayer;

    private Dialog music;
    private ImageView playAudio;
    private ImageView downloadAudio;
    private TextView Info;
    private ProgressBar progressBar1;
    private boolean flag = false;

    private WebView webView;

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

    private Dialog payementSelector;
    private Dialog GooglePayProcessor;

    private View view;
    private AlvargalinManamDatabaseHelper databaseHelper;
    private int groupposition;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_azhwarmusictab,container,false);

        listView = view.findViewById(R.id.lveExp);

        prepareListData(view);
        databaseHelper = new AlvargalinManamDatabaseHelper(view.getContext());


        listAdapter = new Expander(view.getContext(),listDataHeader,listDataChild);
        listView.setAdapter(listAdapter);

        webView = view.findViewById(R.id.web);
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

                DataPriceLister dataLister = (DataPriceLister) listAdapter.getChild(groupPosition,childPosition);

                int lister = listAdapter.getChildrenCount(groupPosition);

                String header = (String) listAdapter.getGroup(groupPosition);

                groupposition = groupPosition;

                //Toast.makeText(view.getContext(),header + "Child COunt : " + lister,Toast.LENGTH_SHORT).show();

                /*if( dataLister.getFormat().equals("pdf"))
                {
                    Toast.makeText(view.getContext(),"pdf",Toast.LENGTH_SHORT).show();
                }*/
                if( dataLister.getName().equals("Sample") || isAudioAvailable(dataLister.getName()) || dataLister.getFormat().equals("pdf"))
                {
                    if (dataLister.getFormat().equals("pdf"))
                    {
                        //Toast.makeText(view.getContext(),"Pdf",Toast.LENGTH_SHORT).show();
                        Intent nextActivity = new Intent(view.getContext(), PdfViewer.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("url", dataLister.getUrl());

                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);
                    }
                    else {
                        webView.loadUrl(dataLister.getUrl());
                    }
                }
                else
                {
                    //Payment
                    payementSelector = new Dialog(view.getContext());
                    payementSelector.setContentView(R.layout.payment_selector);

                    upiPayment = payementSelector.findViewById(R.id.googlePlayButton);

                    upiPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            GooglePayProcessor = new Dialog(view.getContext());
                            GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                            PayNow = GooglePayProcessor.findViewById(R.id.paynow);
                            AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                            AmountToPay.setText(dataLister.getPrice());

                            PayNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                                    UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                                    //Storing the data from layout to the sting variables
                                    Name = NameTextView.getText().toString();
                                    UPI_Id = UPI_id_TextView.getText().toString();
                                    Amount = dataLister.getPrice();

                                    Note = dataLister.getName();

                                    URL_Music = dataLister.getUrl();

                                    payUsingUpi(Amount,UPI_Id,Name,listAdapter,groupPosition);

                                }
                            });

                            GooglePayProcessor.show();

                        }
                    });

                    payementSelector.show();
                }
                /*Toast.makeText(view.getContext(),"Name : " + dataLister.getName() + "\n Url : " + dataLister.getUrl(),Toast.LENGTH_SHORT ).show();

                */
                return false;
            }
        });

        return view;
    }

    private void prepareListData(View view) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<DataPriceLister>>();

        counter = 0;

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("alwaraudio");

                            for (int i = 0; i < jsonArray.length() ; i++ )
                            {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");
                                if( ! (listDataHeader.contains(folder_name)) )
                                {
                                    listDataHeader.add(folder_name);
                                    counter++;

                                    List<DataPriceLister> list = new ArrayList<DataPriceLister>();
                                    audio_name = folder.getString("name");
                                    list.add(new DataPriceLister(audio_name,folder.getString("url"),String.valueOf(folder.getInt("price")),folder.getString("format")));

                                    for (int j = 0 ; j < jsonArray.length() ; j++ )
                                    {
                                        if( i != j )
                                        {
                                            folder = jsonArray.getJSONObject(j);
                                            tempFolderName = folder.getString("folder");

                                            if(tempFolderName.equals(folder_name))
                                            {
                                                audio_name = folder.getString("name");
                                                list.add(new DataPriceLister(audio_name,folder.getString("url"),String.valueOf(folder.getInt("price")),folder.getString("format")));
                                            }
                                        }
                                    }
                                    listDataChild.put(listDataHeader.get(counter-1),list);
                                    listAdapter.notifyDataSetChanged();
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
        });

        queue.add(request);
    }

    public void payUsingUpi(String amount, String upiId, String name, Expander listAdapter, int groupPosition)
    {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",String.valueOf(this.listAdapter.getGroup(groupPosition)))
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay with");

        if(null != chooser.resolveActivity(view.getContext().getPackageManager()))
        {
            startActivityForResult(chooser,0);
        }
        else
        {
            Toast.makeText(view.getContext(),"No UPI found, please install one of the upi",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    private void upiPaymentDataOperation(ArrayList<String> data) {

        if(isConnectionAvailable(view.getContext()))
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
                //isSongUpdated(Note);
                isSongUpdated(listAdapter,groupposition);
                Toasty.success(view.getContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                Log.d("UPI","responseStr : " + approvalRefNo);
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();

                Intent nextActivity = new Intent(view.getContext(),VideoPlayer.class);

                Bundle bundle = new Bundle();
                bundle.putString("list",Note);
                bundle.putString("url",URL_Music);
                //bundle.putInt("flag",1);
                bundle.putBoolean("flag",true);

                nextActivity.putExtras(bundle);
                startActivity(nextActivity);

                /*Uri uri_book = Uri.parse(URL_Music);
                DownloadManager downloadManager; //(DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri_book);
                Context context = view.getContext();
                String filename = Note;
                String fileExtention = ".mp3";
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtention);
                Toast.makeText(view.getContext(),"Downloading : " + Note,Toast.LENGTH_SHORT).show();
                downloadManager.enqueue(request);*/

            }
            else if ("Payment cancelled by user.".equals(paymentCancel))
            {
                //isSongUpdated(listAdapter,groupposition);
                //isSongUpdated(Note,URL_Music);
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();
                //isVideoUpdated(Note,URL_Music);
                Toasty.warning(view.getContext(),"Payment cancelled by the user" ,Toast.LENGTH_SHORT).show();
            }
            else {
                Toasty.error(view.getContext(),"Transaction failed.Please try again later",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toasty.info(view.getContext(),"Internet connection not Available",Toast.LENGTH_SHORT).show();
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
    public void isSongUpdated(Expander songName, int URL_Music)
    {
        //boolean isInsertDataSuccessful = databaseHelper.insertAudio(songName,URL_Music);

        boolean isInsertDataSuccessful;
        boolean flag = false;

        for(int i = 0;i<songName.getChildrenCount(URL_Music);i++)
        {
            DataPriceLister dataLister = (DataPriceLister) songName.getChild(URL_Music,i);

            if(!databaseHelper.insertAudio(dataLister.getName(),dataLister.getUrl()) && !dataLister.getFormat().equals("pdf"))
            {
                flag = true;
            }
        }

        if(!flag)
        {
            Toast.makeText(view.getContext(),"Audio Aviailable",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(view.getContext(),"",Toast.LENGTH_SHORT).show();
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
