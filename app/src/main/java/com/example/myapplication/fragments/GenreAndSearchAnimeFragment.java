package com.example.myapplication.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.myapplication.adapters.RecyclerAllGenreAdapter;
import com.example.myapplication.databinding.FragmentGenreAndSearchAnimeBinding;
import com.example.myapplication.databinding.SelectChapterDialogBinding;
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
public class GenreAndSearchAnimeFragment extends Fragment implements SearchView.OnQueryTextListener, RecyclerAllGenreAdapter.ClickGenreListener {
    private FragmentGenreAndSearchAnimeBinding fragmentGenreAndSearchAnimeBinding;
    private List<AnimeGenreAndSearchResultModel.AnimeSearchResult> animeGenreAndSearchResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeGenreResultModelList = new ArrayList<>();
    private AnimeRecyclerSearchAndGenreAdapter searchAndGenreAdapter;
    private static final int NEW_PAGE = 0;
    private static final int NEW_PAGE_SCROLL = 1;
    private static final int SWIPE_REFRESH = 2;
    private static final int SEARCH_REQUEST = 3;
    private static final int SEARCH_SWIPE_REQUEST = 4;
    private ProgressDialog progressDialog;
    private String hitStatus = "newPage";
    private String homeUrl = "/genres/action" + "/page/" + 1;
    private String hitQuery = "action";
    private int plusPage = 1;
    private int plusSearch = 1;
    private Dialog dialog;

    public GenreAndSearchAnimeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentGenreAndSearchAnimeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre_and_search_anime, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        getMainContentData(hitStatus);
        getGenreData();
        setHasOptionsMenu(true);
        initEvent();
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setHasFixedSize(true);
        searchAndGenreAdapter = new AnimeRecyclerSearchAndGenreAdapter(getActivity(), animeGenreAndSearchResultModelList);
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setAdapter(searchAndGenreAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setLayoutManager(linearLayoutManager);
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                if (animeGenreAndSearchResultModelList.size() < 16) {
                    Log.e("listSize", "Can't scroll anymore");
                } else {
                    fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.scrollToPosition(0);
                    if (hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
                        setTags(hitQuery, NEW_PAGE_SCROLL);
                    } else {
                        setTags(hitQuery, SEARCH_SWIPE_REQUEST);
                    }
                }
            }
        });
        fragmentGenreAndSearchAnimeBinding.swipeRefreshSearchAndGenre.setOnRefreshListener(() -> {
            fragmentGenreAndSearchAnimeBinding.swipeRefreshSearchAndGenre.setRefreshing(false);
            setTags(homeUrl, SWIPE_REFRESH);
            getFragmentManager().beginTransaction().detach(this).attach(this).commitNow();
        });
        return fragmentGenreAndSearchAnimeBinding.getRoot();
    }

    private void setTags(String searchQuery, int option) {
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE_SCROLL:
                plusPage++;
                homeUrl = "/genres/" + searchQuery + "/page/" + plusPage;
                hitStatus = "newPage";
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case NEW_PAGE:
                plusPage = 1;
                homeUrl = "/genres/" + searchQuery + "/page/" + 1;
                hitStatus = "newPage";
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case SWIPE_REFRESH:
                plusPage = 1;
                homeUrl = "/genres/action" + "/page/" + 1;
                hitStatus = "swipeRefresh";
                break;
            case SEARCH_REQUEST:
                plusSearch = 1;
                homeUrl = "/page/" + 1 + "/?s=" + searchQuery + "&post_type=anime";
                hitStatus = "searchRequest";
                break;
            case SEARCH_SWIPE_REQUEST:
                plusSearch++;
                homeUrl = "/page/" + plusSearch + "/?s=" + searchQuery + "&post_type=anime";
                hitStatus = "searchScrollRequest";
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }

    }

    private void getMainContentData(String hitStatus) {
        this.hitStatus = hitStatus;
        progressDialog.show();
        Log.e("homeURL", homeUrl);
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getGenreAnimeData(homeUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        progressDialog.dismiss();
                        if (animeGenreAndSearchResultModelList != null) {
                            animeGenreAndSearchResultModelList.clear();
                        }
                        animeGenreAndSearchResultModelList.addAll(parseHTMLToReadableData(result));
                        searchAndGenreAdapter.recyclerRefresh();
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

    private void getGenreData() {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getSearchAnimeData("/genre-list/")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        animeGenreResultModelList.addAll(parseGenrePageToReadableData(result));
                    }

                    @Override
                    public void onError(Throwable e) {
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
            String genreURL = element.attr("href");
            String genreTitle = element.text();
            AnimeGenreAndSearchResultModel.AnimeGenreResult genreResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
            genreResult.setGenreTitle(genreTitle);
            genreResult.setGenreURL(genreURL);
            genreResultList.add(genreResult);
        }
        return genreResultList;
    }

    private List<AnimeGenreAndSearchResultModel.AnimeSearchResult> parseHTMLToReadableData(String htmlResult) {
        List<AnimeGenreAndSearchResultModel.AnimeSearchResult> animeGenreAndSearchResultModelList = new ArrayList<>();
        Document document = Jsoup.parse(htmlResult);
        Elements getListData = document.getElementsByClass("col-6 col-md-4 col-lg-3 col-wd-per5 col-xl-per5 mb40");
        for (Element element : getListData) {
            String detailURL = element.select("a[href^=https://animeindo.to/anime/]").attr("href");
            String thumbURL = element.getElementsByClass("episode-ratio background-cover").attr("style").substring(element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf("https://"), element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf(")"));
            String animeTitle = element.getElementsByTag("h4").text();
            String animeStatus = "", animeType = "";
            if (element.getElementsByClass("text-h6").eachText().size() < 2) {
                animeType = element.getElementsByClass("text-h6").eachText().get(0);
            } else {
                animeStatus = element.getElementsByClass("text-h6").eachText().get(0);
                animeType = element.getElementsByClass("text-h6").eachText().get(1);
            }

            AnimeGenreAndSearchResultModel.AnimeSearchResult searchResult = new AnimeGenreAndSearchResultModel().new AnimeSearchResult();
            searchResult.setAnimeDetailURL(detailURL);
            searchResult.setAnimeThumb(thumbURL);
            searchResult.setAnimeTitle(animeTitle);
            searchResult.setAnimeStatus(animeStatus);
            searchResult.setAnimeType(animeType);
            animeGenreAndSearchResultModelList.add(searchResult);
        }
        return animeGenreAndSearchResultModelList;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchBar));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        hitQuery = query;
        setTags(query, SEARCH_REQUEST);
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void initEvent() {
        fragmentGenreAndSearchAnimeBinding.fabSelectGenre.setOnClickListener(v -> {
            dialog = new Dialog(getActivity());
            SelectChapterDialogBinding chapterDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.select_chapter_dialog, null, false);
            dialog.setContentView(chapterDialogBinding.getRoot());
            dialog.setTitle("Select other chapter");
            chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true);
            chapterDialogBinding.recyclerAllChapters.setLayoutManager(new LinearLayoutManager(getActivity()));
            chapterDialogBinding.recyclerAllChapters.setAdapter(new RecyclerAllGenreAdapter(this, animeGenreResultModelList));
            dialog.show();
        });
    }

    @Override
    public void onItemClickGenre(int position) {
        dialog.dismiss();
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.scrollToPosition(0);
        hitQuery = animeGenreResultModelList.get(position).getGenreTitle().toLowerCase();
        setTags(animeGenreResultModelList.get(position).getGenreTitle().toLowerCase(), NEW_PAGE);
    }
}
