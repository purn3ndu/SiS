package com.tcs.learning.sis;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 883633 on 30-09-2015.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    @Override
    public void onCreate(){
        super.onCreate();
        mInstance=this;
        //printHashKey();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }
    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
    /*
    public void printHashKey(){

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tcs.learning.sis",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("PURE:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    } */
}