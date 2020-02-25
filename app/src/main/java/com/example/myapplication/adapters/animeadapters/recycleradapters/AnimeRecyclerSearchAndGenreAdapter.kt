package com.example.myapplication.adapters.animeadapters.recycleradapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.activities.animepage.anime_detail_mvp.AnimeDetailActivity
import com.example.myapplication.databinding.ItemListAnimeBinding
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel
import com.squareup.picasso.Picasso

class AnimeRecyclerSearchAndGenreAdapter(private val context: Context, private val searchResultList: List<AnimeGenreAndSearchResultModel.AnimeSearchResult>) : RecyclerView.Adapter<AnimeRecyclerSearchAndGenreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListAnimeBinding>(layoutInflater, R.layout.item_list_anime, parent, false)
        return ViewHolder(itemListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textTitle.text = searchResultList[position].animeTitle
        Picasso.get().load(searchResultList[position].animeThumb).into(holder.itemListBinding.imageViewBackground)
        if (searchResultList[position].animeType.equals(context.resources.getString(R.string.series_string), ignoreCase = true) || searchResultList[position].animeType!!.contains(context.resources.getString(R.string.series_string))) {
            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.blue_series_color))
            holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.series_string)
            holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
        } else if (searchResultList[position].animeType.equals(context.resources.getString(R.string.ova_string), ignoreCase = true) || searchResultList[position].animeType!!.contains(context.resources.getString(R.string.ova_string))) {
            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.pink_series_color))
            holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.ova_string)
            holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
        } else if (searchResultList[position].animeType.equals(context.resources.getString(R.string.ona_string), ignoreCase = true) || searchResultList[position].animeType!!.contains(context.resources.getString(R.string.ona_string))) {
            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.purple_series_color))
            holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.ona_string)
            holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
        } else if (searchResultList[position].animeType.equals(context.resources.getString(R.string.la_string), ignoreCase = true) || searchResultList[position].animeType!!.contains(context.resources.getString(R.string.la_string))) {
            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.red_series_color))
            holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.la_string)
            holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
        } else if (searchResultList[position].animeType.equals(context.resources.getString(R.string.movie_string), ignoreCase = true) || searchResultList[position].animeType!!.contains(context.resources.getString(R.string.movie_string_lower))) {
            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.green_series_color))
            holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.movie_string)
            holder.itemListBinding.cardEpisodeStatus.visibility = View.GONE
        } else if (searchResultList[position].animeType.equals(context.resources.getString(R.string.special_string), ignoreCase = true) || searchResultList[position].animeType!!.contains(context.resources.getString(R.string.special_string_lower))) {
            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.orange_series_color))
            holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.special_string)
            holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
        }

        if (searchResultList[position].animeStatus.equals(context.resources.getString(R.string.ongoing_text), ignoreCase = true)) {
            holder.itemListBinding.cardEpisodeStatus.setCardBackgroundColor(context.resources.getColor(R.color.ongoing_color))
            holder.itemListBinding.textEpisodeStatus.text = context.resources.getString(R.string.ongoing_text)
        } else if (searchResultList[position].animeStatus.equals(context.resources.getString(R.string.completed_text), ignoreCase = true)) {
            holder.itemListBinding.cardEpisodeStatus.setCardBackgroundColor(context.resources.getColor(R.color.completed_color))
            holder.itemListBinding.textEpisodeStatus.text = context.resources.getString(R.string.completed_text)
        }

        holder.itemListBinding.relativeItem.setOnClickListener {
            val intentToVideoWatchActivity = Intent(context.applicationContext, AnimeDetailActivity::class.java)
            intentToVideoWatchActivity.putExtra("animeDetailURL", searchResultList[position].animeDetailURL)
            intentToVideoWatchActivity.putExtra("animeDetailTitle", searchResultList[position].animeTitle)
            intentToVideoWatchActivity.putExtra("animeDetailType", searchResultList[position].animeType)
            intentToVideoWatchActivity.putExtra("animeDetailStatus", searchResultList[position].animeStatus)
            intentToVideoWatchActivity.putExtra("animeDetailThumb", searchResultList[position].animeThumb)
            context.startActivity(intentToVideoWatchActivity)
        }
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    fun recyclerRefresh() {
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val itemListBinding: ItemListAnimeBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
