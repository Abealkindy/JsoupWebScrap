package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.databinding.ItemListGenreBinding
import com.example.myapplication.models.mangamodels.DetailMangaModel

class RecyclerGenreAdapter(private val context: Context, private val detailMangaGenresList: List<DetailMangaModel.DetailMangaGenres>) : RecyclerView.Adapter<RecyclerGenreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListGenreBinding>(layoutInflater, R.layout.item_list_genre, parent, false)
        return ViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textMangaGenre.text = detailMangaGenresList[position].genreTitle
    }

    override fun getItemCount(): Int {
        return detailMangaGenresList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListGenreBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
