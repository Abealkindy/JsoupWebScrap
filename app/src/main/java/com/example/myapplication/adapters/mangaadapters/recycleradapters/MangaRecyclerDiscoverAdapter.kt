package com.example.myapplication.adapters.mangaadapters.recycleradapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity
import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity
import com.example.myapplication.databinding.ItemListMangaSearchResultBinding
import com.example.myapplication.models.mangamodels.DiscoverMangaModel
import com.squareup.picasso.Picasso

import org.jsoup.internal.StringUtil

class MangaRecyclerDiscoverAdapter(private val context: Context, private val animeDiscoverResultModelList: List<DiscoverMangaModel>) : RecyclerView.Adapter<MangaRecyclerDiscoverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val viewHolder: ViewHolder
        val itemListBinding = DataBindingUtil.inflate<ItemListMangaSearchResultBinding>(layoutInflater, R.layout.item_list_manga_search_result, parent, false)
        viewHolder = ViewHolder(itemListBinding)
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textTitleMangaResult.text = animeDiscoverResultModelList[position].mangaTitle
        if (StringUtil.isBlank(animeDiscoverResultModelList[position].mangaThumb)) {
            holder.itemListBinding.imageViewBackgroundMangaResult.setImageDrawable(context.resources.getDrawable(R.drawable.imageplaceholder))
            Log.e("pathNull", "null")
        } else {
            Picasso.get().load(animeDiscoverResultModelList[position].mangaThumb).placeholder(context.resources.getDrawable(R.drawable.imageplaceholder)).into(holder.itemListBinding.imageViewBackgroundMangaResult)
        }
        if (!animeDiscoverResultModelList[position].isMangaStatus) {
            holder.itemListBinding.textMangaStatus.text = context.resources.getString(R.string.ongoing_text)
            holder.itemListBinding.cardMangaStatus.setCardBackgroundColor(context.resources.getColor(R.color.orange_series_color))
        } else if (animeDiscoverResultModelList[position].isMangaStatus) {
            holder.itemListBinding.textMangaStatus.text = context.resources.getString(R.string.completed_text)
            holder.itemListBinding.cardMangaStatus.setCardBackgroundColor(context.resources.getColor(R.color.green_series_color))
        }
        when {
            animeDiscoverResultModelList[position].mangaType.equals(context.resources.getString(R.string.manga_string), ignoreCase = true) -> {
                holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.resources.getColor(R.color.manga_color))
                holder.itemListBinding.textMangaTypeResult.text = context.resources.getString(R.string.manga_string)
            }
            animeDiscoverResultModelList[position].mangaType.equals(context.resources.getString(R.string.manhwa_string), ignoreCase = true) -> {
                holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.resources.getColor(R.color.manhwa_color))
                holder.itemListBinding.textMangaTypeResult.text = context.resources.getString(R.string.manhwa_string)
            }
            animeDiscoverResultModelList[position].mangaType.equals(context.resources.getString(R.string.manhua_string), ignoreCase = true) -> {
                holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.resources.getColor(R.color.manhua_color))
                holder.itemListBinding.textMangaTypeResult.text = context.resources.getString(R.string.manhua_string)
            }
            animeDiscoverResultModelList[position].mangaType.equals(context.resources.getString(R.string.mangaoneshot_string), ignoreCase = true) -> {
                holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.resources.getColor(R.color.manga_color))
                holder.itemListBinding.textMangaTypeResult.text = context.resources.getString(R.string.mangaoneshot_string)
            }
            animeDiscoverResultModelList[position].mangaType.equals(context.resources.getString(R.string.oneshot_string), ignoreCase = true) -> {
                holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.resources.getColor(R.color.manga_color))
                holder.itemListBinding.textMangaTypeResult.text = context.resources.getString(R.string.oneshot_string)
            }
        }
        holder.itemListBinding.mangaRatingBar.numStars = 5
        val replaceComma = animeDiscoverResultModelList[position].mangaRating!!.replace(",", ".")
        if (animeDiscoverResultModelList[position].mangaRating.equals("N/A", ignoreCase = true) || animeDiscoverResultModelList[position].mangaRating.equals("?", ignoreCase = true) || animeDiscoverResultModelList[position].mangaRating.equals("-", ignoreCase = true)) {
            holder.itemListBinding.mangaRatingBar.rating = 0f
            holder.itemListBinding.mangaRatingNumber.text = animeDiscoverResultModelList[position].mangaRating
        } else if (java.lang.Float.parseFloat(replaceComma) <= 0) {
            holder.itemListBinding.mangaRatingBar.rating = 0f
            holder.itemListBinding.mangaRatingNumber.text = animeDiscoverResultModelList[position].mangaRating
        } else {
            holder.itemListBinding.mangaRatingBar.rating = java.lang.Float.parseFloat(replaceComma) / 2
            holder.itemListBinding.mangaRatingNumber.text = replaceComma
        }
        val replaceCh = animeDiscoverResultModelList[position].mangaLatestChapterText!!.replace("Ch.", "Chapter ")
        holder.itemListBinding.textLatestChapterRelease.text = replaceCh
        holder.itemListBinding.cardLatestMangaRelease.setOnClickListener {
            val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
            intent.putExtra("chapterURL", animeDiscoverResultModelList[position].mangaLatestChapter)
            intent.putExtra("appBarColorStatus", animeDiscoverResultModelList[position].mangaType)
            intent.putExtra("chapterTitle", animeDiscoverResultModelList[position].mangaLatestChapterText)
            context.startActivity(intent)
        }
        holder.itemListBinding.relativeItemMangaResult.setOnClickListener {
            val intent = Intent(context.applicationContext, MangaDetailActivity::class.java)
            intent.putExtra("detailURL", animeDiscoverResultModelList[position].mangaURL)
            intent.putExtra("detailType", animeDiscoverResultModelList[position].mangaType)
            intent.putExtra("detailTitle", animeDiscoverResultModelList[position].mangaTitle)
            intent.putExtra("detailRating", animeDiscoverResultModelList[position].mangaRating)
            intent.putExtra("detailStatus", animeDiscoverResultModelList[position].isMangaStatus)
            intent.putExtra("detailThumb", animeDiscoverResultModelList[position].mangaThumb)
            context.startActivity(intent)
        }
    }

    fun recyclerRefresh() {
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return animeDiscoverResultModelList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListMangaSearchResultBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
