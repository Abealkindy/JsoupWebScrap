package com.example.myapplication.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    FragmentDiscoverMangaBinding discoverMangaBinding;
    private int pageCount = 1;
    public static final int NEW_PAGE = 1;
    public static final int SWIPE_REFRESH = 2;
    public static final int SEARCH_REQUEST = 3;
    private List<DiscoverMangaModel> discoverMangaFragmentList = new ArrayList<>();
    private MangaRecyclerDiscoverAdapter mangaRecyclerDiscoverAdapter;
    ProgressDialog progressDialog;
    String homeUrl = "/daftar-komik/page/";

    public DiscoverMangaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDiscoverMangaData(pageCount++, "newPage");
        discoverMangaBinding.swipeDiscoverManga.setOnRefreshListener(() -> {
            discoverMangaBinding.swipeDiscoverManga.setRefreshing(false);
            setTag(homeUrl, NEW_PAGE);
            getDiscoverMangaData(1, "swipeRefresh");
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });
    }

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
                getDiscoverMangaData(pageCount++, "newPage");
            }
        });
        return discoverMangaBinding.getRoot();
    }

    public void setTag(String searchQuery, int option) {
        pageCount = 1;
        discoverMangaFragmentList = new ArrayList<>();
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE:
                homeUrl = "/daftar-komik/page/" + pageCount;
                break;
            case SEARCH_REQUEST:
                homeUrl = "/page/" + pageCount + "/?s=" + searchQuery;
                getDiscoverMangaData(pageCount, "searchRequest");
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

    private void getDiscoverMangaData(int pageCount, String hitStatus) {
        progressDialog.show();
        if (hitStatus.equalsIgnoreCase("swipeRefresh") || hitStatus.equalsIgnoreCase("newPage")) {
            ApiEndPointService apiEndPointService = RetrofitConfig.getInitMangaRetrofit();
            apiEndPointService.getNewReleaseMangaData(homeUrl + pageCount)
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
                                discoverMangaFragmentList.addAll(parseResult(result));
                                mangaRecyclerDiscoverAdapter.notifyDataSetChanged();
                            } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                                progressDialog.dismiss();
                                if (discoverMangaFragmentList != null) {
                                    discoverMangaFragmentList.clear();
                                }
                                discoverMangaFragmentList.addAll(parseResult(result));
                                mangaRecyclerDiscoverAdapter.notifyDataSetChanged();
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
        } else if (hitStatus.equalsIgnoreCase("searchRequest")) {
            ApiEndPointService apiEndPointService = RetrofitConfig.getInitMangaRetrofit();
            apiEndPointService.getNewReleaseMangaData(homeUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String result) {
                            progressDialog.dismiss();
                            if (discoverMangaFragmentList != null) {
                                discoverMangaFragmentList.clear();
                            }
                            discoverMangaFragmentList.addAll(parseResult(result));
                            mangaRecyclerDiscoverAdapter.notifyDataSetChanged();

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
        } else {
            progressDialog.dismiss();
        }
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
            DiscoverMangaModel mangaNewReleaseResultModel = new DiscoverMangaModel();
            mangaNewReleaseResultModel.setMangaType(mangaType);
            mangaNewReleaseResultModel.setMangaTitle(mangaTitle);
            mangaNewReleaseResultModel.setMangaThumb(mangaThumbnailBackground);
            mangaNewReleaseResultModel.setMangaRating(chapterRating);
            mangaNewReleaseResultModel.setMangaLatestChapter(chapterUrl);
            mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel);
        }
        Log.e("resultBeforeCut", new Gson().toJson(mangaNewReleaseResultModelList));
        return mangaNewReleaseResultModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        setTag(query, SEARCH_REQUEST);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
