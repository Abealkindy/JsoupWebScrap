package com.example.myapplication.adapters.animeadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activities.animepage.anime_detail_mvp.AnimeDetailActivity;
import com.example.myapplication.databinding.ItemListAnimeNewBinding;
import com.example.myapplication.localstorages.anime_local.anime_bookmark.AnimeBookmarkModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeRecyclerBookmarkAdapterNew extends RecyclerView.Adapter<AnimeRecyclerBookmarkAdapterNew.ViewHolder> {
    private Context context;
    private List<AnimeBookmarkModel> searchResultList;

    public AnimeRecyclerBookmarkAdapterNew(Context context, List<AnimeBookmarkModel> searchResultList) {
        this.context = context;
        this.searchResultList = searchResultList;
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
        holder.itemListBinding.textGenre.setVisibility(View.GONE);
        holder.itemListBinding.linearRating.setVisibility(View.VISIBLE);
        holder.itemListBinding.seriesType.setVisibility(View.GONE);
        holder.itemListBinding.textTitle.setText(searchResultList.get(position).getAnimeTitle());
        holder.itemListBinding.ratingAnime.setNumStars(5);
        String stringToFloat = searchResultList.get(position).getAnimeRating().replace(",", ".");
        if (Float.parseFloat(stringToFloat) <= 0) {
            holder.itemListBinding.ratingAnime.setRating(0);
        } else {
            holder.itemListBinding.ratingAnime.setRating(Float.parseFloat(stringToFloat));
        }
        holder.itemListBinding.ratingNumber.setText(stringToFloat);
        Picasso.get().load(searchResultList.get(position).getAnimeThumb()).into(holder.itemListBinding.imageViewBackground);
        holder.itemListBinding.relativeItem.setOnClickListener(v -> {
            Intent intentToVideoWatchActivity = new Intent(context.getApplicationContext(), AnimeDetailActivity.class);
            intentToVideoWatchActivity.putExtra("animeDetailURL", searchResultList.get(position).getAnimeDetailURL());
            intentToVideoWatchActivity.putExtra("animeDetailTitle", searchResultList.get(position).getAnimeTitle());
            intentToVideoWatchActivity.putExtra("animeDetailThumb", searchResultList.get(position).getAnimeThumb());
            intentToVideoWatchActivity.putExtra("animeDetailType", "");
            intentToVideoWatchActivity.putExtra("animeDetailStatus", "");
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
        private final ItemListAnimeNewBinding itemListBinding;

        public ViewHolder(final ItemListAnimeNewBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }
}
