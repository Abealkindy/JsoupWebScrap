package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.fragments.AnimeNewReleaseFragment;
import com.example.myapplication.fragments.DiscoverMangaFragment;
import com.example.myapplication.fragments.GenreAndSearchAnimeFragment;
import com.example.myapplication.fragments.MangaNewReleaseFragment;

/*
 * Created by Rosinante24 on 2019-05-30.
 */
public class ViewPagerAnimeMenuTabAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAnimeMenuTabAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AnimeNewReleaseFragment();
        } else if (position == 1) {
            return new GenreAndSearchAnimeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
