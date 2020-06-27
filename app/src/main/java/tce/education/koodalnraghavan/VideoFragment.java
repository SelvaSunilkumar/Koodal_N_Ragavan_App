package tce.education.koodalnraghavan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koodalnraghavan.R;

import java.util.ArrayList;

public class VideoFragment extends Fragment {

    private ListView listView;
    private TextView textView;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> list;
    private ArrayList<String> url;
    private AzhwarVideoDatabaseHelper databaseHelper;
    private Cursor data;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        listView = view.findViewById(R.id.listView);
        textView = view.findViewById(R.id.warner);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.musicinfo,R.id.portal,list);

        databaseHelper = new AzhwarVideoDatabaseHelper(view.getContext());
        data = databaseHelper.fetchVideoList();

        if(data.getCount() == 0)
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No purchases so far");
            Toast.makeText(view.getContext(),"No Purchases so far",Toast.LENGTH_SHORT).show();
        }
        else
        {
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
                    Intent nextActivity = new Intent(view.getContext(),WebVideo.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("list",list.get(position));
                    bundle.putString("url",url.get(position));
                    //bundle.putInt("flag",1);
                    bundle.putBoolean("flag",true);

                    nextActivity.putExtras(bundle);
                    startActivity(nextActivity);
                }
            });
        }
        return view;
    }
}
