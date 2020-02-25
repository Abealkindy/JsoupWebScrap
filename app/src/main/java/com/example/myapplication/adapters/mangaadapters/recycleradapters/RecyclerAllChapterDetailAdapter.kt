package com.example.myapplication.adapters.mangaadapters.recycleradapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity
import com.example.myapplication.databinding.ItemListSelectChapterDetailBinding
import com.example.myapplication.models.mangamodels.DetailMangaModel

class RecyclerAllChapterDetailAdapter(private val context: Context, private val allChapterDatasArrayList: List<DetailMangaModel.DetailAllChapterDatas>, private val mangaType: String?) : RecyclerView.Adapter<RecyclerAllChapterDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListSelectChapterDetailBinding>(layoutInflater, R.layout.item_list_select_chapter_detail, parent, false)
        return ViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textViewChapterAllTitle.text = allChapterDatasArrayList[position].chapterTitle
        holder.itemListBinding.textViewChapterAllReleaseTime.text = allChapterDatasArrayList[position].chapterReleaseTime
        holder.itemListBinding.linearChapterDetail.setOnClickListener {
            val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
            intent.putExtra("chapterURL", allChapterDatasArrayList[position].chapterURL)
            intent.putExtra("appBarColorStatus", mangaType)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allChapterDatasArrayList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListSelectChapterDetailBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
