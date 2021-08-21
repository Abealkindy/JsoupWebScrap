package com.example.myapplication.activities.mangapage.manga_home_mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityMangaHomeBinding;

public class MangaHomeActivity extends AppCompatActivity {
    private ActivityMangaHomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMangaHomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}