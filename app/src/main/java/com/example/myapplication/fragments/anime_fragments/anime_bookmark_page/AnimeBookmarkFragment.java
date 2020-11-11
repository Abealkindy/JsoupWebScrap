package com.example.myapplication.fragments.anime_fragments.anime_bookmark_page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.myapplication.MyApp.localAppDB;

import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerBookmarkAdapterNew;
import com.example.myapplication.databinding.FragmentAnimeBookmarkBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimeBookmarkFragment extends Fragment {
    private FragmentAnimeBookmarkBinding mBinding;

    public AnimeBookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBookmarkBinding.inflate(inflater, container, false);
        getDataFromLocalDB();
        initEvent();
//        getUserVisibleHint()
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
        if (localAppDB.animeBookmarkDAO().getAnimeBookmarkData() != null) {
            mBinding.recylerAnimeBookmark.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            mBinding.recylerAnimeBookmark.setAdapter(new AnimeRecyclerBookmarkAdapterNew(requireActivity(), localAppDB.animeBookmarkDAO().getAnimeBookmarkData()));
            mBinding.recylerAnimeBookmark.setHasFixedSize(true);
        }
    }
}
