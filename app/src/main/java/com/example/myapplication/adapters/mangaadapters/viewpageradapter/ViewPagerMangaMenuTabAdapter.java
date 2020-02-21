package com.example.myapplication.adapters.mangaadapters.viewpageradapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.fragments.manga_fragments.discover_manga_mvp.DiscoverMangaFragment;
import com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp.MangaNewReleaseFragment;

/*
 * Created by Rosinante24 on 2019-05-30.
 */
public class ViewPagerMangaMenuTabAdapter extends FragmentStatePagerAdapter {
    public ViewPagerMangaMenuTabAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MangaNewReleaseFragment();
        } else if (position == 1) {
            return new DiscoverMangaFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
