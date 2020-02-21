package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListSelectChapterBinding;
import com.example.myapplication.models.mangamodels.ReadMangaModel;

import java.util.List;

public class RecyclerAllChapterAdapter extends RecyclerView.Adapter<RecyclerAllChapterAdapter.ViewHolder> {
    private Context context;
    private List<ReadMangaModel.AllChapterDatas> allChapterDatasArrayList;
    private ClickListener clickListener;

    public RecyclerAllChapterAdapter(Context context, List<ReadMangaModel.AllChapterDatas> allChapterDatasArrayList) {
        this.context = context;
        this.allChapterDatasArrayList = allChapterDatasArrayList;
        if (context instanceof ClickListener) {
            clickListener = (ClickListener) context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListSelectChapterBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_select_chapter, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textViewChapterAll.setText(allChapterDatasArrayList.get(position).getChapterTitle());
        holder.itemListBinding.linearViewChapterAll.setOnClickListener(v -> clickListener.onItemClick(position, v));
    }

    @Override
    public int getItemCount() {
        return allChapterDatasArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListSelectChapterBinding itemListBinding;

        public ViewHolder(final ItemListSelectChapterBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
