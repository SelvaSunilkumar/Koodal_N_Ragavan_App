package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

public class Events extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private Intent nextActivity;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private CalendarView calendarView;
    private LinearLayout linearLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView date;
    private ToggleButton toggleButton;
    private TextView pageTitle;
    private Button sambavanai;

    private String selectedDate;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        init();

        pageTitle.setText("Events");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle(" About us");
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sambavanai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Events.this,Sambavanai.class);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                date.setText(selectedDate);
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int sheetCode) {
                if (sheetCode == BottomSheetBehavior.STATE_EXPANDED)
                {
                    toggleButton.setChecked(true);
                }
                if (sheetCode == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    toggleButton.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void init() {
        this.drawerLayout = findViewById(R.id.drawer);
        this.toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        this.calendarView = findViewById(R.id.calenderView);
        this.linearLayout = findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        this.date = findViewById(R.id.dater);
        this.toggleButton = findViewById(R.id.toogleButton);
        this.pageTitle = findViewById(R.id.titleId);
        this.sambavanai = findViewById(R.id.donation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.home:
                nextActivity = new Intent(this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                nextActivity = new Intent(this,AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.events:
                nextActivity = new Intent(this,Events.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                nextActivity = new Intent(this,GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                nextActivity = new Intent(this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                nextActivity = new Intent(this,ContactUs.class);
                startActivity(nextActivity);
                break;
            case R.id.google:
                dialog = new AlertDialog.Builder(Events.this);
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
                dialog = new AlertDialog.Builder(Events.this);
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
                dialog = new AlertDialog.Builder(Events.this);
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
                dialog = new AlertDialog.Builder(Events.this);
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
                nextActivity = new Intent(this,Settings.class);
                startActivity(nextActivity);
                break;
        }
        return false;
    }
}
