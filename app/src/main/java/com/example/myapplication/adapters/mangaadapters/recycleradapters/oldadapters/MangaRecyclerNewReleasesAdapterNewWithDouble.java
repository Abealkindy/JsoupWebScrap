package com.example.myapplication.adapters.mangaadapters.recycleradapters.oldadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerSubChapterAdapter;
import com.example.myapplication.databinding.ItemListMangaDoubleListBinding;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;
import com.squareup.picasso.Cache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.myapplication.MyApp.cookiesz;
import static com.example.myapplication.MyApp.ua;

public class MangaRecyclerNewReleasesAdapterNewWithDouble extends RecyclerView.Adapter<MangaRecyclerNewReleasesAdapterNewWithDouble.ViewHolder> {
    private final Context context;
    private final List<MangaNewReleaseResultModel> animeNewReleaseResultModelList;

    public MangaRecyclerNewReleasesAdapterNewWithDouble(Context context, List<MangaNewReleaseResultModel> animeNewReleaseResultModelList) {
        this.context = context;
        this.animeNewReleaseResultModelList = animeNewReleaseResultModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        ItemListMangaDoubleListBinding itemListBinding = ItemListMangaDoubleListBinding.inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(itemListBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemListBinding.mangaTitleText.setText(animeNewReleaseResultModelList.get(position).getMangaTitle());
        String kuki = "";
        if (CookieManager.getInstance().getCookie(animeNewReleaseResultModelList.get(position).getMangaThumb()) != null && !CookieManager.getInstance().getCookie(animeNewReleaseResultModelList.get(position).getMangaThumb()).isEmpty()) {
            kuki = CookieManager.getInstance().getCookie(animeNewReleaseResultModelList.get(position).getMangaThumb());
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
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .memoryCache(Cache.NONE)
                .build();
        picasso.load(animeNewReleaseResultModelList.get(position).getMangaThumb())
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
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
        List<MangaNewReleaseResultModel.LatestMangaDetailModel> latestMangaDetailModels = animeNewReleaseResultModelList.get(position).getLatestMangaDetail();
        if (latestMangaDetailModels != null && latestMangaDetailModels.size() > 0) {
            for (int pos = 0; pos < latestMangaDetailModels.size(); pos++) {
                if (latestMangaDetailModels.get(pos) != null &&
                        latestMangaDetailModels.get(pos).getChapterReleaseTime() != null &&
                        latestMangaDetailModels.get(pos).getChapterReleaseTime().size() > 0 &&
                        latestMangaDetailModels.get(pos).getChapterTitle() != null &&
                        latestMangaDetailModels.get(pos).getChapterTitle().size() > 0 &&
                        latestMangaDetailModels.get(pos).getChapterURL() != null &&
                        latestMangaDetailModels.get(pos).getChapterURL().size() > 0) {
                    holder.itemListBinding.recyclerChapterInside.setHasFixedSize(true);
                    holder.itemListBinding.recyclerChapterInside.setAdapter(new MangaRecyclerSubChapterAdapter(
                            context,
                            latestMangaDetailModels.get(pos).getChapterTitle(),
                            latestMangaDetailModels.get(pos).getChapterReleaseTime(),
                            latestMangaDetailModels.get(pos).getChapterURL(),
                            animeNewReleaseResultModelList.get(position).getMangaThumb(),
                            animeNewReleaseResultModelList.get(position).getMangaType()
                    ));
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return animeNewReleaseResultModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListMangaDoubleListBinding itemListBinding;

        public ViewHolder(final ItemListMangaDoubleListBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }
}
