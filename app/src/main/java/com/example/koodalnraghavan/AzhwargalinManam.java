package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class AzhwargalinManam extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TabLayout tabLayout;
    private TabItem MusicTab;
    private TabItem VideoTab;
    private ViewPager viewPager;

    public AzharwargalAdapter azharwargalAdapter;

    private Intent nextActivity;

    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azhwargalin_manam);

        tabLayout = findViewById(R.id.tablayout);
        MusicTab = findViewById(R.id.musictab);
        VideoTab = findViewById(R.id.videotab);
        viewPager = findViewById(R.id.viewpager);

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


        azharwargalAdapter=new AzharwargalAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        //pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(azharwargalAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)
                {
                    azharwargalAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==1)
                {
                    azharwargalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.home:
                Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(AzhwargalinManam.this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                Toast.makeText(getApplicationContext(),"About Us",Toast.LENGTH_SHORT).show();
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
                nextActivity = new Intent(AzhwargalinManam.this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
