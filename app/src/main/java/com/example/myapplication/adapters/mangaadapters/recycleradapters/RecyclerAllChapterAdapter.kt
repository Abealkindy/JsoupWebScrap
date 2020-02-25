package com.example.myapplication.adapters.mangaadapters.recycleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.databinding.ItemListSelectChapterBinding
import com.example.myapplication.models.mangamodels.ReadMangaModel

class RecyclerAllChapterAdapter(private val context: Context, private val allChapterDatasArrayList: List<ReadMangaModel.AllChapterDatas>) : RecyclerView.Adapter<RecyclerAllChapterAdapter.ViewHolder>() {
    private var clickListener: ClickListener? = null

    init {
        if (context is ClickListener) {
            clickListener = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListSelectChapterBinding>(layoutInflater, R.layout.item_list_select_chapter, parent, false)
        return ViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textViewChapterAll.text = allChapterDatasArrayList[position].chapterTitle
        holder.itemListBinding.linearViewChapterAll.setOnClickListener { v -> clickListener!!.onItemClick(position, v) }
    }

    override fun getItemCount(): Int {
        return allChapterDatasArrayList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListSelectChapterBinding) : RecyclerView.ViewHolder(itemListBinding.root)

    interface ClickListener {
        fun onItemClick(position: Int, v: View)
    }
}
