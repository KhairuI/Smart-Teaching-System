package com.example.smartteachingsystem.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    public SharedPrefUtils(Context context) {
        sharedPreferences= context.getSharedPreferences(context.getPackageName() + "_pref",Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }

    // save user role
    public void setRole(String role){
        editor.putString("role",role).commit();
    }

    // get user role
    public String getRole(){
        return sharedPreferences.getString("role","null");
    }

    // set current Uid
    public void setCurrentUId(String uId){
        editor.putString("uid",uId).commit();
    }

    // get current Uid
    public String getCurrentUId(){
        return sharedPreferences.getString("uid","null");
    }

    // set image
    public void setImage(String image){
        editor.putString("image",image).commit();
    }

    // get image
    public String getImage(){
        return sharedPreferences.getString("image","null");
    }

    // set name
    public void setName(String name){
        editor.putString("name",name).commit();
    }

    // get image
    public String getName(){
        return sharedPreferences.getString("name","null");
    }

    // clear all data
    public void clearPref(){
        editor.clear().commit();
    }

}
