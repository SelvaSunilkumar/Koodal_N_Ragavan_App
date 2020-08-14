package tce.education.koodalnraghavan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import tce.education.koodalnraghavan.adapter.AlwarAdapter;

public class AlwarVideoSubFolder extends AppCompatActivity {

    private Bundle bundle;
    private String selectedFolder;
    private int index;
    private String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";

    private TextView textView;
    private ListView listView;
    private ArrayList<DataPriceLister> list;
    private AlwarAdapter alwarAdapter;

    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private JSONArray array;
    private JSONObject object;

    private AzhwarVideoDatabaseHelper databaseHelper;
    private DataPriceLister lister;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alwar_video_sub_folder);

        textView = findViewById(R.id.folderName);
        listView = findViewById(R.id.listView);

        bundle = getIntent().getExtras();
        list = new ArrayList<>();
        alwarAdapter = new AlwarAdapter(getApplicationContext(),R.layout.videoinfo,list);
        databaseHelper = new AzhwarVideoDatabaseHelper(getApplicationContext());

        index = bundle.getInt("index");
        selectedFolder = bundle.getString("folderName");
        textView.setText(selectedFolder);

        requestQueue = Volley.newRequestQueue(AlwarVideoSubFolder.this);
        request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            array = response.getJSONArray("alwarvideo");

                            for (int i=0;i<array.length();i++)
                            {
                                object = array.getJSONObject(i);

                                if (selectedFolder.equals(object.getString("folder")))
                                {
                                    list.add(new DataPriceLister(object.getString("name"),object.getString("url"),String.valueOf(object.getInt("price")),object.getString("format")));
                                }
                            }
                            listView.setAdapter(alwarAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    lister = list.get(position);

                                    if (lister.getFormat().equals("pdf"))
                                    {
                                        Intent intent = new Intent(AlwarVideoSubFolder.this,PdfViewAuthentication.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("url",lister.getUrl());

                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    if (lister.getFormat().equals("video"))
                                    {
                                        if (isVideoAvailable(lister.getName()))
                                        {
                                            Intent nextActivity = new Intent(view.getContext(),VideoPlayerAuthentication.class);

                                            bundle = new Bundle();
                                            bundle.putString("list",lister.getName());
                                            bundle.putString("url",lister.getUrl());
                                            //bundle.putInt("flag",1);
                                            bundle.putBoolean("flag",true);

                                            nextActivity.putExtras(bundle);
                                            startActivity(nextActivity);
                                        }
                                        else {
                                            payementSelector = new Dialog(AlwarVideoSubFolder.this);
                                            payementSelector.setContentView(R.layout.payment_selector);

                                            upiPayment = payementSelector.findViewById(R.id.googlePlayButton);

                                            upiPayment.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    GooglePayProcessor = new Dialog(AlwarVideoSubFolder.this);
                                                    GooglePayProcessor.setContentView(R.layout.google_pay_account_info);

                                                    PayNow = GooglePayProcessor.findViewById(R.id.paynow);
                                                    AmountToPay = GooglePayProcessor.findViewById(R.id.amountPayable);
                                                    AmountToPay.setText(lister.getPrice());

                                                    PayNow.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            NameTextView = GooglePayProcessor.findViewById(R.id.payee_name);
                                                            UPI_id_TextView = GooglePayProcessor.findViewById(R.id.payee_upi);

                                                            Name = NameTextView.getText().toString();
                                                            UPI_Id = UPI_id_TextView.getText().toString();
                                                            Amount = lister.getPrice();

                                                            Note = selectedFolder;

                                                            URL_Music = lister.getUrl();

                                                            payUsingUpi(Amount,UPI_Id,Name,Note);
                                                        }
                                                    });

                                                    GooglePayProcessor.show();
                                                }
                                            });

                                            payementSelector.show();
                                        }
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
                Toast.makeText(getApplicationContext(),"Please Try again Later",Toast.LENGTH_SHORT).show();
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

        requestQueue.add(request);
    }

    private void payUsingUpi(String amount, String upi_id, String name, String note) {
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

            if(status.equals("success"))
            {
                isVideoUpdated(list);
                Toasty.success(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                Log.d("UPI","responseStr : " + approvalRefNo);
                GooglePayProcessor.dismiss();
                payementSelector.dismiss();

                Intent nextActivity = new Intent(getApplicationContext(),VideoPlayer.class);

                Bundle bundle = new Bundle();
                bundle.putString("list",Note);
                bundle.putString("url",URL_Music);
                bundle.putBoolean("flag",true);

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

    public void isVideoUpdated(ArrayList<DataPriceLister> dataLister)
    {
        boolean flag = false;

        for (int i=0;i<dataLister.size();i++)
        {
            if (!dataLister.get(i).getFormat().equals("pdf"))
            {
                databaseHelper.insertValue(dataLister.get(i).getName(),dataLister.get(i).getUrl());
                flag = true;
            }
            else {
                Toast.makeText(getApplicationContext(),"Video not Available free",Toast.LENGTH_SHORT).show();
            }
        }
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
