package com.example.myapplication.activities.mangapage.read_manga_mvp

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

import com.example.myapplication.R
import com.example.myapplication.activities.mangapage.MangaReleaseListActivity
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerAllChapterAdapter
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerReadMangaAdapter
import com.example.myapplication.databinding.ActivityReadMangaBinding
import com.example.myapplication.databinding.SelectChapterDialogBinding
import com.example.myapplication.models.mangamodels.ReadMangaModel

import java.util.ArrayList

class ReadMangaActivity : AppCompatActivity(), RecyclerAllChapterAdapter.ClickListener, ReadMangaInterface {
    private var readMangaBinding: ActivityReadMangaBinding? = null
    private val readMangaPresenter = ReadMangaPresenter(this)
    private var allChapterDatasList: List<ReadMangaModel.AllChapterDatas> = ArrayList()
    private var dialog: Dialog? = null
    private lateinit var progressDialog: ProgressDialog
    private var appColorBarStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readMangaBinding = DataBindingUtil.setContentView(this, R.layout.activity_read_manga)
        setUI()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUI() {
        progressDialog = ProgressDialog(this@ReadMangaActivity)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3")
        appColorBarStatus = intent.getStringExtra("appBarColorStatus")
        val chapterURL = intent.getStringExtra("chapterURL")

        if (chapterURL != null) {
            getReadMangaContentData(chapterURL)
        } else {
            Toast.makeText(this, "Chapter URL Null!", Toast.LENGTH_SHORT).show()
        }
        if (appColorBarStatus != null) {
            when {
                appColorBarStatus!!.equals(resources.getString(R.string.manga_string), ignoreCase = true) -> {
                    readMangaBinding!!.appBarReadManga.setBackgroundColor(resources.getColor(R.color.manga_color))
                    readMangaBinding!!.designBottomSheet.setBackgroundColor(resources.getColor(R.color.manga_color))
                }
                appColorBarStatus!!.equals(resources.getString(R.string.manhua_string), ignoreCase = true) -> {
                    readMangaBinding!!.appBarReadManga.setBackgroundColor(resources.getColor(R.color.manhua_color))
                    readMangaBinding!!.designBottomSheet.setBackgroundColor(resources.getColor(R.color.manhua_color))
                }
                appColorBarStatus!!.equals(resources.getString(R.string.manhwa_string), ignoreCase = true) -> {
                    readMangaBinding!!.appBarReadManga.setBackgroundColor(resources.getColor(R.color.manhwa_color))
                    readMangaBinding!!.designBottomSheet.setBackgroundColor(resources.getColor(R.color.manhwa_color))
                }
                appColorBarStatus!!.equals(resources.getString(R.string.oneshot_string), ignoreCase = true) -> {
                    readMangaBinding!!.appBarReadManga.setBackgroundColor(resources.getColor(R.color.manga_color))
                    readMangaBinding!!.designBottomSheet.setBackgroundColor(resources.getColor(R.color.manga_color))
                }
                appColorBarStatus!!.equals(resources.getString(R.string.mangaoneshot_string), ignoreCase = true) -> {
                    readMangaBinding!!.appBarReadManga.setBackgroundColor(resources.getColor(R.color.manga_color))
                    readMangaBinding!!.designBottomSheet.setBackgroundColor(resources.getColor(R.color.manga_color))
                }
            }
        } else {
            readMangaBinding!!.appBarReadManga.setBackgroundColor(resources.getColor(R.color.manga_color))
            readMangaBinding!!.designBottomSheet.setBackgroundColor(resources.getColor(R.color.manga_color))
        }
    }

    fun getReadMangaContentData(chapterURL: String?) {
        progressDialog.show()
        readMangaPresenter.getMangaContent(chapterURL)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ReadMangaActivity, MangaReleaseListActivity::class.java))
        finish()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onItemClick(position: Int, v: View) {
        dialog!!.dismiss()
        getReadMangaContentData(allChapterDatasList[position].chapterUrl)
    }

    override fun onGetMangaContentDataSuccess(mangaContents: ReadMangaModel) {
        runOnUiThread {
            progressDialog.dismiss()
            readMangaBinding!!.textViewChapterTitle.text = mangaContents.chapterTitle
            readMangaBinding!!.recyclerImageContentManga.setHasFixedSize(true)
            val mangaRecyclerNewReleasesAdapter = RecyclerReadMangaAdapter(this@ReadMangaActivity, mangaContents.imageContent)
            readMangaBinding!!.recyclerImageContentManga.adapter = mangaRecyclerNewReleasesAdapter
            val linearLayoutManager = LinearLayoutManager(this@ReadMangaActivity)
            readMangaBinding!!.recyclerImageContentManga.layoutManager = linearLayoutManager

            val getNextChapterURL = mangaContents.nextMangaURL
            if (getNextChapterURL == null || getNextChapterURL.isEmpty()) {
                readMangaBinding!!.nextChapButton.visibility = View.GONE
            } else {
                readMangaBinding!!.nextChapButton.visibility = View.VISIBLE
                readMangaBinding!!.nextChapButton.setOnClickListener { getReadMangaContentData(getNextChapterURL) }
            }

            val getPrevChapterURL = mangaContents.previousMangaURL
            if (getPrevChapterURL == null || getPrevChapterURL.isEmpty()) {
                readMangaBinding!!.prevChapButton.visibility = View.GONE
            } else {
                readMangaBinding!!.prevChapButton.visibility = View.VISIBLE
                readMangaBinding!!.prevChapButton.setOnClickListener { getReadMangaContentData(getPrevChapterURL) }
            }

            readMangaBinding!!.mangaInfoButton.setOnClickListener {
                val intent = Intent(this@ReadMangaActivity, MangaDetailActivity::class.java)
                intent.putExtra("detailURL", mangaContents.mangaDetailURL)
                intent.putExtra("detailType", appColorBarStatus)
                intent.putExtra("detailTitle", "")
                intent.putExtra("detailRating", "")
                intent.putExtra("detailStatus", "")
                intent.putExtra("detailThumb", "")
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onGetMangaContentDataFailed() {
        progressDialog.dismiss()
        Toast.makeText(this@ReadMangaActivity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show()
    }

    override fun onGetMangaChaptersDataSuccess(allChapters: List<ReadMangaModel.AllChapterDatas>) {
        progressDialog.dismiss()
        allChapterDatasList = allChapters
        readMangaBinding!!.showAllChap.setOnClickListener {
            dialog = Dialog(this@ReadMangaActivity)
            val chapterDialogBinding = DataBindingUtil.inflate<SelectChapterDialogBinding>(LayoutInflater.from(this@ReadMangaActivity), R.layout.select_chapter_dialog, null, false)
            dialog!!.setContentView(chapterDialogBinding.root)
            dialog!!.setTitle("Select other chapter")
            chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true)
            chapterDialogBinding.recyclerAllChapters.layoutManager = LinearLayoutManager(this@ReadMangaActivity)
            chapterDialogBinding.recyclerAllChapters.adapter = RecyclerAllChapterAdapter(this@ReadMangaActivity, allChapterDatasList)
            dialog!!.show()
        }
    }

    override fun onGetMangaChaptersDataFailed() {
        progressDialog.dismiss()
        Toast.makeText(this@ReadMangaActivity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show()
    }
}
