package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity;
import com.example.myapplication.databinding.ItemListMangaNewBinding;
import com.example.myapplication.databinding.ItemListMangaSearchResultBinding;
import com.example.myapplication.localstorages.manga_local.MangaBookmarkModel;
import com.squareup.picasso.Picasso;

import org.jsoup.internal.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
        holder.itemListBinding.textViewStatus.setVisibility(View.VISIBLE);
        holder.itemListBinding.linearRatingList.setVisibility(View.VISIBLE);
        holder.itemListBinding.textViewHotStatus.setVisibility(View.GONE);
        holder.itemListBinding.linearNewest.setVisibility(View.GONE);
        holder.itemListBinding.linearSecondNewest.setVisibility(View.GONE);
        holder.itemListBinding.linearThirdNewest.setVisibility(View.GONE);
        holder.itemListBinding.newestTextChapterReleaseTime.setVisibility(View.GONE);
        holder.itemListBinding.mangaTitleText.setText(animeDiscoverResultModelList.get(position).getMangaTitle());
        if (StringUtil.isBlank(animeDiscoverResultModelList.get(position).getMangaThumb())) {
            holder.itemListBinding.mangaThumb.setImageDrawable(context.getResources().getDrawable(R.drawable.imageplaceholder));
            Log.e("pathNull", "null");
        } else {
            try {
                Glide.with(context)
                        .asDrawable()
                        .load(new URL(animeDiscoverResultModelList.get(position).getMangaThumb()))
                        .apply(
                                new RequestOptions()
                                        .transform(new RoundedCorners(20))
                                        .timeout(30000)
                        )
                        .error(context.getResources().getDrawable(R.drawable.error))
                        .placeholder(context.getResources().getDrawable(R.drawable.imageplaceholder))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.itemListBinding.mangaThumb);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (!animeDiscoverResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.textViewStatus.setText(context.getResources().getString(R.string.ongoing_text));
            holder.itemListBinding.textViewStatus.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_ongoing));
        } else if (animeDiscoverResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.textViewStatus.setText(context.getResources().getString(R.string.completed_text));
            holder.itemListBinding.textViewStatus.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_completed));
        }
        if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.textMangaType.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_manga));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.textMangaType.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_manhwa));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.textMangaType.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_manhua));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.manhua_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.mangaoneshot_string))) {
            holder.itemListBinding.textMangaType.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_manga));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.oneshot_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.oneshot_string))) {
            holder.itemListBinding.textMangaType.setBackground(context.getResources().getDrawable(R.drawable.bubble_background_manga));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.oneshot_string));
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
            context.startActivity(intent);
        });
    }

    public void recyclerRefresh() {
        this.notifyDataSetChanged();
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
