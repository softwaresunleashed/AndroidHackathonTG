package com.unleashed.android.androidhackathontg.helpers.jsonparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sudhanshu on 20/11/17.
 */

public class JsonParser {

    private JSONArray jsonArray;
    private JSONObject jsonRootObject;

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JSONObject getJsonRootObject() {
        return jsonRootObject;
    }

    public void setJsonRootObject(JSONObject jsonRootObject) {
        this.jsonRootObject = jsonRootObject;
    }

    public JSONArray parseJSON(String strJSON){
        try {
            // Create the root JSONObject from the JSON string.
            jsonRootObject = new JSONObject(strJSON);

            //Get the instance of JSONArray that contains JSONObjects
            jsonArray = jsonRootObject.optJSONArray("root");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonArray;
    }

}
