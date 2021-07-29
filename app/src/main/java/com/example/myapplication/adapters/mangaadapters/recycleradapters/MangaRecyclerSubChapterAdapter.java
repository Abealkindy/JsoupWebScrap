package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity;
import com.example.myapplication.databinding.ItemListSelectChapterSmallBinding;

import java.util.List;

public class MangaRecyclerSubChapterAdapter extends RecyclerView.Adapter<MangaRecyclerSubChapterAdapter.ViewHolder> {
    Context context;
    List<String> mangaChapterTitle, mangaChapterTime, mangaChapterURL;
    String mangaThumb, mangaType;

    public MangaRecyclerSubChapterAdapter(
            Context context,
            List<String> mangaChapterTitle,
            List<String> mangaChapterTime,
            List<String> mangaChapterURL,
            String mangaThumb,
            String mangaType
    ) {
        this.context = context;
        this.mangaChapterTitle = mangaChapterTitle;
        this.mangaChapterTime = mangaChapterTime;
        this.mangaChapterURL = mangaChapterURL;
        this.mangaThumb = mangaThumb;
        this.mangaType = mangaType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListSelectChapterSmallBinding itemListBinding = ItemListSelectChapterSmallBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.chapterText.setText(mangaChapterTitle.get(position));
        holder.itemListBinding.chapterReleaseTimeText.setText(mangaChapterTime.get(position));
        holder.itemListBinding.linearChapterDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
            intent.putExtra("chapterURL", mangaChapterURL.get(position));
            intent.putExtra("appBarColorStatus", mangaType);
            intent.putExtra("chapterTitle", mangaChapterTitle.get(position));
            intent.putExtra("chapterThumb", mangaThumb);
            intent.putExtra("readFrom", "MangaReleases");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mangaChapterTime.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListSelectChapterSmallBinding itemListBinding;

        public ViewHolder(final ItemListSelectChapterSmallBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
