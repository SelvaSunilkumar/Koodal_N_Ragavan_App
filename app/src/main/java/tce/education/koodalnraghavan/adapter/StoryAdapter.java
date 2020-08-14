package tce.education.koodalnraghavan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tce.education.koodalnraghavan.R;
import tce.education.koodalnraghavan.classes.storyDataLoader;

public class StoryAdapter extends ArrayAdapter<storyDataLoader> {

    private Context context;
    private ArrayList<storyDataLoader> list;
    private int resource;


    public StoryAdapter(@NonNull Context context, int resource, @NonNull List<storyDataLoader> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.videoinfo,null,true);

            storyDataLoader loader = getItem(position);

            ImageView imageView = convertView.findViewById(R.id.imageIcon);
            TextView textView = convertView.findViewById(R.id.portal);

            textView.setText(loader.getName());
            Picasso.get().load(R.drawable.loader).into(imageView);
            Picasso.get().load(loader.getImage()).into(imageView);
        }
        return convertView;
    }
}
