package com.unleashed.android.androidhackathontg.customadapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unleashed.android.androidhackathontg.R;
import com.unleashed.android.androidhackathontg.gui.ItemDetailActivity;
import com.unleashed.android.androidhackathontg.gui.ItemDetailFragment;
import com.unleashed.android.androidhackathontg.gui.ItemListActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private final ItemListActivity mParentActivity;
    private ArrayList<CountryListRowItem> mValues;// = CountryListRowItemDataBase.getInstance().getValues();
    private ArrayList<CountryListRowItem> mValuesFilteredList; // = CountryListRowItemDataBase.getInstance().getValues();
    private final boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter instance;

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

    public SimpleItemRecyclerViewAdapter getInstance() {
        return instance;
    }

    public SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                  ArrayList<CountryListRowItem> items,
                                  boolean twoPane) {
        mValues = items;
        mValuesFilteredList = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
        instance = this;
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
        return mValuesFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mValuesFilteredList = mValues;
                } else {

                    ArrayList<CountryListRowItem> filteredList = new ArrayList<>();

                    for (CountryListRowItem countryListSingleItem : mValues) {

                        if (countryListSingleItem.getCountryName().toLowerCase().contains(charString)) {

                            filteredList.add(countryListSingleItem);
                        }
                    }

                    mValuesFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mValuesFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mValuesFilteredList = (ArrayList<CountryListRowItem>) results.values;

                notifyDataSetChanged();
            }
        };
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
