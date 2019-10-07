package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
        Picasso.get().load(animeDiscoverResultModelList.get(position).getMangaThumb()).placeholder(context.getResources().getDrawable(R.drawable.imageplaceholder)).into(holder.itemListBinding.imageViewBackgroundMangaResult);
        if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manhwa_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeDiscoverResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.cardMangaTypeResult.setCardBackgroundColor(context.getResources().getColor(R.color.manhua_color));
            holder.itemListBinding.textMangaTypeResult.setText(context.getResources().getString(R.string.manhua_string));
        }
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
