package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import tce.education.koodalnraghavan.adapter.PrasadhamAdapter;

public class Prasadham extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PrasadhamAdapter adapter;

    private Intent nextActivity;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private Button Donation;
    private LinearLayout infoLayout;
    private BottomSheetBehavior infoBottomSheetBehaviour;
    private ToggleButton infoToggleButton;
    private ListView infoListView;

    private ArrayList<String> infoList;
    private ArrayList<String> infoUrl;
    private ArrayAdapter<String> infoAdaper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prasadham);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_tool_bar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Donation = findViewById(R.id.donation);
        Donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prasadham.this, Sambavanai.class);
                startActivity(intent);
            }
        });

        infoLayout = findViewById(R.id.InformbottomSheet);
        infoBottomSheetBehaviour = BottomSheetBehavior.from(infoLayout);
        infoListView = findViewById(R.id.infoLister);
        infoToggleButton = findViewById(R.id.toogleInformation);

        infoList = new ArrayList<>();
        infoUrl = new ArrayList<>();
        infoAdaper = new ArrayAdapter<>(this,R.layout.pdfinfo,R.id.portal,infoList);

        infoList.add("Prasadam -Tamil");
        infoUrl.add("https://tpvs.tce.edu/unrestricted/Dhinacharyai/description/Prasadam%20Front%20Page-Tam.pdf");

        infoListView.setAdapter(infoAdaper);
        infoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Prasadham.this,PdfViewer.class);
                Bundle bundle = new Bundle();

                bundle.putString("url",infoUrl.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        infoToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    infoBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    infoBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        infoBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int sheetStatus) {

                if (sheetStatus == BottomSheetBehavior.STATE_EXPANDED)
                {
                    infoToggleButton.setChecked(true);
                }
                else {
                    infoToggleButton.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        adapter = new PrasadhamAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
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
                //Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                //Toast.makeText(getApplicationContext(),"About Us",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.events:
                nextActivity = new Intent(this,Events.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                //Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                //Toast.makeText(getApplicationContext(),"Free Downloads",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                //Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,ContactUs.class);
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
                dialog = new AlertDialog.Builder(this);
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
                dialog = new AlertDialog.Builder(this);
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
                dialog = new AlertDialog.Builder(this);
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
