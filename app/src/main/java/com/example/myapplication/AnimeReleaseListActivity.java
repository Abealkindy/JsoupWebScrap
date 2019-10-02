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
    private List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList = new ArrayList<>();
    private AnimeRecyclerNewReleasesAdapter animeRecyclerNewReleasesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime_release_list);
        getNewReleasesAnime(pageCount++, "newPage");
        animeReleaseListBinding.recyclerNewReleases.setHasFixedSize(true);
        animeRecyclerNewReleasesAdapter = new AnimeRecyclerNewReleasesAdapter(AnimeReleaseListActivity.this, animeNewReleaseResultModelList);
        animeReleaseListBinding.recyclerNewReleases.setAdapter(animeRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        animeReleaseListBinding.recyclerNewReleases.setLayoutManager(linearLayoutManager);
        animeReleaseListBinding.recyclerNewReleases.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesAnime(pageCount++, "newPage");
            }
        });
        animeReleaseListBinding.swipeRefreshAnimeList.setOnRefreshListener(() -> {
            animeReleaseListBinding.swipeRefreshAnimeList.setRefreshing(false);
            getNewReleasesAnime(1, "swipeRefresh");
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AnimeReleaseListActivity.this, MainActivity.class));
        finish();

    }

    private void getNewReleasesAnime(int pageCount, String hitStatus) {
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
                        if (hitStatus.equalsIgnoreCase("newPage")) {
                            animeNewReleaseResultModelList.addAll(parseResult(result));
                            animeRecyclerNewReleasesAdapter.notifyDataSetChanged();
                            Log.e("result", result);
                        } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                            if (animeNewReleaseResultModelList != null) {
                                animeNewReleaseResultModelList.clear();
                            }
                            animeNewReleaseResultModelList.addAll(parseResult(result));
                            animeRecyclerNewReleasesAdapter.notifyDataSetChanged();
                            Log.e("result", result);
                        }
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

    private List<AnimeNewReleaseResultModel> parseResult(String result) {
        Document doc = Jsoup.parse(result);
        Elements newepisodecon = doc.select("a[href~=episode|movie|ova|ona]");
        List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList = new ArrayList<>();

        for (Element el : newepisodecon) {
            String animeThumbnailBackground = el.getElementsByTag("img").attr("src");
            String animeEpisode = el.getElementsByTag("h4").text();
            String animeEpisodeNumber = el.getElementsByClass("newepisodefloat right bgwhitetr").text();
            String animeEpisodeType = el.getElementsByAttributeValueContaining("class", "newepisodefloat left").text();
            String animeEpisodeStatus = el.getElementsByClass("hoverother").text();
            String epsiodeURL = el.absUrl("href");
            AnimeNewReleaseResultModel animeNewReleaseResultModel = new AnimeNewReleaseResultModel();
            animeNewReleaseResultModel.setAnimeEpisode(animeEpisode);
            animeNewReleaseResultModel.setEpisodeThumb(animeThumbnailBackground);
            animeNewReleaseResultModel.setEpisodeURL(epsiodeURL);
            animeNewReleaseResultModel.setAnimeEpisodeNumber(animeEpisodeNumber);
            animeNewReleaseResultModel.setAnimeEpisodeType(animeEpisodeType);
            animeNewReleaseResultModel.setAnimeEpisodeStatus(animeEpisodeStatus);
            animeNewReleaseResultModelList.add(animeNewReleaseResultModel);
        }
        List<AnimeNewReleaseResultModel> animeNewReleaseResultModelListAfterCut = new ArrayList<>(animeNewReleaseResultModelList.subList(6, animeNewReleaseResultModelList.size() - 1));
        Log.e("resultBeforeCut", new Gson().toJson(animeNewReleaseResultModelList));
        Log.e("resultAfterCut", new Gson().toJson(animeNewReleaseResultModelListAfterCut));
        return animeNewReleaseResultModelListAfterCut;
    }
}
