package tce.education.koodalnraghavan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private LinearLayout eBooks;
    private LinearLayout DailyVoice;
    private LinearLayout ThinachariyaiButton;
    private LinearLayout KadhaiKekumNeram;
    private LinearLayout jodhidam;
    private LinearLayout AlvargalinManam;
    private LinearLayout BabyName;
    private LinearLayout DanceIcon;
    private LinearLayout OthersAndUpanyasam;
    private LinearLayout PrasadhamView;
    private Button Donation;

    private TextView DailyKural;
    private TextView Jodhidam;
    private TextView Alwargal;
    private TextView ebooks;
    private TextView StoryTime;
    private TextView dhinachariyai;
    private TextView babyName;
    private TextView Dance;
    private TextView OthersText;
    private TextView prasadhamText;

    private Intent nextActivity;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private SharedPreferences sharedPreferences;
    private boolean LanguageSelector;

    private String userRegisterUrl = "https://tpvs.tce.edu/restricted/koodal_app/insert_token.php";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //------------------------------- Layout Initialization ------------------------------------

        DailyKural = findViewById(R.id.dhinamoru);
        Jodhidam = findViewById(R.id.astrology);
        Alwargal = findViewById(R.id.alvars);
        ebooks = findViewById(R.id.books);
        StoryTime = findViewById(R.id.storytime);
        dhinachariyai = findViewById(R.id.routine);
        babyName = findViewById(R.id.nameBaby);
        Dance = findViewById(R.id.danceId);
        OthersAndUpanyasam = findViewById(R.id.othersUpanyasam);
        OthersText = findViewById(R.id.othersUpanyasamText);
        prasadhamText = findViewById(R.id.prasadhamId);
        //donation = findViewById(R.id.)

        //------------------------------------------------------------------------------------------
        sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);

        LanguageSelector = sharedPreferences.getBoolean("value",true);

        RegisterUser();

        if(LanguageSelector)
        {
            //------------- Language Selected is English -----------------
            DailyKural.setText(R.string.thinamorukural);
            Jodhidam.setText(R.string.jodhidam_en);
            Alwargal.setText(R.string.alwargalinManam_en);
            StoryTime.setText(R.string.kadhai);
            babyName.setText(R.string.babyName_en);
            ebooks.setText(R.string.ebooks_en);
            dhinachariyai.setText(R.string.thinachariyai);
            Dance.setText(R.string.dance_en);
            OthersText.setText(R.string.other_en);
            prasadhamText.setText(R.string.prasadham_en);
        }
        else
        {
            //------------- Language Selected is Tamil ------------------
            DailyKural.setText(R.string.thinamorukuralTamil);
            Jodhidam.setText(R.string.jodhidam_tml);
            Alwargal.setText(R.string.alwargalinManam_tml);
            StoryTime.setText(R.string.kadhai_tml);
            babyName.setText(R.string.babyName_tml);
            ebooks.setText(R.string.ebooks_tml);
            dhinachariyai.setText(R.string.thinachariyai_tml);
            Dance.setText(R.string.dance_tml);
            OthersText.setText(R.string.other_tml);
            prasadhamText.setText(R.string.prasadham_tml);
        }
        //------------------------------------------------------------------------------------------
        DailyVoice = findViewById(R.id.ThinamOrukural);
        DailyVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,ThinamOruKural.class);
                startActivity(nextActivity);
            }
        });

        eBooks = findViewById(R.id.ebooks);
        eBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,eBooksDisplay.class);
                startActivity(nextActivity);
            }
        });

        ThinachariyaiButton = findViewById(R.id.thinahiriyai);
        ThinachariyaiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,Thinachariyai.class);
                startActivity(nextActivity);
            }
        });

        jodhidam = findViewById(R.id.Jodhidam);
        jodhidam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,jodhidam.class);
                startActivity(nextActivity);
            }
        });


        KadhaiKekumNeram = findViewById(R.id.KadhaiKekumNeram);
        KadhaiKekumNeram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,KadhaiKekumNeram.class);
                startActivity(nextActivity);
            }
        });

        AlvargalinManam = findViewById(R.id.azhwargalinManam);
        AlvargalinManam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,Azhwarmanam.class);
                startActivity(nextActivity);
            }
        });

        BabyName = findViewById(R.id.babyname);
        BabyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,BabyNames.class);
                startActivity(nextActivity);
            }
        });

        DanceIcon = findViewById(R.id.dance);
        DanceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity = new Intent(home.this,Dance.class);
                startActivity(nextActivity);
            }
        });

        OthersAndUpanyasam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,Others.class);
                startActivity(nextActivity);
            }
        });

        PrasadhamView = findViewById(R.id.prasadham);
        PrasadhamView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,Prasadham.class);
                startActivity(nextActivity);
            }
        });

        Donation = findViewById(R.id.donation);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,Sambavanai.class);
                startActivity(nextActivity);
            }
        });
        //__________________________________________________________________________________________
    }

    private void RegisterUser() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String token = task.getResult().getToken();

                        System.out.println("Token : " + token);

                        RegisterToken(token);
                    }
                });
    }

    private void RegisterToken(String token) {

        StringRequest request = new StringRequest(Request.Method.POST, userRegisterUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("success");
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
                params.put("token",token);
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

        RequestQueue queue = Volley.newRequestQueue(home.this);
        queue.add(request);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.home:
                nextActivity = new Intent(home.this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                nextActivity = new Intent(home.this,AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.events:
                nextActivity = new Intent(this,Events.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                nextActivity = new Intent(home.this,GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                nextActivity = new Intent(home.this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                nextActivity = new Intent(home.this,ContactUs.class);
                startActivity(nextActivity);
                break;
            case R.id.google:
                dialog = new AlertDialog.Builder(home.this);
                dialog.setMessage("Taking you to Google");
                dialog.setTitle("Google");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("http://www.kavignakoodalnraghavan.com");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uri);
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
                dialog = new AlertDialog.Builder(home.this);
                dialog.setMessage("Taking you to Facebook");
                dialog.setTitle("Facebook");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://www.facebook.com/kavignakoodal.n.raghavan");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uri);
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
                dialog = new AlertDialog.Builder(home.this);
                dialog.setMessage("Taking you to Twitter");
                dialog.setTitle("Twitter");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://twitter.com/koodalraghavan?lang=en");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uri);
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
                dialog = new AlertDialog.Builder(home.this);
                dialog.setMessage("Taking you to Youtube");
                dialog.setTitle("Youtube");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("https://www.youtube.com/user/RANGASRI4");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uri);
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
}
