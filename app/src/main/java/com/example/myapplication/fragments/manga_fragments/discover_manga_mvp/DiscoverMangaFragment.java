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
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerDiscoverAdapterNew;
import com.example.myapplication.databinding.FragmentDiscoverMangaBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverMangaFragment extends Fragment implements SearchView.OnQueryTextListener, DiscoverMangaInterface {
    private FragmentDiscoverMangaBinding discoverMangaBinding;
    private int pageCount = 1, plusPage = 1, plusSearch = 1;
    private static final int NEW_PAGE = 0,
            NEW_PAGE_SCROLL = 1,
            SWIPE_REFRESH = 2,
            SEARCH_REQUEST = 3,
            SEARCH_SWIPE_REQUEST = 4;
    private List<DiscoverMangaModel> discoverMangaFragmentList = new ArrayList<>();
    private MangaRecyclerDiscoverAdapterNew mangaRecyclerDiscoverAdapter;
    private DiscoverMangaPresenter discoverMangaPresenter = new DiscoverMangaPresenter(this);
    private ProgressDialog progressDialog;
    private Context mContext;
    private String hitStatus = "newPage", homeUrl = "/daftar-komik/page/" + pageCount + "/", searchQuery = "";

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
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        discoverMangaBinding = FragmentDiscoverMangaBinding.inflate(inflater, container, false);
        initUI();
        return discoverMangaBinding.getRoot();
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
                    } else {
                        setTag(searchQuery, SEARCH_SWIPE_REQUEST);
                    }
                }
            }
        });
    }

    private void setTag(String searchQuery, int option) {
        discoverMangaFragmentList = new ArrayList<>();
        Log.e("option", String.valueOf(option));
        switch (option) {
            case NEW_PAGE_SCROLL:
                plusPage++;
                homeUrl = "/daftar-komik/page/" + plusPage + "/";
                hitStatus = "newPage";
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
                break;
            case NEW_PAGE:
            case SWIPE_REFRESH:
                plusPage = 1;
                homeUrl = "/daftar-komik/page/" + 1 + "/";
                hitStatus = "swipeRefresh";
                break;
            case SEARCH_REQUEST:
                plusSearch = 1;
                homeUrl = "/page/" + 1 + "/?s=" + searchQuery + "/";
                hitStatus = "searchRequest";
                break;
            case SEARCH_SWIPE_REQUEST:
                plusSearch++;
                homeUrl = "/page/" + plusSearch + "/?s=" + searchQuery + "/";
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
        String totalURL = "https://komikcast.com" + homeUrl;
        discoverMangaPresenter.getDiscoverOrSearchData(totalURL);
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
        getActivity().runOnUiThread(() -> {
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
        getActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            discoverMangaBinding.recyclerDiscoverManga.setVisibility(View.GONE);
            Glide.with(mContext).asGif().load(R.raw.aquacry).into(discoverMangaBinding.imageError);
            discoverMangaBinding.linearError.setVisibility(View.VISIBLE);
        });
    }
}
