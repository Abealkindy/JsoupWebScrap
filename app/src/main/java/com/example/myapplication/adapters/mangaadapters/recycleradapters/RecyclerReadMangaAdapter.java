package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListMangaContentBinding;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RecyclerReadMangaAdapter extends RecyclerView.Adapter<RecyclerReadMangaAdapter.ViewHolder> {
    private Context context;
    private List<String> imageContent;
    private ClickItemListener clickListener;

    public RecyclerReadMangaAdapter(Context context, List<String> imageContent) {
        this.context = context;
        this.imageContent = imageContent;
        if (context instanceof RecyclerAllChapterAdapter.ClickListener) {
            clickListener = (ClickItemListener) context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemListMangaContentBinding itemListBinding = ItemListMangaContentBinding.inflate(layoutInflater);
        return new ViewHolder(itemListBinding);
    }

    @SneakyThrows
    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    final Request authorized = original.newBuilder()
                            .addHeader("Cookie", CookieManager.getInstance().getCookie(imageContent.get(position)))
                            .addHeader("User-Agent", "")
                            .build();
                    return chain.proceed(authorized);
                })
                .cache(new Cache(context.getCacheDir(), 25 * 1024 * 1024))
                .build();
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .memoryCache(new LruCache(context))
                .build();
        picasso.load(imageContent.get(position))
                .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                .into(holder.itemListBinding.imageMangaContentItem);
        holder.itemListBinding.imageMangaContentItem.setOnClickListener(v -> {
            clickListener.onItemClickMangaContent();
        });
    }

    @Override
    public int getItemCount() {
        return imageContent.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListMangaContentBinding itemListBinding;

        public ViewHolder(final ItemListMangaContentBinding itemView) {
            super(itemView.getRoot());
            this.itemListBinding = itemView;
        }
    }

    public interface ClickItemListener {
        void onItemClickMangaContent();
    }
}
