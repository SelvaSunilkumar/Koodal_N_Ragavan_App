package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.koodalnraghavan.R;

public class WebVideo extends AppCompatActivity {

    private WebView webView;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onPause() {
        super.onPause();
        webView.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video);

        bundle = getIntent().getExtras();

        String playingNow = bundle.getString("list");
        String url = bundle.getString("url");

        webView = findViewById(R.id.web);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setHttpAuthUsernamePassword("https://tpvs.tce.edu/","myrealm","tpvsuser1","tpvs@userONE");

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

        webView.loadUrl(url);
    }
}
