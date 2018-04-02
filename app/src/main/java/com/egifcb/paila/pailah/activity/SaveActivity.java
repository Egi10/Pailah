package com.egifcb.paila.pailah.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.egifcb.paila.pailah.R;
import com.egifcb.paila.pailah.model.Constants;
import com.egifcb.paila.pailah.model.Tour;
import com.egifcb.paila.pailah.session.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class SaveActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppCompatImageView toolbarBack;
    AppCompatImageView ivImage;
    AppCompatEditText judul, detail, fasilitas, lat, lag;
    FancyButton save;

    private static final int PICK_IMAGE_REQUEST = 234;

    //Uri
    private Uri filePath;

    //Firebase Yang Merefresh Ke Firebase Realtime Database
    StorageReference storageReference;
    DatabaseReference databaseReference;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        toolbarBack = (AppCompatImageView) findViewById(R.id.toolbarBack);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sessionManager = new SessionManager(getBaseContext());

        ivImage = findViewById(R.id.ivImage);
        judul = findViewById(R.id.tvTitle);
        detail = findViewById(R.id.tvDetail);
        fasilitas = findViewById(R.id.tvFasilitas);
        lat = findViewById(R.id.tvLat);
        lag = findViewById(R.id.tvLog);
        save = findViewById(R.id.btnSave);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvjudul = judul.getText().toString();
                String tvdetail = detail.getText().toString();
                String tvfasilitas = fasilitas.getText().toString();
                String tvlat = lat.getText().toString();
                String tvlag = lag.getText().toString();

                HashMap<String, String> user = sessionManager.getUserDetail();
                String nm = user.get(SessionManager.NAMA);
                String em = user.get(SessionManager.EMAIL);

                if (tvjudul.equals("") || tvdetail.equals("") || tvfasilitas.equals("") || tvlat.equals("") || tvlag.equals("")){
                    Toast.makeText(getBaseContext(), "Jangan Ada Data Yang Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile(tvjudul, tvdetail, tvfasilitas, tvlat, tvlag, nm, em);
                }
            }
        });
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadFile(final String tvjudul, final String tvdetail, final String tvfasilitas, final String tvlat, final String tvlag,
                            final String nama, final String email){
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Mohon Menunggu");
            progressDialog.setMessage("Sedang Melakukan Proses Simpan...");
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            StorageReference storage = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            storage.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("tourall");

                            String id = databaseReference.push().getKey();

                            Tour tour = new Tour(id, tvjudul, tvdetail, tvfasilitas, tvlat, tvlag, taskSnapshot.getDownloadUrl().toString(), nama, email);
                            databaseReference.child(id).setValue(tour);

                            Toast.makeText(getBaseContext(), "Data Sudah Tersimpan", Toast.LENGTH_SHORT).show();

                            judul.setText("");
                            detail.setText("");
                            fasilitas.setText("");
                            lat.setText("");
                            lag.setText("");
                            ivImage.setImageResource(R.drawable.ic_upload);

                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("Ini Salah ", e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnSave), "Pilih Gambar Terlebih Dahulu", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
