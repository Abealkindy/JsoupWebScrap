package com.example.myapplication.adapters.animeadapters.recycleradapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListSelectChapterBinding;
import com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp.GenreAndSearchAnimeFragment;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;

import java.util.List;

public class RecyclerAllGenreAdapter extends RecyclerView.Adapter<RecyclerAllGenreAdapter.ViewHolder> {
    private GenreAndSearchAnimeFragment context;
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> allGenreList;
    private ClickGenreListener clickListener;

    public RecyclerAllGenreAdapter(GenreAndSearchAnimeFragment context, List<AnimeGenreAndSearchResultModel.AnimeGenreResult> allGenreList) {
        this.context = context;
        this.allGenreList = allGenreList;
        if (context != null) {
            clickListener = context;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context.getContext());
        ItemListSelectChapterBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_select_chapter, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textViewChapterAll.setText(allGenreList.get(position).getGenreTitle());
        Log.e("positionFromAdapter", "" + position);
        holder.itemListBinding.linearViewChapterAll.setOnClickListener(v -> clickListener.onItemClickGenre(position));
    }

    @Override
    public int getItemCount() {
        return allGenreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListSelectChapterBinding itemListBinding;

        public ViewHolder(final ItemListSelectChapterBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }

    public interface ClickGenreListener {
        void onItemClickGenre(int position);
    }
}
