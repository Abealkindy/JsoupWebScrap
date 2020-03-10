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
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerSearchAndGenreAdapter;
import com.example.myapplication.adapters.animeadapters.recycleradapters.RecyclerAllGenreAdapter;
import com.example.myapplication.databinding.FragmentGenreAndSearchAnimeBinding;
import com.example.myapplication.databinding.SelectChapterDialogBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenreAndSearchAnimeFragment extends Fragment implements SearchView.OnQueryTextListener, RecyclerAllGenreAdapter.ClickGenreListener, GenreAndSearchAnimeInterface {
    private AnimeRecyclerSearchAndGenreAdapter searchAndGenreAdapter;
    private FragmentGenreAndSearchAnimeBinding fragmentGenreAndSearchAnimeBinding;
    private List<AnimeGenreAndSearchResultModel.AnimeGenreResult> animeGenreResultModelList = new ArrayList<>();
    private List<AnimeGenreAndSearchResultModel.AnimeSearchResult> animeGenreAndSearchResultModelList = new ArrayList<>();
    private GenreAndSearchAnimePresenter genreAndSearchAnimePresenter = new GenreAndSearchAnimePresenter(this);
    private ProgressDialog progressDialog;
    private Context mContext;
    private Dialog dialog;
    private String hitStatus = "newPage", homeUrl = "/anime/page/" + 1, hitQuery = "action";
    private int plusPage = 1, plusSearch = 1, plusGenre = 1;
    private static final int NEW_PAGE = 0,
            NEW_PAGE_SCROLL = 1,
            SWIPE_REFRESH = 2,
            SEARCH_REQUEST = 3,
            SEARCH_SCROLL_REQUEST = 4,
            GENRE_HIT_REQUEST = 5,
            GENRE_SCROLL_REQUEST = 6;

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
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        if (getUserVisibleHint()) {
            getMainContentData(hitStatus);
            getGenreData();
        }
        setHasOptionsMenu(true);
        initEvent();
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setHasFixedSize(true);
        searchAndGenreAdapter = new AnimeRecyclerSearchAndGenreAdapter(mContext, animeGenreAndSearchResultModelList);
        fragmentGenreAndSearchAnimeBinding.recyclerGenreAndSearchAnime.setAdapter(searchAndGenreAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
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
                    } else if (hitStatus.equalsIgnoreCase("searchScrollRequest")) {
                        setTags(hitQuery, SEARCH_SCROLL_REQUEST);
                    } else if (hitStatus.equalsIgnoreCase("genrePage")) {
                        setTags(hitQuery, GENRE_SCROLL_REQUEST);
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
        return fragmentGenreAndSearchAnimeBinding.getRoot();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            getMainContentData(hitStatus);
            getGenreData();
        }
    }

    private void setTags(String searchQuery, int option) {
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE_SCROLL:
                plusPage++;
                homeUrl = "/anime/page/" + plusPage;
                hitStatus = "newPage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case NEW_PAGE:
                plusPage = 1;
                homeUrl = "/anime/page/" + 1;
                hitStatus = "newPage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case GENRE_HIT_REQUEST:
                plusGenre = 1;
                homeUrl = "/genres/" + searchQuery + "/page/" + 1;
                hitStatus = "genrePage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case GENRE_SCROLL_REQUEST:
                plusGenre++;
                homeUrl = "/genres/" + searchQuery + "/page/" + plusGenre;
                hitStatus = "genrePage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case SWIPE_REFRESH:
                plusPage = 1;
                homeUrl = "/anime/page/" + 1;
                hitStatus = "swipeRefresh";
                break;
            case SEARCH_REQUEST:
                plusSearch = 1;
                homeUrl = "/page/" + 1 + "/?s=" + searchQuery + "&post_type=anime";
                hitStatus = "searchRequest";
                break;
            case SEARCH_SCROLL_REQUEST:
                plusSearch++;
                homeUrl = "/page/" + plusSearch + "/?s=" + searchQuery + "&post_type=anime";
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
        Log.e("homeURL", homeUrl);
        String genreAndSearchTotalURL = "https://animeindo.co" + homeUrl;
        genreAndSearchAnimePresenter.getGenreAndSearchData(genreAndSearchTotalURL);
    }

    private void getGenreData() {
        String genreTotalURL = "https://animeindo.co/genre-list/";
        genreAndSearchAnimePresenter.getOnlyGenreData(genreTotalURL);
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
            dialog = new Dialog(mContext);
            SelectChapterDialogBinding chapterDialogBinding = SelectChapterDialogBinding.inflate(LayoutInflater.from(mContext), null, false);
            dialog.setContentView(chapterDialogBinding.getRoot());
            dialog.setTitle("Select other chapter");
            chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true);
            chapterDialogBinding.recyclerAllChapters.setLayoutManager(new LinearLayoutManager(mContext));
            chapterDialogBinding.recyclerAllChapters.setAdapter(new RecyclerAllGenreAdapter(this, animeGenreResultModelList));
            dialog.show();
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
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onGetSearchAndGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeSearchResult> searchAndGenreHTMLResult) {
        getActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            if (animeGenreAndSearchResultModelList != null) {
                animeGenreAndSearchResultModelList.clear();
            }
            animeGenreAndSearchResultModelList.addAll(searchAndGenreHTMLResult);
            searchAndGenreAdapter.recyclerRefresh();
        });
    }

    @Override
    public void onGetSearchAndGenreDataFailed() {
        getActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            Toast.makeText(mContext, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onGetOnlyGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> onlyGenreHTMLResult) {
        getActivity().runOnUiThread(() -> {
            animeGenreResultModelList.clear();
            animeGenreResultModelList.addAll(onlyGenreHTMLResult);
        });
    }

    @Override
    public void onGetOnlyGenreDataFailed() {
        getActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            Toast.makeText(mContext, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
        });
    }
}
