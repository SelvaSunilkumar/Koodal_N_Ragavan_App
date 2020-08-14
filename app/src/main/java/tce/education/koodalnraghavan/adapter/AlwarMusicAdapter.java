package tce.education.koodalnraghavan.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tce.education.koodalnraghavan.R;
import tce.education.koodalnraghavan.classes.AlwarMusicDetails;

public class AlwarMusicAdapter extends RecyclerView.Adapter<AlwarMusicAdapter.AlwarMusicViewHolder> {

    public Context context;
    public ArrayList<AlwarMusicDetails> details;
    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AlwarMusicAdapter(Context context,ArrayList<AlwarMusicDetails> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AlwarMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.videoinfo,parent,false);
        return new AlwarMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlwarMusicViewHolder holder, int position) {
        AlwarMusicDetails musicDetails = details.get(position);

        String fileName = musicDetails.getAudioName();
        String fileUrl = musicDetails.getAudioUrl();
        String fileAmount = musicDetails.getPrice();
        String fileFormat = musicDetails.getFormat();

        holder.songName.setText(fileName);

        if (fileFormat.equals("pdf")) {
            //holder.icon.setBackground(context.getResources().getDrawable(R.drawable.file));
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.file));
        }
        else if (fileFormat.equals("audio")) {
            //holder.icon.setBackground(context.getResources().getDrawable(R.drawable.videoplay));
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.videoplay));
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AlwarMusicViewHolder extends RecyclerView.ViewHolder {

        public TextView songName;
        public ImageView icon;

        public AlwarMusicViewHolder(View view) {
            super(view);

            songName = view.findViewById(R.id.portal);
            icon = view.findViewById(R.id.imageIcon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
