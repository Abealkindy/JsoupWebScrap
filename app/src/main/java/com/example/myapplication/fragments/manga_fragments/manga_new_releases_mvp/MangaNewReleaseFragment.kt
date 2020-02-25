package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp


import android.app.ProgressDialog
import android.content.Context
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

import com.example.myapplication.R
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerNewReleasesAdapter
import com.example.myapplication.databinding.FragmentMangaNewReleaseBinding
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class MangaNewReleaseFragment : Fragment(), MangaNewReleaseInterface {

    private var newReleaseBinding: FragmentMangaNewReleaseBinding? = null
    private var pageCount = 1
    private var mContext: Context? = null
    private val mangaNewReleaseResultModels = ArrayList<MangaNewReleaseResultModel>()
    private var mangaRecyclerNewReleasesAdapter: MangaRecyclerNewReleasesAdapter? = null
    private val newReleasePresenter = MangaNewReleasePresenter(this)
    private var progressDialog: ProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newReleaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manga_new_release, container, false)
        initProgressDialog()
        getNewReleasesManga(pageCount++, "newPage")
        initRecyclerView()
        newReleaseBinding!!.swipeRefreshMangaList.setOnRefreshListener {
            newReleaseBinding!!.swipeRefreshMangaList.isRefreshing = false
            getNewReleasesManga(1, "swipeRefresh")
        }
        return newReleaseBinding!!.root
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Be patient please onii-chan, it just take less than a minute :3")
    }

    private fun initRecyclerView() {
        newReleaseBinding!!.recyclerNewReleasesManga.setHasFixedSize(true)
        mangaRecyclerNewReleasesAdapter = MangaRecyclerNewReleasesAdapter(activity!!, mangaNewReleaseResultModels)
        newReleaseBinding!!.recyclerNewReleasesManga.adapter = mangaRecyclerNewReleasesAdapter
        val linearLayoutManager = newReleaseBinding!!.recyclerNewReleasesManga.layoutManager as LinearLayoutManager?
        newReleaseBinding!!.recyclerNewReleasesManga.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                getNewReleasesManga(pageCount++, "newPage")
            }
        })
    }

    private fun getNewReleasesManga(pageCount: Int, hitStatus: String) {
        progressDialog!!.show()
        if (hitStatus.equals("swipeRefresh", ignoreCase = true)) {
            if (this.pageCount <= 2) {
                Log.e("minusStatus", "Can't!")
            } else {
                this.pageCount--
            }
        }
        newReleasePresenter.getNewReleasesMangaData(pageCount, "https://komikcast.com", hitStatus)
    }

    override fun onGetNewReleasesDataSuccess(mangaNewReleaseResultModels: List<MangaNewReleaseResultModel>, hitStatus: String) {
        activity!!.runOnUiThread {
            if (hitStatus.equals("newPage", ignoreCase = true)) {
                progressDialog!!.dismiss()
                this.mangaNewReleaseResultModels.addAll(mangaNewReleaseResultModels)
                mangaRecyclerNewReleasesAdapter!!.notifyDataSetChanged()
            } else if (hitStatus.equals("swipeRefresh", ignoreCase = true)) {
                progressDialog!!.dismiss()
                if (this.mangaNewReleaseResultModels.isNotEmpty()) {
                    this.mangaNewReleaseResultModels.clear()
                    this.mangaNewReleaseResultModels.addAll(mangaNewReleaseResultModels)
                }
                mangaRecyclerNewReleasesAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onGetNewReleasesDataFailed() {
        progressDialog!!.dismiss()
        val builder = AlertDialog.Builder(mContext!!)
        builder.setTitle("Oops...")
        builder.setIcon(resources.getDrawable(R.drawable.appicon))
        builder.setMessage("Your internet connection is worse than your face onii-chan :3")
        builder.setPositiveButton("Reload") { _, _ -> Toast.makeText(activity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show() }
        builder.show()
    }
}// Required empty public constructor
