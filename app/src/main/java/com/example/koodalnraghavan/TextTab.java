package com.example.koodalnraghavan;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class TextTab extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    public PdfLoader pdfLoader;

    public TextTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_tab, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Freetexts");

        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.textinfo,R.id.portal,list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    pdfLoader = ds.getValue(PdfLoader.class);
                    list.add(String.valueOf(pdfLoader.getPortal()));
                    url.add(String.valueOf(pdfLoader.getUrl()));
                }
                progressBar.setVisibility(View.GONE);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent nextActivity = new Intent(view.getContext(),PdfViewer.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("url",url.get(position));

                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);

                        /*Toast.makeText(getContext(),"Downloading : " + list.get(position),Toast.LENGTH_SHORT).show();

                        Uri uri = Uri.parse(url.get(position));

                        DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);

                        Context context = view.getContext();
                        String filename = list.get(position);
                        String fileExtension = ".pdf";

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtension);

                        downloadManager.enqueue(request);*/

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
