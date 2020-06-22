package com.example.koodalnraghavan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHOlder> {

    private Context context;
    private ArrayList<GalleryResource> resources;

    public GalleryAdapter(Context context, ArrayList<GalleryResource> resources)
    {
        this.context = context;
        this.resources = resources;
    }


    @NonNull
    @Override
    public GalleryViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        return new  GalleryViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHOlder holder, int position) {

        GalleryResource  galleryResource = resources.get(position);

        String imageUrl = galleryResource.getImageUrl();

        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public class GalleryViewHOlder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;

        public GalleryViewHOlder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
    }
}
