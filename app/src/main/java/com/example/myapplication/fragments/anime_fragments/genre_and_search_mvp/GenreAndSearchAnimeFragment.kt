package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp


import android.annotation.SuppressLint
import android.app.Dialog
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
import android.widget.Toast

import com.example.myapplication.R
import com.example.myapplication.adapters.animeadapters.recycleradapters.AnimeRecyclerSearchAndGenreAdapter
import com.example.myapplication.adapters.animeadapters.recycleradapters.RecyclerAllGenreAdapter
import com.example.myapplication.databinding.FragmentGenreAndSearchAnimeBinding
import com.example.myapplication.databinding.SelectChapterDialogBinding
import com.example.myapplication.listener.EndlessRecyclerViewScrollListener
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class GenreAndSearchAnimeFragment : Fragment(), SearchView.OnQueryTextListener, RecyclerAllGenreAdapter.ClickGenreListener, GenreAndSearchAnimeInterface {
    private var searchAndGenreAdapter: AnimeRecyclerSearchAndGenreAdapter? = null
    private var fragmentGenreAndSearchAnimeBinding: FragmentGenreAndSearchAnimeBinding? = null
    private val animeGenreResultModelList = ArrayList<AnimeGenreAndSearchResultModel.AnimeGenreResult>()
    private val animeGenreAndSearchResultModelList = ArrayList<AnimeGenreAndSearchResultModel.AnimeSearchResult>()
    private val genreAndSearchAnimePresenter = GenreAndSearchAnimePresenter(this)
    private var progressDialog: ProgressDialog? = null
    private var mContext: Context? = null
    private var dialog: Dialog? = null
    private var hitStatus = "newPage"
    private var homeUrl = "/genres/action" + "/page/" + 1
    private var hitQuery = "action"
    private var plusPage = 1
    private var plusSearch = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentGenreAndSearchAnimeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre_and_search_anime, container, false)
        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Be patient please onii-chan, it just take less than a minute :3")
        getMainContentData(hitStatus)
        getGenreData()
        setHasOptionsMenu(true)
        initEvent()
        fragmentGenreAndSearchAnimeBinding!!.recyclerGenreAndSearchAnime.setHasFixedSize(true)
        searchAndGenreAdapter = AnimeRecyclerSearchAndGenreAdapter(mContext!!, animeGenreAndSearchResultModelList)
        fragmentGenreAndSearchAnimeBinding!!.recyclerGenreAndSearchAnime.adapter = searchAndGenreAdapter
        val linearLayoutManager = LinearLayoutManager(mContext)
        fragmentGenreAndSearchAnimeBinding!!.recyclerGenreAndSearchAnime.layoutManager = linearLayoutManager
        fragmentGenreAndSearchAnimeBinding!!.recyclerGenreAndSearchAnime.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (animeGenreAndSearchResultModelList.size < 16) {
                    Log.e("listSize", "Can't scroll anymore")
                } else {
                    fragmentGenreAndSearchAnimeBinding!!.recyclerGenreAndSearchAnime.scrollToPosition(0)
                    if (hitStatus.equals("newPage", ignoreCase = true) || hitStatus.equals("swipeRefresh", ignoreCase = true)) {
                        setTags(hitQuery, NEW_PAGE_SCROLL)
                    } else {
                        setTags(hitQuery, SEARCH_SWIPE_REQUEST)
                    }
                }
            }
        })
        fragmentGenreAndSearchAnimeBinding!!.swipeRefreshSearchAndGenre.setOnRefreshListener {
            fragmentGenreAndSearchAnimeBinding!!.swipeRefreshSearchAndGenre.isRefreshing = false
            setTags(homeUrl, SWIPE_REFRESH)
            if (fragmentManager != null) {
                fragmentManager!!.beginTransaction().detach(this).attach(this).commitNow()
            }
        }
        return fragmentGenreAndSearchAnimeBinding!!.root
    }

    private fun setTags(searchQuery: String, option: Int) {
        Log.e("option", option.toString())
        when (option) {
            NEW_PAGE_SCROLL -> {
                plusPage++
                homeUrl = "/genres/$searchQuery/page/$plusPage"
                hitStatus = "newPage"
                if (fragmentManager != null) {
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }
            NEW_PAGE -> {
                plusPage = 1
                homeUrl = "/genres/$searchQuery/page/1"
                hitStatus = "newPage"
                if (fragmentManager != null) {
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }
            SWIPE_REFRESH -> {
                plusPage = 1
                homeUrl = "/genres/action" + "/page/" + 1
                hitStatus = "swipeRefresh"
            }
            SEARCH_REQUEST -> {
                plusSearch = 1
                homeUrl = "/page/1/?s=$searchQuery&post_type=anime"
                hitStatus = "searchRequest"
            }
            SEARCH_SWIPE_REQUEST -> {
                plusSearch++
                homeUrl = "/page/$plusSearch/?s=$searchQuery&post_type=anime"
                hitStatus = "searchScrollRequest"
                if (fragmentManager != null) {
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }
        }

    }

    private fun getMainContentData(hitStatus: String) {
        this.hitStatus = hitStatus
        progressDialog!!.show()
        Log.e("homeURL", homeUrl)
        val genreAndSearchTotalURL = "https://animeindo.to$homeUrl"
        genreAndSearchAnimePresenter.getGenreAndSearchData(genreAndSearchTotalURL)
    }

    private fun getGenreData() {
        val genreTotalURL = "https://animeindo.to/genre-list/"
        genreAndSearchAnimePresenter.getOnlyGenreData(genreTotalURL)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.searchBar)) as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        hitQuery = query
        setTags(query, SEARCH_REQUEST)
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    private fun initEvent() {
        fragmentGenreAndSearchAnimeBinding!!.fabSelectGenre.setOnClickListener {
            dialog = Dialog(mContext!!)
            val chapterDialogBinding = DataBindingUtil.inflate<SelectChapterDialogBinding>(LayoutInflater.from(mContext), R.layout.select_chapter_dialog, null, false)
            dialog!!.setContentView(chapterDialogBinding.root)
            dialog!!.setTitle("Select other chapter")
            chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true)
            chapterDialogBinding.recyclerAllChapters.layoutManager = LinearLayoutManager(mContext)
            chapterDialogBinding.recyclerAllChapters.adapter = RecyclerAllGenreAdapter(this, animeGenreResultModelList)
            dialog!!.show()
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onItemClickGenre(position: Int) {
        dialog!!.dismiss()
        fragmentGenreAndSearchAnimeBinding!!.recyclerGenreAndSearchAnime.scrollToPosition(0)
        hitQuery = animeGenreResultModelList[position].genreTitle!!.toLowerCase()
        setTags(animeGenreResultModelList[position].genreTitle!!.toLowerCase(), NEW_PAGE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onGetSearchAndGenreDataSuccess(searchAndGenreHTMLResult: List<AnimeGenreAndSearchResultModel.AnimeSearchResult>) {
        activity!!.runOnUiThread {
            progressDialog!!.dismiss()
            animeGenreAndSearchResultModelList.clear()
            animeGenreAndSearchResultModelList.addAll(searchAndGenreHTMLResult)
            searchAndGenreAdapter!!.recyclerRefresh()
        }
    }

    override fun onGetSearchAndGenreDataFailed() {
        progressDialog!!.dismiss()
        Toast.makeText(mContext, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show()
    }

    override fun onGetOnlyGenreDataSuccess(onlyGenreHTMLResult: List<AnimeGenreAndSearchResultModel.AnimeGenreResult>) {
        animeGenreResultModelList.clear()
        animeGenreResultModelList.addAll(onlyGenreHTMLResult)
    }

    override fun onGetOnlyGenreDataFailed() {
        Toast.makeText(mContext, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val NEW_PAGE = 0
        private const val NEW_PAGE_SCROLL = 1
        private const val SWIPE_REFRESH = 2
        private const val SEARCH_REQUEST = 3
        private const val SEARCH_SWIPE_REQUEST = 4
    }
}// Required empty public constructor
