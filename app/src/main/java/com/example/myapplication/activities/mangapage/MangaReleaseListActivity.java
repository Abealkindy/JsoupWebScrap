package com.example.myapplication.activities.mangapage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.mangaadapters.viewpageradapter.ViewPagerMangaMenuTabAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMangaReleaseListBinding;
import com.google.android.material.tabs.TabLayout;

import static com.example.myapplication.MyApp.localAppDB;

public class MangaReleaseListActivity extends AppCompatActivity {
    ActivityMangaReleaseListBinding mangaReleaseListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mangaReleaseListBinding = ActivityMangaReleaseListBinding.inflate(getLayoutInflater());
        setContentView(mangaReleaseListBinding.getRoot());
        UISettings();
        addUIEvents();
    }

    private void addUIEvents() {
        String cameFrom = getIntent().getStringExtra("cameFrom");
        if (cameFrom != null && !cameFrom.isEmpty()) {
            if (cameFrom.equalsIgnoreCase("MangaReleases")) {
                mangaReleaseListBinding.viewPagerTabs.setCurrentItem(0);
                setTitle("New releases");
            } else if (cameFrom.equalsIgnoreCase("MangaDiscover")) {
                mangaReleaseListBinding.viewPagerTabs.setCurrentItem(1);
                setTitle("Discover");
            } else {
                mangaReleaseListBinding.viewPagerTabs.setCurrentItem(2);
                if (localAppDB.mangaBookmarkDAO().getMangaBookmarkData() != null && localAppDB.mangaBookmarkDAO().getMangaBookmarkData().size() > 1) {
                    setTitle(localAppDB.mangaBookmarkDAO().getMangaBookmarkData().size() + " titles in Favourite menu");
                } else if (localAppDB.mangaBookmarkDAO().getMangaBookmarkData() != null && localAppDB.mangaBookmarkDAO().getMangaBookmarkData().size() == 1) {
                    setTitle("Just a title in Favourite menu");
                } else {
                    setTitle("0 title in Favourite menu");
                }
            }
        }
        mangaReleaseListBinding.tabHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mangaReleaseListBinding.viewPagerTabs.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    setTitle("New releases");
                } else if (tab.getPosition() == 1) {
                    setTitle("Discover");
                } else if (tab.getPosition() == 2) {
                    if (localAppDB.mangaBookmarkDAO().getMangaBookmarkData() != null && localAppDB.mangaBookmarkDAO().getMangaBookmarkData().size() > 1) {
                        setTitle(localAppDB.mangaBookmarkDAO().getMangaBookmarkData().size() + " titles in Favourite menu");
                    } else if (localAppDB.mangaBookmarkDAO().getMangaBookmarkData() != null && localAppDB.mangaBookmarkDAO().getMangaBookmarkData().size() == 1) {
                        setTitle("Just a title in Favourite menu");
                    } else {
                        setTitle("0 title in Favourite menu");
                    }
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
        mangaReleaseListBinding.tabHome.addTab(mangaReleaseListBinding.tabHome.newTab().setIcon(getResources().getDrawable(R.drawable.ic_home_white_24dp)));
        mangaReleaseListBinding.tabHome.addTab(mangaReleaseListBinding.tabHome.newTab().setIcon(getResources().getDrawable(R.drawable.ic_view_list_white_24dp)));
        mangaReleaseListBinding.tabHome.addTab(mangaReleaseListBinding.tabHome.newTab().setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp)));
        mangaReleaseListBinding.tabHome.setTabIconTint(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
        mangaReleaseListBinding.viewPagerTabs.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mangaReleaseListBinding.tabHome));
        mangaReleaseListBinding.viewPagerTabs.setAdapter(new ViewPagerMangaMenuTabAdapter(getSupportFragmentManager()));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MangaReleaseListActivity.this, MainActivity.class));
        finish();

    }

}
