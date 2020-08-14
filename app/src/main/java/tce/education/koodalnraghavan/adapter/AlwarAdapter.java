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

import java.util.ArrayList;
import java.util.List;

import tce.education.koodalnraghavan.DataPriceLister;
import tce.education.koodalnraghavan.R;

public class AlwarAdapter extends ArrayAdapter<DataPriceLister> {

    private Context context;
    private ArrayList<DataPriceLister> list;
    private int resource;

    public AlwarAdapter(Context context, int resource, ArrayList<DataPriceLister> list)
    {
        super(context,resource,list);
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

            DataPriceLister lister = getItem(position);

            ImageView imageView = convertView.findViewById(R.id.imageIcon);
            TextView textView = convertView.findViewById(R.id.portal);

            textView.setText(lister.getName());

            if (lister.getFormat().equals("video"))
            {
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.videoplay));
            }
            if (lister.getFormat().equals("pdf"))
            {
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.file));
            }
            if (lister.getFormat().equals("audio")) {
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.mp3));
            }
        }
        return convertView;
    }
}
