package com.example.koodalnraghavan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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
    private Button Donation;

    private TextView DailyKural;
    private TextView Jodhidam;
    private TextView Alwargal;
    private TextView ebooks;
    private TextView StoryTime;
    private TextView dhinachariyai;
    private TextView babyName;
    private TextView Dance;
    private TextView TitleTootlbar;
    //private TextView donation;

    private Intent nextActivity;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private SharedPreferences sharedPreferences;
    private boolean LanguageSelector;

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
        //donation = findViewById(R.id.)

        //------------------------------------------------------------------------------------------
        sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);

        LanguageSelector = sharedPreferences.getBoolean("value",true);
        //Toast.makeText(getApplicationContext()," " + LanguageSelector,Toast.LENGTH_SHORT).show();

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
                //nextActivity = new Intent(home.this,KadhaiKekumNeram.class);
                nextActivity = new Intent(home.this,NotFound.class);
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
                nextActivity = new Intent(home.this,NotFound.class);
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

        //Donation = getSupportActionBar().getCustomView().findViewById(R.id.donation);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.home:
                Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                Toast.makeText(getApplicationContext(),"About Us",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.activity:
                Toast.makeText(getApplicationContext(),"Activity",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.event:
                Toast.makeText(getApplicationContext(),"Eventt",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                Toast.makeText(getApplicationContext(),"Free Downloads",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.settings:
                nextActivity = new Intent(this, Settings.class);
                startActivity(nextActivity);
                break;
            case R.id.exit:
                Toast.makeText(getApplicationContext(),"Exit",Toast.LENGTH_SHORT).show();

                dialog = new AlertDialog.Builder(home.this);
                dialog.setMessage("Do you wish to quit !");
                dialog.setTitle("Exit");
                dialog.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        System.exit(0);

                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog = dialog.create();
                alertDialog.show();
                break;
        }
        return false;
    }
}
