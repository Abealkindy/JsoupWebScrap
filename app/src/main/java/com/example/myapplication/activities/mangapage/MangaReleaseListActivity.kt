package com.example.myapplication.activities.mangapage

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle

import com.example.myapplication.activities.MainActivity
import com.example.myapplication.adapters.mangaadapters.viewpageradapter.ViewPagerMangaMenuTabAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMangaReleaseListBinding
import com.google.android.material.tabs.TabLayout

class MangaReleaseListActivity : AppCompatActivity() {
    internal lateinit var mangaReleaseListBinding: ActivityMangaReleaseListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mangaReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_manga_release_list)
        UISettings()
        addUIEvents()
    }

    private fun addUIEvents() {
        mangaReleaseListBinding.tabHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mangaReleaseListBinding.viewPagerTabs.currentItem = tab.position
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
        mangaReleaseListBinding.tabHome.addTab(mangaReleaseListBinding.tabHome.newTab().setIcon(resources.getDrawable(R.drawable.ic_home_white_24dp)))
        mangaReleaseListBinding.tabHome.addTab(mangaReleaseListBinding.tabHome.newTab().setIcon(resources.getDrawable(R.drawable.ic_view_list_white_24dp)))
        mangaReleaseListBinding.tabHome.tabIconTint = ColorStateList.valueOf(resources.getColor(android.R.color.white))
        mangaReleaseListBinding.viewPagerTabs.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mangaReleaseListBinding.tabHome))
        mangaReleaseListBinding.viewPagerTabs.adapter = ViewPagerMangaMenuTabAdapter(supportFragmentManager)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MangaReleaseListActivity, MainActivity::class.java))
        finish()

    }

}
