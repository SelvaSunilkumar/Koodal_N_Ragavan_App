package com.example.koodalnraghavan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private ImageView eBooks;
    private ImageView DailyVoice;
    private ImageView Thinachariyai;
    private ImageView KadhaiKekumNeram;
    private ImageView jodhidam;
    private Intent nextActivity;

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
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

        Thinachariyai = findViewById(R.id.thinahiriyai);
        Thinachariyai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(home.this,NotFound.class);
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
                break;
            case R.id.event:
                Toast.makeText(getApplicationContext(),"Eventt",Toast.LENGTH_SHORT).show();
                break;
            case R.id.Gallery:
                Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
                break;
            case R.id.freedownload:
                Toast.makeText(getApplicationContext(),"Free Downloads",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(home.this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
