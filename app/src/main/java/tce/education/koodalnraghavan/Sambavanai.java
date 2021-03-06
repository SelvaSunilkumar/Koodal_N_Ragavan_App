package tce.education.koodalnraghavan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class Sambavanai extends AppCompatActivity {

    private LinearLayout googlePay;

    private Button Paynow;
    private TextView NameTextView;
    private TextView UPI_TextView;
    private EditText NoteEditText;
    private EditText AmountEditText;
    private EditText PayeeNameEditText;

    private String Name;
    private String UPI_Id;
    private String Note;
    private String Amount;
    private String PayeeName;
    private String UserDatabaseId;

    private Dialog floatBanner;

    private UUID uuid;

    private String PAYMENT_MARKER_URL = "https://tpvs.tce.edu/restricted/koodal_app/sambavanai_payment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sambavanai);

        googlePay = findViewById(R.id.googlePay);



        googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"Payment using google pay",Toast.LENGTH_SHORT).show();

                floatBanner = new Dialog(Sambavanai.this);
                floatBanner.setContentView(R.layout.google_pay_payable);

                Paynow = floatBanner.findViewById(R.id.paynow);

                Paynow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(),"Payment using Google Pay",Toast.LENGTH_SHORT).show();

                        NameTextView = floatBanner.findViewById(R.id.payee_name);
                        UPI_TextView = floatBanner.findViewById(R.id.payee_upi);
                        NoteEditText = floatBanner.findViewById(R.id.payee_note);
                        AmountEditText = floatBanner.findViewById(R.id.payee_amount);
                        PayeeNameEditText = floatBanner.findViewById(R.id.payer_name);

                        Name = NameTextView.getText().toString();
                        UPI_Id = UPI_TextView.getText().toString();
                        Note = NoteEditText.getText().toString();
                        Amount = AmountEditText.getText().toString();
                        PayeeName = PayeeNameEditText.getText().toString();

                        if(Note.isEmpty() && Amount.isEmpty() && PayeeName.isEmpty())
                        {
                            PayeeNameEditText.setError("Please enter your Name");
                            NoteEditText.setError("Please give a note about the Payment");
                            AmountEditText.setError("Please fill the amount ");
                            PayeeNameEditText.requestFocus();
                        }
                        else if(PayeeName.isEmpty())
                        {
                            PayeeNameEditText.setError("Please enter your Name");
                            PayeeNameEditText.requestFocus();
                        }
                        else if(Note.isEmpty())
                        {
                            NoteEditText.setError("Please give a note about the Payment");
                            NoteEditText.requestFocus();
                        }
                        else if(Amount.isEmpty())
                        {
                            AmountEditText.setError("Please fill the amount");
                            AmountEditText.requestFocus();
                        }
                        else
                        {
                            payUsingUpi(Amount,UPI_Id,Name,Note);
                        }

                    }
                });

                floatBanner.show();
            }
        });
    }

    void payUsingUpi(String amount,String upiId,String name,String note)
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
        if(isConnectionAvailable(Sambavanai.this))
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
                Toasty.success(Sambavanai.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                Log.d("UPI","responseStr : " + approvalRefNo);

                NotePayment(PayeeName,approvalRefNo,Amount,Note);
                //uuid = UUID.randomUUID();

            }
            else if ("Payment cancelled by user.".equals(paymentCancel))
            {
                //NotePayment(PayeeName,"1233d435fd",Amount,Note);
                Toasty.warning(Sambavanai.this,"Payment cancelled by the user",Toast.LENGTH_SHORT).show();
            }
            else {
                Toasty.error(Sambavanai.this,"Transaction failed.Please try again later",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toasty.info(Sambavanai.this,"Internet connection not Available",Toast.LENGTH_SHORT).show();
        }
    }

    private void NotePayment(String name, String approvalRefNo, String amount, String note) {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String dateFormulated = simpleDateFormat.format(date);

        StringRequest request = new StringRequest(Request.Method.POST, PAYMENT_MARKER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("transaction");

                            if (result.equals("1"))
                            {
                                Toast.makeText(Sambavanai.this,"Payment Noted",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("name",name);
                params.put("reference",approvalRefNo);
                params.put("amount",amount);
                params.put("note",note);
                params.put("date",dateFormulated);

                return params;
            }

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

        RequestQueue queue = Volley.newRequestQueue(Sambavanai.this);
        queue.add(request);
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
}
