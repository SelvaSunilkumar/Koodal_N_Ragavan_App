package com.example.koodalnraghavan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView mainImagae;

    private MediaPlayer mediaPlayer;

    private NetworkInfo networkInfo;
    private ConnectivityManager connectivityManager;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            try {
                // --------- try catch 1 ----------
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            catch (Exception a)
            {
                Toast.makeText(getApplicationContext(),"(Catch 1) Exception : " + a,Toast.LENGTH_LONG).show();
            }

            if(networkInfo != null && networkInfo.isConnected())
            {

                try {
                    //--------- try catch - 2 ----------
                    mainImagae = findViewById(R.id.imageAtMain);
                }
                catch (Exception b)
                {
                    Toast.makeText(getApplicationContext(),"(catch 2) Exception : " + b,Toast.LENGTH_LONG).show();
                }

                try {
                    //----- try catch - 3 -------
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                    mainImagae.startAnimation(animation);
                }
                catch (Exception c)
                {
                    Toast.makeText(getApplicationContext(),"(catch 3) Exception : " + c,Toast.LENGTH_LONG).show();
                }

                Toast.makeText(getApplicationContext(),"Active Network Connection",Toast.LENGTH_SHORT).show();

                try {
                    //----- try catch 4 -----
                    mediaPlayer = new MediaPlayer();
                }
                catch (Exception d)
                {
                    Toast.makeText(getApplicationContext(),"(catch 4) Exception : " + d,Toast.LENGTH_LONG).show();
                }


                try {

                    try {
                        //----- try catch 5 -----
                        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.backround_music);
                    }
                    catch (Exception f)
                    {
                        Toast.makeText(getApplicationContext(),"(Catch 5) Exception : " + f,Toast.LENGTH_LONG).show();
                    }

                    try {
                        //---------- try catch 6 ----------------
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);;
                    }
                    catch (Exception g)
                    {
                        Toast.makeText(getApplicationContext(),"(Catch 6) Exception : " + g,Toast.LENGTH_LONG).show();
                    }
                    //mediaPlayer.prepare();
                    try {
                        //-------- try catch 7------------
                        mediaPlayer.start();
                    }
                    catch(Exception h)
                    {
                        Toast.makeText(getApplicationContext(),"(Catch 7) Exception : " + h,Toast.LENGTH_LONG).show();
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(getApplicationContext(),"Song Completed",Toast.LENGTH_SHORT).show();
                            Intent goHome = new Intent(MainActivity.this,home.class);
                            startActivity(goHome);
                            finish();
                        }
                    });

                }
                catch (Exception e)
                {
                    //------- try catch 8 -------------
                    Toast.makeText(getApplicationContext(),"(catch 8)Exception : " + e,Toast.LENGTH_LONG).show();
                    System.out.print("\n\n\n\n");
                    e.printStackTrace();
                    //System.out.print("Exception : " + e);
                    Intent goHome = new Intent(MainActivity.this,home.class);
                    startActivity(goHome);
                    finish();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Not Network Connection",Toast.LENGTH_SHORT).show();

                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Press 'RESTART' after switching on Internet");
                dialog.setTitle("No Internet");
                dialog.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent repeatActivity = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(repeatActivity);
                        finish();

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog = dialog.create();
                alertDialog.show();
            }

        }

        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"(whole)Excpetion : " + e,Toast.LENGTH_LONG).show();
        }

    }
}
