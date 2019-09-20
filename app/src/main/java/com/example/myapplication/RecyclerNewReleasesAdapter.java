package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

class RecyclerNewReleasesAdapter extends RecyclerView.Adapter<RecyclerNewReleasesAdapter.ViewHolder> {
    private Context context;
    private List<NewReleaseResultModel> newReleaseResultModelList;

    public RecyclerNewReleasesAdapter(Context context, List<NewReleaseResultModel> newReleaseResultModelList) {
        this.context = context;
        this.newReleaseResultModelList = newReleaseResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textTitle.setText(newReleaseResultModelList.get(position).getAnimeEpisode());
        Picasso.get().load(newReleaseResultModelList.get(position).getEpisodeThumb()).into(holder.itemListBinding.imageViewBackground);
        holder.itemListBinding.relativeItem.setOnClickListener(v -> {
            Intent intentToVideoWatchActivity = new Intent(context.getApplicationContext(), WatchAnimeEpisodeActivity.class);
            intentToVideoWatchActivity.putExtra("animeEpisodeToWatch", newReleaseResultModelList.get(position).getEpisodeURL());
            intentToVideoWatchActivity.putExtra("animeEpisodeThumb", newReleaseResultModelList.get(position).getEpisodeThumb());
            intentToVideoWatchActivity.putExtra("animeEpisodeTitle", newReleaseResultModelList.get(position).getAnimeEpisode());
            context.startActivity(intentToVideoWatchActivity);
        });
    }

    @Override
    public int getItemCount() {
        return newReleaseResultModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListBinding itemListBinding;

        public ViewHolder(final ItemListBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }

    }
}
