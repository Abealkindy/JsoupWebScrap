package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerDiscoverAdapterNew;
import com.example.myapplication.databinding.FragmentMangaNewReleaseBinding;
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MangaNewReleaseFragment extends Fragment implements MangaNewReleaseInterface {

    private FragmentMangaNewReleaseBinding newReleaseBinding;
    private int pageCount = 1;
    private Context mContext;
    private MangaRecyclerDiscoverAdapterNew mangaRecyclerNewReleasesAdapter;
    private final List<DiscoverMangaModel> mangaNewReleaseResultModels = new ArrayList<>();
    private final MangaNewReleasePresenter newReleasePresenter = new MangaNewReleasePresenter(this);
    private ProgressDialog progressDialog;
    private boolean hitAPI = false;

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
        newReleaseBinding = FragmentMangaNewReleaseBinding.inflate(inflater, container, false);
        initProgressDialog();
        initRecyclerView();
        newReleaseBinding.swipeRefreshMangaList.setOnRefreshListener(() -> {
            newReleaseBinding.swipeRefreshMangaList.setRefreshing(false);
            getNewReleasesManga(1, "swipeRefresh");
        });
        return newReleaseBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hitAPI) {
            hitAPI = true;
            getNewReleasesManga(pageCount++, "newPage");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, Void> {
        int pageCount;
        String hitStatus;

        public MyTask(int pageCount, String hitStatus) {
            this.pageCount = pageCount;
            this.hitStatus = hitStatus;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newReleasePresenter.getNewReleasesMangaData(this.pageCount, "https://komikcast.com/komik/", this.hitStatus);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
    }

    private void initRecyclerView() {
        newReleaseBinding.recyclerNewReleasesManga.setHasFixedSize(true);
        /* older version */
//        mangaRecyclerNewReleasesAdapter = new MangaRecyclerNewReleasesAdapterNew(requireActivity(), mangaNewReleaseResultModels);
        mangaRecyclerNewReleasesAdapter = new MangaRecyclerDiscoverAdapterNew(requireActivity(), mangaNewReleaseResultModels);
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
        new MyTask(pageCount, hitStatus).execute();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onGetNewReleasesDataSuccess(List<DiscoverMangaModel> mangaNewReleaseResultModel, String hitStatus, Map<String, String> cookies) {
        requireActivity().runOnUiThread(() -> {
            newReleaseBinding.recyclerNewReleasesManga.setVisibility(View.VISIBLE);
            newReleaseBinding.linearError.setVisibility(View.GONE);
            if (hitStatus.equalsIgnoreCase("newPage")) {
                progressDialog.dismiss();
                mangaNewReleaseResultModels.addAll(mangaNewReleaseResultModel);
                mangaRecyclerNewReleasesAdapter.notifyDataSetChanged();
            } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                progressDialog.dismiss();
                mangaNewReleaseResultModels.clear();
                mangaNewReleaseResultModels.addAll(mangaNewReleaseResultModel);
                mangaRecyclerNewReleasesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onGetNewReleasesDataFailed() {
        requireActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            newReleaseBinding.recyclerNewReleasesManga.setVisibility(View.GONE);
            Glide.with(mContext).asGif().load(R.raw.aquacry).into(newReleaseBinding.imageError);
            newReleaseBinding.linearError.setVisibility(View.VISIBLE);
        });
    }
}
