package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PdfViewAuthentication extends AppCompatActivity {

    private PDFView pdfView;
    private Bundle bundle;
    private String PDF_URL;
    private String userName;
    private String password;
    private String credentials;
    private String authenticater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view_authentication);

        pdfView = findViewById(R.id.pdfView);

        bundle = getIntent().getExtras();
        PDF_URL = bundle.getString("url");

        new PDFLoaderAuth().execute(PDF_URL);
    }

    class PDFLoaderAuth extends AsyncTask<String,Void, InputStream>
    {
        InputStream inputStream;

        @Override
        protected InputStream doInBackground(String... strings) {

            userName = "tpvsuser1";
            password = "tpvs@userONE";
            credentials = userName + ":" + password;
            authenticater = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);

            try {
                URL linkUrl = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) linkUrl.openConnection();
                urlConnection.setRequestProperty("Authorization",authenticater);
                urlConnection.setRequestProperty("Accept","...");

                if (urlConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
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

            Toast.makeText(getApplicationContext(),"Please wait, It may take time to Load",Toast.LENGTH_LONG).show();
            pdfView.fromStream(inputStream).load();
        }
    }
}
