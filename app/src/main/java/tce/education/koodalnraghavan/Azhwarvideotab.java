package tce.education.koodalnraghavan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
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


public class Azhwarvideotab extends Fragment {

    public Azhwarvideotab() {
        // Required empty public constructor
    }

    private Expander listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String, List<DataPriceLister>> listDataChild;

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

    //private ExpandableListView listView;
    private ProgressBar progressBar;
    private LinearLayout upiPayment;
    private TextView NameTextView;
    private TextView UPI_id_TextView;
    private Button Paynow;
    private TextView AmountToPay;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayList<String> price;
    private ArrayAdapter<String> adapter;
    private String Name;
    private String UPI_Id;
    private String Amount;
    private String Note;
    private String URL_Music;


    private Intent nextActivity;
    private Bundle bundle;
    private Dialog payementSelector;
    private Dialog GooglePayProcessor;
    private View view;

    //public PdfLoader pdfLoader;
    private AzhwarVideoDatabaseHelper databaseHelper;

    /*private String JSON_URL = " https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject video;
    private String videoName;
    private String videoUrl;
    private String videoAmount;*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_azhwarvideotab, container, false);

        listView = view.findViewById(R.id.listView);


        databaseHelper = new AzhwarVideoDatabaseHelper(view.getContext());

        prepareListData(view);

        listAdapter = new Expander(view.getContext(), listDataHeader, listDataChild);
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

        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                DataPriceLister lister = (DataPriceLister) listAdapter.getChild(groupPosition, childPosition);

                if (isVideoAvailable(lister.getName()) || lister.getFormat().equals("pdf")) {

                    if (lister.getFormat().equals("pdf"))
                    {
                        Intent nextActivity = new Intent(view.getContext(), PdfViewer.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("url", lister.getUrl());

                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);
                    }
                    else {
                        nextActivity = new Intent(view.getContext(), WebVideo.class);

                        bundle = new Bundle();
                        bundle.putString("list", lister.getName());
                        bundle.putString("url", lister.getUrl());
                        //bundle.putInt("flag",1);
                        bundle.putBoolean("flag", true);

                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);
                    }

                } else {
                    payementSelector = new Dialog(view.getContext());
                    payementSelector.setContentView(R.layout.payment_selector);

                    upiPayment = payementSelector.findViewById(R.id.googlePlayButton);

                    upiPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            GooglePayProcessor = new Dialog(view.getContext());
                            GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                            Paynow = GooglePayProcessor.findViewById(R.id.paynow);
                            AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                            AmountToPay.setText(lister.getPrice());

                            Paynow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                                    UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                                    //Storing the data from layout to the sting variables
                                    Name = NameTextView.getText().toString();
                                    UPI_Id = UPI_id_TextView.getText().toString();
                                    Amount = lister.getPrice();
                                    //Amount = "1";

                                    Note = lister.getName();

                                    URL_Music = lister.getUrl();

                                    payUsingUpi(Amount, UPI_Id, Name, Note, URL_Music);
                                }
                            });

                            GooglePayProcessor.show();
                        }
                    });
                    payementSelector.show();
                }
                return false;
            }
        });

        //------------------------------------------------------------------------------------------

        /*queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("alwarvideo");


                            for(int iterator = 0 ; iterator < jsonArray.length() ; iterator++)
                            {
                                video = jsonArray.getJSONObject(iterator);

                                videoName = video.getString("name");
                                videoUrl = video.getString("url");
                                videoAmount = String.valueOf(video.getInt("price"));

                                list.add(videoName);
                                url.add(videoUrl);
                                price.add(videoAmount);
                            }
//                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    if(position == 0 || isVideoAvailable(list.get(position)))
                                    {
                                        nextActivity = new Intent(view.getContext(),WebVideo.class);

                                        bundle = new Bundle();
                                        bundle.putString("list",list.get(position));
                                        bundle.putString("url",url.get(position));
                                        //bundle.putInt("flag",1);
                                        bundle.putBoolean("flag",true);

                                        nextActivity.putExtras(bundle);
                                        startActivity(nextActivity);
                                    }
                                    else
                                    {
                                        payementSelector = new Dialog(view.getContext());
                                        payementSelector.setContentView(R.layout.payment_selector);

                                        upiPayment = payementSelector.findViewById(R.id.googlePlayButton);

                                        upiPayment.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                GooglePayProcessor = new Dialog(view.getContext());
                                                GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                                                Paynow = GooglePayProcessor.findViewById(R.id.paynow);
                                                AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                                                AmountToPay.setText(price.get(position));

                                                Paynow.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                                                        UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                                                        //Storing the data from layout to the sting variables
                                                        Name = NameTextView.getText().toString();
                                                        UPI_Id = UPI_id_TextView.getText().toString();
                                                        Amount = price.get(position);
                                                        //Amount = "1";

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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"PLease try again later",Toast.LENGTH_SHORT).show();
            }
        });
        //------------------------------------------------------------------------------------------

        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                priceList = new NamePriceList();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                   // progressBar.setVisibility(View.VISIBLE);
                    priceList = ds.getValue(NamePriceList.class);
                    list.add(String.valueOf(priceList.getPortal()));
                    url.add(String.valueOf(priceList.getUrl()));
                    price.add(String.valueOf(priceList.getValue()));
                }
                //progressBar.setVisibility(View.GONE);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0 || isVideoAvailable(list.get(position)))
                        {
                            nextActivity = new Intent(view.getContext(),VideoPlayer.class);

                            bundle = new Bundle();
                            bundle.putString("list",list.get(position));
                            bundle.putString("url",url.get(position));
                            //bundle.putInt("flag",1);
                            bundle.putBoolean("flag",true);

                            nextActivity.putExtras(bundle);
                            startActivity(nextActivity);
                        }
                        else
                        {
                            payementSelector = new Dialog(view.getContext());
                            payementSelector.setContentView(R.layout.payment_selector);

                            upiPayment = payementSelector.findViewById(R.id.googlePlayButton);

                            upiPayment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    GooglePayProcessor = new Dialog(view.getContext());
                                    GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                                    Paynow = GooglePayProcessor.findViewById(R.id.paynow);
                                    AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                                    AmountToPay.setText(price.get(position));

                                    Paynow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                                            UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                                            //Storing the data from layout to the sting variables
                                            Name = NameTextView.getText().toString();
                                            UPI_Id = UPI_id_TextView.getText().toString();
                                            Amount = price.get(position);
                                            //Amount = "1";

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
        });*/

