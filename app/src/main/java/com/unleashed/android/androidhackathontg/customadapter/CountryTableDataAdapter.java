package com.unleashed.android.androidhackathontg.customadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unleashed.android.androidhackathontg.R;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by nxf29631 on 11/28/2017.
 */


public class CountryTableDataAdapter extends TableDataAdapter<CountryTableCols> {

    public CountryTableDataAdapter(Context context, List<CountryTableCols> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CountryTableCols tableCols = getRowData(rowIndex);
        View renderedView = null;
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                ((int) ViewGroup.LayoutParams.MATCH_PARENT,(int) ViewGroup.LayoutParams.MATCH_PARENT);
        switch (columnIndex) {
            case 0:
                renderedView = new TextView(parentView.getContext());
                ((TextView)renderedView).setText(tableCols.desc);
                ((TextView)renderedView).setLayoutParams(params);
                ((TextView)renderedView).setTextColor(getResources().getColor(R.color.black));
                ((TextView)renderedView).setBackgroundColor(Color.parseColor("#FF0000"));

                break;
            case 1:
                renderedView = new TextView(parentView.getContext());
                ((TextView)renderedView).setText(tableCols.detail);
                ((TextView)renderedView).setLayoutParams(params);
                ((TextView)renderedView).setTextColor(getResources().getColor(R.color.black));
                ((TextView)renderedView).setBackgroundColor(Color.parseColor("#FF0000"));
                break;

        }

        return renderedView;
    }


}
