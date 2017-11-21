package com.unleashed.android.androidhackathontg.gui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unleashed.android.androidhackathontg.R;
import com.unleashed.android.androidhackathontg.customadapter.CountryListRowItem;
import com.unleashed.android.androidhackathontg.data.CountryListRowItemDataBase;

import java.util.ArrayList;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        String index = getArguments().getString(ARG_ITEM_ID);
        mItemArrayList = CountryListRowItemDataBase.getInstance().getValues();
        mItem = mItemArrayList.get(Integer.valueOf(index));

        // Populate Detail Screen with contents
        if (mItem != null) {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getCountryName());
            }
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getCountryFlagURL());
        }

        return rootView;
    }
}
