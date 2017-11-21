package com.unleashed.android.androidhackathontg.customadapter;

import android.os.Parcel;
import android.os.Parcelable;


public class CountryListRowItem implements Parcelable {

    private int id;
    private String country_name;
    private String country_flag_url;

    public static final Creator<CountryListRowItem> CREATOR = new Creator<CountryListRowItem>() {
        @Override
        public CountryListRowItem createFromParcel(Parcel in) {
            return new CountryListRowItem(in);
        }

        @Override
        public CountryListRowItem[] newArray(int size) {
            return new CountryListRowItem[size];
        }
    };

    public int getId() { return id; }
    public String getCountryName(){
        return country_name;
    }
    public String getCountryFlagURL(){
        return country_flag_url;
    }

    public void setId(int _id) { id = _id; }
    public void setCountryName(String countryName){
        country_name = countryName;
    }
    public void setCountryFlagURL(String countryFlagURL){
        country_flag_url = countryFlagURL;
    }


    public CountryListRowItem()
    {

    }


    private CountryListRowItem(Parcel in) {
        id = in.readInt();
        country_name = in.readString();
        country_flag_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(country_name);
        parcel.writeString(country_flag_url);

    }
}
