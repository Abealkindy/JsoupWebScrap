package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemListMangaContentBinding;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.myapplication.MyApp.cookiesz;
import static com.example.myapplication.MyApp.ua;

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
        loadImage(holder, position);
        holder.itemListBinding.reloadImage.setOnClickListener(v -> {
            holder.itemListBinding.imageMangaContentItem.setVisibility(View.VISIBLE);
            holder.itemListBinding.reloadImage.setVisibility(View.GONE);
            loadImage(holder, position);
        });
    }

    private void loadImage(@NonNull ViewHolder holder, int position) {
        String kuki;
        if (CookieManager.getInstance().getCookie(imageContent.get(position)) != null && !CookieManager.getInstance().getCookie(imageContent.get(position)).isEmpty()) {
            kuki = CookieManager.getInstance().getCookie(imageContent.get(position));
        } else {
            kuki = String.valueOf(cookiesz);
        }
        String finalKuki = kuki;
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    final Request authorized = original.newBuilder()
                            .addHeader("Cookie", finalKuki)
                            .addHeader("User-Agent", ua)
                            .build();
                    return chain.proceed(authorized);
                })
                .build();
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap scaledBitmap;
                if (source.getByteCount() / 1000000 >= 60) {
                    scaledBitmap = Bitmap.createScaledBitmap(
                            source,
                            (int) (source.getWidth() * 0.5),
                            (int) (source.getHeight() * 0.5),
                            true
                    );
                } else {
                    scaledBitmap = source;
                }
                if (scaledBitmap != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return scaledBitmap;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .memoryCache(Cache.NONE)
                .build();
        picasso.load(imageContent.get(position))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                .transform(transformation)
                .into(holder.itemListBinding.imageMangaContentItem, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.itemListBinding.imageMangaContentItem.setVisibility(View.VISIBLE);
                        holder.itemListBinding.reloadImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.itemListBinding.imageMangaContentItem.setVisibility(View.GONE);
                        holder.itemListBinding.reloadImage.setVisibility(View.VISIBLE);
                    }
                });
        holder.itemListBinding.imageMangaContentItem.setOnClickListener(v -> clickListener.onItemClickMangaContent());
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
