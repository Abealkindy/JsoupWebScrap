package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.adapters.mangaadapters.recycleradapters.MangaRecyclerDiscoverAdapter
import com.example.myapplication.databinding.FragmentDiscoverMangaBinding
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener
import com.example.myapplication.models.mangamodels.DiscoverMangaModel

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DiscoverMangaFragment : Fragment(), SearchView.OnQueryTextListener, DiscoverMangaInterface {
    private var discoverMangaBinding: FragmentDiscoverMangaBinding? = null
    private val pageCount = 1
    private var discoverMangaFragmentList: MutableList<DiscoverMangaModel>? = ArrayList()
    private var mangaRecyclerDiscoverAdapter: MangaRecyclerDiscoverAdapter? = null
    private val discoverMangaPresenter = DiscoverMangaPresenter(this)
    private var progressDialog: ProgressDialog? = null
    private var mContext: Context? = null
    private var hitStatus = "newPage"
    private var homeUrl = "/daftar-komik/page/$pageCount"
    private var searchQuery = ""
    private var plusPage = 1
    private var plusSearch = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDiscoverMangaData(hitStatus)
        discoverMangaBinding!!.swipeDiscoverManga.setOnRefreshListener {
            discoverMangaBinding!!.swipeDiscoverManga.isRefreshing = false
            setTag(homeUrl, SWIPE_REFRESH)
            if (fragmentManager != null) {
                fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        discoverMangaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover_manga, container, false)
        initUI()
        return discoverMangaBinding!!.root
    }

    private fun initUI() {
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Be patient please onii-chan, it just take less than a minute :3")
        setHasOptionsMenu(true)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        discoverMangaBinding!!.recyclerDiscoverManga.setHasFixedSize(true)
        mangaRecyclerDiscoverAdapter = MangaRecyclerDiscoverAdapter(activity!!, discoverMangaFragmentList!!)
        discoverMangaBinding!!.recyclerDiscoverManga.adapter = mangaRecyclerDiscoverAdapter
        val linearLayoutManager = LinearLayoutManager(activity)
        discoverMangaBinding!!.recyclerDiscoverManga.layoutManager = linearLayoutManager
        discoverMangaBinding!!.recyclerDiscoverManga.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (discoverMangaFragmentList!!.size < 30) {
                    Log.e("listSize", "Can't scroll anymore")
                } else {
                    if (hitStatus.equals("newPage", ignoreCase = true) || hitStatus.equals("swipeRefresh", ignoreCase = true)) {
                        setTag("", NEW_PAGE_SCROLL)
                    } else {
                        setTag(searchQuery, SEARCH_SWIPE_REQUEST)
                    }
                }
            }
        })
    }

    private fun setTag(searchQuery: String, option: Int) {
        discoverMangaFragmentList = ArrayList()
        Log.e("option", option.toString())
        when (option) {
            NEW_PAGE_SCROLL -> {
                plusPage++
                homeUrl = "/daftar-komik/page/$plusPage"
                hitStatus = "newPage"
                if (fragmentManager != null) {
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }
            NEW_PAGE, SWIPE_REFRESH -> {
                plusPage = 1
                homeUrl = "/daftar-komik/page/" + 1
                hitStatus = "swipeRefresh"
            }
            SEARCH_REQUEST -> {
                plusSearch = 1
                homeUrl = "/page/1/?s=$searchQuery"
                hitStatus = "searchRequest"
            }
            SEARCH_SWIPE_REQUEST -> {
                plusSearch++
                homeUrl = "/page/$plusSearch/?s=$searchQuery"
                hitStatus = "searchScrollRequest"
                if (fragmentManager != null) {
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.searchBar)) as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDiscoverMangaData(hitStatus: String) {
        progressDialog!!.show()
        this.hitStatus = hitStatus
        val totalURL = "https://komikcast.com$homeUrl"
        discoverMangaPresenter.getDiscoverOrSearchData(totalURL)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchQuery = query
        setTag(query, SEARCH_REQUEST)
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onGetDiscoverMangaDataSuccess(discoverMangaResultList: List<DiscoverMangaModel>) {
        activity!!.runOnUiThread {
            progressDialog!!.dismiss()
            discoverMangaBinding!!.recyclerDiscoverManga.visibility = View.VISIBLE
            discoverMangaBinding!!.linearError.visibility = View.GONE
            if (discoverMangaFragmentList != null) {
                discoverMangaFragmentList!!.clear()
                discoverMangaFragmentList!!.addAll(discoverMangaResultList)
            }
            mangaRecyclerDiscoverAdapter!!.recyclerRefresh()
        }
    }

    override fun onGetDiscoverMangaDataFailed() {
        progressDialog!!.dismiss()
        discoverMangaBinding!!.recyclerDiscoverManga.visibility = View.GONE
        Glide.with(mContext!!).asGif().load(R.raw.aquacry).into(discoverMangaBinding!!.imageError)
        discoverMangaBinding!!.linearError.visibility = View.VISIBLE
    }

    companion object {
        private const val NEW_PAGE = 0
        private const val NEW_PAGE_SCROLL = 1
        private const val SWIPE_REFRESH = 2
        private const val SEARCH_REQUEST = 3
        private const val SEARCH_SWIPE_REQUEST = 4
    }
}// Required empty public constructor
