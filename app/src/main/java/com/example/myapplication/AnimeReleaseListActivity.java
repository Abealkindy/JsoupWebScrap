package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityAnimeReleaseListBinding;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnimeReleaseListActivity extends AppCompatActivity {
    ActivityAnimeReleaseListBinding animeReleaseListBinding;

    private int pageCount = 1;
    private List<NewReleaseResultModel> newReleaseResultModelList = new ArrayList<>();
    private RecyclerNewReleasesAdapter recyclerNewReleasesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime_release_list);
        getNewReleasesAnime(pageCount++);
        animeReleaseListBinding.recyclerNewReleases.setHasFixedSize(true);
        recyclerNewReleasesAdapter = new RecyclerNewReleasesAdapter(AnimeReleaseListActivity.this, newReleaseResultModelList);
        animeReleaseListBinding.recyclerNewReleases.setAdapter(recyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        animeReleaseListBinding.recyclerNewReleases.setLayoutManager(linearLayoutManager);
        animeReleaseListBinding.recyclerNewReleases.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesAnime(pageCount++);
            }
        });
        animeReleaseListBinding.swipeRefreshAnimeList.setOnRefreshListener(() -> {
            animeReleaseListBinding.swipeRefreshAnimeList.setRefreshing(false);
            getNewReleasesAnime(pageCount++);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AnimeReleaseListActivity.this, MainActivity.class));
        finish();

    }

    private void getNewReleasesAnime(int pageCount) {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getNewReleaseAnimeData("/page/" + pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        newReleaseResultModelList.addAll(parseResult(result));
                        recyclerNewReleasesAdapter.notifyDataSetChanged();
                        Log.e("result", result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(AnimeReleaseListActivity.this);
                        builder.setTitle("Oops...");
                        builder.setIcon(getResources().getDrawable(R.drawable.appicon));
                        builder.setMessage("Your internet connection is worse than your face onii-chan :3");
                        builder.setPositiveButton("Reload", (dialog, which) -> Toast.makeText(AnimeReleaseListActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
                        builder.show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<NewReleaseResultModel> parseResult(String result) {
        Document doc = Jsoup.parse(result);
        Elements newepisodecon = doc.select("a[href~=episode|movie|ova|ona]");
        List<NewReleaseResultModel> newReleaseResultModelList = new ArrayList<>();

        for (Element el : newepisodecon) {
            String animeThumbnailBackground = el.getElementsByTag("img").attr("src");
            String animeEpisode = el.getElementsByTag("h4").text();
            String animeEpisodeNumber = el.getElementsByClass("newepisodefloat right bgwhitetr").text();
            String animeEpisodeType = el.getElementsByAttributeValueContaining("class", "newepisodefloat left").text();
            String animeEpisodeStatus = el.getElementsByClass("hoverother").text();
            String epsiodeURL = el.absUrl("href");
            NewReleaseResultModel newReleaseResultModel = new NewReleaseResultModel();
            newReleaseResultModel.setAnimeEpisode(animeEpisode);
            newReleaseResultModel.setEpisodeThumb(animeThumbnailBackground);
            newReleaseResultModel.setEpisodeURL(epsiodeURL);
            newReleaseResultModel.setAnimeEpisodeNumber(animeEpisodeNumber);
            newReleaseResultModel.setAnimeEpisodeType(animeEpisodeType);
            newReleaseResultModel.setAnimeEpisodeStatus(animeEpisodeStatus);
            newReleaseResultModelList.add(newReleaseResultModel);
        }
        List<NewReleaseResultModel> newReleaseResultModelListAfterCut = new ArrayList<>(newReleaseResultModelList.subList(6, newReleaseResultModelList.size() - 1));
        Log.e("resultBeforeCut", new Gson().toJson(newReleaseResultModelList));
        Log.e("resultAfterCut", new Gson().toJson(newReleaseResultModelListAfterCut));
        return newReleaseResultModelListAfterCut;
    }
}
