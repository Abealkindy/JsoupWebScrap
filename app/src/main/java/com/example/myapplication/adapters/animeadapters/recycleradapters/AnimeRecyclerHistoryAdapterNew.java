package com.example.myapplication.adapters.animeadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.animepage.watch_anime_mvp.WatchAnimeEpisodeActivity;
import com.example.myapplication.databinding.ItemListAnimeNewBinding;
import com.example.myapplication.localstorages.anime_local.watch_history.AnimeHistoryModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AnimeRecyclerHistoryAdapterNew extends RecyclerView.Adapter<AnimeRecyclerHistoryAdapterNew.ViewHolder> {
    private Context context;
    private List<AnimeHistoryModel> animeDiscoverResultModelList;

    public AnimeRecyclerHistoryAdapterNew(Context context, List<AnimeHistoryModel> animeDiscoverResultModelList) {
        this.context = context;
        this.animeDiscoverResultModelList = animeDiscoverResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListAnimeNewBinding itemListBinding = ItemListAnimeNewBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textGenre.setVisibility(View.GONE);
        holder.itemListBinding.linearRating.setVisibility(View.GONE);
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    final Request authorized = original.newBuilder()
                            .addHeader("Cookie", CookieManager.getInstance().getCookie(animeDiscoverResultModelList.get(position).getAnimeThumb()))
                            .addHeader("User-Agent", "")
                            .build();
                    return chain.proceed(authorized);
                })
                .build();
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
        picasso.load(animeDiscoverResultModelList.get(position).getAnimeThumb())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                .into(holder.itemListBinding.imageViewBackground);
        if (animeDiscoverResultModelList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeDiscoverResultModelList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.manhwa_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeDiscoverResultModelList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.manhua_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.manhua_string));
        } else if (animeDiscoverResultModelList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.mangaoneshot_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.mangaoneshot_string));
        } else if (animeDiscoverResultModelList.get(position).getAnimeType().equalsIgnoreCase(context.getResources().getString(R.string.oneshot_string))) {
            holder.itemListBinding.seriesType.setSlantedBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.seriesType.setText(context.getResources().getString(R.string.oneshot_string));
        }
        holder.itemListBinding.textTitle.setText(animeDiscoverResultModelList.get(position).getAnimeTitle());
        holder.itemListBinding.relativeItem.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), WatchAnimeEpisodeActivity.class);
            intent.putExtra("chapterURL", animeDiscoverResultModelList.get(position).getAnimeDetailURL());
            intent.putExtra("appBarColorStatus", animeDiscoverResultModelList.get(position).getAnimeType());
            intent.putExtra("chapterTitle", animeDiscoverResultModelList.get(position).getAnimeTitle());
            intent.putExtra("chapterThumb", animeDiscoverResultModelList.get(position).getAnimeThumb());
            intent.putExtra("readFrom", "AnimeHistory");
            context.startActivity(intent);
//            ((AnimeReleaseListActivity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return animeDiscoverResultModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListAnimeNewBinding itemListBinding;

        public ViewHolder(final ItemListAnimeNewBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
