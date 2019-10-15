package com.example.myapplication.activities.animepage;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityAnimeDetailBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.example.myapplication.R;

public class AnimeDetailActivity extends AppCompatActivity {

    ActivityAnimeDetailBinding animeDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime_detail);
        setSupportActionBar(animeDetailBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
