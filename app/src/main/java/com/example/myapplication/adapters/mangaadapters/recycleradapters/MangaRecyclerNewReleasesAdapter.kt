package com.example.myapplication.adapters.mangaadapters.recycleradapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemListMangaBinding
import com.squareup.picasso.Picasso

class MangaRecyclerNewReleasesAdapter(private val context: Context, private val animeNewReleaseResultModelList: List<MangaNewReleaseResultModel>) : RecyclerView.Adapter<MangaRecyclerNewReleasesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val viewHolder: ViewHolder
        val itemListBinding = DataBindingUtil.inflate<ItemListMangaBinding>(layoutInflater, R.layout.item_list_manga, parent, false)
        viewHolder = ViewHolder(itemListBinding)
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemListBinding.textTitleManga.text = animeNewReleaseResultModelList[position].mangaTitle
        Picasso.get().load(animeNewReleaseResultModelList[position].mangaThumb).placeholder(context.resources.getDrawable(R.drawable.imageplaceholder)).into(holder.itemListBinding.imageViewBackgroundManga)
        if (!animeNewReleaseResultModelList[position].isMangaStatus) {
            holder.itemListBinding.textViewHotStatus.visibility = View.GONE
        } else {
            holder.itemListBinding.textViewHotStatus.visibility = View.VISIBLE
        }
        when {
            animeNewReleaseResultModelList[position].mangaType.equals(context.resources.getString(R.string.manga_string), ignoreCase = true) -> {
                holder.itemListBinding.textMangaType.background = context.resources.getDrawable(R.drawable.bubble_background_manga)
                holder.itemListBinding.textMangaType.text = context.resources.getString(R.string.manga_string)
            }
            animeNewReleaseResultModelList[position].mangaType.equals(context.resources.getString(R.string.manhwa_string), ignoreCase = true) -> {
                holder.itemListBinding.textMangaType.background = context.resources.getDrawable(R.drawable.bubble_background_manhwa)
                holder.itemListBinding.textMangaType.text = context.resources.getString(R.string.manhwa_string)
            }
            animeNewReleaseResultModelList[position].mangaType.equals(context.resources.getString(R.string.manhua_string), ignoreCase = true) -> {
                holder.itemListBinding.textMangaType.background = context.resources.getDrawable(R.drawable.bubble_background_manhua)
                holder.itemListBinding.textMangaType.text = context.resources.getString(R.string.manhua_string)
            }
            animeNewReleaseResultModelList[position].mangaType.equals(context.resources.getString(R.string.mangaoneshot_string), ignoreCase = true) -> {
                holder.itemListBinding.textMangaType.background = context.resources.getDrawable(R.drawable.bubble_background_manga)
                holder.itemListBinding.textMangaType.text = context.resources.getString(R.string.mangaoneshot_string)
            }
            animeNewReleaseResultModelList[position].mangaType.equals(context.resources.getString(R.string.oneshot_string), ignoreCase = true) -> {
                holder.itemListBinding.textMangaType.background = context.resources.getDrawable(R.drawable.bubble_background_manga)
                holder.itemListBinding.textMangaType.text = context.resources.getString(R.string.oneshot_string)
            }
        }
        if (animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!!.isEmpty()
                && animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!!.isEmpty()
                && animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!!.isEmpty()) {
            holder.itemListBinding.cardFirstMangaChapter.visibility = View.GONE
            holder.itemListBinding.cardSecondMangaChapter.visibility = View.GONE
            holder.itemListBinding.cardThirdMangaChapter.visibility = View.GONE
        } else if (animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!!.size == 1
                && animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!!.size == 1
                && animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!!.size == 1) {
            holder.itemListBinding.cardFirstMangaChapter.visibility = View.VISIBLE
            holder.itemListBinding.textFirstMangaChapter.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![0]
            holder.itemListBinding.textFirstMangaChapterReleaseTime.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!![0]
            holder.itemListBinding.cardFirstMangaChapter.setOnClickListener {
                val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
                intent.putExtra("chapterURL", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!![0])
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList[position].mangaType)
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![0])
                context.startActivity(intent)
            }
            holder.itemListBinding.cardSecondMangaChapter.visibility = View.GONE
            holder.itemListBinding.cardThirdMangaChapter.visibility = View.GONE
        } else if (animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!!.size == 2
                && animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!!.size == 2
                && animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!!.size == 2) {
            holder.itemListBinding.cardFirstMangaChapter.visibility = View.VISIBLE
            holder.itemListBinding.textFirstMangaChapter.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![0]
            holder.itemListBinding.textFirstMangaChapterReleaseTime.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!![0]
            holder.itemListBinding.cardFirstMangaChapter.setOnClickListener {
                val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
                intent.putExtra("chapterURL", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!![0])
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList[position].mangaType)
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![0])
                context.startActivity(intent)
            }
            holder.itemListBinding.cardSecondMangaChapter.visibility = View.VISIBLE
            holder.itemListBinding.textSecondMangaChapter.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![1]
            holder.itemListBinding.textSecondMangaChapterReleaseTime.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!![1]
            holder.itemListBinding.cardSecondMangaChapter.setOnClickListener {
                val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
                intent.putExtra("chapterURL", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!![1])
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList[position].mangaType)
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![1])
                context.startActivity(intent)
            }
            holder.itemListBinding.cardThirdMangaChapter.visibility = View.GONE
        } else {
            holder.itemListBinding.cardFirstMangaChapter.visibility = View.VISIBLE
            holder.itemListBinding.textFirstMangaChapter.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![0]
            holder.itemListBinding.textFirstMangaChapterReleaseTime.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!![0]
            holder.itemListBinding.cardFirstMangaChapter.setOnClickListener {
                val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
                intent.putExtra("chapterURL", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!![0])
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList[position].mangaType)
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![0])
                context.startActivity(intent)
            }
            holder.itemListBinding.cardSecondMangaChapter.visibility = View.VISIBLE
            holder.itemListBinding.textSecondMangaChapter.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![1]
            holder.itemListBinding.textSecondMangaChapterReleaseTime.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!![1]
            holder.itemListBinding.cardSecondMangaChapter.setOnClickListener {
                val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
                intent.putExtra("chapterURL", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!![1])
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList[position].mangaType)
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![1])
                context.startActivity(intent)
            }
            holder.itemListBinding.cardThirdMangaChapter.visibility = View.VISIBLE
            holder.itemListBinding.textThirdMangaChapter.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![2]
            holder.itemListBinding.textThirdMangaChapterReleaseTime.text = animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterReleaseTime!![2]
            holder.itemListBinding.cardThirdMangaChapter.setOnClickListener {
                val intent = Intent(context.applicationContext, ReadMangaActivity::class.java)
                intent.putExtra("chapterURL", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterURL!![2])
                intent.putExtra("appBarColorStatus", animeNewReleaseResultModelList[position].mangaType)
                intent.putExtra("chapterTitle", animeNewReleaseResultModelList[position].latestMangaDetail[0].chapterTitle!![2])
                context.startActivity(intent)
            }
        }
    }


    override fun getItemCount(): Int {
        return animeNewReleaseResultModelList.size
    }

    inner class ViewHolder(val itemListBinding: ItemListMangaBinding) : RecyclerView.ViewHolder(itemListBinding.root)
}
