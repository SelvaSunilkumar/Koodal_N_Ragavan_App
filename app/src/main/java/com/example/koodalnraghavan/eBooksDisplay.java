package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class eBooksDisplay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private ListView listView;
    private ProgressBar progressBar;
    private Button GooglePayNow;
    private TextView NameTextView;
    private TextView UPI_id_TextView;
    private TextView AmountPayable;
    private TextView TitleTootlbar;
    private Button Donation;
    private ImageView info;


    private String Name;
    private String UPI_Id;
    private String Amount;
    private String Note;
    private String bookUrl;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayList<String> price;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private LinearLayout googlePayGateway;

    private Dialog paymentSelectorButton;
    private Dialog GooglePayGatewayProcessor;

    private Intent nextActivity;
    private Bundle bundle;

    private eBooksDatabaseHelper databaseHelper;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private final String JSON_URL = "https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject ebook;
    private String ebook_name;
    private String ebook_url;
    private String ebook_amount;

    private WebView webView;
    private Dialog information;

    public void onPause() {

        webView.goBack();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(webView != null)
        {
            webView.destroy();
        }
        super.onDestroy();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_books_display);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        TitleTootlbar = findViewById(R.id.titleId);
        TitleTootlbar.setText(R.string.ebooks_en);
        TitleTootlbar.setSelected(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle(" e-Books");
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.loader);
        webView = findViewById(R.id.web);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        price = new ArrayList<String>();
        databaseHelper = new eBooksDatabaseHelper(this);

        adapter = new ArrayAdapter<String>(this, R.layout.pdfinfo, R.id.portal, list);

        Donation = findViewById(R.id.donation);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(eBooksDisplay.this, Sambavanai.class);
                startActivity(intent);
            }
        });

        info = findViewById(R.id.infor);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                information = new Dialog(eBooksDisplay.this);
                information.setContentView(R.layout.infor);
                information.show();
            }
        });

        queue = Volley.newRequestQueue(eBooksDisplay.this);
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            progressBar.setVisibility(View.VISIBLE);

                            jsonArray = response.getJSONArray("ebooks");

                            for (int iterator = 0; iterator < jsonArray.length(); iterator++) {
                                ebook = jsonArray.getJSONObject(iterator);

                                ebook_name = ebook.getString("name");
                                ebook_amount = String.valueOf(ebook.getInt("price"));
                                ebook_url = ebook.getString("url");

                                list.add(ebook_name);
                                url.add(ebook_url);
                                price.add(ebook_amount);

                                if(iterator > 0)
                                {
                                    if(ebook_name.equals("Sample Book"))
                                    {
                                        Collections.swap(list,0,iterator);
                                        Collections.swap(url,0,iterator);
                                        Collections.swap(price,0,iterator);
                                    }
                                }
                            }

                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (list.get(position).equals("Sample Book") || isBookAvailable(list.get(position))) {

                                        if (list.get(position).equals("Sample Book"))
                                        {
                                            nextActivity = new Intent(eBooksDisplay.this, PdfViewer.class);

                                            bundle = new Bundle();
                                            bundle.putString("url", url.get(position));

                                            nextActivity.putExtras(bundle);
                                            startActivity(nextActivity);
                                        }

                                        else
                                        {
                                            downloadBook(url.get(position),list.get(position));
                                        }

                                    } else {
                                        paymentSelectorButton = new Dialog(eBooksDisplay.this);
                                        paymentSelectorButton.setContentView(R.layout.payment_selector);

                                        googlePayGateway = paymentSelectorButton.findViewById(R.id.googlePlayButton);

                                        googlePayGateway.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(getApplicationContext(), "Processing to Gpay", Toast.LENGTH_SHORT).show();

                                                GooglePayGatewayProcessor = new Dialog(eBooksDisplay.this);
                                                GooglePayGatewayProcessor.setContentView(R.layout.google_pay_account_info);

                                                GooglePayNow = GooglePayGatewayProcessor.findViewById(R.id.paynow);

                                                AmountPayable = GooglePayGatewayProcessor.findViewById(R.id.amountPayable);
                                                AmountPayable.setText(price.get(position));

                                                GooglePayNow.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //Toast.makeText(getApplicationContext(),"Payment code pending",Toast.LENGTH_SHORT).show();

                                                        NameTextView = GooglePayGatewayProcessor.findViewById(R.id.payee_name);
                                                        UPI_id_TextView = GooglePayGatewayProcessor.findViewById(R.id.payee_upi);

                                                        Name = NameTextView.getText().toString();
                                                        UPI_Id = UPI_id_TextView.getText().toString();
                                                        Amount = price.get(position);

                                                        Note = list.get(position);

                                                        bookUrl = url.get(position);

                                                        payUsingUpi(Amount, UPI_Id, Name, Note, bookUrl);

                                                    }
                                                });

                                                GooglePayGatewayProcessor.show();
                                            }
                                        });
                                        paymentSelectorButton.show();
                                        //}
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
                error.printStackTrace();
            }
        });
        queue.add(request);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.home:
                //Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                //Toast.makeText(getApplicationContext(),"About Us",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.others:
                nextActivity = new Intent(this, Others.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                //Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                ///Toast.makeText(getApplicationContext(),"Free Downloads",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this, Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                //Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, ContactUs.class);
                startActivity(nextActivity);
                break;
            case R.id.google:
                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Taking you to Google");
                dialog.setTitle("Google");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("http://www.kavignakoodalnraghavan.com");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(launchBrowser);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
                break;
            case R.id.facebook:
                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Taking you to Facebook");
                dialog.setTitle("Facebook");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://www.facebook.com/kavignakoodal.n.raghavan");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(launchBrowser);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
                break;
            case R.id.twitter:
                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Taking you to Twitter");
                dialog.setTitle("Twitter");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://twitter.com/koodalraghavan?lang=en");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(launchBrowser);
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
                break;
            case R.id.youtube:
                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Taking you to Youtube");
                dialog.setTitle("Youtube");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://www.youtube.com/user/RANGASRI4");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(launchBrowser);
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                alertDialog = dialog.create();
                alertDialog.show();
                break;
            case R.id.settings:
                nextActivity = new Intent(this, Settings.class);
                startActivity(nextActivity);
                break;
        }
        return false;
    }

    void payUsingUpi(String amount, String upiId, String name, String note, String bookUrl) {
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

        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, 0);
        } else {
            Toast.makeText(getApplicationContext(), "No UPI found, please install one of the upi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
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
        if (isConnectionAvailable(eBooksDisplay.this)) {
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
                AddData(Note, bookUrl);

                paymentSelectorButton.dismiss();
                GooglePayGatewayProcessor.dismiss();

                Toasty.success(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                //Toast.makeText(eBooksDisplay.this,"Transaction Successful",Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr : " + approvalRefNo);

                downloadBook(bookUrl,Note);

                /*nextActivity = new Intent(eBooksDisplay.this, PdfViewer.class);

                bundle = new Bundle();
                bundle.putString("url", bookUrl);

                nextActivity.putExtras(bundle);
                startActivity(nextActivity);*/

                /*Uri uri_book = Uri.parse(bookUrl);

                DownloadManager downloadManager; //(DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri_book);

                Context context = getApplicationContext();
                String filename = Note;
                String fileExtention = ".pdf";

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtention);

                Toast.makeText(getApplicationContext(),"Downloading : " + Note,Toast.LENGTH_SHORT).show();

                downloadManager.enqueue(request);*/

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                //AddData(Note,bookUrl);
                GooglePayGatewayProcessor.dismiss();
                paymentSelectorButton.dismiss();
                //Toast.makeText(eBooksDisplay.this,"Payment cancelled by the user",Toast.LENGTH_SHORT).show();

                Toasty.warning(getApplicationContext(), "Payment Cancelled by User", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(eBooksDisplay.this,"Transaction failed.Please try again later",Toast.LENGTH_SHORT).show();
                Toasty.error(getApplicationContext(), "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(eBooksDisplay.this,"Internet connection not Available",Toast.LENGTH_SHORT).show();
            Toasty.info(getApplicationContext(), "Internet Connection not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadBook(String bookUrl,String name) {

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setHttpAuthUsernamePassword("https://tpvs.tce.edu/restricted/","realm","tpvsuser1","tpvs@userONE");

        webView.loadUrl(bookUrl);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                String cookie = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("Cookie",cookie);
                request.addRequestHeader("User-agent",userAgent);
                request.allowScanningByMediaScanner();

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,name);

                manager.enqueue(request);


                /*DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                manager.enqueue(request);

                Toast.makeText(eBooksDisplay.this,"Downloading file...",Toast.LENGTH_SHORT).show();

                /*Uri uri_book = Uri.parse(url);

                DownloadManager downloadManager; //(DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri_book);

                Context context = getApplicationContext();
                String filename = Note;
                String fileExtention = ".pdf";

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtention);

                Toast.makeText(getApplicationContext(),"Downloading : " + Note,Toast.LENGTH_SHORT).show();

                downloadManager.enqueue(request);*/
            }
        });
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

    public void AddData(String bookName, String BookUrl) {
        boolean insertBook = databaseHelper.insertBook(bookName, BookUrl);

        if (insertBook) {
            Toast.makeText(getApplicationContext(), "Book available ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isBookAvailable(String bookName) {
        Cursor data = databaseHelper.fetchBooks();

        while (data.moveToNext()) {
            if (bookName.equals(data.getString(0))) {
                return true;
            }
        }
        return false;
    }
}
