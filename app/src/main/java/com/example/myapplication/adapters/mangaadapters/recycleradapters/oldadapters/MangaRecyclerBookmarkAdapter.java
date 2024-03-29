package com.example.myapplication.adapters.mangaadapters.recycleradapters.oldadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity;
import com.example.myapplication.databinding.ItemListMangaSearchResultBinding;
import com.example.myapplication.localstorages.manga_local.manga_bookmark.MangaBookmarkModel;
import com.squareup.picasso.Picasso;

import org.jsoup.internal.StringUtil;

import java.util.List;
import java.util.Objects;

public class MangaRecyclerBookmarkAdapter extends RecyclerView.Adapter<MangaRecyclerBookmarkAdapter.ViewHolder> {
    private Context context;
    private List<MangaBookmarkModel> animeDiscoverResultModelList;

    public MangaRecyclerBookmarkAdapter(Context context, List<MangaBookmarkModel> animeDiscoverResultModelList) {
        this.context = context;
        this.animeDiscoverResultModelList = animeDiscoverResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListMangaSearchResultBinding itemListBinding = ItemListMangaSearchResultBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textTitleMangaResult.setText(animeDiscoverResultModelList.get(position).getMangaTitle());
        if (StringUtil.isBlank(animeDiscoverResultModelList.get(position).getMangaThumb())) {
            holder.itemListBinding.imageViewBackgroundMangaResult.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme()));
            Log.e("pathNull", "null");
        } else {
            Picasso.get().load(animeDiscoverResultModelList.get(position).getMangaThumb()).placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme()))).into(holder.itemListBinding.imageViewBackgroundMangaResult);
        }
        if (!animeDiscoverResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.textMangaStatus.setText(context.getResources().getString(R.string.ongoing_text));
            holder.itemListBinding.cardMangaStatus.setCardBackgroundColor(context.getResources().getColor(R.color.orange_series_color));
        } else if (animeDiscoverResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.textMangaStatus.setText(context.getResources().getString(R.string.completed_text));
            holder.itemListBinding.cardMangaStatus.setCardBackgroundColor(context.getResources().getColor(R.color.green_series_color));
        }
        if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manhwa_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manhua_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.manhua_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.mangaoneshot_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.mangaoneshot_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.oneshot_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.oneshot_string));
        }
        holder.itemListBinding.mangaRatingBar.setNumStars(5);
        String replaceComma = animeDiscoverResultModelList.get(position).getMangaRating().replace(",", ".");
        if (animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("N/A") ||
                animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("?") ||
                animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("-") ||
                animeDiscoverResultModelList.get(position).getMangaRating().equalsIgnoreCase("Unknown")) {
            holder.itemListBinding.mangaRatingBar.setRating(0);
            holder.itemListBinding.mangaRatingNumber.setText(animeDiscoverResultModelList.get(position).getMangaRating());
        } else if (Float.parseFloat(replaceComma) <= 0) {
            holder.itemListBinding.mangaRatingBar.setRating(0);
            holder.itemListBinding.mangaRatingNumber.setText(animeDiscoverResultModelList.get(position).getMangaRating());
        } else {
            holder.itemListBinding.mangaRatingBar.setRating(Float.parseFloat(replaceComma) / 2);
            holder.itemListBinding.mangaRatingNumber.setText(replaceComma);
        }
        holder.itemListBinding.cardLatestMangaRelease.setVisibility(View.GONE);
        holder.itemListBinding.relativeItemMangaResult.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), MangaDetailActivity.class);
            intent.putExtra("detailURL", animeDiscoverResultModelList.get(position).getMangaDetailURL());
            intent.putExtra("detailType", animeDiscoverResultModelList.get(position).getMangaType());
            intent.putExtra("detailTitle", animeDiscoverResultModelList.get(position).getMangaTitle());
            intent.putExtra("detailRating", animeDiscoverResultModelList.get(position).getMangaRating());
            intent.putExtra("detailStatus", animeDiscoverResultModelList.get(position).isMangaStatus());
            intent.putExtra("detailThumb", animeDiscoverResultModelList.get(position).getMangaThumb());
            intent.putExtra("detailFrom", "MangaBookmark");
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
        private ItemListMangaSearchResultBinding itemListBinding;

        public ViewHolder(final ItemListMangaSearchResultBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
