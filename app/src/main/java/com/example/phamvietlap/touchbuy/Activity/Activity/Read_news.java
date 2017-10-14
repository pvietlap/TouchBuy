package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.phamvietlap.touchbuy.R;

public class Read_news extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        Anhxa();
        toolbar();
        Intent intent=getIntent();
        String Link=intent.getStringExtra("link");
        webView.loadUrl(Link);
        webView.setWebViewClient(new WebViewClient());
    }
    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.nav_toolbar_detail);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent   intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("Page",4);

                finish();
                startActivity(intent);
            }
        });
    }
    private void Anhxa(){
        webView=(WebView)findViewById(R.id.wvReadNews);
    }
}
