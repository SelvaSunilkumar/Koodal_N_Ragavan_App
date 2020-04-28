package com.example.koodalnraghavan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private NetworkInfo networkInfo;
    private ConnectivityManager connectivityManager;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() == true)
        {

            Toast.makeText(getApplicationContext(),"Active Network Connection",Toast.LENGTH_SHORT).show();

            mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.backround_music);
            mediaPlayer.start();
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
}
