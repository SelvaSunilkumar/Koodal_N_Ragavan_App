package com.example.koodalnraghavan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryViewer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private NetworkInfo networkInfo;
    private ConnectivityManager connectivityManager;

    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    @Override
    protected void onStart() {
        super.onStart();

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            FirebaseRecyclerAdapter<Model,ViewHolder> adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                    Model.class,
                    R.layout.row,
                    ViewHolder.class,
                    reference
            ) {
                @Override
                protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                    viewHolder.setDetails(getApplicationContext(),model.getName(),model.getUrl());
                }
            };
            recyclerView.setAdapter(adapter);
        }
        else
        {
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle("No Internet");
            dialog.setMessage("Press 'RESTART' after switching on Internet");
            dialog.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

            alertDialog = dialog.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_viewer);

        recyclerView = findViewById(R.id.imageLister);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Gallery");
    }
}
