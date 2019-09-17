package com.example.crawaling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.webview_layout );
        Intent intent = getIntent();
        url = intent.getStringExtra( "url" );

        webView = (WebView)findViewById( R.id.webView );
        webView.setWebViewClient( new WebViewClient() );
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled( true );
        webSettings.setSupportMultipleWindows( false );
        webSettings.setJavaScriptCanOpenWindowsAutomatically( false );
        webSettings.setLoadWithOverviewMode( true );
        webSettings.setUseWideViewPort( true );
        webSettings.setSupportZoom( false );
        webSettings.setBuiltInZoomControls( false );
        webSettings.setLayoutAlgorithm( WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
        webSettings.setCacheMode( WebSettings.LOAD_NO_CACHE );
        webSettings.setDomStorageEnabled( true );

        webView.loadUrl( url );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
