package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityMangaReleaseListBinding;
import com.example.myapplication.databinding.ActivityMangaReleaseListBindingImpl;

public class MangaReleaseListActivity extends AppCompatActivity {
    ActivityMangaReleaseListBinding mangaReleaseListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mangaReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_manga_release_list);
    }
}
