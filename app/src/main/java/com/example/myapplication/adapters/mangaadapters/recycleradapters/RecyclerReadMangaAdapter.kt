package com.example.myapplication.adapters.mangaadapters.recycleradapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemListMangaContentBinding

import java.net.MalformedURLException
import java.net.URL

class RecyclerReadMangaAdapter(private val context: Context, private val imageContent: List<String>?) : RecyclerView.Adapter<RecyclerReadMangaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListMangaContentBinding>(layoutInflater, R.layout.item_list_manga_content, parent, false)
        return ViewHolder(itemListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Glide.with(context)
                    .asDrawable()
                    .load(URL(this.imageContent?.get(position)))
                    .apply(RequestOptions().timeout(30000))
                    .error(context.resources.getDrawable(R.drawable.error))
                    .placeholder(context.resources.getDrawable(R.drawable.imageplaceholder))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.itemListBinding.imageMangaContentItem)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return imageContent!!.size
    }

    inner class ViewHolder(val itemListBinding: ItemListMangaContentBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
