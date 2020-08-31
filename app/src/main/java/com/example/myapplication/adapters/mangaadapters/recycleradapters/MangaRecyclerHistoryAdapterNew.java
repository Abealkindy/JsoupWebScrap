package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity;
import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity;
import com.example.myapplication.databinding.ItemListAnimeNewBinding;
import com.example.myapplication.databinding.ItemListMangaNewBinding;
import com.example.myapplication.databinding.ItemListSelectChapterDetailBinding;
import com.example.myapplication.localstorages.manga_local.read_history.MangaHistoryModel;
import com.example.myapplication.localstorages.manga_local.read_history.MangaHistoryModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MangaRecyclerHistoryAdapterNew extends RecyclerView.Adapter<MangaRecyclerHistoryAdapterNew.ViewHolder> {
    private Context context;
    private List<MangaHistoryModel> animeDiscoverResultModelList;

    public MangaRecyclerHistoryAdapterNew(Context context, List<MangaHistoryModel> animeDiscoverResultModelList) {
        this.context = context;
        this.animeDiscoverResultModelList = animeDiscoverResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListSelectChapterDetailBinding itemListBinding = ItemListSelectChapterDetailBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.textViewChapterAllReleaseTime.setVisibility(View.GONE);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemListBinding.linearChapterDetail.getLayoutParams();
        layoutParams.topMargin = (int) context.getResources().getDimension(R.dimen._5sdp);
        layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen._5sdp);
        layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen._10sdp);
        layoutParams.rightMargin = (int) context.getResources().getDimension(R.dimen._10sdp);
        holder.itemListBinding.linearChapterDetail.setLayoutParams(layoutParams);
        holder.itemListBinding.textViewChapterAllTitle.setText(animeDiscoverResultModelList.get(position).getChapterTitle());
        holder.itemListBinding.linearChapterDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
            intent.putExtra("chapterURL", animeDiscoverResultModelList.get(position).getChapterURL());
            intent.putExtra("appBarColorStatus", animeDiscoverResultModelList.get(position).getChapterType());
            intent.putExtra("chapterTitle", animeDiscoverResultModelList.get(position).getChapterTitle());
            intent.putExtra("readFrom", "MangaHistory");
            context.startActivity(intent);
//            ((MangaReleaseListActivity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return animeDiscoverResultModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListSelectChapterDetailBinding itemListBinding;

        public ViewHolder(final ItemListSelectChapterDetailBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
