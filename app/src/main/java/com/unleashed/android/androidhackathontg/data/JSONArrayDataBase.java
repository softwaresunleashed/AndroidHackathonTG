package com.unleashed.android.androidhackathontg.data;

import org.json.JSONArray;

/**
 * Created by sudhanshu on 21/11/17.
 */

public class JSONArrayDataBase {

    private static final JSONArrayDataBase ourInstance = new JSONArrayDataBase();
    private JSONArray jsonArray;

    public static JSONArrayDataBase getInstance() {
        return ourInstance;
    }

    private JSONArrayDataBase() {
    }



    public static JSONArray getJsonArray() {
        return JSONArrayDataBase.getInstance().jsonArray;
    }

    public static void setJsonArray(JSONArray jsonArray) {
        JSONArrayDataBase.getInstance().jsonArray = jsonArray;
    }
}
