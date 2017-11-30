package com.unleashed.android.androidhackathontg.gui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unleashed.android.androidhackathontg.R;
import com.unleashed.android.androidhackathontg.customadapter.CountryListRowItem;
import com.unleashed.android.androidhackathontg.customadapter.CountryTableCols;
import com.unleashed.android.androidhackathontg.customadapter.CountryTableDataAdapter;
import com.unleashed.android.androidhackathontg.data.CountryListRowItemDataBase;
import com.unleashed.android.androidhackathontg.data.JSONArrayDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private CountryListRowItem mItem;
    private ArrayList<CountryListRowItem> mItemArrayList;
//    private List<CountryTableCols> data_to_show;
//    private Context mContextFragment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

//        mContextFragment = this.getContext();

//        TableView tableView = (TableView) rootView.findViewById(R.id.tableView);
//        data_to_show = new ArrayList<CountryTableCols>();

        String index = getArguments().getString(ARG_ITEM_ID);
        int index_int = Integer.valueOf(index);

        JSONObject jsonObject = null;
        StringBuilder sbCountryDetails = new StringBuilder();

        try {
            JSONArray jsonArray = JSONArrayDataBase.getJsonArray();
            jsonObject = jsonArray.getJSONObject(index_int);

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.optString(key);

                if (value.startsWith("[")) {
                    value = getArrayItems(value);
                }

                if (value.startsWith("{")) {
                    value = getNameValuePairs(key, value);
                }

                key = key.substring(0, 1).toUpperCase() + key.substring(1);
                sbCountryDetails.append(key + "  :  " + value + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mItemArrayList = CountryListRowItemDataBase.getInstance().getValues();
        mItem = mItemArrayList.get(index_int);

        // Populate Detail Screen with contents
        if (mItem != null) {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getCountryName());
            }

            ((TextView) rootView.findViewById(R.id.item_detail)).setText(sbCountryDetails.toString());

        }
        return rootView;
    }

    private String getNameValuePairs(String parentKey, String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            Iterator<String> subKeys = jsonObject.keys();
            StringBuilder subString = new StringBuilder();
            subString.append("\n");     // Do this only once

            while (subKeys.hasNext()) {
                String subkey = subKeys.next();
                value = jsonObject.optString(subkey);
                subString.append(subkey + "  :  " + value + "\n");
                //subString.append(parentKey + "_" + subkey + "  :  " + value + "\n");
            }

            subString.append("\n");     // Do this only once
            value = subString.toString();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return value;
    }

    private String getArrayItems(String value){
        try{
            JSONArray ja = new JSONArray(value);
            StringBuilder subString = new StringBuilder();
            for(int i = 0; i < ja.length(); i++){
                String delimiter = (i < (ja.length()-1)) ? "," : "";
                subString.append(ja.get(i) + delimiter);
            }
            value = subString.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }


}
