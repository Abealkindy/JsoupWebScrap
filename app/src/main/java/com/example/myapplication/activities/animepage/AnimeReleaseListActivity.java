package com.example.myapplication.activities.animepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.example.myapplication.adapters.animeadapters.viewpageradapter.ViewPagerAnimeMenuTabAdapter;
import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.databinding.ActivityAnimeReleaseListBinding;
import com.google.android.material.tabs.TabLayout;

public class AnimeReleaseListActivity extends AppCompatActivity {
    ActivityAnimeReleaseListBinding animeReleaseListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeReleaseListBinding = ActivityAnimeReleaseListBinding.inflate(getLayoutInflater());
        setContentView(animeReleaseListBinding.getRoot());
        UISettings();
        addUIEvents();
    }

    private void addUIEvents() {
        animeReleaseListBinding.tabHomeAnime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animeReleaseListBinding.viewPagerTabsAnime.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    setTitle("New releases");
                } else if (tab.getPosition() == 1) {
                    setTitle("Discover");
                } else if (tab.getPosition() == 2) {
                    setTitle("Favourites");
                } else if (tab.getPosition() == 3) {
                    setTitle("History");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void UISettings() {
        setTitle("New releases");
        animeReleaseListBinding.tabHomeAnime.addTab(animeReleaseListBinding.tabHomeAnime.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_white_24dp, getTheme())));
        animeReleaseListBinding.tabHomeAnime.addTab(animeReleaseListBinding.tabHomeAnime.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_view_list_white_24dp, getTheme())));
        animeReleaseListBinding.tabHomeAnime.addTab(animeReleaseListBinding.tabHomeAnime.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_black_24dp, getTheme())));
        animeReleaseListBinding.tabHomeAnime.addTab(animeReleaseListBinding.tabHomeAnime.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_history_24, getTheme())));
        animeReleaseListBinding.tabHomeAnime.setTabIconTint(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
        animeReleaseListBinding.viewPagerTabsAnime.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(animeReleaseListBinding.tabHomeAnime));
        animeReleaseListBinding.viewPagerTabsAnime.setAdapter(new ViewPagerAnimeMenuTabAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AnimeReleaseListActivity.this, MainActivity.class));
        finish();

    }

}
