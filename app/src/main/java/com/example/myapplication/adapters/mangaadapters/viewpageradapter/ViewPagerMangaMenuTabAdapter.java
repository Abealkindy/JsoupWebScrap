package com.example.myapplication.adapters.mangaadapters.viewpageradapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.fragments.manga_fragments.discover_manga_mvp.DiscoverMangaFragment;
import com.example.myapplication.fragments.manga_fragments.manga_bookmark_page.MangaBookmarkFragment;
import com.example.myapplication.fragments.manga_fragments.manga_history_page.MangaHistoryFragment;
import com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp.MangaNewReleaseFragment;

import java.util.Objects;

/*
 * Created by Rosinante24 on 2019-05-30.
 */
public class ViewPagerMangaMenuTabAdapter extends FragmentStatePagerAdapter {
    public ViewPagerMangaMenuTabAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MangaNewReleaseFragment();
        } else if (position == 1) {
            fragment = new DiscoverMangaFragment();
        } else if (position == 2) {
            fragment = new MangaBookmarkFragment();
        } else if (position == 3) {
            fragment = new MangaHistoryFragment();
        }
        return Objects.requireNonNull(fragment);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
