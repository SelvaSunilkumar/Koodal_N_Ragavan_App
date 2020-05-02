package com.example.koodalnraghavan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IntegerRes;
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

    private TextView DhinamOruKural;
    private TextView Alvargal;
    private TextView Story;
    private TextView Astrology;
    private TextView NameMyBaby;

    private ImageView eBooks;
    private ImageView DailyVoice;
    private ImageView ThinachariyaiButton;
    private ImageView KadhaiKekumNeram;
    private ImageView jodhidam;
    private ImageView BabyName;
    private ImageView Donation;
    private Intent nextActivity;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

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

        //------------------------------------------------------------------------------------------

        DhinamOruKural = findViewById(R.id.dhinamoru);
        Alvargal = findViewById(R.id.alvars);
        Story = findViewById(R.id.storytime);
        Astrology = findViewById(R.id.astrology);
        NameMyBaby = findViewById(R.id.nameBaby);

        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        Boolean language = sharedPreferences.getBoolean("value",true);
        Toast.makeText(getApplicationContext(),"Language : " + language,Toast.LENGTH_SHORT ).show();

        if(language)
        {
            DhinamOruKural.setText(R.string.thinamorukural);
            Astrology.setText(R.string.jodhidam_en);
            Alvargal.setText(R.string.alwargalinManam_en);

            Story.setText(R.string.kadhai);

            NameMyBaby.setText(R.string.babyName_en);
        }
        else
        {
            DhinamOruKural.setText(R.string.thinamorukuralTamil);
            Astrology.setText(R.string.jodhidam_tml);
            Alvargal.setText(R.string.alwargalinManam_tml);

            Story.setText(R.string.kadhai_tml);

            NameMyBaby.setText(R.string.babyName_tml);
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

        BabyName = findViewById(R.id.babyname);
        BabyName.setClickable(true);
        BabyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity = new Intent(home.this,NotFound.class);
                startActivity(nextActivity);
            }
        });

        Donation = findViewById(R.id.donation);
        Donation.setClickable(true);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextActivity = new Intent(home.this,NotFound.class);
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
                Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,Settings.class);
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
            case R.id.logout:
                Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

}