        //queue.add(request);
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
                            jsonArray = response.getJSONArray("alwarvideo");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");
                                if (!(listDataHeader.contains(folder_name))) {
                                    listDataHeader.add(folder_name);
                                    counter++;

                                    List<DataPriceLister> list = new ArrayList<DataPriceLister>();
                                    audio_name = folder.getString("name");
                                    list.add(new DataPriceLister(audio_name, folder.getString("url"), String.valueOf(folder.getInt("price")), folder.getString("format")));

                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        if (i != j) {
                                            folder = jsonArray.getJSONObject(j);
                                            tempFolderName = folder.getString("folder");

                                            if (tempFolderName.equals(folder_name)) {
                                                audio_name = folder.getString("name");
                                                list.add(new DataPriceLister(audio_name, folder.getString("url"), String.valueOf(folder.getInt("price")), folder.getString("format")));
                                            }
                                        }
                                    }
                                    listDataChild.put(listDataHeader.get(counter - 1), list);
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
                Toast.makeText(view.getContext(), "Please try Again later or Try again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    public void payUsingUpi(String amount, String upiId, String name, String note, String bookUrl) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        if (null != chooser.resolveActivity(view.getContext().getPackageManager())) {
            startActivityForResult(chooser, 0);
        } else {
            Toast.makeText(view.getContext(), "No UPI found, please install one of the upi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if ((Activity.RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult : " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {

        if (isConnectionAvailable(view.getContext())) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation : " + str);
            String paymentCancel = "";

            if (str == null) {
                str = "discard";
            }
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");

            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //isSongUpdated(Note);
                isVideoUpdated(Note, URL_Music);
                Toasty.success(view.getContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr : " + approvalRefNo);
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();

                nextActivity = new Intent(view.getContext(), VideoPlayer.class);

                bundle = new Bundle();
                bundle.putString("list", Note);
                bundle.putString("url", URL_Music);
                //bundle.putInt("flag",1);
                bundle.putBoolean("flag", true);

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

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();
                //isVideoUpdated(Note,URL_Music);
                Toasty.warning(view.getContext(), "Payment cancelled by the user", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(view.getContext(), "Transaction failed.Please try again later", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toasty.info(view.getContext(), "Internet connection not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void isVideoUpdated(String videoName, String url) {
        boolean isUpdated = databaseHelper.insertValue(videoName, url);
        if (isUpdated)
            Toast.makeText(view.getContext(), "Video Available", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
    }

    public boolean isVideoAvailable(String videoName) {
        Cursor data = databaseHelper.fetchVideoList();

        while (data.moveToNext()) {
            if (videoName.equals(data.getString(0))) {
                return true;
            }
        }
        return false;
    }
}
