package com.example.myapplication.adapters.animeadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activities.animepage.anime_detail_mvp.AnimeDetailActivity;
import com.example.myapplication.databinding.ItemListAnimeBinding;
import com.example.myapplication.localstorages.anime_local.anime_bookmark.AnimeBookmarkModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeRecyclerBookmarkAdapter extends RecyclerView.Adapter<AnimeRecyclerBookmarkAdapter.ViewHolder> {
    private Context context;
    private List<AnimeBookmarkModel> searchResultList;

    public AnimeRecyclerBookmarkAdapter(Context context, List<AnimeBookmarkModel> searchResultList) {
        this.context = context;
        this.searchResultList = searchResultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListAnimeBinding itemListBinding = ItemListAnimeBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textTitle.setText(searchResultList.get(position).getAnimeTitle());
        Picasso.get().load(searchResultList.get(position).getAnimeThumb()).into(holder.itemListBinding.imageViewBackground);
//        if (searchResultList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.series_string)) ||
//                searchResultList.get(position).getAnimeType().contains(context.getResources().getString(R.string.series_string))) {
//            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.getResources().getColor(R.color.blue_series_color));
//            holder.itemListBinding.textEpisodeType.setText(context.getResources().getString(R.string.series_string));
//            holder.itemListBinding.cardEpisodeStatus.setVisibility(View.VISIBLE);
//        } else if (searchResultList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.ova_string)) ||
//                searchResultList.get(position).getAnimeType().contains(context.getResources().getString(R.string.ova_string))) {
//            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.getResources().getColor(R.color.pink_series_color));
//            holder.itemListBinding.textEpisodeType.setText(context.getResources().getString(R.string.ova_string));
//            holder.itemListBinding.cardEpisodeStatus.setVisibility(View.VISIBLE);
//        } else if (searchResultList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.ona_string)) ||
//                searchResultList.get(position).getAnimeType().contains(context.getResources().getString(R.string.ona_string))) {
//            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.getResources().getColor(R.color.purple_series_color));
//            holder.itemListBinding.textEpisodeType.setText(context.getResources().getString(R.string.ona_string));
//            holder.itemListBinding.cardEpisodeStatus.setVisibility(View.VISIBLE);
//        } else if (searchResultList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.la_string)) ||
//                searchResultList.get(position).getAnimeType().contains(context.getResources().getString(R.string.la_string))) {
//            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.getResources().getColor(R.color.red_series_color));
//            holder.itemListBinding.textEpisodeType.setText(context.getResources().getString(R.string.la_string));
//            holder.itemListBinding.cardEpisodeStatus.setVisibility(View.VISIBLE);
//        } else if (searchResultList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.movie_string)) ||
//                searchResultList.get(position).getAnimeType().contains(context.getResources().getString(R.string.movie_string_lower))) {
//            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.getResources().getColor(R.color.green_series_color));
//            holder.itemListBinding.textEpisodeType.setText(context.getResources().getString(R.string.movie_string));
//            holder.itemListBinding.cardEpisodeStatus.setVisibility(View.GONE);
//        } else if (searchResultList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.special_string)) ||
//                searchResultList.get(position).getAnimeType().contains(context.getResources().getString(R.string.special_string_lower))) {
//            holder.itemListBinding.cardEpisodeType.setCardBackgroundColor(context.getResources().getColor(R.color.orange_series_color));
//            holder.itemListBinding.textEpisodeType.setText(context.getResources().getString(R.string.special_string));
//            holder.itemListBinding.cardEpisodeStatus.setVisibility(View.VISIBLE);
//        }
//
//        if (searchResultList.get(position).getAnimeStatus().equalsIgnoreCase(context.getResources().getString(R.string.ongoing_text))) {
//            holder.itemListBinding.cardEpisodeStatus.setCardBackgroundColor(context.getResources().getColor(R.color.ongoing_color));
//            holder.itemListBinding.textEpisodeStatus.setText(context.getResources().getString(R.string.ongoing_text));
//        } else if (searchResultList.get(position).getAnimeStatus().equalsIgnoreCase(context.getResources().getString(R.string.completed_text))) {
//            holder.itemListBinding.cardEpisodeStatus.setCardBackgroundColor(context.getResources().getColor(R.color.completed_color));
//            holder.itemListBinding.textEpisodeStatus.setText(context.getResources().getString(R.string.completed_text));
//        }

        holder.itemListBinding.relativeItem.setOnClickListener(v -> {
            Intent intentToVideoWatchActivity = new Intent(context.getApplicationContext(), AnimeDetailActivity.class);
            intentToVideoWatchActivity.putExtra("animeDetailURL", searchResultList.get(position).getAnimeDetailURL());
            intentToVideoWatchActivity.putExtra("animeDetailTitle", searchResultList.get(position).getAnimeTitle());
//            intentToVideoWatchActivity.putExtra("animeDetailType", searchResultList.get(position).getAnimeType());
//            intentToVideoWatchActivity.putExtra("animeDetailStatus", searchResultList.get(position).getAnimeStatus());
            intentToVideoWatchActivity.putExtra("animeDetailThumb", searchResultList.get(position).getAnimeThumb());
            context.startActivity(intentToVideoWatchActivity);
        });
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    public void recyclerRefresh() {
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListAnimeBinding itemListBinding;

        public ViewHolder(final ItemListAnimeBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }

    }
}
