package com.example.chaokuan_hao.nasa_app.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

public class Parameter_Convenient_Store {
    private String mLat;
    private String mLng;
    private String mType;

    public  Parameter_Convenient_Store(String inlat, String inlng, String inType) {
        mLat = inlat;
        mLng = inlng;
        mType = inType;
    }

    public String getLat() {return  mLat;}
    public String getLng() {return mLng; }
    public String getType() { return mType; }
}
