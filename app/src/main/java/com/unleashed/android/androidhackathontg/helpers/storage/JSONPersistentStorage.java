package com.unleashed.android.androidhackathontg.helpers.storage;

import android.content.Context;
import android.preference.PreferenceManager;

import com.unleashed.android.androidhackathontg.helpers.constants.Constants;



/**
 * Created by sudhanshu on 20/11/17.
 */

public class JSONPersistentStorage {



    public static void storeJSONArray(Context context, String jsonArray){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.JSON_STORAGE_TAG,jsonArray).apply();
    }

    public static String getJSONArray(Context context){
        String jsonObject = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.JSON_STORAGE_TAG,"");
        return jsonObject;
    }

}
