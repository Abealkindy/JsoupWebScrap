package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.models.mangamodels.ReadMangaModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAllChapterAdapter extends ArrayAdapter<ReadMangaModel.AllChapterDatas> {
    private Context context;
    private List<ReadMangaModel.AllChapterDatas> allChapterDatasArrayList;

    public SpinnerAllChapterAdapter(Context context, int textViewResourceId, List<ReadMangaModel.AllChapterDatas> allChapterDatasArrayList) {
        super(context, textViewResourceId, allChapterDatasArrayList);
        this.context = context;
        this.allChapterDatasArrayList = allChapterDatasArrayList;
    }

//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//
//        @SuppressLint("ViewHolder") View row = LayoutInflater.from(context).inflate(R.layout.spinner_row, parent, false);
//        TextView textSantri = row.findViewById(R.id.text_nama_santri);
//        textSantri.setText(santriModels.get(position).getNama_santri());
//        return row;
//    }


//    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
//
//        View row = LayoutInflater.from(context).inflate(R.layout.spinner_row, parent, false);
//        TextView textSantri = row.findViewById(R.id.text_nama_santri);
//        textSantri.setText(santriModels.get(position).getNama_santri());
//        return row;
//    }
}
