package com.example.myapplication.fragments.anime_fragments.anime_history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerHistoryAdapterNew;
import com.example.myapplication.databinding.FragmentAnimeBookmarkBinding;
import com.example.myapplication.localstorages.anime_local.watch_history.AnimeHistoryModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.google.gson.Gson;

import static com.example.myapplication.MyApp.localAppDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimeHistoryFragment extends Fragment {
    //        implements SearchView.OnQueryTextListener
    private FragmentAnimeBookmarkBinding mBinding;
    private Context mContext;

    public AnimeHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBookmarkBinding.inflate(inflater, container, false);
        getDataFromLocalDB();
        initUI();
        initEvent();
        return mBinding.getRoot();
    }

    private void initUI() {
//        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initEvent() {
        mBinding.swipeRefreshAnimeBookmark.setOnRefreshListener(() -> {
            mBinding.swipeRefreshAnimeBookmark.setRefreshing(false);
            getDataFromLocalDB();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromLocalDB();
    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu, menu);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchBar));
//        searchView.setOnQueryTextListener(this);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    private void getDataFromLocalDB() {
        List<AnimeHistoryModel> historyModelList;
//        if ("ordinary".equalsIgnoreCase("ordinary")) {
        historyModelList = localAppDB.animeHistoryDAO().getAnimeHistoryData();
        Log.e("read history", new Gson().toJson(historyModelList));
        if (validateList(historyModelList)) {
            showRecyclerResult(historyModelList);
        } else {
            showErrorLayout();
        }
//        }
//        else {
//            historyModelList = localAppDB.animeHistoryDAO().searchByName("%" + newText + "%");
//            if (validateList(historyModelList)) {
//                showRecyclerResult(historyModelList);
//            } else {
//                showErrorLayout("Oops, please type correctly");
//            }
//        }
    }

    private boolean validateList(List<AnimeHistoryModel> historyModelList) {
        return historyModelList != null && historyModelList.size() > 0;
    }

    private void showRecyclerResult(List<AnimeHistoryModel> historyModelList) {
        mBinding.recylerAnimeBookmark.setVisibility(View.VISIBLE);
        mBinding.linearError.setVisibility(View.GONE);
        mBinding.recylerAnimeBookmark.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        mBinding.recylerAnimeBookmark.setAdapter(new AnimeRecyclerHistoryAdapterNew(getActivity(), historyModelList));
        mBinding.recylerAnimeBookmark.setHasFixedSize(true);
    }

    private void showErrorLayout() {
        mBinding.recylerAnimeBookmark.setVisibility(View.GONE);
        Glide.with(mContext).asGif().load(R.raw.aquacry).into(mBinding.imageError);
        mBinding.textViewErrorMessage.setText("Oops, coba nonton dulu, nanti baru ada historynya di sini :D");
        mBinding.linearError.setVisibility(View.VISIBLE);
        //experimental code
//        tryPrintFromAndroid();
    }

    private void tryPrintFromAndroid() {
        new Thread(() -> {
            try {
                Socket sock = new Socket("192.168.1.222", 9100);
                PrintWriter oStream = new PrintWriter(sock.getOutputStream());
                oStream.print(mContext.getAssets().open("firstsample.pdf"));
                oStream.close();
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        getDataFromLocalDB("search", query);
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        getDataFromLocalDB("search", newText);
//        return true;
//    }
}
