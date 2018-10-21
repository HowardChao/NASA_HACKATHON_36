package com.example.chaokuan_hao.nasa_app.Parameters;
public class Parameter_AirQuality {
    private String mName;
    private Double mLong;
    private Double mLat;
    private Double mAqi;
    private Double mO3;
    private Double mPm25;
    private Double mPm10;
    private Double mCo;
    private Double mSo2;
    private Double mNo2;

    public Parameter_AirQuality ( String Name, Double Long, Double Lat, Double Aqi, Double O3,
                                  Double Pm25, Double Pm10, Double Co, Double So2, Double No2){
        mName = Name;
        mLong = Long;
        mLat = Lat;
        mAqi = Aqi;
        mO3 = O3;
        mPm25 = Pm25;
        mPm10 = Pm10;
        mCo = Co;
        mSo2 = So2;
        mNo2 = No2;
    }

    public Double getmAqi() {
        return mAqi;
    }

    public Double getmCo() {
        return mCo;
    }

    public Double getmLong() {
        return mLong;
    }

    public Double getmLat() { return mLat;}

    public String getmName() {
        return mName;
    }

    public Double getmNo2() {
        return mNo2;
    }

    public Double getmO3() { return mO3; }

    public Double getmPm10() {
        return mPm10;
    }

    public Double getmPm25() {
        return mPm25;
    }

    public Double getmSo2() {
        return mSo2;
    }
}
