package com.egifcb.paila.pailah.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.egifcb.paila.pailah.R;
import com.egifcb.paila.pailah.activity.DetailActivity;
import com.egifcb.paila.pailah.model.Constants;
import com.egifcb.paila.pailah.model.Mount;

import java.util.ArrayList;

/**
 * Created by egi_fcb on 1/1/18.
 */

public class AdapterView extends RecyclerView.Adapter<AdapterView.RecyclerViewHolder> {
    ArrayList<Mount> daftar;
    Context context;
    FirebaseDataListener listener;

    public AdapterView(Context ctx, ArrayList<Mount> daftar){
        this.context = ctx;
        this.daftar = daftar;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_layout_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final Mount mount = daftar.get(position);
        String fontName;
        fontName = "fonts/SourceSerifPro-Regular.ttf";
        final String key;
        final String namaGunung;
        final String tipeGunung;
        final String detail;
        final String ketinggian;
        final String letusanTerakhir;
        final String provinsi;
        final String photo;
        final String publish;

        key = mount.getKey();
        namaGunung = mount.getNamaGunung();
        tipeGunung = mount.getTipeGunung();
        detail = mount.getDetail();
        ketinggian = mount.getKetinggian();
        letusanTerakhir = mount.getLetusanTerakhir();
        provinsi = mount.getProvinsi();
        photo = mount.getPhoto();
        publish = mount.getPublish();

        holder.namagunung.setText(namaGunung);
        holder.namagunung.setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        holder.tipegunung.setText(tipeGunung);
        holder.ketinggian.setText(ketinggian);
        holder.detail.setText(detail);
        Glide.with(context).load(photo).into(holder.imageView);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, namaGunung+"\nDownload Segera Aplikasinya Hanya Di Playstore : https://play.google.com/store/apps/details?id=com.egifcb.paila.pailah");
                v.getContext().startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });

        holder.explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constants.KEY, key);
                intent.putExtra(Constants.NAMA_GUNUNG, namaGunung);
                intent.putExtra(Constants.TIPE_GUNUNG, tipeGunung);
                intent.putExtra(Constants.DETAIL, detail);
                intent.putExtra(Constants.KETINGGIAN, ketinggian);
                intent.putExtra(Constants.LETUSAN_TERAKHIR, letusanTerakhir);
                intent.putExtra(Constants.PROVINSI, provinsi);
                intent.putExtra(Constants.PHOTO, photo);
                intent.putExtra(Constants.PUBLISH, publish);
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return daftar.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView namagunung, tipegunung, ketinggian, detail, share, explore;
        AppCompatImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            namagunung = (AppCompatTextView) itemView.findViewById(R.id.tvnamaGunung);
            tipegunung = (AppCompatTextView) itemView.findViewById(R.id.tvtipeGunung);
            ketinggian = (AppCompatTextView) itemView.findViewById(R.id.tvketinggian);
            detail = (AppCompatTextView) itemView.findViewById(R.id.tvDetail);
            share = (AppCompatTextView) itemView.findViewById(R.id.tvShare);
            explore = (AppCompatTextView) itemView.findViewById(R.id.tvExplore);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public interface FirebaseDataListener{
        void onDeleteData(Mount tour, int position);
    }
}
