package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class BabyNames extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem BoyNameTab;
    private TabItem GirlNameTab;
    private Button Donation;
    private TextView TitleTootlbar;

    private BabyNameAdapter nameAdapter;

    private Intent nextActivity;
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private LinearLayout inforLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private ListView infoListView;
    private ToggleButton informToggleButton;
    private ArrayList<String> informList;
    private ArrayList<String> informUrl;
    private ArrayAdapter<String> informAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_names);

        tabLayout = findViewById(R.id.tablayout);
        BoyNameTab = findViewById(R.id.boyName);
        GirlNameTab = findViewById(R.id.girlName);
        viewPager = findViewById(R.id.viewpager);

        TitleTootlbar = findViewById(R.id.titleId);
        TitleTootlbar.setText(R.string.babyName_en);
        TitleTootlbar.setSelected(true);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationbar);

        inforLayout = findViewById(R.id.InformbottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(inforLayout);
        infoListView = findViewById(R.id.infoLister);
        informToggleButton = findViewById(R.id.toogleInformation);

        informToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED)
                {
                    informToggleButton.setChecked(true);
                }
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    informToggleButton.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        informList = new ArrayList<>();
        informUrl = new ArrayList<>();
        informAdapter = new ArrayAdapter<>(BabyNames.this,R.layout.pdfinfo,R.id.portal,informList);

        informList.add("Baby Name - English");
        informList.add("Baby Name - Tamil");
        informUrl.add("https://tpvs.tce.edu/unrestricted/description/Baby%20Name%20Front%20Page-Eng.pdf");
        informUrl.add("https://tpvs.tce.edu/unrestricted/description/Baby%20Name%20Front%20Page-Tam.pdf");

        infoListView.setAdapter(informAdapter);
        infoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(BabyNames.this,PdfViewer.class);
                Bundle bundle = new Bundle();

                bundle.putString("url",informUrl.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(BabyNames.this,Sambavanai.class);
                startActivity(intent);
            }
        });

        nameAdapter = new BabyNameAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(nameAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0)
                {
                    nameAdapter.notifyDataSetChanged();
                }
                else {
                    nameAdapter.notifyDataSetChanged();
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
                nextActivity = new Intent(this, home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                nextActivity = new Intent(this, AboutUs.class);
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
                nextActivity = new Intent(this, FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
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
                nextActivity = new Intent(this,Settings.class);
                startActivity(nextActivity);
                break;
        }
        return false;
    }
}
