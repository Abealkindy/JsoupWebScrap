package com.example.myapplication.adapters.animeadapters.recycleradapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.activities.animepage.watch_anime_mvp.WatchAnimeEpisodeActivity
import com.example.myapplication.databinding.ItemListSelectChapterDetailBinding
import com.example.myapplication.models.animemodels.AnimeDetailModel
import com.example.myapplication.models.mangamodels.DetailMangaModel

class RecyclerAllEpisodeDetailAdapter(private val context: Context, private val allChapterDatasArrayList: List<DetailMangaModel.DetailAllChapterDatas>, private val animeDetailModel: AnimeDetailModel) : RecyclerView.Adapter<RecyclerAllEpisodeDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListSelectChapterDetailBinding>(layoutInflater, R.layout.item_list_select_chapter_detail, parent, false)
        return ViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textViewChapterAllTitle.text = allChapterDatasArrayList[position].chapterTitle
        holder.itemListBinding.textViewChapterAllReleaseTime.visibility = View.GONE
        holder.itemListBinding.linearChapterDetail.setOnClickListener {
            val intent = Intent(context.applicationContext, WatchAnimeEpisodeActivity::class.java)
            intent.putExtra("animeEpisodeToWatch", allChapterDatasArrayList[position].chapterURL)
            intent.putExtra("animeEpisodeTitle", animeDetailModel.episodeTitle)
            intent.putExtra("animeEpisodeThumb", animeDetailModel.episodeThumb)
            intent.putExtra("animeEpisodeType", animeDetailModel.episodeType)
            intent.putExtra("animeEpisodeStatus", animeDetailModel.episodeStatus)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allChapterDatasArrayList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListSelectChapterDetailBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
