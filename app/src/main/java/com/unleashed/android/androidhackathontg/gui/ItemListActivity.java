package com.unleashed.android.androidhackathontg.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unleashed.android.androidhackathontg.R;
import com.unleashed.android.androidhackathontg.customadapter.CountryListRowItem;
import com.unleashed.android.androidhackathontg.customadapter.CustomListViewAdapter;
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


    public CustomListViewAdapter customListViewAdapterObj;
    private Context mContext;

    // Create an Array of Items and a ArrayAdapter around it.
    public static ArrayList<CountryListRowItem> countryListRowItems = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mContext = this;

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


        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize all data structures during app startup.
        read_and_populate_databases();

    }

    private void read_and_populate_databases() {

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

        // Initialize JSONArrayDataBase
        JSONArrayDataBase.setJsonArray(jsonArray);

        // Convert JSON Array into Custom List Loader
        populateAdapter(JSONArrayDataBase.getJsonArray(), CountryListRowItemDataBase.getInstance().getValues());

    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this,
                CountryListRowItemDataBase.getInstance().getValues(),
                mTwoPane));
    }

    public void populateAdapter(JSONArray jsonArray, ArrayList<CountryListRowItem> RowItems) {
        int jsonArrayEntries = jsonArray.length();
        String country_name = null;
        String flag_url = null;

        try {
            // Check if RowItems is already initialized. If yes, then skip populating.
            if(!RowItems.isEmpty())
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

                RowItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private ArrayList<CountryListRowItem> mValues = CountryListRowItemDataBase.getInstance().getValues();
        private final boolean mTwoPane;


        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryListRowItem item = (CountryListRowItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, Integer.toString(item.getId()));
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, Integer.toString(item.getId()));

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      ArrayList<CountryListRowItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // Set Country Name
            holder.mCountryName.setText(mValues.get(position).getCountryName());

            // Set Country Flag
            //holder.mFlagImage.setText(mValues.get(position).getCountryFlagURL());
            Glide.with(this.mParentActivity)
                    .load(mValues.get(position).getCountryFlagURL())
                    .centerCrop()
                    .placeholder(R.drawable.user_default_image)
                    .crossFade()
                    .into(holder.mFlagImage);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mCountryName;
            final ImageView mFlagImage;

            ViewHolder(View view) {
                super(view);
                mCountryName = (TextView) view.findViewById(R.id.tv_countryname);
                mFlagImage = (ImageView) view.findViewById(R.id.iv_countryflag);
            }
        }
    }
}
