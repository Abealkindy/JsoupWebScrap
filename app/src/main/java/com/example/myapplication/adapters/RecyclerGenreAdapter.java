package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListGenreBinding;
import com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp.GenreAndSearchAnimeFragment;
import com.example.myapplication.fragments.manga_fragments.discover_manga_mvp.DiscoverMangaFragment;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.models.mangamodels.DetailMangaModel;

import java.util.List;

public class RecyclerGenreAdapter extends RecyclerView.Adapter<RecyclerGenreAdapter.ViewHolder> {
    private Context context;
    private GenreAndSearchAnimeFragment contexts;
    private DiscoverMangaFragment contextss;
    private List<DetailMangaModel.DetailMangaGenres> detailMangaGenresList;
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeGenreList;
    private ClickItemListener clickItemListener;
    public String from = "";
    private boolean isClicked = false;

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

    public RecyclerGenreAdapter(DiscoverMangaFragment contexts, List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreList, String froms) {
        this.contextss = contexts;
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
            if (contexts != null) {
                layoutInflater = LayoutInflater.from(contexts.getContext());
            } else {
                layoutInflater = LayoutInflater.from(contextss.getContext());
            }
        } else {
            layoutInflater = LayoutInflater.from(context);
        }
        ItemListGenreBinding itemListBinding = ItemListGenreBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (from != null && !from.isEmpty()) {
            if (contexts != null) {
                if (!animeGenreList.get(position).isGenreSelected()) {
                    holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contexts.requireContext().getTheme()));
                } else {
                    isClicked = true;
                    holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contexts.requireContext().getTheme()));
                }
                holder.itemListBinding.textMangaGenre.setText(animeGenreList.get(position).getGenreTitle());
                holder.itemListBinding.textMangaGenre.setOnClickListener(v -> {
                    String genreAdd = "", sortAdd, typeAdd, statusAdd;
                    if (from.equalsIgnoreCase("genre")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contexts.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(true);
                        } else {
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contexts.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                        }
                        StringBuilder stringBuilder = new StringBuilder(genreAdd);
                        genreAdd = "&genre%5B0%5D=" + animeGenreList.get(position).getGenreURL();
                        genreAdd = String.valueOf(stringBuilder.append(genreAdd));
                        clickItemListener.onItemClickGenres(animeGenreList, genreAdd);
                    } else if (from.equalsIgnoreCase("sort")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            if (!isClicked) {
                                isClicked = true;
                                sortAdd = animeGenreList.get(position).getGenreURL();
                                holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contexts.requireContext().getTheme()));
                                animeGenreList.get(position).setGenreSelected(true);
                                clickItemListener.onItemClickSort(animeGenreList, sortAdd);
                            }
                        } else {
                            isClicked = false;
                            sortAdd = "";
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contexts.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                            clickItemListener.onItemClickSort(animeGenreList, sortAdd);
                        }
                    } else if (from.equalsIgnoreCase("type")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            if (!isClicked) {
                                isClicked = true;
                                typeAdd = animeGenreList.get(position).getGenreURL();
                                holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contexts.requireContext().getTheme()));
                                animeGenreList.get(position).setGenreSelected(true);
                                clickItemListener.onItemClickType(animeGenreList, typeAdd);
                            }
                        } else {
                            isClicked = false;
                            typeAdd = "";
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contexts.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                            clickItemListener.onItemClickType(animeGenreList, typeAdd);
                        }
                    } else if (from.equalsIgnoreCase("status")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            if (!isClicked) {
                                isClicked = true;
                                statusAdd = animeGenreList.get(position).getGenreURL();
                                holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contexts.requireContext().getTheme()));
                                animeGenreList.get(position).setGenreSelected(true);
                                clickItemListener.onItemClickStatus(animeGenreList, statusAdd);
                            }
                        } else {
                            isClicked = false;
                            statusAdd = "";
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contexts.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contexts.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                            clickItemListener.onItemClickStatus(animeGenreList, statusAdd);
                        }
                    }
                });
            } else {
                if (!animeGenreList.get(position).isGenreSelected()) {
                    holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contextss.requireContext().getTheme()));
                } else {
                    isClicked = true;
                    holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contextss.requireContext().getTheme()));
                }
                holder.itemListBinding.textMangaGenre.setText(animeGenreList.get(position).getGenreTitle());
                holder.itemListBinding.textMangaGenre.setOnClickListener(v -> {
                    String genreAdd = "", sortAdd = "", typeAdd = "", statusAdd = "";
                    if (from.equalsIgnoreCase("genre")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contextss.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(true);
                        } else {
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contextss.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                        }
                        StringBuilder stringBuilder = new StringBuilder(genreAdd);
                        genreAdd = "&genre%5B0%5D=" + animeGenreList.get(position).getGenreURL();
                        genreAdd = String.valueOf(stringBuilder.append(genreAdd));
                        clickItemListener.onItemClickGenres(animeGenreList, genreAdd);
                    } else if (from.equalsIgnoreCase("sort")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            if (!isClicked) {
                                isClicked = true;
                                sortAdd = animeGenreList.get(position).getGenreURL();
                                holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contextss.requireContext().getTheme()));
                                animeGenreList.get(position).setGenreSelected(true);
                                clickItemListener.onItemClickSort(animeGenreList, sortAdd);
                            }
                        } else {
                            isClicked = false;
                            sortAdd = "";
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contextss.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                            clickItemListener.onItemClickSort(animeGenreList, sortAdd);
                        }
                    } else if (from.equalsIgnoreCase("type")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            if (!isClicked) {
                                isClicked = true;
                                typeAdd = animeGenreList.get(position).getGenreURL();
                                holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contextss.requireContext().getTheme()));
                                animeGenreList.get(position).setGenreSelected(true);
                                clickItemListener.onItemClickType(animeGenreList, typeAdd);
                            }
                        } else {
                            isClicked = false;
                            typeAdd = "";
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contextss.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                            clickItemListener.onItemClickType(animeGenreList, typeAdd);
                        }
                    } else if (from.equalsIgnoreCase("status")) {
                        if (!animeGenreList.get(position).isGenreSelected()) {
                            if (!isClicked) {
                                isClicked = true;
                                statusAdd = animeGenreList.get(position).getGenreURL();
                                holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue_marked, contextss.requireContext().getTheme()));
                                animeGenreList.get(position).setGenreSelected(true);
                                clickItemListener.onItemClickStatus(animeGenreList, statusAdd);
                            }
                        } else {
                            isClicked = false;
                            statusAdd = "";
                            holder.itemListBinding.textMangaGenre.setBackground(ResourcesCompat.getDrawable(contextss.requireContext().getResources(), R.drawable.bubble_background_darker_blue, contextss.requireContext().getTheme()));
                            animeGenreList.get(position).setGenreSelected(false);
                            clickItemListener.onItemClickStatus(animeGenreList, statusAdd);
                        }
                    }
                });
            }

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
        void onItemClickGenres(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList, String position);

        void onItemClickSort(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList, String position);

        void onItemClickType(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList, String position);

        void onItemClickStatus(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList, String position);
    }
}
