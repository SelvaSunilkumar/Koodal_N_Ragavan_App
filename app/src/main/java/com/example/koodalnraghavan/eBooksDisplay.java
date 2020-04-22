package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class eBooksDisplay extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private PdfLoader pdfLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_books_display);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.loader);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("ebooks");
        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,R.layout.pdfinfo,R.id.portal,list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pdfLoader = new PdfLoader();

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
                        Toast.makeText(getApplicationContext(),url.get(position),Toast.LENGTH_SHORT).show();
                        Intent pdfLayout = new Intent(eBooksDisplay.this,PdfViewer.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("url",url.get(position));
                        pdfLayout.putExtras(bundle);
                        startActivity(pdfLayout);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
