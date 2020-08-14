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
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private ToggleButton toggleButton;
    private LinearLayout infoLayout;
    private BottomSheetBehavior informationBottomSheet;
    private ListView infoLister;
    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

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
        toggleButton = findViewById(R.id.toogleInformation);
        infoLister = findViewById(R.id.infoLister);
        infoLayout = findViewById(R.id.InformbottomSheet);
        informationBottomSheet = BottomSheetBehavior.from(infoLayout);

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

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    informationBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    informationBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        informationBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int checker) {
                if (checker == BottomSheetBehavior.STATE_EXPANDED) {
                    toggleButton.setChecked(true);
                }
                if (checker == BottomSheetBehavior.STATE_COLLAPSED) {
                    toggleButton.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        list = new ArrayList<>();
        url = new ArrayList<>();
        adapter = new ArrayAdapter<>(Azhwarmanam.this,R.layout.pdfinfo,R.id.portal,list);

        list.add("108'il Azhwargalin Manam - Eng");
        url.add("https://tpvs.tce.edu/unrestricted/30-7-2020%20new/108il...%20Front%20page%20-Eng.pdf");
        list.add("108'il Azhwargalin Manam - Tamil");
        url.add("https://tpvs.tce.edu/unrestricted/30-7-2020%20new/108il...%20Front%20page-%20Tam.pdf");

        infoLister.setAdapter(adapter);
        infoLister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Azhwarmanam.this,PdfViewer.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",url.get(position));
                intent.putExtras(bundle);
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
                //Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, home.class);
                startActivity(nextActivity);
                finish();
                break;
            case R.id.aboutus:
                //Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, AboutUs.class);
                startActivity(nextActivity);
                break;
            case R.id.events:
                nextActivity = new Intent(this,Events.class);
                startActivity(nextActivity);
                break;
            case R.id.Gallery:
                //Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this,GalleryViewer.class);
                startActivity(nextActivity);
                break;
            case R.id.freedownload:
                //Toast.makeText(getApplicationContext(), "Free Downloads", Toast.LENGTH_SHORT).show();
                nextActivity = new Intent(this, FreeDownload.class);
                startActivity(nextActivity);
                break;
            case R.id.purchases:
                nextActivity = new Intent(this,Purchace.class);
                startActivity(nextActivity);
                break;
            case R.id.contact:
                //Toast.makeText(getApplicationContext(), "Contact", Toast.LENGTH_SHORT).show();
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
