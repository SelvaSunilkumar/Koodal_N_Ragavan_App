package com.example.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PdfViewer extends AppCompatActivity {

    private WebView PDFViewer;
    private ProgressBar progressBar;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        PDFViewer = findViewById(R.id.viewPDF);
        progressBar = findViewById(R.id.progress);
        bundle = getIntent().getExtras();

        String url = bundle.getString("url");
        PDFViewer.getSettings().setJavaScriptEnabled(true);
        PDFViewer.getSettings().setBuiltInZoomControls(true);
        PDFViewer.getSettings().setDisplayZoomControls(true);
        PDFViewer.getSettings().setLoadWithOverviewMode(true);
        PDFViewer.getSettings().setUseWideViewPort(true);

        PDFViewer.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView webView,String url)
            {
                webView.loadUrl("javascript:(function() { document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressBar.setVisibility(View.GONE);
            }
        });
        PDFViewer.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);

    }
}
