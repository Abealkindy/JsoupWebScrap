package com.example.myapplication.adapters.mangaadapters.recycleradapters;

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
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity;
import com.example.myapplication.databinding.ItemListMangaNewBinding;
import com.example.myapplication.localstorages.manga_local.manga_bookmark.MangaBookmarkModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MangaRecyclerBookmarkAdapterNew extends RecyclerView.Adapter<MangaRecyclerBookmarkAdapterNew.ViewHolder> {
    private Context context;
    private List<MangaBookmarkModel> animeDiscoverResultModelList;

    public MangaRecyclerBookmarkAdapterNew(Context context, List<MangaBookmarkModel> animeDiscoverResultModelList) {
        this.context = context;
        this.animeDiscoverResultModelList = animeDiscoverResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListMangaNewBinding itemListBinding = ItemListMangaNewBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.hotLabel.setVisibility(View.VISIBLE);
        holder.itemListBinding.linearRatingList.setVisibility(View.VISIBLE);
        holder.itemListBinding.linearNewest.setVisibility(View.GONE);
        holder.itemListBinding.linearSecondNewest.setVisibility(View.GONE);
        holder.itemListBinding.linearThirdNewest.setVisibility(View.GONE);
        holder.itemListBinding.newestTextChapterReleaseTime.setVisibility(View.GONE);
        holder.itemListBinding.mangaTitleText.setText(animeDiscoverResultModelList.get(position).getMangaTitle());
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    final Request authorized = original.newBuilder()
                            .addHeader("Cookie", CookieManager.getInstance().getCookie(animeDiscoverResultModelList.get(position).getMangaThumb()))
                            .addHeader("User-Agent", "")
                            .build();
                    return chain.proceed(authorized);
                })
                .build();
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
        picasso.load(animeDiscoverResultModelList.get(position).getMangaThumb())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                .into(holder.itemListBinding.mangaThumb);
        if (!animeDiscoverResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.hotLabel.setText(context.getResources().getString(R.string.ongoing_text))
                    .setSlantedBackgroundColor(context.getResources().getColor(R.color.orange_series_color));
        } else if (animeDiscoverResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.hotLabel.setText(context.getResources().getString(R.string.completed_text))
                    .setSlantedBackgroundColor(context.getResources().getColor(R.color.green_series_color));
        }
        if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manhwa_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manhua_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.manhua_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.mangaoneshot_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.mangaoneshot_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.oneshot_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.oneshot_string));
        }
        holder.itemListBinding.mangaRatingBar.setNumStars(5);
        String replaceComma = animeDiscoverResultModelList.get(position).getMangaRating().replace(",", ".");
        if (animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("N/A") || animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("?") || animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("-")) {
            holder.itemListBinding.mangaRatingBar.setRating(0);
            holder.itemListBinding.mangaRatingNumber.setText(animeDiscoverResultModelList.get(position).getMangaRating());
        } else if (Float.parseFloat(replaceComma) <= 0) {
            holder.itemListBinding.mangaRatingBar.setRating(0);
            holder.itemListBinding.mangaRatingNumber.setText(animeDiscoverResultModelList.get(position).getMangaRating());
        } else {
            holder.itemListBinding.mangaRatingBar.setRating(Float.parseFloat(replaceComma) / 2);
            holder.itemListBinding.mangaRatingNumber.setText(replaceComma);
        }
        holder.itemListBinding.relativeItemManga.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), MangaDetailActivity.class);
            intent.putExtra("detailURL", animeDiscoverResultModelList.get(position).getMangaDetailURL());
            intent.putExtra("detailType", animeDiscoverResultModelList.get(position).getMangaType());
            intent.putExtra("detailTitle", animeDiscoverResultModelList.get(position).getMangaTitle());
            intent.putExtra("detailRating", animeDiscoverResultModelList.get(position).getMangaRating());
            intent.putExtra("detailStatus", animeDiscoverResultModelList.get(position).isMangaStatus());
            intent.putExtra("detailThumb", animeDiscoverResultModelList.get(position).getMangaThumb());
            intent.putExtra("detailFrom", "MangaBookmark");
            context.startActivity(intent);
//            ((MangaReleaseListActivity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return animeDiscoverResultModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListMangaNewBinding itemListBinding;

        public ViewHolder(final ItemListMangaNewBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
