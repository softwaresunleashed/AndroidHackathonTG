package com.unleashed.android.androidhackathontg.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unleashed.android.androidhackathontg.JSONReaderTask;
import com.unleashed.android.androidhackathontg.JSONReaderTaskListener;
import com.unleashed.android.androidhackathontg.R;
import com.unleashed.android.androidhackathontg.customadapter.CountryListRowItem;
import com.unleashed.android.androidhackathontg.customadapter.CustomListViewAdapter;
import com.unleashed.android.androidhackathontg.customadapter.SimpleItemRecyclerViewAdapter;
import com.unleashed.android.androidhackathontg.data.CountryListRowItemDataBase;
import com.unleashed.android.androidhackathontg.data.JSONArrayDataBase;
import com.unleashed.android.androidhackathontg.helpers.constants.Constants;
import com.unleashed.android.androidhackathontg.helpers.jsonparser.JsonParser;
import com.unleashed.android.androidhackathontg.helpers.storage.JSONPersistentStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private View recyclerView;

    public CustomListViewAdapter customListViewAdapterObj;

    public SimpleItemRecyclerViewAdapter mAdapterRecyclerView;

    // Create an Array of Items and a ArrayAdapter around it.
    public static ArrayList<CountryListRowItem> countryListRowItems = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        Constants.mContext = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapterRecyclerView.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize all data structures during app startup.
        read_and_populate_databases();

    }


    private JSONReaderTaskListener jsonReaderTaskListener = new JSONReaderTaskListener() {
        @Override
        public void onTaskDone(String responseData) {
            //parse JSON data
            try {
                JSONArray jArray = new JSONArray(responseData);
                JSONArrayDataBase.setJsonArray(jArray);
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }

            // Store data received from network to persistant storage
            Constants.JSON_response = responseData;
            String temp_data_received = "{ \"root\" :" + Constants.JSON_response + "}";
            JSONPersistentStorage.storeJSONArray(getApplicationContext(), temp_data_received);

            getRealData();
        }

        @Override
        public void onError() {
            Constants.JSON_response = "";
        }
    };

    private void read_and_populate_databases() {


        String attempt1 = JSONPersistentStorage.getJSONArray(getApplicationContext());
        if(attempt1.isEmpty()){
            // Get the data from network via Task.
            JSONReaderTask jsonReaderTask = new JSONReaderTask(jsonReaderTaskListener);
            jsonReaderTask.execute();
        } else {
            getRealData();
        }

    }


    private void getRealData(){
        // Read back string from Persistant storage
        String read_back_data = JSONPersistentStorage.getJSONArray(getApplicationContext());

        // Parse JSON Response data into JSON Array
        JSONArray jsonArray = new JsonParser().parseJSON(read_back_data);

        // Initialize JSONArrayDataBase
        JSONArrayDataBase.setJsonArray(jsonArray);

        // Convert JSON Array into Custom List Loader
        populateAdapter(JSONArrayDataBase.getJsonArray(), CountryListRowItemDataBase.getInstance().getValues());

        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        mAdapterRecyclerView = new SimpleItemRecyclerViewAdapter(this,
                CountryListRowItemDataBase.getInstance().getValues(),
                mTwoPane);
        recyclerView.setAdapter(mAdapterRecyclerView);
    }

    public void populateAdapter(JSONArray jsonArray, ArrayList<CountryListRowItem> rowItems) {
        int jsonArrayEntries = jsonArray.length();
        String country_name = null;
        String flag_url = null;

        try {
            // Check if rowItems are already initialized. If yes, then skip populating.
            if(!rowItems.isEmpty())
                return;

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArrayEntries; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Add Country items to Adapter
                CountryListRowItem item = new CountryListRowItem();

                // Set Individual Record Items.
                item.setId(i);
                country_name = jsonObject.optString("name").toString();
                item.setCountryName(country_name);

                flag_url = jsonObject.optString("flag").toString();
                item.setCountryFlagURL(flag_url);

                rowItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
