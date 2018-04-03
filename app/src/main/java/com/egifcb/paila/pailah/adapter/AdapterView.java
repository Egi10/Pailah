package com.egifcb.paila.pailah.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.egifcb.paila.pailah.R;
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

        holder.namagunung.setText(mount.getNamaGunung());
        holder.namagunung.setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        holder.tipegunung.setText(mount.getTipeGunung());
        holder.ketinggian.setText(mount.getKetinggian());
        holder.detail.setText(mount.getDetail());
        Glide.with(context).load(mount.getPhoto()).into(holder.imageView);
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
