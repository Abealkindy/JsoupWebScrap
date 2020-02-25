package com.example.myapplication.adapters.animeadapters.recycleradapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.databinding.ItemListSelectChapterBinding
import com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp.GenreAndSearchAnimeFragment
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel

class RecyclerAllGenreAdapter(private val context: GenreAndSearchAnimeFragment?, private val allGenreList: List<AnimeGenreAndSearchResultModel.AnimeGenreResult>) : RecyclerView.Adapter<RecyclerAllGenreAdapter.ViewHolder>() {
    private var clickListener: ClickGenreListener? = null

    init {
        if (context != null) {
            clickListener = context
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context!!.context)
        val itemListBinding = DataBindingUtil.inflate<ItemListSelectChapterBinding>(layoutInflater, R.layout.item_list_select_chapter, parent, false)
        return ViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textViewChapterAll.text = allGenreList[position].genreTitle
        Log.e("positionFromAdapter", "" + position)
        holder.itemListBinding.linearViewChapterAll.setOnClickListener { clickListener!!.onItemClickGenre(position) }
    }

    override fun getItemCount(): Int {
        return allGenreList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListSelectChapterBinding) : RecyclerView.ViewHolder(itemListBinding.root)

    interface ClickGenreListener {
        fun onItemClickGenre(position: Int)
    }
}
