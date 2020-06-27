package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.koodalnraghavan.R;

public class WebPdf extends AppCompatActivity {

    private WebView webView;
    private String pdfUrl;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_pdf);

        pdfUrl = "https://tpvs.tce.edu/restricted/2.%20108il%20aazhwaargalin%20manam/audio/1.%20Thene%20Thiruvenkatam%20Part-1/10.%20Porulum%20Piravum.mp3";

        webView =findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHttpAuthUsernamePassword("https://tpvs.tce.edu/restricted/","myrealm","tpvsuser1","tpvs@userONE");
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Toast.makeText(getApplicationContext(),"Can't Download video",Toast.LENGTH_SHORT).show();
            }
        });

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed("tpvsuser1","tpvs@userONE");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webView.loadUrl(pdfUrl);
    }
}
