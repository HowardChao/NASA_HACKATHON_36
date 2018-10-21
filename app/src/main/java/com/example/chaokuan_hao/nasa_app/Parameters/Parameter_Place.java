package com.example.chaokuan_hao.nasa_app.Parameters;

public class Parameter_Place {
    private String Type;
    private String Lat;
    private String Lng;
    private String Address = "";
    private String Detail = "";

    public Parameter_Place(String type, String lat, String lng){
        Type = type;
        Lat = lat;
        Lng = lng;
    }

    public String getType() { return Type; }
    public String getLat() { return Lat; }
    public String getLng() { return Lng; }
    public String getAddress() { return Address; }
    public String getDetail() { return Detail; }

    public void setAddress(String s){ Address = s; }
    public void setDetail(String s){Detail = s; }
}
