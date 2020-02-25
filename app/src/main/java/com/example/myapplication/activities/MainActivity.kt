package com.example.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.example.myapplication.R
import com.example.myapplication.activities.animepage.AnimeReleaseListActivity
import com.example.myapplication.activities.mangapage.MangaReleaseListActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson

import java.math.BigInteger
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mainBinding.cardWatchAnime.setOnClickListener {
            startActivity(Intent(this@MainActivity, AnimeReleaseListActivity::class.java))
            finish()
        }
        mainBinding.cardReadManga.setOnClickListener {
            startActivity(Intent(this@MainActivity, MangaReleaseListActivity::class.java))
            finish()
        }
    }
}
