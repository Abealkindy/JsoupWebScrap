package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.ReadMangaActivity;
import com.example.myapplication.databinding.ItemListMangaSearchResultBinding;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaRecyclerDiscoverAdapter extends RecyclerView.Adapter<MangaRecyclerDiscoverAdapter.ViewHolder> {
    private Context context;
    private List<DiscoverMangaModel> animeDiscoverResultModelList;

    public MangaRecyclerDiscoverAdapter(Context context, List<DiscoverMangaModel> animeDiscoverResultModelList) {
        this.context = context;
        this.animeDiscoverResultModelList = animeDiscoverResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListMangaSearchResultBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_manga_search_result, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textTitleMangaResult.setText(animeDiscoverResultModelList.get(position).getMangaTitle());
        if (animeDiscoverResultModelList.get(position).getMangaThumb().isEmpty() || animeDiscoverResultModelList.get(position).getMangaThumb() == null) {
            holder.itemListBinding.imageViewBackgroundMangaResult.setImageDrawable(context.getResources().getDrawable(R.drawable.imageplaceholder));
            Log.e("pathNull", "null");
        } else {
            Picasso.get().load(animeDiscoverResultModelList.get(position).getMangaThumb()).placeholder(context.getResources().getDrawable(R.drawable.imageplaceholder)).into(holder.itemListBinding.imageViewBackgroundMangaResult);
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
        String replaceCh = animeDiscoverResultModelList.get(position).getMangaLatestChapterText().replace("Ch.", "Chapter ");
        holder.itemListBinding.textLatestChapterRelease.setText(replaceCh);
        holder.itemListBinding.cardLatestMangaRelease.setOnClickListener(view -> {
            Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
            intent.putExtra("chapterURL", animeDiscoverResultModelList.get(position).getMangaLatestChapter());
            intent.putExtra("appBarColorStatus", animeDiscoverResultModelList.get(position).getMangaType());
            intent.putExtra("chapterTitle", animeDiscoverResultModelList.get(position).getMangaLatestChapterText());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return animeDiscoverResultModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListMangaSearchResultBinding itemListBinding;

        public ViewHolder(final ItemListMangaSearchResultBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
