package com.example.myapplication.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AnimeRecyclerSearchAndGenreAdapter;
import com.example.myapplication.adapters.MangaRecyclerDiscoverAdapter;
import com.example.myapplication.databinding.FragmentGenreAndSearchAnimeBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;

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
public class GenreAndSearchAnimeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentGenreAndSearchAnimeBinding fragmentGenreAndSearchAnimeBinding;
    private ProgressDialog progressDialog;
    private int contentByGenreCount = 1, contentBySearchCount = 1;
    private String searchQuery;
    private String hitStatus, genreQuery;
    private AnimeGenreAndSearchResultModel animeGenreAndSearchResultModel = new AnimeGenreAndSearchResultModel();
    private ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
    private AnimeRecyclerSearchAndGenreAdapter searchAndGenreAdapter;

    public GenreAndSearchAnimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentGenreAndSearchAnimeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre_and_search_anime, container, false);
        initUI();
        initEvent();
        getGenreData();
        hitStatus = "genreList";
        genreQuery = "action";
        getMainContentData(hitStatus, genreQuery);
        return fragmentGenreAndSearchAnimeBinding.getRoot();
    }

    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        setHasOptionsMenu(true);
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setHasFixedSize(true);
        if (hitStatus.equalsIgnoreCase("genreList")) {
            searchAndGenreAdapter = new AnimeRecyclerSearchAndGenreAdapter(getActivity(), animeGenreAndSearchResultModel.getAnimeSearchResults());
            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setAdapter(searchAndGenreAdapter);
        } else if (hitStatus.equalsIgnoreCase("searchList")) {
            searchAndGenreAdapter = new AnimeRecyclerSearchAndGenreAdapter(getActivity(), animeGenreAndSearchResultModel.getAnimeSearchResults());
            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setAdapter(searchAndGenreAdapter);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setLayoutManager(linearLayoutManager);
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                if (hitStatus.equalsIgnoreCase("genreList")) {
                    contentByGenreCount++;
                    getMainContentData(hitStatus, genreQuery);
                } else if (hitStatus.equalsIgnoreCase("searchList")) {
                    if (animeGenreAndSearchResultModel.getAnimeGenreResults().size() <= 16) {
                        Log.e("listSize", "Can't scroll anymore");
                    } else {
                        contentBySearchCount++;
                        getMainContentData(hitStatus, searchQuery);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchBar));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getMainContentData(String hitStatus, String hitQuery) {
        if (hitStatus.equalsIgnoreCase("genreList")) {
            apiEndPointService.getSearchAnimeData("/genres/" + hitQuery + "/page/" + contentByGenreCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String result) {
                            progressDialog.dismiss();
                            searchAndGenreAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            apiEndPointService.getSearchAnimeData("/page/" + contentBySearchCount + "/?cat=s&s=" + hitQuery + "&post_type=anime")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String result) {
                            progressDialog.dismiss();
                            searchAndGenreAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    private void getGenreData() {
        apiEndPointService.getSearchAnimeData("/genre-list/")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        progressDialog.dismiss();
                        animeGenreAndSearchResultModel.setAnimeGenreResults(parseGenrePageToReadableData(result));
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> parseGenrePageToReadableData(String result) {
        List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList = new ArrayList<>();
        Document document = Jsoup.parse(result);
        Elements getGenreData = document.select("a[href^=https://animeindo.to/genres/]");
        for (Element element : getGenreData) {
            String genreTitle = element.attr("href");
            String genreURL = element.text();
            AnimeGenreAndSearchResultModel.AnimeGenreResult genreResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
            genreResult.setGenreTitle(genreTitle);
            genreResult.setGenreURL(genreURL);
            genreResultList.add(genreResult);
        }
        return genreResultList;
    }

    private void initEvent() {
        fragmentGenreAndSearchAnimeBinding.fabSelectGenre.setOnClickListener(v -> {

        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query;
        hitStatus = "searchList";
        contentBySearchCount = 1;
        getMainContentData("searchList", searchQuery);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
