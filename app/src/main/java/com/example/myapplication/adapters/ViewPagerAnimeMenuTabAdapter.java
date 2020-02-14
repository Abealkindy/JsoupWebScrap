package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp.AnimeNewReleaseFragment;
import com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp.GenreAndSearchAnimeFragment;

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
