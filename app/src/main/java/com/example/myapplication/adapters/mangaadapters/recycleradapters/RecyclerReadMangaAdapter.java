package com.example.myapplication.adapters.mangaadapters.recycleradapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity;
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
        ItemListMangaContentBinding itemListBinding = ItemListMangaContentBinding.inflate(layoutInflater);
        return new ViewHolder(itemListBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.imageMangaContentItem.getSettings().setJavaScriptEnabled(true);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        double x = screenWidth / displayMetrics.density;
        String imageURLModify = "<html><body style=\"margin: 0;\"><img width=\"" + x + "\" src=\"" + imageContent.get(position) + "\"></img></body></html>";
        holder.itemListBinding.imageMangaContentItem.loadData(imageURLModify, "text/html", "UTF-8");
        Log.e("imageContent", imageURLModify);
        Log.e("screenWidth", "" + x);
        holder.itemListBinding.imageMangaContentItem.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                holder.itemListBinding.imageReplacement.setVisibility(View.VISIBLE);
                holder.itemListBinding.imageMangaContentItem.setVisibility(View.INVISIBLE);
            }

            public void onPageFinished(WebView view, String url) {
                holder.itemListBinding.imageReplacement.setVisibility(View.GONE);
                holder.itemListBinding.imageMangaContentItem.setVisibility(View.VISIBLE);
            }

        });
//        holder.itemListBinding.imageMangaContentItem.loadUrl(imageContent.get(position));
//        Picasso.get()
//                .load(imageContent.get(position))
//                .placeholder(context.getResources().getDrawable(R.drawable.imageplaceholder))
//                .error(context.getResources().getDrawable(R.drawable.error))
//                .into(holder.itemListBinding.imageMangaContentItem);
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
}
