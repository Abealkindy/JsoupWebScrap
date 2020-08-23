package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.MangaReleaseListActivity;
import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity;
import com.example.myapplication.databinding.ItemListMangaNewBinding;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MangaRecyclerNewReleasesAdapterNew extends RecyclerView.Adapter<MangaRecyclerNewReleasesAdapterNew.ViewHolder> {
    private Context context;
    private List<MangaNewReleaseResultModel> animeNewReleaseResultModelList;

    public MangaRecyclerNewReleasesAdapterNew(Context context, List<MangaNewReleaseResultModel> animeNewReleaseResultModelList) {
        this.context = context;
        this.animeNewReleaseResultModelList = animeNewReleaseResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListMangaNewBinding itemListBinding = ItemListMangaNewBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.mangaTitleText.setText(animeNewReleaseResultModelList.get(position).getMangaTitle());
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    final Request authorized = original.newBuilder()
                            .addHeader("Cookie", CookieManager.getInstance().getCookie(animeNewReleaseResultModelList.get(position).getMangaThumb()))
                            .addHeader("User-Agent", "")
                            .build();
                    return chain.proceed(authorized);
                })
                .build();
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
        picasso.load(animeNewReleaseResultModelList.get(position).getMangaThumb())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                .into(holder.itemListBinding.mangaThumb);
        if (!animeNewReleaseResultModelList.get(position).isMangaStatus()) {
            holder.itemListBinding.hotLabel.setVisibility(View.GONE);
        } else {
            holder.itemListBinding.hotLabel.setVisibility(View.VISIBLE);
        }
        if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manga_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.manga_string));
        } else if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhwa_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manhwa_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.manhwa_string));
        } else if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.manhua_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manhua_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.manhua_string));
        } else if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.mangaoneshot_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.mangaoneshot_string));
        } else if (animeNewReleaseResultModelList.get(position).getMangaType().equalsIgnoreCase(context.getResources().getString(R.string.oneshot_string))) {
            holder.itemListBinding.textMangaTypes.setBackgroundColor(context.getResources().getColor(R.color.manga_color));
            holder.itemListBinding.textMangaTypes.setText(context.getResources().getString(R.string.oneshot_string));
        }
        if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() > 0)
            if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() == 0
                    && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().size() == 0
                    && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().size() == 0) {
                holder.itemListBinding.linearNewest.setVisibility(View.GONE);
                holder.itemListBinding.linearSecondNewest.setVisibility(View.GONE);
                holder.itemListBinding.linearThirdNewest.setVisibility(View.GONE);
            } else if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() == 1
                    && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().size() == 1
                    && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().size() == 1) {
                holder.itemListBinding.linearNewest.setVisibility(View.VISIBLE);
                holder.itemListBinding.newestTextChapterRelease.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0).replace("Chapter", "Ch."));
                holder.itemListBinding.newestTextChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(0));
                holder.itemListBinding.linearNewest.setOnClickListener(v -> {
                    Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(0));
                    intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                    intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
                    intent.putExtra("readFrom", "MangaReleases");
                    context.startActivity(intent);
//                    ((MangaReleaseListActivity) context).finish();
                });
                holder.itemListBinding.linearSecondNewest.setVisibility(View.GONE);
                holder.itemListBinding.linearThirdNewest.setVisibility(View.GONE);
            } else if (animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().size() == 2
                    && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().size() == 2
                    && animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().size() == 2) {
                holder.itemListBinding.linearNewest.setVisibility(View.VISIBLE);
                holder.itemListBinding.newestTextChapterRelease.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0).replace("Chapter", "Ch."));
                holder.itemListBinding.newestTextChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(0));
                holder.itemListBinding.linearNewest.setOnClickListener(v -> {
                    Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(0));
                    intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                    intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
                    intent.putExtra("readFrom", "MangaReleases");
                    context.startActivity(intent);
//                    ((MangaReleaseListActivity) context).finish();
                });
                holder.itemListBinding.linearSecondNewest.setVisibility(View.VISIBLE);
                holder.itemListBinding.secondNewestTextChapterRelease.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1).replace("Chapter", "Ch."));
                holder.itemListBinding.secondNewestTextChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(1));
                holder.itemListBinding.linearSecondNewest.setOnClickListener(v -> {
                    Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(1));
                    intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                    intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1));
                    intent.putExtra("readFrom", "MangaReleases");
                    context.startActivity(intent);
//                    ((MangaReleaseListActivity) context).finish();
                });
                holder.itemListBinding.linearThirdNewest.setVisibility(View.GONE);
            } else {
                holder.itemListBinding.linearNewest.setVisibility(View.VISIBLE);
                holder.itemListBinding.newestTextChapterRelease.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0).replace("Chapter", "Ch."));
                holder.itemListBinding.newestTextChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(0));
                holder.itemListBinding.linearNewest.setOnClickListener(v -> {
                    Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(0));
                    intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                    intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(0));
                    intent.putExtra("readFrom", "MangaReleases");
                    context.startActivity(intent);
//                    ((MangaReleaseListActivity) context).finish();
                });
                holder.itemListBinding.linearSecondNewest.setVisibility(View.VISIBLE);
                holder.itemListBinding.secondNewestTextChapterRelease.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1).replace("Chapter", "Ch."));
                holder.itemListBinding.secondNewestTextChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(1));
                holder.itemListBinding.linearSecondNewest.setOnClickListener(v -> {
                    Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(1));
                    intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                    intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(1));
                    intent.putExtra("readFrom", "MangaReleases");
                    context.startActivity(intent);
//                    ((MangaReleaseListActivity) context).finish();
                });
                holder.itemListBinding.linearThirdNewest.setVisibility(View.VISIBLE);
                holder.itemListBinding.thirdNewestTextChapterRelease.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(2).replace("Chapter", "Ch."));
                holder.itemListBinding.thirdNewestTextChapterReleaseTime.setText(animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterReleaseTime().get(2));
                holder.itemListBinding.linearThirdNewest.setOnClickListener(v -> {
                    Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
                    intent.putExtra("chapterURL", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterURL().get(2));
                    intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList.get(position).getMangaType());
                    intent.putExtra("chapterTitle", animeNewReleaseResultModelList.get(position).getLatestMangaDetail().get(0).getChapterTitle().get(2));
                    intent.putExtra("readFrom", "MangaReleases");
                    context.startActivity(intent);
//                    ((MangaReleaseListActivity) context).finish();
                });
            }
    }


    @Override
    public int getItemCount() {
        return animeNewReleaseResultModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListMangaNewBinding itemListBinding;

        public ViewHolder(final ItemListMangaNewBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
