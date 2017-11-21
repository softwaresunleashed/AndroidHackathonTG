package com.unleashed.android.androidhackathontg;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.unleashed.android.androidhackathontg.customadapter.CountryListRowItem;
import com.unleashed.android.androidhackathontg.customadapter.CustomListViewAdapter;
import com.unleashed.android.androidhackathontg.helpers.constants.Constants;
import com.unleashed.android.androidhackathontg.helpers.jsonparser.JsonParser;
import com.unleashed.android.androidhackathontg.helpers.storage.JSONPersistentStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener{

    private TextView textView;
    private ListView lv_countryNamesRecords;
    private SearchView mSearchView;     // Search View Edit Box for filtering out
    private Context mContext;

    public CustomListViewAdapter customListViewObj;

    // Create an Array of Items and a ArrayAdapter around it.
    public static ArrayList<CountryListRowItem> countryListRowItems = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mContext = this;

        textView = (TextView)findViewById(R.id.temp_text);



        String attempt1 = JSONPersistentStorage.getJSONArray(getApplicationContext());
        if(attempt1.isEmpty()){
            // Store data received from network to persistant storage
            String temp_data_received = "{ \"root\" :" + Constants.JSON_response + "}";
            JSONPersistentStorage.storeJSONArray(getApplicationContext(), temp_data_received);
        }


        // Read back string from Persistant storage
        String read_back_data = JSONPersistentStorage.getJSONArray(getApplicationContext());

        // Parse JSON Response data into JSON Array
        JSONArray jsonArray = new JsonParser().parseJSON(read_back_data);


        // Convert JSON Array into Custom List Loader
        countryListRowItems = new ArrayList<CountryListRowItem>();
        populateAdapter(jsonArray, countryListRowItems);

        // Initialize the Search View for phone contacts.
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint(getString(R.string.search_view_hint));

        // Initialize the Country List Adapter
        customListViewObj = new CustomListViewAdapter(mContext, R.layout.custom_list_item, countryListRowItems);

        // Set the Array Adapter containting items, to ListView
        lv_countryNamesRecords = (ListView)findViewById(R.id.listView_custom_records);
        lv_countryNamesRecords.setAdapter(customListViewObj);

        // Implement the listener
        lv_countryNamesRecords.setOnItemClickListener(this);
        lv_countryNamesRecords.setItemsCanFocus(false);
        lv_countryNamesRecords.setTextFilterEnabled(true);     // needed for search view for phone contacts.


        // Add OnScrollListener() : This was needed to address the strange problem :
        // that List View was not clickable once I scrolled it up / down.
        lv_countryNamesRecords.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    lv_countryNamesRecords.invalidateViews();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String strPhoneContactToSearch) {
        if (TextUtils.isEmpty(strPhoneContactToSearch)) {
            lv_countryNamesRecords.clearTextFilter();
        } else {
            lv_countryNamesRecords.setFilterText(strPhoneContactToSearch.toString());
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


        // TODO :
        // Get the index which is clicked, and then pass Details Activity with index clicked.
        // Inside details activity , populate the screen with data inside adapter.



    }


    public void populateAdapter(JSONArray jsonArray, ArrayList<CountryListRowItem> RowItems) {
        int jsonArrayEntries = jsonArray.length();
        String country_name = null;
        String flag_url = null;

        try {
            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArrayEntries; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                // Add Country items to Adapter
                CountryListRowItem item = new CountryListRowItem();


                // Set Individual Record Items.
                country_name = jsonObject.optString("name").toString();
                item.setCountryName(country_name);

                flag_url = jsonObject.optString("flag").toString();
                item.setCountryFlagURL(flag_url);

                RowItems.add(item);

                // int id = Integer.parseInt(jsonObject.optString("id").toString());
                // float salary = Float.parseFloat(jsonObject.optString("salary").toString());

                // Reset all the variables to NULL before next iteration
                country_name = null;
                flag_url = null;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
