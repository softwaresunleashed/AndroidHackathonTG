package com.unleashed.android.androidhackathontg.helpers.interfaces;

/**
 * Created by sudhanshu on 22/11/17.
 */

public interface JSONReaderTaskListener {
    void onTaskDone(String responseData);

    void onError();
}
