package com.example.myapplication.fragments;


import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.MangaRecyclerDiscoverAdapter;
import com.example.myapplication.databinding.FragmentDiscoverMangaBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
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
public class DiscoverMangaFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentDiscoverMangaBinding discoverMangaBinding;
    private int pageCount = 1;
    private static final int NEW_PAGE = 0;
    private static final int NEW_PAGE_SCROLL = 1;
    private static final int SWIPE_REFRESH = 2;
    private final int SEARCH_REQUEST = 3;
    private final int SEARCH_SWIPE_REQUEST = 4;
    private List<DiscoverMangaModel> discoverMangaFragmentList = new ArrayList<>();
    private MangaRecyclerDiscoverAdapter mangaRecyclerDiscoverAdapter;
    private ProgressDialog progressDialog;
    private String hitStatus = "newPage";
    private String homeUrl = "/daftar-komik/page/" + pageCount;
    private String searchQuery = "";
    private int plusPage = 1;
    private int plusSearch = 1;

    public DiscoverMangaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDiscoverMangaData(hitStatus);
        discoverMangaBinding.swipeDiscoverManga.setOnRefreshListener(() -> {
            discoverMangaBinding.swipeDiscoverManga.setRefreshing(false);
            setTag(homeUrl, SWIPE_REFRESH);
            getChildFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        discoverMangaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover_manga, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        setHasOptionsMenu(true);
        discoverMangaBinding.recyclerDiscoverManga.setHasFixedSize(true);
        mangaRecyclerDiscoverAdapter = new MangaRecyclerDiscoverAdapter(getActivity(), discoverMangaFragmentList);
        discoverMangaBinding.recyclerDiscoverManga.setAdapter(mangaRecyclerDiscoverAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        discoverMangaBinding.recyclerDiscoverManga.setLayoutManager(linearLayoutManager);
        discoverMangaBinding.recyclerDiscoverManga.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                if (discoverMangaFragmentList.size() < 30) {
                    Log.e("listSize", "Can't scroll anymore");
                } else {
                    if (hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
                        setTag("", NEW_PAGE_SCROLL);
                    } else {
                        setTag(searchQuery, SEARCH_SWIPE_REQUEST);
                    }
                }
            }
        });
        return discoverMangaBinding.getRoot();
    }

    private void setTag(String searchQuery, int option) {
        discoverMangaFragmentList = new ArrayList<>();
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE_SCROLL:
                plusPage++;
                homeUrl = "/daftar-komik/page/" + plusPage;
                hitStatus = "newPage";
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
            case NEW_PAGE:
            case SWIPE_REFRESH:
                plusPage = 1;
                homeUrl = "/daftar-komik/page/" + 1;
                hitStatus = "swipeRefresh";
                break;
            case SEARCH_REQUEST:
                plusSearch = 1;
                homeUrl = "/page/" + 1 + "/?s=" + searchQuery;
                hitStatus = "searchRequest";
                break;
            case SEARCH_SWIPE_REQUEST:
                plusSearch++;
                homeUrl = "/page/" + plusSearch + "/?s=" + searchQuery;
                hitStatus = "searchScrollRequest";
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchBar));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getDiscoverMangaData(String hitStatus) {
        progressDialog.show();
        this.hitStatus = hitStatus;
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitMangaRetrofit();
        apiEndPointService.getDiscoverMangaData(homeUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        progressDialog.dismiss();
                        discoverMangaBinding.recyclerDiscoverManga.setVisibility(View.VISIBLE);
                        discoverMangaBinding.linearError.setVisibility(View.GONE);
                        if (discoverMangaFragmentList != null || !discoverMangaFragmentList.isEmpty()) {
                            discoverMangaFragmentList.clear();
                        }
                        discoverMangaFragmentList.addAll(parseResult(result));
                        mangaRecyclerDiscoverAdapter.recyclerRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        discoverMangaBinding.recyclerDiscoverManga.setVisibility(View.GONE);
                        Glide.with(getActivity()).asGif().load(R.raw.aquacry).into(discoverMangaBinding.imageError);
                        discoverMangaBinding.linearError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<DiscoverMangaModel> parseResult(String result) {
        Document doc = Jsoup.parse(result);
        Elements newchaptercon = doc.getElementsByClass("bs");
        List<DiscoverMangaModel> mangaNewReleaseResultModelList = new ArrayList<>();
        for (Element el : newchaptercon) {
            String mangaType = el.getElementsByAttributeValueContaining("class", "type ").text();
            String mangaThumbnailBackground = el.getElementsByTag("img").attr("src");
            String mangaTitle = el.getElementsByClass("tt").text();
            String chapterRating = el.getElementsByTag("i").text();
            String chapterUrl = el.select("a[href^=https://komikcast.com/chapter/]").attr("href");
            String mangaUrl = el.select("a[href^=https://komikcast.com/komik/]").attr("href");
            String chapterText = el.select("a[href^=https://komikcast.com/chapter/]").text();
            String completedStatusParameter = el.getElementsByClass("status Completed").text();
            DiscoverMangaModel mangaNewReleaseResultModel = new DiscoverMangaModel();
            if (completedStatusParameter.isEmpty() || completedStatusParameter == null || !completedStatusParameter.equalsIgnoreCase("Completed")) {
                mangaNewReleaseResultModel.setMangaStatus(false);
            } else {
                mangaNewReleaseResultModel.setMangaStatus(true);
            }
            mangaNewReleaseResultModel.setMangaURL(mangaUrl);
            mangaNewReleaseResultModel.setMangaType(mangaType);
            mangaNewReleaseResultModel.setMangaTitle(mangaTitle);
            mangaNewReleaseResultModel.setMangaThumb(mangaThumbnailBackground);
            mangaNewReleaseResultModel.setMangaRating(chapterRating);
            mangaNewReleaseResultModel.setMangaLatestChapter(chapterUrl);
            mangaNewReleaseResultModel.setMangaLatestChapterText(chapterText);
            mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel);
        }
        Log.e("resultBeforeCutDiscover", new Gson().toJson(mangaNewReleaseResultModelList));
        return mangaNewReleaseResultModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query;
        setTag(query, SEARCH_REQUEST);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
