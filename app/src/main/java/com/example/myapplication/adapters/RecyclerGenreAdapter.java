package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListGenreBinding;
import com.example.myapplication.databinding.ItemListSelectChapterBinding;
import com.example.myapplication.models.mangamodels.DetailMangaModel;
import com.example.myapplication.models.mangamodels.ReadMangaModel;

import java.util.List;

public class RecyclerGenreAdapter extends RecyclerView.Adapter<RecyclerGenreAdapter.ViewHolder> {
    private Context context;
    private List<DetailMangaModel.DetailMangaGenres> detailMangaGenresList;

    public RecyclerGenreAdapter(Context context, List<DetailMangaModel.DetailMangaGenres> genresList) {
        this.context = context;
        this.detailMangaGenresList = genresList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListGenreBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_genre, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textMangaGenre.setText(detailMangaGenresList.get(position).getGenreTitle());
    }

    @Override
    public int getItemCount() {
        return detailMangaGenresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListGenreBinding itemListBinding;

        public ViewHolder(final ItemListGenreBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }
}
