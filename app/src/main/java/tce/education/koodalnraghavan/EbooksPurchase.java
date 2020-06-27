package tce.education.koodalnraghavan;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koodalnraghavan.R;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class EbooksPurchase extends Fragment {

    private ListView listView;
    private TextView textView;
    private WebView webView;

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
        webView = view.findViewById(R.id.web);

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

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setHttpAuthUsernamePassword("https://tpvs.tce.edu/restricted/","realm","tpvsuser1","tpvs@userONE");
                    webView.loadUrl(url.get(position));

                    webView.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onDownloadStart(String urle, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                            Uri uri_book = Uri.parse(url.get(position));

                            DownloadManager downloadManager; //(DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(uri_book);

                            Context context = view.getContext();
                            String filename = list.get(position);
                            String fileExtention = ".pdf";

                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,filename + fileExtention);

                            Toast.makeText(view.getContext(),"Downloading : " + filename,Toast.LENGTH_SHORT).show();

                            downloadManager.enqueue(request);
                        }
                    });
                    //Toast.makeText(view.getContext(),list.get(position) + url.get(position),Toast.LENGTH_SHORT).show();
                    /*Intent nextActivity = new Intent(view.getContext(),PdfViewer.class);

                    Bundle bundle;
                    bundle = new Bundle();
                    bundle.putString("url",url.get(position));
                    Toast.makeText(view.getContext(),"Please wait, Loading Book...",Toast.LENGTH_LONG).show();
                    nextActivity.putExtras(bundle);
                    startActivity(nextActivity);*/
                }
            });
        }
        return view;
    }
}
