package com.unleashed.android.androidhackathontg.data;

import com.unleashed.android.androidhackathontg.customadapter.CountryListRowItem;

import java.util.ArrayList;

/**
 * Created by sudhanshu on 21/11/17.
 */

public class CountryListRowItemDataBase {

    public ArrayList<CountryListRowItem> getValues() {
        if(ourInstance.mValues == null)
            ourInstance.mValues = new ArrayList<CountryListRowItem>();

        return ourInstance.mValues;
    }

    private static ArrayList<CountryListRowItem> mValues = null;

    private static final CountryListRowItemDataBase ourInstance = new CountryListRowItemDataBase();

    public static CountryListRowItemDataBase getInstance() {
        return ourInstance;
    }

    private CountryListRowItemDataBase() {
    }



}
