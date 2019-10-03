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

import com.example.myapplication.activities.mangapage.ReadMangaActivity;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListMangaBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaRecyclerNewReleasesAdapter extends RecyclerView.Adapter<MangaRecyclerNewReleasesAdapter.ViewHolder> {
    private Context context;
    private List<MangaNewReleaseResultModel> animeNewReleaseResultModelList;

    public MangaRecyclerNewReleasesAdapter(Context context, List<MangaNewReleaseResultModel> animeNewReleaseResultModelList) {
        this.context = context;
        this.animeNewReleaseResultModelList = animeNewReleaseResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListMangaBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_manga, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textTitleManga.setText(animeNewReleaseResultModelList.get(position).getMangaTitle());
        Picasso.get().load(animeNewReleaseResultModelList.get(position).getMangaThumb()).into(holder.itemListBinding.imageViewBackgroundManga);
        if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.cardMangaType.setCardBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.cardMangaType.setCardBackgroundColor(context.getResources().getColor(R.color.manhwa_color));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.cardMangaType.setCardBackgroundColor(context.getResources().getColor(R.color.manhua_color));
            holder.itemListBinding.textMangaType.setText(context.getResources().getString(R.string.manhua_string));
        }
        holder.itemListBinding.textFirstMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(0).getChapterTitle());
        holder.itemListBinding.textFirstMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(0).getChapterReleaseTime());
        holder.itemListBinding.cardFirstMangaChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(0).getChapterURL());
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                context.startActivity(intent);
            }
        });
        holder.itemListBinding.textSecondMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(1).getChapterTitle());
        holder.itemListBinding.textSecondMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(1).getChapterReleaseTime());
        holder.itemListBinding.cardSecondMangaChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(1).getChapterURL());
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                context.startActivity(intent);
            }
        });
        holder.itemListBinding.textThirdMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(2).getChapterTitle());
        holder.itemListBinding.textThirdMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(2).getChapterReleaseTime());
        holder.itemListBinding.cardThirdMangaChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().subList(position * 3, position * 3 + 3).get(2).getChapterURL());
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeNewReleaseResultModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListMangaBinding itemListBinding;

        public ViewHolder(final ItemListMangaBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }

    }
}
