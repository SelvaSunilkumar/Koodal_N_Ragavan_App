package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    public Switch LanguageSelector;
    private TextView opted;

    //public SharedPreferences sharedPreferences;
    //public SharedPreferences.Editor editor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LanguageSelector = findViewById(R.id.languageSelector);
        opted = findViewById(R.id.option);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(" Settings");
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        LanguageSelector.setChecked(sharedPreferences.getBoolean("value",false));

        LanguageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(LanguageSelector.isChecked())
                {

                   SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                   editor.putBoolean("value",true);
                   editor.apply();
                   LanguageSelector.setChecked(true);
                   opted.setText("Tamil");

                }
                else
                {

                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();;
                    LanguageSelector.setChecked(false);
                    opted.setText("English");

                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
