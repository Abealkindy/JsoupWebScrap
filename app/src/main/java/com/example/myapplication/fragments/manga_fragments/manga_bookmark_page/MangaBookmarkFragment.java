package com.example.myapplication.fragments.manga_fragments.manga_bookmark_page;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerBookmarkAdapterNew;
import com.example.myapplication.databinding.FragmentAnimeBookmarkBinding;
import com.example.myapplication.localstorages.manga_local.manga_bookmark.MangaBookmarkModel;

import java.util.List;

import static com.example.myapplication.MyApp.localAppDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaBookmarkFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentAnimeBookmarkBinding mBinding;
    private Context mContext;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initEvent() {
        mBinding.swipeRefreshAnimeBookmark.setOnRefreshListener(() -> {
            mBinding.swipeRefreshAnimeBookmark.setRefreshing(false);
            getDataFromLocalDB("ordinary", "");
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
        MenuItem menuItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getDataFromLocalDB(String hitStatus, String newText) {
        List<MangaBookmarkModel> bookmarkModelList;
        if (hitStatus.equalsIgnoreCase("ordinary")) {
            bookmarkModelList = localAppDB.mangaBookmarkDAO().getMangaBookmarkData();
            if (validateList(bookmarkModelList)) {
                showRecyclerResult(bookmarkModelList);
            } else {
                showErrorLayout("Oops, you haven't marked your favourite manga");
            }
        } else {
            bookmarkModelList = localAppDB.mangaBookmarkDAO().searchByName("%" + newText + "%");
            if (validateList(bookmarkModelList)) {
                showRecyclerResult(bookmarkModelList);
            } else {
                showErrorLayout("Oops, please type correctly");
            }
        }
    }

    private boolean validateList(List<MangaBookmarkModel> bookmarkModelList) {
        return bookmarkModelList != null && bookmarkModelList.size() > 0;
    }

    private void showRecyclerResult(List<MangaBookmarkModel> bookmarkModelList) {
        mBinding.recylerAnimeBookmark.setVisibility(View.VISIBLE);
        mBinding.linearError.setVisibility(View.GONE);
        mBinding.recylerAnimeBookmark.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.recylerAnimeBookmark.setAdapter(new MangaRecyclerBookmarkAdapterNew(requireActivity(), bookmarkModelList));
        mBinding.recylerAnimeBookmark.setHasFixedSize(true);
    }

    private void showErrorLayout(String errorMessage) {
        mBinding.recylerAnimeBookmark.setVisibility(View.GONE);
        Glide.with(mContext).asGif().load(R.raw.aquacry).into(mBinding.imageError);
        mBinding.textViewErrorMessage.setText(errorMessage);
        mBinding.linearError.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getDataFromLocalDB("search", query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            getDataFromLocalDB("search", newText);
        }
        return true;
    }
}
