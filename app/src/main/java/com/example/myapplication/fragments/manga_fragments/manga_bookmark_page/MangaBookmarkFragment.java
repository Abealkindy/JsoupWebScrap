package com.example.myapplication.fragments.manga_fragments.manga_bookmark_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerBookmarkAdapterNew;
import com.example.myapplication.databinding.FragmentAnimeBookmarkBinding;

import static com.example.myapplication.MyApp.localAppDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaBookmarkFragment extends Fragment {
    private FragmentAnimeBookmarkBinding mBinding;

    public MangaBookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBookmarkBinding.inflate(inflater, container, false);
        getDataFromLocalDB();
        initEvent();
        return mBinding.getRoot();
    }

    private void initEvent() {
        mBinding.swipeRefreshAnimeBookmark.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshAnimeBookmark.setRefreshing(false);
                getDataFromLocalDB();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromLocalDB();
    }

    private void getDataFromLocalDB() {
        if (localAppDB.mangaBookmarkDAO().getMangaBookmarkData() != null) {
            mBinding.recylerAnimeBookmark.setAdapter(new MangaRecyclerBookmarkAdapterNew(getActivity(), localAppDB.mangaBookmarkDAO().getMangaBookmarkData()));
            mBinding.recylerAnimeBookmark.setHasFixedSize(true);
        }
    }
}
