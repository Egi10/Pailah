package com.egifcb.paila.pailah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.egifcb.paila.pailah.R;
import com.egifcb.paila.pailah.model.Constants;

public class DetailActivity extends AppCompatActivity {
    AppCompatImageView imageView;
    AppCompatTextView Detail, LetusanTerakhir, Provinsi, Publish;
    String key;
    String namaGunung;
    String tipeGunung;
    String detail;
    String ketinggian;
    String letusanTerakhir;
    String provinsi;
    String photo;
    String publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setExpandedTitleGravity(Gravity.BOTTOM);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(R.drawable.ic_saveblue);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageView = (AppCompatImageView) findViewById(R.id.imageView);
        Detail = (AppCompatTextView) findViewById(R.id.tvDetail);
        LetusanTerakhir = (AppCompatTextView) findViewById(R.id.tvletusanTerakhir);
        Provinsi = (AppCompatTextView) findViewById(R.id.tvprovinsi);
        Publish = (AppCompatTextView) findViewById(R.id.tvpublish);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            namaGunung = (String) bundle.get(Constants.NAMA_GUNUNG);
            photo = (String) bundle.get(Constants.PHOTO);
            detail = (String) bundle.get(Constants.DETAIL);
            letusanTerakhir = (String) bundle.get(Constants.LETUSAN_TERAKHIR);
            provinsi = (String) bundle.get(Constants.PROVINSI);
            publish = (String) bundle.get(Constants.PUBLISH);
        }

        Glide.with(getBaseContext()).load(photo).into(imageView);
        Detail.setText(detail);
        LetusanTerakhir.setText(letusanTerakhir);
        Provinsi.setText(provinsi);
        Publish.setText(publish);
        setTitle(namaGunung);
    }

    //Tombol Kembali
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = new Intent(getBaseContext(), MainActivity.class);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // return to the App's Home Activity
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent parentIntent = new Intent(getBaseContext(), MainActivity.class);
            parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(parentIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
