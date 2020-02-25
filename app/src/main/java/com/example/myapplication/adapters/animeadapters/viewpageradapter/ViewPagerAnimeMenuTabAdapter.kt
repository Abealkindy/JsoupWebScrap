package com.example.myapplication.adapters.animeadapters.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp.AnimeNewReleaseFragment
import com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp.GenreAndSearchAnimeFragment

/*
 * Created by Rosinante24 on 2019-05-30.
 */
class ViewPagerAnimeMenuTabAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        lateinit var fragment : Fragment
        if (position == 0) {
            fragment = AnimeNewReleaseFragment()
        } else if (position == 1) {
            fragment = GenreAndSearchAnimeFragment()
        }
        return fragment
    }

    override fun getCount(): Int {
        return 2
    }
}
