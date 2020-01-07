package com.example.myapplication.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.MangaRecyclerNewReleasesAdapter;
import com.example.myapplication.databinding.FragmentMangaNewReleaseBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;
import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaNewReleaseFragment extends Fragment {

    private FragmentMangaNewReleaseBinding newReleaseBinding;
    private int pageCount = 1;
    private List<MangaNewReleaseResultModel> mangaNewReleaseResultModels = new ArrayList<>();
    private MangaRecyclerNewReleasesAdapter mangaRecyclerNewReleasesAdapter;
    private ProgressDialog progressDialog;

    public MangaNewReleaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newReleaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manga_new_release, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        getNewReleasesManga(pageCount++, "newPage");
        newReleaseBinding.recyclerNewReleasesManga.setHasFixedSize(true);
        mangaRecyclerNewReleasesAdapter = new MangaRecyclerNewReleasesAdapter(getActivity(), mangaNewReleaseResultModels);
        newReleaseBinding.recyclerNewReleasesManga.setAdapter(mangaRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newReleaseBinding.recyclerNewReleasesManga.setLayoutManager(linearLayoutManager);
        newReleaseBinding.recyclerNewReleasesManga.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesManga(pageCount++, "newPage");
            }
        });
        newReleaseBinding.swipeRefreshMangaList.setOnRefreshListener(() -> {
            newReleaseBinding.swipeRefreshMangaList.setRefreshing(false);
            getNewReleasesManga(1, "swipeRefresh");
        });
        return newReleaseBinding.getRoot();
    }

    private void getNewReleasesManga(int pageCount, String hitStatus) {
        progressDialog.show();
        if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
            if (this.pageCount <= 2) {
                Log.e("minusStatus", "Can't!");
            } else {
                this.pageCount--;
            }
        }
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
                                AlertDialog.Builder(getActivity());
                        builder.setTitle("Oops...");
                        builder.setIcon(getResources().getDrawable(R.drawable.appicon));
                        builder.setMessage("Your internet connection is worse than your face onii-chan :3");
                        builder.setPositiveButton("Reload", (dialog, which) -> Toast.makeText(getActivity(), "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
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
        List<MangaNewReleaseResultModel> mangaNewReleaseResultModelList = new ArrayList<>();
        for (Element el : newchaptercon) {
            String mangaType = el.getElementsByTag("ul").attr("class");
            String mangaThumbnailBackground = el.getElementsByTag("img").attr("data-src");

            if (mangaThumbnailBackground.isEmpty()) {
                mangaThumbnailBackground = el.getElementsByTag("img").attr("src");
            }

            if (!mangaThumbnailBackground.contains("https")) {
                mangaThumbnailBackground = "https:" + mangaThumbnailBackground;
            } else if (!mangaThumbnailBackground.contains("http")) {
                mangaThumbnailBackground = "http:" + mangaThumbnailBackground;
            }

            String mangaTitle = el.getElementsByTag("h3").text();
            String url = el.getElementsByTag("a").attr("href");
            String mangaStatusParameter = el.getElementsByClass("hot").text();
            List<String> chapterReleaseTime = el.getElementsByTag("i").eachText();
            List<String> chapterUrl = el.select("a[href^=https://komikcast.com/chapter/]").eachAttr("href");
            List<String> chapterTitle = el.select("a[href^=https://komikcast.com/chapter/]").eachText();
            MangaNewReleaseResultModel mangaNewReleaseResultModel = new MangaNewReleaseResultModel();
            MangaNewReleaseResultModel.LatestMangaDetailModel mangaDetailModel = new MangaNewReleaseResultModel().new LatestMangaDetailModel();
            if (!mangaStatusParameter.equalsIgnoreCase("Hot") || mangaStatusParameter.isEmpty() || mangaStatusParameter == null) {
                mangaNewReleaseResultModel.setMangaStatus(false);
            } else if (mangaStatusParameter.equalsIgnoreCase("Hot")) {
                mangaNewReleaseResultModel.setMangaStatus(true);
            }
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
            mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel);
        }
        List<MangaNewReleaseResultModel> mangaNewReleaseResultModelListAfterCut = new ArrayList<>(mangaNewReleaseResultModelList.subList(9, mangaNewReleaseResultModelList.size()));
        Log.e("resultBeforeCut", new Gson().toJson(mangaNewReleaseResultModelList));
        Log.e("resultAfterCut", new Gson().toJson(mangaNewReleaseResultModelListAfterCut));
        return mangaNewReleaseResultModelListAfterCut;
    }
}
