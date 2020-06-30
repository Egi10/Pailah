package com.egifcb.paila.pailah.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.egifcb.paila.pailah.R;
import com.egifcb.paila.pailah.model.Constants;
import com.egifcb.paila.pailah.model.Mount;
import com.egifcb.paila.pailah.session.SessionManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class SaveActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppCompatImageView toolbarBack;
    AppCompatImageView ivImage;
    AppCompatEditText NamaGunung, TipeGunung, Detail, Ketinggian, LetusanTerakhir, Provinsi;
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        toolbarBack = findViewById(R.id.toolbarBack);
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
        NamaGunung = findViewById(R.id.tvnamaGunung);
        TipeGunung = findViewById(R.id.tvtipeGunung);
        Detail = findViewById(R.id.tvDetail);
        Ketinggian = findViewById(R.id.tvketinggian);
        LetusanTerakhir = findViewById(R.id.tvletusanTerakhir);
        Provinsi = findViewById(R.id.tvprovinsi);
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
                String namaGunung, tipeGunung, detail, ketinggian, letusanTerakhir, provinsi;

                namaGunung = NamaGunung.getText().toString();
                tipeGunung = TipeGunung.getText().toString();
                detail = Detail.getText().toString();
                ketinggian = Ketinggian.getText().toString();
                letusanTerakhir = LetusanTerakhir.getText().toString();
                provinsi = Provinsi.getText().toString();

                HashMap<String, String> user = sessionManager.getUserDetail();
                String nm = user.get(SessionManager.NAMA);
                String em = user.get(SessionManager.EMAIL);

                if (namaGunung.equals("") || tipeGunung.equals("") || detail.equals("") || ketinggian.equals("") || letusanTerakhir.equals("") || provinsi.equals("")){
                    Toast.makeText(getBaseContext(), "Jangan Ada Data Yang Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile(namaGunung, tipeGunung, detail, ketinggian, letusanTerakhir, provinsi, nm);
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

    private void uploadFile(final String namaGunung, final String tipeGunung, final String detail, final String ketinggian, final String letusanTerakhir, final String provinsi, final String publish){
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Mohon Menunggu");
            progressDialog.setMessage("Sedang Melakukan Proses Simpan...");
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            final StorageReference storage = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            storage.putFile(filePath)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    throw Objects.requireNonNull(task.getException());
                                }
                            }
                            return storage.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("mount");

                            String id = databaseReference.push().getKey();

                            Uri uriDownload = task.getResult();
                            Mount mount = new Mount(id, namaGunung, tipeGunung, detail, ketinggian,
                                    letusanTerakhir, provinsi, uriDownload.toString(), publish);
                            assert id != null;
                            databaseReference.child(id).setValue(mount);

                            Toast.makeText(getBaseContext(), "Data Sudah Tersimpan", Toast.LENGTH_SHORT).show();

                            NamaGunung.setText("");
                            TipeGunung.setText("");
                            Detail.setText("");
                            Ketinggian.setText("");
                            LetusanTerakhir.setText("");
                            Provinsi.setText("");
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
