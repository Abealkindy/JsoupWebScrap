package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerNewReleasesAdapter;
import com.example.myapplication.databinding.FragmentAnimeNewReleaseBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimeNewReleaseFragment extends Fragment implements AnimeNewReleasesInterface {

    private int pageCount = 1;
    private List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList = new ArrayList<>();
    private AnimeRecyclerNewReleasesAdapter animeRecyclerNewReleasesAdapter;
    private ProgressDialog progressDialog;
    private FragmentAnimeNewReleaseBinding animeNewReleaseBinding;
    private AnimeNewReleasesPresenter newReleasesPresenter = new AnimeNewReleasesPresenter(this);

    public AnimeNewReleaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        animeNewReleaseBinding = FragmentAnimeNewReleaseBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        getNewReleasesAnime(pageCount++, "newPage");
        animeNewReleaseBinding.recyclerNewReleasesAnime.setHasFixedSize(true);
        animeRecyclerNewReleasesAdapter = new AnimeRecyclerNewReleasesAdapter(getActivity(), animeNewReleaseResultModelList);
        animeNewReleaseBinding.recyclerNewReleasesAnime.setAdapter(animeRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        animeNewReleaseBinding.recyclerNewReleasesAnime.setLayoutManager(linearLayoutManager);
        animeNewReleaseBinding.recyclerNewReleasesAnime.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesAnime(pageCount++, "newPage");
            }
        });
        animeNewReleaseBinding.swipeRefreshAnimeListRelease.setOnRefreshListener(() -> {
            animeNewReleaseBinding.swipeRefreshAnimeListRelease.setRefreshing(false);
            getNewReleasesAnime(1, "swipeRefresh");
        });
        return animeNewReleaseBinding.getRoot();
    }

    private void getNewReleasesAnime(int pageCount, String hitStatus) {
        progressDialog.show();
        if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
            if (this.pageCount <= 2) {
                Log.e("minusStatus", "Can't!");
            } else {
                this.pageCount--;
            }
        }
        newReleasesPresenter.getNewReleasesAnimeData(pageCount, "https://animeindo.co", hitStatus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onGetNewReleasesDataSuccess(List<AnimeNewReleaseResultModel> animeNewReleases, String hitStatus) {
        getActivity().runOnUiThread(() -> {
            if (hitStatus.equalsIgnoreCase("newPage")) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (animeNewReleaseResultModelList != null) {
                    animeNewReleaseResultModelList.clear();
                }
            }
            animeNewReleaseResultModelList.addAll(animeNewReleases);
            animeRecyclerNewReleasesAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onGetNewReleasesDataFailed() {
        getActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle("Oops...");
            builder.setIcon(getResources().getDrawable(R.drawable.appicon));
            builder.setMessage("Your internet connection is worse than your face onii-chan :3");
            builder.setPositiveButton("Reload", (dialog, which) -> Toast.makeText(getActivity(), "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
            builder.show();
        });
    }
}
