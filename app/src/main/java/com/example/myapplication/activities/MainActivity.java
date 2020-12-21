package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.activities.mangapage.MangaReleaseListActivity;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.networks.InternetConnection;
import com.google.gson.Gson;
import com.zhkrb.cloudflare_scrape_webview.CfCallback;
import com.zhkrb.cloudflare_scrape_webview.Cloudflare;
import com.zhkrb.cloudflare_scrape_webview.util.ConvertUtil;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;

import static com.example.myapplication.MyApp.cookiesz;
import static com.example.myapplication.MyApp.ua;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
//        mainBinding.cardWatchAnime.setVisibility(View.GONE);
        mainBinding.cardWatchAnime.setOnClickListener(v -> {
            Toast.makeText(this, "Masih develop deui!", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(MainActivity.this, AnimeReleaseListActivity.class));
//            finish();
        });
        mainBinding.cardReadManga.setOnClickListener(v -> {
            if (InternetConnection.checkConnection(this)) {
                try {
                    Cloudflare cloudflare = new Cloudflare(this, "https://komikcast.com");
                    cloudflare.setUser_agent(ua);
                    cloudflare.setCfCallback(new CfCallback() {
                        @Override
                        public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                            cookiesz = ConvertUtil.List2Map(cookieList);
                            startActivity(new Intent(MainActivity.this, MangaReleaseListActivity.class));
                            finish();
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            Toast.makeText(MainActivity.this, "Lagi tutup dulu, nanti balik lagi yah!", Toast.LENGTH_LONG).show();
                        }
                    });
                    cloudflare.getCookies();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Lagi tutup dulu, nanti balik lagi yah!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Cek internetnya dulu boss, nanti balik lagi yah!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
