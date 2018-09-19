package com.egifcb.paila.pailah.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.egifcb.paila.pailah.R;
import com.egifcb.paila.pailah.adapter.AdapterView;
import com.egifcb.paila.pailah.model.Mount;
import com.egifcb.paila.pailah.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager sessionManager;
    TextView email, name;
    CircleImageView imageView;

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Mount> daftar;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Lah Tibo Sanak");

        sessionManager = new SessionManager(getBaseContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        email = (TextView)header.findViewById(R.id.tvemail);
        name = (TextView)header.findViewById(R.id.tvName);
        imageView = (CircleImageView) header.findViewById(R.id.imageView);

        MenuItem nav_login;
        Menu menu;
        menu = navigationView.getMenu();
        nav_login = menu.findItem(R.id.nav_signIn);

        if(sessionManager.isLoggedIn() == true){
            nav_login.setTitle("Logout");
            HashMap<String, String> user = sessionManager.getUserDetail();
            String nm = user.get(SessionManager.NAMA);
            String em = user.get(SessionManager.EMAIL);
            String ph = user.get(SessionManager.PHOTO);

            name.setText(nm);
            email.setText(em);
            Glide.with(this).load(ph).into(imageView);
        }else if (sessionManager.isLoggedIn() == false){
            nav_login.setTitle("Sign In Google");
        }

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView)findViewById(R.id.rv_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        loadview();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                loadview();
            }
        });
    }

    public void loadview(){
        swipeRefreshLayout.setRefreshing(true);
        //View Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("mount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftar = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    Mount mount = noteDataSnapshot.getValue(Mount.class);
                    mount.setKey(noteDataSnapshot.getKey());

                    daftar.add(mount);
                }

                adapter = new AdapterView(getApplicationContext(), daftar);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error"+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signIn) {
            if (item.getTitle().equals("Logout")){
                sessionManager.logoutUser();
            }else if(item.getTitle().equals("Sign In Google")){
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.nav_save) {
            if(sessionManager.isLoggedIn() == true){
                Intent intent = new Intent(getBaseContext(), SaveActivity.class);
                startActivity(intent);
                finish();
            }else if (sessionManager.isLoggedIn() == false){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Untuk Mengakses Menu Tambah Wisata Saudara Harus Telebih Dahulu Login. Apa Saudara Ingin Login ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getBaseContext(), GaleryActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_otherResaurces) {
            Intent intent = new Intent(getBaseContext(), OtherResaurcesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_openSource) {
            Intent intent = new Intent(getBaseContext(), OpenSourceActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alBuilder = new AlertDialog.Builder(MainActivity.this);
            alBuilder.setMessage("Are you really to close this app");
            alBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent keluar = new Intent(Intent.ACTION_MAIN);
                    keluar.addCategory(Intent.CATEGORY_HOME);
                    keluar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(keluar);
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alBuilder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
