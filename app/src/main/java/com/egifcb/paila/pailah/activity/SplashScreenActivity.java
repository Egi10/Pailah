package com.egifcb.paila.pailah.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.egifcb.paila.pailah.R;

public class SplashScreenActivity extends AppCompatActivity {
    TextView tvName;
    String fontName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fontName = "fonts/Chicle-Regular.ttf";

        tvName = findViewById(R.id.tvName);
        tvName.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), fontName));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}

//Rules
//- Pertama Dibuka Aplikasi Menampilkan Daftar Seluruh Wisata Sumbar (Target Memakai Shimmer dan Firebase)
//- Yang Ingin Menambahkan Data Cuma Administrator
//- Selesai
