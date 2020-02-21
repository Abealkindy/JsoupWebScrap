package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerNewReleasesAdapter;
import com.example.myapplication.databinding.FragmentMangaNewReleaseBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaNewReleaseFragment extends Fragment implements MangaNewReleaseInterface {

    private FragmentMangaNewReleaseBinding newReleaseBinding;
    private int pageCount = 1;
    private Context mContext;
    private List<MangaNewReleaseResultModel> mangaNewReleaseResultModels = new ArrayList<>();
    private MangaRecyclerNewReleasesAdapter mangaRecyclerNewReleasesAdapter;
    private MangaNewReleasePresenter newReleasePresenter = new MangaNewReleasePresenter(this);
    private ProgressDialog progressDialog;

    public MangaNewReleaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newReleaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manga_new_release, container, false);
        initProgressDialog();
        getNewReleasesManga(pageCount++, "newPage");
        initRecyclerView();
        newReleaseBinding.swipeRefreshMangaList.setOnRefreshListener(() -> {
            newReleaseBinding.swipeRefreshMangaList.setRefreshing(false);
            getNewReleasesManga(1, "swipeRefresh");
        });
        return newReleaseBinding.getRoot();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
    }

    private void initRecyclerView() {
        newReleaseBinding.recyclerNewReleasesManga.setHasFixedSize(true);
        mangaRecyclerNewReleasesAdapter = new MangaRecyclerNewReleasesAdapter(getActivity(), mangaNewReleaseResultModels);
        newReleaseBinding.recyclerNewReleasesManga.setAdapter(mangaRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) newReleaseBinding.recyclerNewReleasesManga.getLayoutManager();
        newReleaseBinding.recyclerNewReleasesManga.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                getNewReleasesManga(pageCount++, "newPage");
            }
        });
    }

    private void getNewReleasesManga(int pageCount, String hitStatus) {
        progressDialog.show();
        if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
            if (this.pageCount <= 2) {
                Log.e("minusStatus", "Can't!");
            } else {
                this.pageCount--;
            }
        }
        newReleasePresenter.getNewReleasesMangaData(pageCount, "https://komikcast.com", hitStatus);
    }

    @Override
    public void onGetNewReleasesDataSuccess(List<MangaNewReleaseResultModel> mangaNewReleaseResultModel, String hitStatus) {
        getActivity().runOnUiThread(() -> {
            if (hitStatus.equalsIgnoreCase("newPage")) {
                progressDialog.dismiss();
                mangaNewReleaseResultModels.addAll(mangaNewReleaseResultModel);
                mangaRecyclerNewReleasesAdapter.notifyDataSetChanged();
            } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                progressDialog.dismiss();
                if (mangaNewReleaseResultModels != null) {
                    mangaNewReleaseResultModels.clear();
                    mangaNewReleaseResultModels.addAll(mangaNewReleaseResultModel);
                }
                mangaRecyclerNewReleasesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onGetNewReleasesDataFailed() {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new
                AlertDialog.Builder(mContext);
        builder.setTitle("Oops...");
        builder.setIcon(getResources().getDrawable(R.drawable.appicon));
        builder.setMessage("Your internet connection is worse than your face onii-chan :3");
        builder.setPositiveButton("Reload", (dialog, which) -> Toast.makeText(getActivity(), "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
        builder.show();
    }
}
