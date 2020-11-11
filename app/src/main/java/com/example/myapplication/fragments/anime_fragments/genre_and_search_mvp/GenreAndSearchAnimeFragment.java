package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RecyclerGenreAdapter;
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerSearchAndGenreAdapter;
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerSearchAndGenreAdapterNew;
import com.example.myapplication.adapters.animeadapters.recycleradapters.RecyclerAllGenreAdapter;
import com.example.myapplication.databinding.FragmentGenreAndSearchAnimeBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreAndSearchAnimeFragment extends Fragment implements SearchView.OnQueryTextListener, RecyclerAllGenreAdapter.ClickGenreListener, RecyclerGenreAdapter.ClickItemListener, GenreAndSearchAnimeInterface {
    private AnimeRecyclerSearchAndGenreAdapter searchAndGenreAdapter;
    private AnimeRecyclerSearchAndGenreAdapterNew searchAndGenreAdapters;
    private FragmentGenreAndSearchAnimeBinding fragmentGenreAndSearchAnimeBinding;
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeGenreResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeTypeResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeStatusResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeSortResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeSearchResult> animeGenreAndSearchResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeSearchResultNew> animeGenreAndSearchResultModelLists = new ArrayList<>();
    private GenreAndSearchAnimePresenter genreAndSearchAnimePresenter = new GenreAndSearchAnimePresenter(this);
    private ProgressDialog progressDialog;
    private Context mContext;
    private Dialog dialog;
    public String hitStatus = "newPage", homeUrl = "/anime/page/" + 1, hitQuery = "action",
            genreURL = "", statusURL = "", typeURL = "", sortURL = "", totalURL = "", searchURL = "https://animeindo.cc/anime-list/?order%1$s&status%2$s&type%3$s", searchURLPaging = "https://animeindo.cc/anime-list/page/%1$s/?order%2$s&status%3$s&type%4$s";
    private int plusPage = 1, plusSearch = 1, plusGenre = 1;
    private static final int NEW_PAGE = 0,
            NEW_PAGE_SCROLL = 1,
            SWIPE_REFRESH = 2,
            SEARCH_REQUEST = 3,
            SEARCH_SCROLL_REQUEST = 4,
            GENRE_HIT_REQUEST = 5,
            GENRE_SCROLL_REQUEST = 6;
    private boolean hitGenreAPI = false;

    public GenreAndSearchAnimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentGenreAndSearchAnimeBinding = FragmentGenreAndSearchAnimeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        initUI();
        initEvent();
        return fragmentGenreAndSearchAnimeBinding.getRoot();
    }

    private void initUI() {
        //progress dialog setting
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        //main recycler setting
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setHasFixedSize(true);
        if (hitStatus.equalsIgnoreCase("genrePage") || hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh") || hitStatus.equalsIgnoreCase("searchRequest") || hitStatus.equalsIgnoreCase("searchScrollRequest")) {
            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
            searchAndGenreAdapters = new AnimeRecyclerSearchAndGenreAdapterNew(mContext, animeGenreAndSearchResultModelLists);
            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setAdapter(searchAndGenreAdapters);
        } else {
            searchAndGenreAdapter = new AnimeRecyclerSearchAndGenreAdapter(mContext, animeGenreAndSearchResultModelList);
            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setAdapter(searchAndGenreAdapter);
        }
        //recycler genre setting
        fragmentGenreAndSearchAnimeBinding.recyclerGenreDiscAnime.setAdapter(new RecyclerGenreAdapter(this, animeGenreResultModelList, "genre"));
        //recycler status setting
        fragmentGenreAndSearchAnimeBinding.recyclerStatusDiscAnime.setAdapter(new RecyclerGenreAdapter(this, animeStatusResultModelList, "status"));
        //recycler type setting
        fragmentGenreAndSearchAnimeBinding.recyclerTypeDiscAnime.setAdapter(new RecyclerGenreAdapter(this, animeTypeResultModelList, "type"));
        //recycler sort setting
        fragmentGenreAndSearchAnimeBinding.recyclerSortDiscAnime.setAdapter(new RecyclerGenreAdapter(this, animeSortResultModelList, "sort"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainContentData(hitStatus);
    }

    private void setTags(String searchQuery, int option) {
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE_SCROLL:
                plusPage++;
                homeUrl = String.format(searchURLPaging, plusPage, "", "", "");
                hitStatus = "newPage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case NEW_PAGE:
                plusPage = 1;
                homeUrl = String.format(searchURL, "", "", "");
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
                homeUrl = String.format(searchURLPaging, plusGenre, sortURL, statusURL, typeURL) + genreURL;
                hitStatus = "genrePage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case SWIPE_REFRESH:
                plusPage = 1;
                homeUrl = String.format(searchURL, "", "", "");
                hitStatus = "swipeRefresh";
                break;
            case SEARCH_REQUEST:
                plusSearch = 1;
                homeUrl = "/page/" + 1 + "/?s=" + searchQuery;
                hitStatus = "searchRequest";
                break;
            case SEARCH_SCROLL_REQUEST:
                plusSearch++;
                homeUrl = "/page/" + plusSearch + "/?s=" + searchQuery;
                hitStatus = "searchScrollRequest";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
        }

    }

    private void getMainContentData(String hitStatus) {
        this.hitStatus = hitStatus;
        progressDialog.show();
        String genreAndSearchTotalURL = "";
        if (hitStatus.equalsIgnoreCase("genrePage") || hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
            genreAndSearchTotalURL = homeUrl;
            if (!genreAndSearchTotalURL.contains("http")) {
                genreAndSearchTotalURL = String.format(searchURL, "", "", "");
            }
        } else {
            genreAndSearchTotalURL = "https://animeindo.cc" + homeUrl;
        }
        Log.e("homeURL", genreAndSearchTotalURL);
        genreAndSearchAnimePresenter.getGenreAndSearchData(hitStatus, genreAndSearchTotalURL);
    }

    private void getGenreData() {
        if (!hitGenreAPI) {
            hitGenreAPI = true;
            String genreTotalURL = "https://animeindo.cc/anime-list/";
            genreAndSearchAnimePresenter.getOnlyGenreData(genreTotalURL);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) menuItem.getActionView();
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
            fragmentGenreAndSearchAnimeBinding.relativeMainDiscAnime.setVisibility(View.GONE);
            fragmentGenreAndSearchAnimeBinding.linearFilterDiscAnime.setVisibility(View.VISIBLE);
        });

        fragmentGenreAndSearchAnimeBinding.imgClose.setOnClickListener(v -> {
            fragmentGenreAndSearchAnimeBinding.relativeMainDiscAnime.setVisibility(View.VISIBLE);
            fragmentGenreAndSearchAnimeBinding.linearFilterDiscAnime.setVisibility(View.GONE);
        });

        fragmentGenreAndSearchAnimeBinding.buttonSubmit.setOnClickListener(v -> {
            totalURL = String.format(searchURL, sortURL, statusURL, typeURL) + genreURL;
            setTags("", GENRE_HIT_REQUEST);
            Log.e("previewURL ", totalURL);
        });

        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.addOnScrollListener(new EndlessRecyclerViewScrollListener((GridLayoutManager) fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.getLayoutManager()) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                if (animeGenreAndSearchResultModelList.size() < 15) {
                    if (hitStatus.equalsIgnoreCase("genrePage") || hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
                        if (animeGenreAndSearchResultModelLists.size() < 30) {
                            Log.e("listSize", "Can't scroll anymore");
                        } else {
                            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.scrollToPosition(0);
                            if (hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
                                setTags(hitQuery, NEW_PAGE_SCROLL);
                            } else if (hitStatus.equalsIgnoreCase("searchRequest") || hitStatus.equalsIgnoreCase("searchScrollRequest")) {
                                setTags(hitQuery, SEARCH_SCROLL_REQUEST);
                            } else if (hitStatus.equalsIgnoreCase("genrePage")) {
                                setTags("", GENRE_SCROLL_REQUEST);
                            }
                        }
                    } else {
                        if (animeGenreAndSearchResultModelLists.size() < 15) {
                            Log.e("listSize", "Can't scroll anymore");
                        } else {
                            fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.scrollToPosition(0);
                            if (hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
                                setTags(hitQuery, NEW_PAGE_SCROLL);
                            } else if (hitStatus.equalsIgnoreCase("searchRequest") || hitStatus.equalsIgnoreCase("searchScrollRequest")) {
                                setTags(hitQuery, SEARCH_SCROLL_REQUEST);
                            } else if (hitStatus.equalsIgnoreCase("genrePage")) {
                                setTags("", GENRE_SCROLL_REQUEST);
                            }
                        }
                    }
                } else {
                    fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.scrollToPosition(0);
                    if (hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh")) {
                        setTags(hitQuery, NEW_PAGE_SCROLL);
                    } else if (hitStatus.equalsIgnoreCase("searchRequest") || hitStatus.equalsIgnoreCase("searchScrollRequest")) {
                        setTags(hitQuery, SEARCH_SCROLL_REQUEST);
                    } else if (hitStatus.equalsIgnoreCase("genrePage")) {
                        setTags("", GENRE_SCROLL_REQUEST);
                    }
                }
            }
        });

        fragmentGenreAndSearchAnimeBinding.swipeRefreshSearchAndGenre.setOnRefreshListener(() -> {
            fragmentGenreAndSearchAnimeBinding.swipeRefreshSearchAndGenre.setRefreshing(false);
            setTags(homeUrl, SWIPE_REFRESH);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(this).attach(this).commitNow();
            }
        });
    }

    @Override
    public void onItemClickGenre(int position) {
        dialog.dismiss();
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.scrollToPosition(0);
        hitQuery = animeGenreResultModelList.get(position).getGenreTitle().toLowerCase();
        setTags(animeGenreResultModelList.get(position).getGenreTitle().toLowerCase(), GENRE_HIT_REQUEST);
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
        sortURL = "=" + position;
    }

    @Override
    public void onItemClickType(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList, String position) {
        this.animeTypeResultModelList = typeResultList;
        typeURL = "=" +position;
    }

    @Override
    public void onItemClickStatus(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList, String position) {
        this.animeStatusResultModelList = statusResultList;
        statusURL = "=" +position;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onGetSearchAndGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeSearchResult> searchAndGenreHTMLResult) {
        requireActivity().runOnUiThread(() -> {
            Log.e("getResultSuccess", "Yes");
            progressDialog.dismiss();
            if (animeGenreAndSearchResultModelList != null) {
                animeGenreAndSearchResultModelList.clear();
            }
            animeGenreAndSearchResultModelList.addAll(searchAndGenreHTMLResult);
            searchAndGenreAdapter.recyclerRefresh();

            getGenreData();
        });
    }

    @Override
    public void onGetSearchAndGenreDataSuccessNew(List<AnimeGenreAndSearchResultModel.AnimeSearchResultNew> searchAndGenreHTMLResult) {
        requireActivity().runOnUiThread(() -> {
            Log.e("getResultSuccess", "Yes");
            progressDialog.dismiss();
            if (animeGenreAndSearchResultModelLists != null) {
                animeGenreAndSearchResultModelLists.clear();
            }
            animeGenreAndSearchResultModelLists.addAll(searchAndGenreHTMLResult);
            searchAndGenreAdapters.recyclerRefresh();

            getGenreData();
        });
    }

    @Override
    public void onGetSearchAndGenreDataFailed() {
        requireActivity().runOnUiThread(() -> {
            Log.e("getResultSuccess", "No");
            progressDialog.dismiss();
            Toast.makeText(mContext, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onGetOnlyGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> onlyGenreHTMLResult) {
        requireActivity().runOnUiThread(() -> {
            Log.e("getGenreSuccess", "Yes");
            animeGenreResultModelList.clear();
            animeGenreResultModelList.addAll(onlyGenreHTMLResult);
        });
    }

    @Override
    public void onGetOnlyGenreDataFailed() {
        requireActivity().runOnUiThread(() -> {
            Log.e("getGenreSuccess", "No");
            progressDialog.dismiss();
            Toast.makeText(mContext, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onGetOnlySortDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> onlyGenreHTMLResult) {
        requireActivity().runOnUiThread(() -> {
            animeSortResultModelList.clear();
            animeSortResultModelList.addAll(onlyGenreHTMLResult);
        });
    }

    @Override
    public void onGetOnlyTypeDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> onlyGenreHTMLResult) {
        requireActivity().runOnUiThread(() -> {
            animeTypeResultModelList.clear();
            animeTypeResultModelList.addAll(onlyGenreHTMLResult);
        });
    }


    @Override
    public void onGetOnlyStatusDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> onlyGenreHTMLResult) {
        requireActivity().runOnUiThread(() -> {
            animeStatusResultModelList.clear();
            animeStatusResultModelList.addAll(onlyGenreHTMLResult);
        });
    }
}
