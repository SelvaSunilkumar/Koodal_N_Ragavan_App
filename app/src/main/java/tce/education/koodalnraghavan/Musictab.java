package tce.education.koodalnraghavan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Musictab extends Fragment {

    private LinearLayout folder1;
    private LinearLayout folder2;

    private Intent subFolderActivity;
    private Bundle bundle;

    public Musictab() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_musictab, container, false);

        folder1 = view.findViewById(R.id.folder1);
        folder2 = view.findViewById(R.id.folder2);

        folder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subFolderActivity = new Intent(view.getContext(),SubFolder.class);

                bundle = new Bundle();
                bundle.putInt("number",0);

                subFolderActivity.putExtras(bundle);
                startActivity(subFolderActivity);
            }
        });

        folder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subFolderActivity = new Intent(view.getContext(),SubFolder.class);

                bundle = new Bundle();
                bundle.putInt("number",1);

                subFolderActivity.putExtras(bundle);
                startActivity(subFolderActivity);
            }
        });

        return view;
    }


}
