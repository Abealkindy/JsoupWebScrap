package com.example.myapplication.fragments.manga_fragments.manga_bookmark_page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myapplication.R;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerBookmarkAdapterNew;
import com.example.myapplication.databinding.FragmentAnimeBookmarkBinding;
import com.google.gson.Gson;

import static com.example.myapplication.MyApp.localAppDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaBookmarkFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentAnimeBookmarkBinding mBinding;

    public MangaBookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBookmarkBinding.inflate(inflater, container, false);
        getDataFromLocalDB("ordinary", "");
        initUI();
        initEvent();
        return mBinding.getRoot();
    }

    private void initUI() {
        setHasOptionsMenu(true);
    }

    private void initEvent() {
        mBinding.swipeRefreshAnimeBookmark.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshAnimeBookmark.setRefreshing(false);
                getDataFromLocalDB("ordinary", "");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromLocalDB("ordinary", "");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchBar));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getDataFromLocalDB(String hitStatus, String newText) {
        if (hitStatus.equalsIgnoreCase("ordinary")) {
            if (localAppDB.mangaBookmarkDAO().getMangaBookmarkData() != null) {
                mBinding.recylerAnimeBookmark.setAdapter(new MangaRecyclerBookmarkAdapterNew(getActivity(), localAppDB.mangaBookmarkDAO().sortByNameASC()));
                mBinding.recylerAnimeBookmark.setHasFixedSize(true);
            }
        } else {
            mBinding.recylerAnimeBookmark.setAdapter(new MangaRecyclerBookmarkAdapterNew(getActivity(), localAppDB.mangaBookmarkDAO().searchByName("%" + newText + "%")));
            mBinding.recylerAnimeBookmark.setHasFixedSize(true);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getDataFromLocalDB("search", query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getDataFromLocalDB("search", newText);
        return true;
    }
}
