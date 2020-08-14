package tce.education.koodalnraghavan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tce.education.koodalnraghavan.R;
import tce.education.koodalnraghavan.classes.SampleEBooksDetails;

public class SampleBooksAdapter extends RecyclerView.Adapter<SampleBooksAdapter.SampleBooksViewHolder> {

    private Context context;
    private ArrayList<SampleEBooksDetails> details;
    private OnItemClickListener listener;

    public interface  OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SampleBooksAdapter(Context context,ArrayList<SampleEBooksDetails> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public SampleBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdfinfo,parent,false);
        return new SampleBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleBooksViewHolder holder, int position) {

        SampleEBooksDetails sampleEBooksDetails = details.get(position);

        String name = sampleEBooksDetails.getName();
        String url = sampleEBooksDetails.getUrl();

        holder.bookName.setText(name);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class SampleBooksViewHolder extends RecyclerView.ViewHolder {

        public TextView bookName;

        public SampleBooksViewHolder(View view) {
            super(view);

            bookName = view.findViewById(R.id.portal);

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
