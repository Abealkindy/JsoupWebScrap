package com.example.myapplication.fragments.anime_fragments.anime_bookmark_page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.myapplication.MyApp.animeLocalDB;

import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerBookmarkAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBookmarkBinding.inflate(inflater, container, false);
        getDataFromLocalDB();
        return mBinding.getRoot();
    }

    private void getDataFromLocalDB() {
        if (animeLocalDB.animeBookmarkDAO().getAnimeBookmarkData() != null) {
            mBinding.recylerAnimeBookmark.setAdapter(new AnimeRecyclerBookmarkAdapter(getActivity(), animeLocalDB.animeBookmarkDAO().getAnimeBookmarkData()));
            mBinding.recylerAnimeBookmark.setHasFixedSize(true);
        }
    }
}
