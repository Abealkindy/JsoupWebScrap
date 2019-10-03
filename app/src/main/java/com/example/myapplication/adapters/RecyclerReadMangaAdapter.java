package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListMangaContentBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerReadMangaAdapter extends RecyclerView.Adapter<RecyclerReadMangaAdapter.ViewHolder> {
    private Context context;
    private List<String> imageContent;

    public RecyclerReadMangaAdapter(Context context, List<String> imageContent) {
        this.context = context;
        this.imageContent = imageContent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListMangaContentBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_manga_content, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(imageContent.get(position)).into(holder.itemListBinding.imageMangaContentItem);
    }

    @Override
    public int getItemCount() {
        return imageContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListMangaContentBinding itemListBinding;

        public ViewHolder(final ItemListMangaContentBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }

    }
}
