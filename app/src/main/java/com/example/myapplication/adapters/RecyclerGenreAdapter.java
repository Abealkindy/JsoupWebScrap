package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListGenreBinding;
import com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp.GenreAndSearchAnimeFragment;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.models.mangamodels.DetailMangaModel;

import java.util.List;
import java.util.Objects;

public class RecyclerGenreAdapter extends RecyclerView.Adapter<RecyclerGenreAdapter.ViewHolder> {
    private Context context;
    private GenreAndSearchAnimeFragment contexts;
    private List<DetailMangaModel.DetailMangaGenres> detailMangaGenresList;
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeGenreList;
    private ClickItemListener clickItemListener;
    public String from = "";

    public RecyclerGenreAdapter(Context context, List<DetailMangaModel.DetailMangaGenres> genresList) {
        this.context = context;
        this.detailMangaGenresList = genresList;
    }

    public RecyclerGenreAdapter(GenreAndSearchAnimeFragment contexts, List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreList, String froms) {
        this.contexts = contexts;
        this.animeGenreList = genreList;
        this.from = froms;
        if (contexts != null) {
            clickItemListener = contexts;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        if (from != null && !from.isEmpty()) {
            layoutInflater = LayoutInflater.from(contexts.getContext());
        } else {
            layoutInflater = LayoutInflater.from(context);
        }
        ItemListGenreBinding itemListBinding = ItemListGenreBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (from != null && !from.isEmpty()) {
            holder.itemListBinding.textMangaGenre.setText(animeGenreList.get(position).getGenreTitle());
            holder.itemListBinding.textMangaGenre.setOnClickListener(v -> {
                String genreAdd = "", sortAdd = "", typeAdd = "", statusAdd = "";
                if (from.equalsIgnoreCase("genre")) {
                    if (contexts.genreURL.contains(animeGenreList.get(position).getGenreURL())) {
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue));
                    } else {
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue_marked));
                    }
                    StringBuilder stringBuilder = new StringBuilder(genreAdd);
                    genreAdd = "&genre[]=" + animeGenreList.get(position).getGenreURL();
                    genreAdd = String.valueOf(stringBuilder.append(genreAdd));
                    clickItemListener.onItemClickGenres(genreAdd);
                } else if (from.equalsIgnoreCase("sort")) {
                    if (contexts.sortURL.equalsIgnoreCase(animeGenreList.get(position).getGenreURL())) {
                        sortAdd = "";
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue));
                    } else {
                        sortAdd = animeGenreList.get(position).getGenreURL();
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue_marked));
                    }
                    clickItemListener.onItemClickSort(sortAdd);
                } else if (from.equalsIgnoreCase("type")) {
                    if (contexts.typeURL.equalsIgnoreCase(animeGenreList.get(position).getGenreURL())) {
                        typeAdd = "";
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue));
                    } else {
                        typeAdd = animeGenreList.get(position).getGenreURL();
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue_marked));
                    }
                    clickItemListener.onItemClickType(typeAdd);
                } else if (from.equalsIgnoreCase("status")) {
                    if (contexts.statusURL.equalsIgnoreCase(animeGenreList.get(position).getGenreURL())) {
                        statusAdd = "";
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue));
                    } else {
                        statusAdd = animeGenreList.get(position).getGenreURL();
                        holder.itemListBinding.textMangaGenre.setBackground(Objects.requireNonNull(contexts.getContext()).
                                getResources().getDrawable(R.drawable.bubble_background_darker_blue_marked));
                    }
                    clickItemListener.onItemClickStatus(statusAdd);
                }
            });
        } else {
            holder.itemListBinding.textMangaGenre.setText(detailMangaGenresList.get(position).getGenreTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (from != null && !from.isEmpty()) {
            return animeGenreList.size();
        } else {
            return detailMangaGenresList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListGenreBinding itemListBinding;

        public ViewHolder(final ItemListGenreBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }

    public interface ClickItemListener {
        void onItemClickGenres(String position);

        void onItemClickSort(String position);

        void onItemClickType(String position);

        void onItemClickStatus(String position);
    }
}
