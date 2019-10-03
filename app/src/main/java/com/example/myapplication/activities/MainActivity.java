package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.activities.animepage.AnimeReleaseListActivity;
import com.example.myapplication.activities.mangapage.MangaReleaseListActivity;
import com.example.myapplication.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.cardWatchAnime.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AnimeReleaseListActivity.class));
            finish();
        });
        mainBinding.cardReadManga.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MangaReleaseListActivity.class));
            finish();
        });
    }


}
