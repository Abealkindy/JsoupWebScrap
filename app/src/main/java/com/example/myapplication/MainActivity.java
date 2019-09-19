package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observer;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private int pageCount = 1;
    private List<NewReleaseResultModel> newReleaseResultModelList = new ArrayList<>();
    private RecyclerNewReleasesAdapter recyclerNewReleasesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNewReleasesAnime(pageCount++);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.recyclerNewReleases.setHasFixedSize(true);
        recyclerNewReleasesAdapter = new RecyclerNewReleasesAdapter(MainActivity.this, newReleaseResultModelList);
        mainBinding.recyclerNewReleases.setAdapter(recyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mainBinding.recyclerNewReleases.setLayoutManager(linearLayoutManager);
        mainBinding.recyclerNewReleases.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesAnime(pageCount++);
            }
        });
    }

    private void getNewReleasesAnime(int pageCount) {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitRetrofit();
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<NewReleaseResultModel> parseResult(String result) {
        Document doc = Jsoup.parse(result);
        Elements newepisodecon = doc.select("a[href~=episode|movie]");
        List<NewReleaseResultModel> newReleaseResultModelList = new ArrayList<>();

        for (Element el : newepisodecon) {
            String animeThumbnailBackground = el.getElementsByTag("img").attr("src");
            String animeEpisode = el.getElementsByTag("h4").text();
            String epsiodeURL = el.absUrl("href");
            NewReleaseResultModel newReleaseResultModel = new NewReleaseResultModel();
            newReleaseResultModel.setAnimeEpisode(animeEpisode);
            newReleaseResultModel.setEpisodeThumb(animeThumbnailBackground);
            newReleaseResultModel.setEpisodeURL(epsiodeURL);
            newReleaseResultModelList.add(newReleaseResultModel);
        }
        List<NewReleaseResultModel> newReleaseResultModelListAfterCut = new ArrayList<>(newReleaseResultModelList.subList(6, newReleaseResultModelList.size() - 1));
        Log.e("resultBeforeCut", new Gson().toJson(newReleaseResultModelList));
        Log.e("resultAfterCut", new Gson().toJson(newReleaseResultModelListAfterCut));
        return newReleaseResultModelListAfterCut;
    }
}
