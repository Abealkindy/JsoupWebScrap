package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity;
import com.example.myapplication.databinding.ItemListSelectChapterDetailBinding;
import com.example.myapplication.models.mangamodels.DetailMangaModel;

import java.util.List;

public class RecyclerAllChapterDetailAdapter extends RecyclerView.Adapter<RecyclerAllChapterDetailAdapter.ViewHolder> {
    private Context context;
    private List<DetailMangaModel.DetailAllChapterDatas> allChapterDatasArrayList;
    private String mangaType;

    public RecyclerAllChapterDetailAdapter(Context context, List<DetailMangaModel.DetailAllChapterDatas> allChapterDatasArrayList, String mangaType) {
        this.context = context;
        this.allChapterDatasArrayList = allChapterDatasArrayList;
        this.mangaType = mangaType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListSelectChapterDetailBinding itemListBinding = ItemListSelectChapterDetailBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textViewChapterAllTitle.setText(allChapterDatasArrayList.get(position).getChapterTitle());
        holder.itemListBinding.textViewChapterAllReleaseTime.setText(allChapterDatasArrayList.get(position).getChapterReleaseTime());
        holder.itemListBinding.linearChapterDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
            intent.putExtra("chapterURL", allChapterDatasArrayList.get(position).getChapterURL());
            intent.putExtra("appBarColorStatus", mangaType);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allChapterDatasArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListSelectChapterDetailBinding itemListBinding;

        public ViewHolder(final ItemListSelectChapterDetailBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }
}
