package com.example.koodalnraghavan;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.List;

public class Expander extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<DataPriceLister>> listDataChild;

    public Expander(Context context, List<String> listDataHeader, HashMap<String, List<DataPriceLister>> listDataChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group,null);

            TextView lblHeader = convertView.findViewById(R.id.lblListHeader);
            lblHeader.setTypeface(null, Typeface.BOLD);
            lblHeader.setText(headerTitle);
        }
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //String childText = (String) getChild(groupPosition,childPosition);

        DataPriceLister lister = (DataPriceLister) getChild(groupPosition,childPosition);

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items,null);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);

        if (lister.getFormat().equals("pdf"))
        {
            ImageView imageView = convertView.findViewById(R.id.imager);
            //imageView.setBackground(convertView.getResources().getDrawable(R.drawable.file));
            imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.file));
        }
        else if (lister.getFormat().equals("video"))
        {
            ImageView imageView = convertView.findViewById(R.id.imager);
            imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.videoplay));
        }
        else
        {
            ImageView imageView = convertView.findViewById(R.id.imager);
            imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.mp3));
        }
        txtListChild.setText(lister.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
