package com.egifcb.paila.pailah.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.egifcb.paila.pailah.activity.MainActivity;
import com.egifcb.paila.pailah.activity.SaveActivity;

import java.util.HashMap;

/**
 * Created by egi_fcb on 11/18/17.
 */

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Tour";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "KeyId";
    public static final String NAMA = "Nama";
    public static final String EMAIL = "Email";
    public static final String PHOTO = "Photo";

    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String id, String nama, String email, String photo){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(NAMA, nama);
        editor.putString(EMAIL, email);
        editor.putString(PHOTO, photo);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent = new Intent(_context, SaveActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, sharedPreferences.getString(KEY_ID, ""));
        user.put(NAMA, sharedPreferences.getString(NAMA, ""));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, ""));
        user.put(PHOTO, sharedPreferences.getString(PHOTO, ""));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }
}