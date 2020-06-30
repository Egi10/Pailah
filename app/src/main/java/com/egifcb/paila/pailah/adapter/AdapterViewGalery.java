package com.egifcb.paila.pailah.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
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

public class AdapterViewGalery extends RecyclerView.Adapter<AdapterViewGalery.RecyclerViewHolder> {
    ArrayList<Mount> daftar;
    Context context;
    FirebaseDataListener listener;

    public AdapterViewGalery(Context ctx, ArrayList<Mount> daftar){
        this.context = ctx;
        this.daftar = daftar;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_layout_galery, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final Mount mount = daftar.get(position);
        String fontName;
        fontName = "fonts/SourceSerifPro-Regular.ttf";

        final String namaGunung;
        final String photo;

        namaGunung = mount.getNamaGunung();
        photo = mount.getPhoto();

        holder.namagunung.setText(namaGunung);
        holder.namagunung.setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        Glide.with(context).load(photo).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return daftar.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView namagunung;
        AppCompatImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            namagunung = (AppCompatTextView) itemView.findViewById(R.id.tvnamaGunung);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public interface FirebaseDataListener{
        void onDeleteData(Mount tour, int position);
    }
}
