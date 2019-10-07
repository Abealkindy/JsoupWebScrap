package com.example.myapplication.activities.mangapage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;
import com.example.myapplication.adapters.MangaRecyclerNewReleasesAdapter;
import com.example.myapplication.R;
import com.example.myapplication.networks.RetrofitConfig;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.databinding.ActivityMangaReleaseListBinding;
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

public class MangaReleaseListActivity extends AppCompatActivity {
    ActivityMangaReleaseListBinding mangaReleaseListBinding;

    private int pageCount = 1;
    private List<MangaNewReleaseResultModel> mangaNewReleaseResultModels = new ArrayList<>();
    private MangaRecyclerNewReleasesAdapter mangaRecyclerNewReleasesAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mangaReleaseListBinding = DataBindingUtil.setContentView(this, R.layout.activity_manga_release_list);
        progressDialog = new ProgressDialog(MangaReleaseListActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        setTitle("Read Manga/Manhua/Manhwa");
        getNewReleasesManga(pageCount++, "newPage");
        mangaReleaseListBinding.recyclerNewReleasesManga.setHasFixedSize(true);
        mangaRecyclerNewReleasesAdapter = new MangaRecyclerNewReleasesAdapter(MangaReleaseListActivity.this, mangaNewReleaseResultModels);
        mangaReleaseListBinding.recyclerNewReleasesManga.setAdapter(mangaRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mangaReleaseListBinding.recyclerNewReleasesManga.setLayoutManager(linearLayoutManager);
        mangaReleaseListBinding.recyclerNewReleasesManga.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesManga(pageCount++, "newPage");
            }
        });
        mangaReleaseListBinding.swipeRefreshMangaList.setOnRefreshListener(() -> {
            mangaReleaseListBinding.swipeRefreshMangaList.setRefreshing(false);
            getNewReleasesManga(1, "swipeRefresh");
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MangaReleaseListActivity.this, MainActivity.class));
        finish();

    }

    private void getNewReleasesManga(int pageCount, String hitStatus) {
        progressDialog.show();
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitMangaRetrofit();
        apiEndPointService.getNewReleaseMangaData("/page/" + pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        if (hitStatus.equalsIgnoreCase("newPage")) {
                            progressDialog.dismiss();
                            mangaNewReleaseResultModels.addAll(parseResult(result));
                            mangaRecyclerNewReleasesAdapter.notifyDataSetChanged();
                        } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                            progressDialog.dismiss();
                            if (mangaNewReleaseResultModels != null) {
                                mangaNewReleaseResultModels.clear();
                            }
                            mangaNewReleaseResultModels.addAll(parseResult(result));
                            mangaRecyclerNewReleasesAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(MangaReleaseListActivity.this);
                        builder.setTitle("Oops...");
                        builder.setIcon(getResources().getDrawable(R.drawable.appicon));
                        builder.setMessage("Your internet connection is worse than your face onii-chan :3");
                        builder.setPositiveButton("Reload", (dialog, which) -> Toast.makeText(MangaReleaseListActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
                        builder.show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<MangaNewReleaseResultModel> parseResult(String result) {
        Document doc = Jsoup.parse(result);
        Elements newchaptercon = doc.getElementsByClass("utao");
        Log.e("elemnts", "" + newchaptercon);
        List<MangaNewReleaseResultModel> mangaNewReleaseResultModelList = new ArrayList<>();
        for (Element el : newchaptercon) {
            String mangaType = el.getElementsByTag("ul").attr("class");
            String mangaThumbnailBackground = el.getElementsByTag("img").attr("src");
            String mangaTitle = el.getElementsByTag("h3").text();
            String url = el.getElementsByTag("a").attr("href");
            List<String> chapterReleaseTime = el.getElementsByTag("i").eachText();
            List<String> chapterUrl = el.select("a[href^=https://komikcast.com/chapter/]").eachAttr("href");
            List<String> chapterTitle = el.select("a[href^=https://komikcast.com/chapter/]").eachText();
            MangaNewReleaseResultModel mangaNewReleaseResultModel = new MangaNewReleaseResultModel();
            MangaNewReleaseResultModel.LatestMangaDetailModel mangaDetailModel = new MangaNewReleaseResultModel().new LatestMangaDetailModel();
            mangaNewReleaseResultModel.setMangaType(mangaType);
            mangaNewReleaseResultModel.setMangaTitle(mangaTitle);
            mangaNewReleaseResultModel.setMangaThumb(mangaThumbnailBackground);
            if (url.startsWith("https://komikcast.com/komik/")) {
                mangaNewReleaseResultModel.setMangaDetailURL(url);
            }
            List<MangaNewReleaseResultModel.LatestMangaDetailModel> latestMangaDetailModelList = new ArrayList<>();
            mangaDetailModel.setChapterReleaseTime(chapterReleaseTime);
            mangaDetailModel.setChapterTitle(chapterTitle);
            mangaDetailModel.setChapterURL(chapterUrl);
            latestMangaDetailModelList.add(mangaDetailModel);
            mangaNewReleaseResultModel.setLatestMangaDetail(latestMangaDetailModelList);
            Log.e("paging3", new Gson().toJson(mangaNewReleaseResultModel.getLatestMangaDetail()));
            mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel);
        }
        List<MangaNewReleaseResultModel> mangaNewReleaseResultModelListAfterCut = new ArrayList<>(mangaNewReleaseResultModelList.subList(9, mangaNewReleaseResultModelList.size()));
        Log.e("resultBeforeCut", new Gson().toJson(mangaNewReleaseResultModelList));
        Log.e("resultAfterCut", new Gson().toJson(mangaNewReleaseResultModelListAfterCut));
        return mangaNewReleaseResultModelListAfterCut;
    }
}
