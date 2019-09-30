package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityMangaReleaseListBinding;

import java.util.ArrayList;
import java.util.List;

public class MangaReleaseListActivity extends AppCompatActivity {
    ActivityMangaReleaseListBinding mangaReleaseListBinding;

    private int pageCount = 1;
    private List<MangaNewReleaseResultModel> mangaNewReleaseResultModels = new ArrayList<>();
    private RecyclerNewReleasesAdapter recyclerNewReleasesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mangaReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_manga_release_list);
    }
}
