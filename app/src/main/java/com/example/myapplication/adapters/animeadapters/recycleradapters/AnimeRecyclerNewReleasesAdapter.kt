package com.example.myapplication.adapters.animeadapters.recycleradapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel
import com.example.myapplication.R
import com.example.myapplication.activities.animepage.watch_anime_mvp.WatchAnimeEpisodeActivity
import com.example.myapplication.databinding.ItemListAnimeBinding
import com.squareup.picasso.Picasso

class AnimeRecyclerNewReleasesAdapter(private val context: Context, private val animeNewReleaseResultModelList: List<AnimeNewReleaseResultModel>) : RecyclerView.Adapter<AnimeRecyclerNewReleasesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemListBinding = DataBindingUtil.inflate<ItemListAnimeBinding>(layoutInflater, R.layout.item_list_anime, parent, false)
        return ViewHolder(itemListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //        holder.itemListBinding.textTitle.setText(animeNewReleaseResultModelList.get(position).getAnimeEpisode() + " Episode " + animeNewReleaseResultModelList.get(position).getAnimeEpisodeNumber());
        holder.itemListBinding.textTitle.text = animeNewReleaseResultModelList[position].animeEpisode
        Picasso.get().load(animeNewReleaseResultModelList[position].episodeThumb).into(holder.itemListBinding.imageViewBackground)

        when {
            animeNewReleaseResultModelList[position].animeEpisodeType.equals(context.resources.getString(R.string.series_string), ignoreCase = true) -> {
                holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.blue_series_color))
                holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.series_string)
                holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
            }
            animeNewReleaseResultModelList[position].animeEpisodeType.equals(context.resources.getString(R.string.ova_string), ignoreCase = true) -> {
                holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.pink_series_color))
                holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.ova_string)
                holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
            }
            animeNewReleaseResultModelList[position].animeEpisodeType.equals(context.resources.getString(R.string.ona_string), ignoreCase = true) -> {
                holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.purple_series_color))
                holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.ona_string)
                holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
            }
            animeNewReleaseResultModelList[position].animeEpisodeType.equals(context.resources.getString(R.string.la_string), ignoreCase = true) -> {
                holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.red_series_color))
                holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.la_string)
                holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
            }
            animeNewReleaseResultModelList[position].animeEpisodeType.equals(context.resources.getString(R.string.movie_string), ignoreCase = true) -> {
                holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.green_series_color))
                holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.movie_string)
                holder.itemListBinding.cardEpisodeStatus.visibility = View.GONE
            }
            animeNewReleaseResultModelList[position].animeEpisodeType.equals(context.resources.getString(R.string.special_string), ignoreCase = true) -> {
                holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.resources.getColor(R.color.orange_series_color))
                holder.itemListBinding.textEpisodeType.text = context.resources.getString(R.string.special_string)
                holder.itemListBinding.cardEpisodeStatus.visibility = View.VISIBLE
            }

            //        holder.itemListBinding.cardEpisodeNumber.setCardBackgroundColor(context.getResources().getColor(R.color.white_with_opacity));
            //        holder.itemListBinding.textEpisodeNumber.setText("Episode " + animeNewReleaseResultModelList.get(position).getAnimeEpisodeNumber());
        }

        if (animeNewReleaseResultModelList[position].animeEpisodeStatus.equals(context.resources.getString(R.string.ongoing_text), ignoreCase = true)) {
            holder.itemListBinding.cardEpisodeStatus.setCardBackgroundColor(context.resources.getColor(R.color.ongoing_color))
            holder.itemListBinding.textEpisodeStatus.text = context.resources.getString(R.string.ongoing_text)
        } else if (animeNewReleaseResultModelList[position].animeEpisodeStatus.equals(context.resources.getString(R.string.completed_text), ignoreCase = true)) {
            holder.itemListBinding.cardEpisodeStatus.setCardBackgroundColor(context.resources.getColor(R.color.completed_color))
            holder.itemListBinding.textEpisodeStatus.text = context.resources.getString(R.string.completed_text)
        }

        //        holder.itemListBinding.cardEpisodeNumber.setCardBackgroundColor(context.getResources().getColor(R.color.white_with_opacity));
        //        holder.itemListBinding.textEpisodeNumber.setText("Episode " + animeNewReleaseResultModelList.get(position).getAnimeEpisodeNumber());
        holder.itemListBinding.relativeItem.setOnClickListener {
            val intentToVideoWatchActivity = Intent(context.applicationContext, WatchAnimeEpisodeActivity::class.java)
            intentToVideoWatchActivity.putExtra("animeEpisodeToWatch", animeNewReleaseResultModelList[position].episodeURL)
            intentToVideoWatchActivity.putExtra("animeEpisodeTitle", animeNewReleaseResultModelList[position].animeEpisode)
            intentToVideoWatchActivity.putExtra("animeEpisodeType", animeNewReleaseResultModelList[position].animeEpisodeType)
            intentToVideoWatchActivity.putExtra("animeEpisodeStatus", animeNewReleaseResultModelList[position].animeEpisodeStatus)
            intentToVideoWatchActivity.putExtra("animeEpisodeThumb", animeNewReleaseResultModelList[position].episodeThumb)
            context.startActivity(intentToVideoWatchActivity)
        }
    }

    override fun getItemCount(): Int {
        return animeNewReleaseResultModelList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListAnimeBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
