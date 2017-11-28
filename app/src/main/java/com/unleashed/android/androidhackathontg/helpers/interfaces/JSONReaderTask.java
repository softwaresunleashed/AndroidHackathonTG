package com.unleashed.android.androidhackathontg.helpers.interfaces;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.unleashed.android.androidhackathontg.helpers.constants.Constants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sudhanshu on 22/11/17.
 */

public class JSONReaderTask extends AsyncTask<String, String, String> {


    private HttpURLConnection connection = null;
    private ProgressDialog progressDialog = new ProgressDialog(Constants.mContext);
    private JSONReaderTaskListener jsonReaderTaskListener;
    private String result = "";

    public JSONReaderTask(JSONReaderTaskListener jsonReaderTaskListener) {
        this.jsonReaderTaskListener = jsonReaderTaskListener;
    }

    protected void onPreExecute() {
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                JSONReaderTask.this.cancel(true);
            }
        });
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            // Set up HTTP post

            // URL Connection
            URL url = new URL(Constants.URL);
            connection = (HttpURLConnection)url.openConnection();

            // Read content & Log
            InputStream inputStream = connection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            inputStream.close();
            Constants.JSON_response = result = sBuilder.toString();

        } catch (Exception e) {
            Constants.JSON_response = result = "";
            e.printStackTrace();
        } finally {
            // Close connection in a finally block
            connection.disconnect();
        }

        return result;

    }

    protected void onPostExecute(String v) {


        if (jsonReaderTaskListener != null && !result.isEmpty()) {
            jsonReaderTaskListener.onTaskDone(result);
        } else
            jsonReaderTaskListener.onError();

        this.progressDialog.dismiss();
    }

}
