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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Azhwarmanam extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem MusicTab;
    private TabItem VideoTab;
    private Button Donation;
    private TextView TitleTootlbar;
    public AzhwarAdapter azhwarAdapter;

    private Intent nextActivity;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azhwarmanam);

        tabLayout = findViewById(R.id.tablayout);
        MusicTab = findViewById(R.id.musictab);
        VideoTab = findViewById(R.id.videotab);
        viewPager = findViewById(R.id.viewpager);

        TitleTootlbar = findViewById(R.id.titleId);
        TitleTootlbar.setText(R.string.alwargalinManam_en);
        TitleTootlbar.setSelected(true);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("108 Azhwargalin Manam");

        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Donation = findViewById(R.id.donation);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Azhwarmanam.this,Sambavanai.class);
                startActivity(intent);
            }
        });

        azhwarAdapter = new AzhwarAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(azhwarAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)
                {
                    azhwarAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==1)
                {
                    azhwarAdapter.notifyDataSetChanged();
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
        switch(menuItem.getItemId()) {
            case R.id.home:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.activity:
                Toast.makeText(getApplicationContext(), "Activity", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.event:
                Toast.makeText(getApplicationContext(), "Eventt", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                Toast.makeText(getApplicationContext(), "Free Downloads", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                Toast.makeText(getApplicationContext(), "Contact", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, NotFound.class);
                startActivity(nextActivity);
                break;
            case R.id.exit:
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
