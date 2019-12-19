package com.example.myapplication.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

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
import com.example.myapplication.adapters.AnimeRecyclerNewReleasesAdapter;
import com.example.myapplication.databinding.FragmentAnimeNewReleaseBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel;
import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimeNewReleaseFragment extends Fragment {

    private int pageCount = 1;
    private List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList = new ArrayList<>();
    private AnimeRecyclerNewReleasesAdapter animeRecyclerNewReleasesAdapter;
    private ProgressDialog progressDialog;
    private FragmentAnimeNewReleaseBinding animeNewReleaseBinding;

    public AnimeNewReleaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        animeNewReleaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_anime_new_release, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        getNewReleasesAnime(pageCount++, "newPage");
        animeNewReleaseBinding.recyclerNewReleasesAnime.setHasFixedSize(true);
        animeRecyclerNewReleasesAdapter = new AnimeRecyclerNewReleasesAdapter(getActivity(), animeNewReleaseResultModelList);
        animeNewReleaseBinding.recyclerNewReleasesAnime.setAdapter(animeRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        animeNewReleaseBinding.recyclerNewReleasesAnime.setLayoutManager(linearLayoutManager);
        animeNewReleaseBinding.recyclerNewReleasesAnime.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesAnime(pageCount++, "newPage");
            }
        });
        animeNewReleaseBinding.swipeRefreshAnimeListRelease.setOnRefreshListener(() -> {
            animeNewReleaseBinding.swipeRefreshAnimeListRelease.setRefreshing(false);
            getNewReleasesAnime(1, "swipeRefresh");
        });
        return animeNewReleaseBinding.getRoot();
    }

    private void getNewReleasesAnime(int pageCount, String hitStatus) {
        progressDialog.show();
        if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
            if (this.pageCount <= 2) {
                Log.e("minusStatus", "Can't!");
            } else {
                this.pageCount--;
            }
        }
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
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            animeNewReleaseResultModelList.addAll(parseResult(result));
                            animeRecyclerNewReleasesAdapter.notifyDataSetChanged();
                        } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            if (animeNewReleaseResultModelList != null) {
                                animeNewReleaseResultModelList.clear();
                            }
                            animeNewReleaseResultModelList.addAll(parseResult(result));
                            animeRecyclerNewReleasesAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(Objects.requireNonNull(getActivity()));
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

    private List<AnimeNewReleaseResultModel> parseResult(String result) {
        Document doc = Jsoup.parse(result);
        Elements newepisodecon = doc.getElementsByClass("col-6 col-sm-4 col-md-3 col-xl-per5 mb40");
        List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList = new ArrayList<>();

        for (Element el : newepisodecon) {
            String animeThumbnailBackground = el.getElementsByClass("episode-ratio background-cover").attr("style");
            String thumbnailCut = animeThumbnailBackground.substring(animeThumbnailBackground.indexOf("https://"), animeThumbnailBackground.indexOf(")"));
            String animeEpisode = el.getElementsByTag("h3").text();
            String animeEpisodeNumber = el.getElementsByClass("episode-number").text();
            List<String> animeStatusAndType = el.getElementsByClass("text-h6").eachText();
            String animeEpisodeStatus = "", animeEpisodeType = "";
            if (animeStatusAndType.size() < 2) {
                animeEpisodeType = el.getElementsByClass("text-h6").eachText().get(0);
            } else {
                animeEpisodeStatus = el.getElementsByClass("text-h6").eachText().get(0);
                animeEpisodeType = el.getElementsByClass("text-h6").eachText().get(1);
            }
            String epsiodeURL = el.getElementsByTag("a").attr("href");

            AnimeNewReleaseResultModel animeNewReleaseResultModel = new AnimeNewReleaseResultModel();
            animeNewReleaseResultModel.setAnimeEpisode(animeEpisode);
            animeNewReleaseResultModel.setEpisodeThumb(thumbnailCut);
            animeNewReleaseResultModel.setEpisodeURL(epsiodeURL);
            animeNewReleaseResultModel.setAnimeEpisodeNumber(animeEpisodeNumber);
            animeNewReleaseResultModel.setAnimeEpisodeType(animeEpisodeType);
            animeNewReleaseResultModel.setAnimeEpisodeStatus(animeEpisodeStatus);
            animeNewReleaseResultModelList.add(animeNewReleaseResultModel);
        }
        List<AnimeNewReleaseResultModel> animeNewReleaseResultModelListAfterCut = new ArrayList<>(animeNewReleaseResultModelList.subList(6, animeNewReleaseResultModelList.size() - 1));
        Log.e("resultBeforeCut", new Gson().toJson(animeNewReleaseResultModelList));
        Log.e("resultAfterCut", new Gson().toJson(animeNewReleaseResultModelListAfterCut));
        return animeNewReleaseResultModelList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
