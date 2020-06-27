package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.koodalnraghavan.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PdfViewer extends AppCompatActivity {

    private String pdfUrl;

    private ProgressBar progressBar;
    private PDFView pdfView;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfView = findViewById(R.id.pdfViewer);
        progressBar = findViewById(R.id.progress);

        //get pdf url from the other activity
        bundle = getIntent().getExtras();
        pdfUrl = bundle.getString("url");

        progressBar.setVisibility(View.VISIBLE);
        new PDFLoader().execute(pdfUrl);

        //-------------------------------------------------------------------------------------------

    }

    class PDFLoader extends AsyncTask<String,Void,InputStream>
    {
        InputStream inputStream;

        @Override
        protected InputStream doInBackground(String... strings) {

            try {
                URL linkUrl = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) linkUrl.openConnection();

                if(urlConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    //pdfView.fromStream(inputStream).load();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
            if(pdfView.isShown())
            {
                progressBar.setVisibility(View.GONE);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
