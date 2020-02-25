package com.example.myapplication.activities.animepage

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle

import com.example.myapplication.adapters.animeadapters.viewpageradapter.ViewPagerAnimeMenuTabAdapter
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.databinding.ActivityAnimeReleaseListBinding
import com.google.android.material.tabs.TabLayout

class AnimeReleaseListActivity : AppCompatActivity() {
    internal lateinit var animeReleaseListBinding: ActivityAnimeReleaseListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animeReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime_release_list)
        UISettings()
        addUIEvents()
    }

    private fun addUIEvents() {
        animeReleaseListBinding.tabHomeAnime.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                animeReleaseListBinding.viewPagerTabsAnime.currentItem = tab.position
                if (tab.position == 0) {
                    title = "New releases"
                } else if (tab.position == 1) {
                    title = "Discover"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun UISettings() {
        title = "New releases"
        animeReleaseListBinding.tabHomeAnime.addTab(animeReleaseListBinding.tabHomeAnime.newTab().setIcon(resources.getDrawable(R.drawable.ic_home_white_24dp)))
        animeReleaseListBinding.tabHomeAnime.addTab(animeReleaseListBinding.tabHomeAnime.newTab().setIcon(resources.getDrawable(R.drawable.ic_view_list_white_24dp)))
        animeReleaseListBinding.tabHomeAnime.tabIconTint = ColorStateList.valueOf(resources.getColor(android.R.color.white))
        animeReleaseListBinding.viewPagerTabsAnime.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(animeReleaseListBinding.tabHomeAnime))
        animeReleaseListBinding.viewPagerTabsAnime.adapter = ViewPagerAnimeMenuTabAdapter(supportFragmentManager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AnimeReleaseListActivity, MainActivity::class.java))
        finish()

    }

}
