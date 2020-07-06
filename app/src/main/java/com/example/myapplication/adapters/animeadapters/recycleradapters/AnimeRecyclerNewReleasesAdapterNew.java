package com.example.myapplication.adapters.animeadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.animepage.watch_anime_mvp.WatchAnimeEpisodeActivity;
import com.example.myapplication.databinding.ItemListAnimeBinding;
import com.example.myapplication.databinding.ItemListAnimeNewBinding;
import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeRecyclerNewReleasesAdapterNew extends RecyclerView.Adapter<AnimeRecyclerNewReleasesAdapterNew.ViewHolder> {
    private Context context;
    private List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList;

    public AnimeRecyclerNewReleasesAdapterNew(Context context, List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList) {
        this.context = context;
        this.animeNewReleaseResultModelList = animeNewReleaseResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListAnimeNewBinding itemListBinding = ItemListAnimeNewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String episode = animeNewReleaseResultModelList.get(position).getAnimeEpisodeNumber();
        if (episode.startsWith("0")) {
            episode = episode.substring(1);
        }
        holder.itemListBinding.textTitle.setText(animeNewReleaseResultModelList.get(position).getAnimeEpisode() + " Episode " + episode);
//        holder.itemListBinding.textTitle.setText(animeNewReleaseResultModelList.get(position).getAnimeEpisode());
        Picasso.get().load(animeNewReleaseResultModelList.get(position).getEpisodeThumb()).into(holder.itemListBinding.imageViewBackground);

        if (animeNewReleaseResultModelList.get(position).getAnimeEpisodeType().equalsIgnoreCase(context.getResources().getString(R.string.series_string))) {
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.series_string));
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.blue_series_color));
        } else if (animeNewReleaseResultModelList.get(position).getAnimeEpisodeType().equalsIgnoreCase(context.getResources().getString(R.string.ova_string))) {
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.ova_string));
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.pink_series_color));
        } else if (animeNewReleaseResultModelList.get(position).getAnimeEpisodeType().equalsIgnoreCase(context.getResources().getString(R.string.ona_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.purple_series_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.ona_string));
        } else if (animeNewReleaseResultModelList.get(position).getAnimeEpisodeType().equalsIgnoreCase(context.getResources().getString(R.string.la_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.red_series_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.la_string));
        } else if (animeNewReleaseResultModelList.get(position).getAnimeEpisodeType().equalsIgnoreCase(context.getResources().getString(R.string.movie_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.green_series_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.movie_string));
        } else if (animeNewReleaseResultModelList.get(position).getAnimeEpisodeType().equalsIgnoreCase(context.getResources().getString(R.string.special_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.orange_series_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.special_string));
        }


        holder.itemListBinding.relativeItem.setOnClickListener(v -> {
//            Intent intentToVideoWatchActivity = new Intent(context.getApplicationContext(), AnimeDetailActivity.class);
//            intentToVideoWatchActivity.putExtra("animeDetailURL", animeNewReleaseResultModelList.get(position).getEpisodeURL());
//            intentToVideoWatchActivity.putExtra("animeDetailTitle", animeNewReleaseResultModelList.get(position).getAnimeEpisode());
//            intentToVideoWatchActivity.putExtra("animeDetailType", animeNewReleaseResultModelList.get(position).getAnimeEpisodeType());
//            intentToVideoWatchActivity.putExtra("animeDetailStatus", animeNewReleaseResultModelList.get(position).getAnimeEpisodeStatus());
//            intentToVideoWatchActivity.putExtra("animeDetailThumb", animeNewReleaseResultModelList.get(position).getEpisodeThumb());
            Intent intentToVideoWatchActivity = new Intent(context.getApplicationContext(), WatchAnimeEpisodeActivity.class);
            intentToVideoWatchActivity.putExtra("animeEpisodeToWatch", animeNewReleaseResultModelList.get(position).getEpisodeURL());
            intentToVideoWatchActivity.putExtra("animeEpisodeTitle", animeNewReleaseResultModelList.get(position).getAnimeEpisode());
            intentToVideoWatchActivity.putExtra("animeEpisodeThumb", animeNewReleaseResultModelList.get(position).getEpisodeThumb());
            intentToVideoWatchActivity.putExtra("animeEpisodeType", animeNewReleaseResultModelList.get(position).getAnimeEpisodeType());
            intentToVideoWatchActivity.putExtra("animeEpisodeStatus", animeNewReleaseResultModelList.get(position).getAnimeEpisodeStatus());
            context.startActivity(intentToVideoWatchActivity);
        });
    }

    @Override
    public int getItemCount() {
        return animeNewReleaseResultModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListAnimeNewBinding itemListBinding;

        public ViewHolder(final ItemListAnimeNewBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }

    }
}
