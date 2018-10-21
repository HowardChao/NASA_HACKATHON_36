package com.example.chaokuan_hao.nasa_app.Parameters;
/**
 * Created by chaokuanhao on 25/11/2017.
 */

public class Parameter_Coreport_Point {
    private String mLatitude;
    private String mLogitude;
    private String mAccidentType;

    public Parameter_Coreport_Point( String Latitude, String Longitude, String Accident) {
        mLatitude = Latitude;
        mLogitude = Longitude;
        mAccidentType = Accident;
    }


    public String getmLatitude() {
        return mLatitude;
    }

    public String getmLogitude() {
        return mLogitude;
    }

    public String getmAccidentType() {
        return mAccidentType;
    }


    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLogitude(String mLogitude) {
        this.mLogitude = mLogitude;
    }

    public void setmAccidentType(String mAccidentType) {
        this.mAccidentType = mAccidentType;
    }
}
