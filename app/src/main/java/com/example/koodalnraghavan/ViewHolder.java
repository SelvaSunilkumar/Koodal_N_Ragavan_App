package com.example.koodalnraghavan;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View view;

    public ViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }

    public void setDetails(Context context,String name,String url)
    {
        ImageView imageView = view.findViewById(R.id.image);

        Picasso.get().load(url).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),"Clicked : " + name,Toast.LENGTH_SHORT).show();

                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.image_dialog_viewer);

                ImageView imager = dialog.findViewById(R.id.dialogImage);
                
                Picasso.get().load(url).into(imager);
                dialog.show();
            }
        });
    }
}
