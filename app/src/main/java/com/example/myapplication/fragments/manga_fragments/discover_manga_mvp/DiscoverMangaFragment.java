package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
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
import com.example.myapplication.adapters.RecyclerGenreAdapter;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerDiscoverAdapterNew;
import com.example.myapplication.databinding.FragmentDiscoverMangaBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverMangaFragment extends Fragment implements SearchView.OnQueryTextListener, DiscoverMangaInterface, RecyclerGenreAdapter.ClickItemListener {
    private FragmentDiscoverMangaBinding discoverMangaBinding;
    private int pageCount = 1, plusPage = 1, plusSearch = 1, plusGenre = 1;
    private static final int NEW_PAGE = 0,
            NEW_PAGE_SCROLL = 1,
            SWIPE_REFRESH = 2,
            SEARCH_REQUEST = 3,
            SEARCH_SWIPE_REQUEST = 4,
            GENRE_HIT_REQUEST = 5,
            GENRE_SCROLL_REQUEST = 6;
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeGenreResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeTypeResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeStatusResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeSortResultModelList = new ArrayList<>();
    private List<DiscoverMangaModel> discoverMangaFragmentList = new ArrayList<>();
    private MangaRecyclerDiscoverAdapterNew mangaRecyclerDiscoverAdapter;
    private DiscoverMangaPresenter discoverMangaPresenter = new DiscoverMangaPresenter(this);
    private ProgressDialog progressDialog;
    private Context mContext;
    private String hitStatus = "newPage", homeUrl = "/daftar-komik/page/" + pageCount + "/", searchQuery = "",
            genreURL = "", statusURL = "", typeURL = "", sortURL = "", totalURL = "",
            searchURL = "https://komikcast.com/daftar-komik/page/%1$s/?order=%2$s&status=%3$s&type=%4$s";
    private boolean hitGenreAPI = false;

    public DiscoverMangaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        discoverMangaBinding.swipeDiscoverManga.setOnRefreshListener(() -> {
            discoverMangaBinding.swipeDiscoverManga.setRefreshing(false);
            setTag(homeUrl, SWIPE_REFRESH);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDiscoverMangaData(hitStatus);
        if (!hitGenreAPI) {
            getFilterComponents();
        }
    }

    private void getFilterComponents() {
        String genreTotalURL = String.format(searchURL, 1, "", "", "");
        discoverMangaPresenter.getDiscoverOrSearchData(genreTotalURL, "genre");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        discoverMangaBinding = FragmentDiscoverMangaBinding.inflate(inflater, container, false);
        initUI();
        initEvent();
        return discoverMangaBinding.getRoot();
    }

    private void initEvent() {
        discoverMangaBinding.buttonSubmit.setOnClickListener(v -> {
            hitGenreAPI = true;
            totalURL = String.format(searchURL, "1", sortURL, statusURL, typeURL) + genreURL;
            setTag("", GENRE_HIT_REQUEST);
        });
        discoverMangaBinding.fabSelectGenre.setOnClickListener(v -> {
            discoverMangaBinding.relativeMainDiscManga.setVisibility(View.GONE);
            discoverMangaBinding.linearFilterDiscManga.setVisibility(View.VISIBLE);
        });
        discoverMangaBinding.imgClose.setOnClickListener(v -> {
            discoverMangaBinding.relativeMainDiscManga.setVisibility(View.VISIBLE);
            discoverMangaBinding.linearFilterDiscManga.setVisibility(View.GONE);
        });
    }

    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        setHasOptionsMenu(true);
        initRecyclerView();
    }

    private void initRecyclerView() {
        discoverMangaBinding.recyclerDiscoverManga.setHasFixedSize(true);
        mangaRecyclerDiscoverAdapter = new MangaRecyclerDiscoverAdapterNew(getActivity(), discoverMangaFragmentList);
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
                    } else if (hitStatus.equalsIgnoreCase("searchRequest")) {
                        setTag(searchQuery, SEARCH_SWIPE_REQUEST);
                    } else {
                        setTag("", GENRE_SCROLL_REQUEST);
                    }
                }
            }
        });
        //recycler genre setting
        discoverMangaBinding.recyclerGenreDiscManga.setAdapter(new RecyclerGenreAdapter(this, animeGenreResultModelList, "genre"));
        //recycler status setting
        discoverMangaBinding.recyclerStatusDiscManga.setAdapter(new RecyclerGenreAdapter(this, animeStatusResultModelList, "status"));
        //recycler type setting
        discoverMangaBinding.recyclerTypeDiscManga.setAdapter(new RecyclerGenreAdapter(this, animeTypeResultModelList, "type"));
        //recycler sort setting
        discoverMangaBinding.recyclerSortDiscManga.setAdapter(new RecyclerGenreAdapter(this, animeSortResultModelList, "sort"));
    }

    private void setTag(String searchQuery, int option) {
        discoverMangaFragmentList = new ArrayList<>();
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE_SCROLL:
                plusPage++;
                homeUrl = String.format(searchURL, plusPage, "", "", "");
                hitStatus = "newPage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case NEW_PAGE:
                plusPage = 1;
                homeUrl = String.format(searchURL, plusPage, "", "", "");
                hitStatus = "newPage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case GENRE_HIT_REQUEST:
                plusGenre = 1;
                homeUrl = totalURL;
                hitStatus = "genrePage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case GENRE_SCROLL_REQUEST:
                plusGenre++;
                homeUrl = String.format(searchURL, plusGenre, sortURL, statusURL, typeURL) + genreURL;
                hitStatus = "genrePage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case SWIPE_REFRESH:
                plusPage = 1;
                homeUrl = String.format(searchURL, plusPage, "", "", "");
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
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
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
        String totalURL = "";
        if (hitStatus.equalsIgnoreCase("genrePage") || hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
            totalURL = homeUrl;
            if (!totalURL.contains("http")) {
                totalURL = String.format(searchURL, plusPage, "", "", "");
            }
        } else {
            totalURL = "https://komikcast.com" + homeUrl;
        }
        Log.e("total URL ", totalURL);
        discoverMangaPresenter.getDiscoverOrSearchData(totalURL, "other");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query;
        setTag(query, SEARCH_REQUEST);
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onGetDiscoverMangaDataSuccess(List<DiscoverMangaModel> discoverMangaResultList) {
        requireActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            discoverMangaBinding.recyclerDiscoverManga.setVisibility(View.VISIBLE);
            discoverMangaBinding.linearError.setVisibility(View.GONE);
            if (discoverMangaFragmentList != null) {
                discoverMangaFragmentList.clear();
                discoverMangaFragmentList.addAll(discoverMangaResultList);
            }
            mangaRecyclerDiscoverAdapter.recyclerRefresh();
        });
    }

    @Override
    public void onGetDiscoverMangaDataFailed() {
        requireActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            discoverMangaBinding.recyclerDiscoverManga.setVisibility(View.GONE);
            Glide.with(mContext).asGif().load(R.raw.aquacry).into(discoverMangaBinding.imageError);
            discoverMangaBinding.linearError.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onGetStatusDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList) {
        requireActivity().runOnUiThread(() -> {
            animeStatusResultModelList.clear();
            animeStatusResultModelList.addAll(statusResultList);
        });
    }

    @Override
    public void onGetTypeDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList) {
        requireActivity().runOnUiThread(() -> {
            animeTypeResultModelList.clear();
            animeTypeResultModelList.addAll(typeResultList);
        });
    }

    @Override
    public void onGetSortDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList) {
        requireActivity().runOnUiThread(() -> {
            animeSortResultModelList.clear();
            animeSortResultModelList.addAll(sortResultList);
        });
    }

    @Override
    public void onGetGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList) {
        requireActivity().runOnUiThread(() -> {
            animeGenreResultModelList.clear();
            animeGenreResultModelList.addAll(genreResultList);
        });
    }

    @Override
    public void onItemClickGenres(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList, String position) {
        this.animeGenreResultModelList = genreResultList;
        StringBuilder stringBuilder = new StringBuilder(genreURL);
        if (!genreURL.contains(position)) {
            genreURL = String.valueOf(stringBuilder.append(position));
        } else {
            genreURL = genreURL.replace(position, "");
        }
    }

    @Override
    public void onItemClickSort(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList, String position) {
        this.animeSortResultModelList = sortResultList;
        sortURL = position;
    }

    @Override
    public void onItemClickType(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList, String position) {
        this.animeTypeResultModelList = typeResultList;
        typeURL = position;
    }

    @Override
    public void onItemClickStatus(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList, String position) {
        this.animeStatusResultModelList = statusResultList;
        statusURL = position;
    }
}
