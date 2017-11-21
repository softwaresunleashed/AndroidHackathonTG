package com.unleashed.android.androidhackathontg.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.unleashed.android.androidhackathontg.R;

import java.util.ArrayList;


public class CustomListViewAdapter extends ArrayAdapter<CountryListRowItem> {

    private LayoutInflater inflater;
    private int mResource;
    private ViewGroup mParent;
    private CountryListRowItem rowItem;


    public ArrayList<CountryListRowItem> countryListRowItems;
    public ArrayList<CountryListRowItem> orig;


    public CustomListViewAdapter(Context context, int resource, ArrayList<CountryListRowItem> objects) {
        super(context, resource, objects);


        this.countryListRowItems = objects;

        // Activity class is inherited from Context. So we can typecase context object to Activity
        Activity activity = (Activity) context;
        inflater = activity.getWindow().getLayoutInflater();

        // Keep a record of Resource passed from initialising class
        mResource = resource;

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<CountryListRowItem> results = new ArrayList<CountryListRowItem>();
                if (orig == null){
                    // Make a copy of phonebook list at a different memory addres
                    // called only once.
                    orig = (ArrayList<CountryListRowItem>) countryListRowItems.clone();
                }

                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final CountryListRowItem g : orig) {
                            if (g.getCountryName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                }

                oReturn.values = results;

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                // Applying these operations to adapter
                clear();
                addAll((ArrayList<CountryListRowItem>) filterResults.values);

                // Now update the Adapter
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



    // getView() method is called every time a row is to be added in the ListView.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        rowItem = getItem(position);
        mParent = parent;

        final HolderRowItem holder;

        if (convertView == null) {
            // = createNewView();      //inflater.inflate(mResource, parent, false);
            // Instantiate view
            convertView = inflater.inflate(mResource, mParent, false);

            holder = new HolderRowItem();

            holder.country_name = (TextView)convertView.findViewById(R.id.tv_countryname);
            holder.country_name.setText(rowItem.getCountryName());

//            holder.country_flag = (ImageView) convertView.findViewById(R.id.iv_countryflag);
//            holder.country_flag.setImageURI(rowItem.getCountryFlagURL());
//
//

            convertView.setTag(holder);
        }else{
            // Gets the holder pointing to the views
            holder = (HolderRowItem) convertView.getTag();

            holder.country_name.setText(rowItem.getCountryName());
           // holder.country_flag.setImageURI(rowItem.getCountryFlagURL());
        }

        return convertView;
    }



}
