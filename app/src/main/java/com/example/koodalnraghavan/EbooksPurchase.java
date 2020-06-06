package com.example.koodalnraghavan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class EbooksPurchase extends Fragment {

    private ListView listView;
    private TextView textView;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;
    private Cursor data;
    private eBooksDatabaseHelper databaseHelper;

    public EbooksPurchase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ebooks_purchase, container, false);

        listView = view.findViewById(R.id.listView);
        textView = view.findViewById(R.id.warner);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.pdfinfo,R.id.portal,list);


        databaseHelper = new eBooksDatabaseHelper(view.getContext());
        data = databaseHelper.fetchBooks();

        if(data.getCount() == 0)
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Purchases so far");
            Toast.makeText(view.getContext(),"No purchases are made yet",Toast.LENGTH_SHORT).show();
        }
        else {
            textView.setVisibility(View.GONE);
            while (data.moveToNext())
            {
                list.add(String.valueOf(data.getString(0)));
                url.add(String.valueOf(data.getString(1)));
            }
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(view.getContext(),list.get(position) + url.get(position),Toast.LENGTH_SHORT).show();
                    Intent nextActivity = new Intent(view.getContext(),PdfViewer.class);

                    Bundle bundle;
                    bundle = new Bundle();
                    bundle.putString("url",url.get(position));
                    Toast.makeText(view.getContext(),"Please wait, Loading Book...",Toast.LENGTH_LONG).show();
                    nextActivity.putExtras(bundle);
                    startActivity(nextActivity);
                }
            });
        }
        return view;
    }
}
