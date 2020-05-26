package com.example.koodalnraghavan;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class Azhwarvideotab extends Fragment {

    public Azhwarvideotab() {
        // Required empty public constructor
    }

    private ListView listView;
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

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private Intent nextActivity;
    private Bundle bundle;
    private Dialog payementSelector;
    private Dialog GooglePayProcessor;
    private View view;

    //public PdfLoader pdfLoader;
    private NamePriceList priceList;
    private AzhwarVideoDatabaseHelper databaseHelper;


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
        progressBar = view.findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("108Azhwarvideos");

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        price = new ArrayList<String>();
        databaseHelper = new AzhwarVideoDatabaseHelper(view.getContext());

        adapter = new ArrayAdapter<String >(view.getContext(),R.layout.musicinfo,R.id.portal,list);

        reference.addValueEventListener(new ValueEventListener() {
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

                        if(position == -1 || isVideoAvailable(list.get(position)))
                        {
                            Intent pdfLayout = new Intent(view.getContext(), VideoPlayer.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url.get(position));
                            pdfLayout.putExtras(bundle);
                            startActivity(pdfLayout);
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
                                            //Amount = price.get(position);
                                            Amount = "1";

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
        return view;
    }

    public void payUsingUpi(String amount,String upiId,String name,String note,String bookUrl)
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
                isVideoUpdated(Note);
                Toast.makeText(view.getContext(),"Transaction Successful",Toast.LENGTH_SHORT).show();
                Log.d("UPI","responseStr : " + approvalRefNo);
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();

                nextActivity = new Intent(view.getContext(),VideoPlayer.class);

                bundle = new Bundle();
                bundle.putString("list",Note);
                bundle.putString("url",URL_Music);

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
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();
                Toast.makeText(view.getContext(),"Payment cancelled by the user" ,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(view.getContext(),"Transaction failed.Please try again later",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(view.getContext(),"Internet connection not Available",Toast.LENGTH_SHORT).show();
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

    public void isVideoUpdated(String videoName)
    {
        boolean isUpdated = databaseHelper.insertValue(videoName);
        if(isUpdated)
            Toast.makeText(view.getContext(),"Inserted Successfully",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(view.getContext(),"Insertion failed",Toast.LENGTH_SHORT).show();
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
