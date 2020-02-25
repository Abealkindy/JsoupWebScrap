package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp


import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

import com.example.myapplication.R
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerNewReleasesAdapter
import com.example.myapplication.databinding.FragmentAnimeNewReleaseBinding
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener
import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel

import java.util.ArrayList
import java.util.Objects

/**
 * A simple [Fragment] subclass.
 */
class AnimeNewReleaseFragment : Fragment(), AnimeNewReleasesInterface {

    private var pageCount = 1
    private val animeNewReleaseResultModelList = ArrayList<AnimeNewReleaseResultModel>()
    private var animeRecyclerNewReleasesAdapter: AnimeRecyclerNewReleasesAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var animeNewReleaseBinding: FragmentAnimeNewReleaseBinding? = null
    private val newReleasesPresenter = AnimeNewReleasesPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        animeNewReleaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_anime_new_release, container, false)
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Be patient please onii-chan, it just take less than a minute :3")
        getNewReleasesAnime(pageCount++, "newPage")
        animeNewReleaseBinding!!.recyclerNewReleasesAnime.setHasFixedSize(true)
        animeRecyclerNewReleasesAdapter = AnimeRecyclerNewReleasesAdapter(activity!!, animeNewReleaseResultModelList)
        animeNewReleaseBinding!!.recyclerNewReleasesAnime.adapter = animeRecyclerNewReleasesAdapter
        val linearLayoutManager = LinearLayoutManager(activity)
        animeNewReleaseBinding!!.recyclerNewReleasesAnime.layoutManager = linearLayoutManager
        animeNewReleaseBinding!!.recyclerNewReleasesAnime.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(index: Int, totalItemsCount: Int, view: RecyclerView) {
                getNewReleasesAnime(pageCount++, "newPage")
            }
        })
        animeNewReleaseBinding!!.swipeRefreshAnimeListRelease.setOnRefreshListener {
            animeNewReleaseBinding!!.swipeRefreshAnimeListRelease.isRefreshing = false
            getNewReleasesAnime(1, "swipeRefresh")
        }
        return animeNewReleaseBinding!!.root
    }

    private fun getNewReleasesAnime(pageCount: Int, hitStatus: String) {
        progressDialog!!.show()
        if (hitStatus.equals("swipeRefresh", ignoreCase = true)) {
            if (this.pageCount <= 2) {
                Log.e("minusStatus", "Can't!")
            } else {
                this.pageCount--
            }
        }
        newReleasesPresenter.getNewReleasesAnimeData(pageCount, "https://animeindo.to", hitStatus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onGetNewReleasesDataSuccess(animeNewReleases: List<AnimeNewReleaseResultModel>, hitStatus: String) {
        activity!!.runOnUiThread {
            if (hitStatus.equals("newPage", ignoreCase = true)) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            } else if (hitStatus.equals("swipeRefresh", ignoreCase = true)) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                animeNewReleaseResultModelList.clear()
            }
            animeNewReleaseResultModelList.addAll(animeNewReleases)
            animeRecyclerNewReleasesAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onGetNewReleasesDataFailed() {
        progressDialog!!.dismiss()
        val builder = AlertDialog.Builder(Objects.requireNonNull<FragmentActivity>(activity))
        builder.setTitle("Oops...")
        builder.setIcon(resources.getDrawable(R.drawable.appicon))
        builder.setMessage("Your internet connection is worse than your face onii-chan :3")
        builder.setPositiveButton("Reload") { _, _ -> Toast.makeText(activity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show() }
        builder.show()
    }
}// Required empty public constructor
