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
        ViewHolder viewHolder;
        ItemListMangaBinding itemListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_manga, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textTitleManga.setText(animeNewReleaseResultModelList.get(position).getMangaTitle());
        Picasso.get().load(animeNewReleaseResultModelList.get(position).getMangaThumb()).placeholder(context.getResources().getDrawable(R.drawable.imageplaceholder)).into(holder.itemListBinding.imageViewBackgroundManga);
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
        if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() == 0
                && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().size() == 0
                && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().size() == 0) {
            holder.itemListBinding.cardFirstMangaChapter.setVisibility(View.GONE);
            holder.itemListBinding.cardSecondMangaChapter.setVisibility(View.GONE);
            holder.itemListBinding.cardThirdMangaChapter.setVisibility(View.GONE);
        } else if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() == 1
                && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().size() == 1
                && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().size() == 1) {
            holder.itemListBinding.cardFirstMangaChapter.setVisibility(View.VISIBLE);
            holder.itemListBinding.textFirstMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
            holder.itemListBinding.textFirstMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(0));
            holder.itemListBinding.cardFirstMangaChapter.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(0));
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
                context.startActivity(intent);
            });
            holder.itemListBinding.cardSecondMangaChapter.setVisibility(View.GONE);
            holder.itemListBinding.cardThirdMangaChapter.setVisibility(View.GONE);
        } else if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() == 2
                && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().size() == 2
                && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().size() == 2) {
            holder.itemListBinding.cardFirstMangaChapter.setVisibility(View.VISIBLE);
            holder.itemListBinding.textFirstMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
            holder.itemListBinding.textFirstMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(0));
            holder.itemListBinding.cardFirstMangaChapter.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(0));
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
                context.startActivity(intent);
            });
            holder.itemListBinding.cardSecondMangaChapter.setVisibility(View.VISIBLE);
            holder.itemListBinding.textSecondMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1));
            holder.itemListBinding.textSecondMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(1));
            holder.itemListBinding.cardSecondMangaChapter.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(1));
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1));
                context.startActivity(intent);
            });
            holder.itemListBinding.cardThirdMangaChapter.setVisibility(View.GONE);
        } else {
            holder.itemListBinding.cardFirstMangaChapter.setVisibility(View.VISIBLE);
            holder.itemListBinding.textFirstMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
            holder.itemListBinding.textFirstMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(0));
            holder.itemListBinding.cardFirstMangaChapter.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(0));
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
                context.startActivity(intent);
            });
            holder.itemListBinding.cardSecondMangaChapter.setVisibility(View.VISIBLE);
            holder.itemListBinding.textSecondMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1));
            holder.itemListBinding.textSecondMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(1));
            holder.itemListBinding.cardSecondMangaChapter.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(1));
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1));
                context.startActivity(intent);
            });
            holder.itemListBinding.cardThirdMangaChapter.setVisibility(View.VISIBLE);
            holder.itemListBinding.textThirdMangaChapter.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(2));
            holder.itemListBinding.textThirdMangaChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(2));
            holder.itemListBinding.cardThirdMangaChapter.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(2));
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(2));
                context.startActivity(intent);
            });
        }
    }


    @Override
    public int getItemCount() {
        return animeNewReleaseResultModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListMangaBinding itemListBinding;

        public ViewHolder(final ItemListMangaBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
