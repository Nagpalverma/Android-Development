package com.example.earthquakereport;

import android.os.Parcel;
import android.os.Parcelable;

public class Earthquake_items_prototype implements Parcelable{
    private double magnitude;

    private  String location;
    private long time;
    private String url;

    public Earthquake_items_prototype(double magnitude, String url, String location, long time) {
        this.magnitude = magnitude;

        this.location =location;
        this.time = time;
        this.url=url;
    }

    protected Earthquake_items_prototype(Parcel in) {
        magnitude = in.readDouble();
        location = in.readString();
        time = in.readLong();
        url = in.readString();
    }

    public static final Creator<Earthquake_items_prototype> CREATOR = new Creator<Earthquake_items_prototype>() {
        @Override
        public Earthquake_items_prototype createFromParcel(Parcel in) {
            return new Earthquake_items_prototype(in);
        }

        @Override
        public Earthquake_items_prototype[] newArray(int size) {
            return new Earthquake_items_prototype[size];
        }
    };

    public double getMagnitude() {
        return magnitude;
    }

    public String getUrl() {
        return url;
    }



    public String getlocation() {
        return location;
    }

    public long getTime() {
        return time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(magnitude);
        dest.writeString(location);
        dest.writeLong(time);
        dest.writeString(url);
    }
}
