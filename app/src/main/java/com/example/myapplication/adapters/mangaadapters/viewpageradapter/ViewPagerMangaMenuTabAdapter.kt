package com.example.myapplication.adapters.mangaadapters.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.example.myapplication.fragments.manga_fragments.discover_manga_mvp.DiscoverMangaFragment
import com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp.MangaNewReleaseFragment

/*
 * Created by Rosinante24 on 2019-05-30.
 */
class ViewPagerMangaMenuTabAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        lateinit var fragment : Fragment
        if (position == 0) {
            fragment = MangaNewReleaseFragment()
        } else if (position == 1) {
            fragment =  DiscoverMangaFragment()
        }
        return fragment
    }

    override fun getCount(): Int {
        return 2
    }
}
