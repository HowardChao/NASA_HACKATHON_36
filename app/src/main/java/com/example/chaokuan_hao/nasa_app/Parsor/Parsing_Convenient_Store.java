package com.example.chaokuan_hao.nasa_app.Parsor;

import android.content.Context;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Convenient_Store;

import android.content.res.AssetManager;
import android.widget.TextView;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parsing_Convenient_Store {
    public Parsing_Convenient_Store() {}
    public static ArrayList<Parameter_Convenient_Store> extractFromTxt(Context context) {
        Log.d("Debugggger", "file before");
//        File file = new File("/Users/chaokuan-hao/Documents/NASA_Hackathon/NASA_APP/app/src/main/res/raw/rlf_lng_lat.txt");
        AssetManager am = context.getAssets();
        Log.d("Debugggger", "file after");
        //Read text from file
        ArrayList<Parameter_Convenient_Store> Convenient_Store_List = new ArrayList<Parameter_Convenient_Store>();
        try {

            //萊爾富
            Log.d("Debugggger", "Reader!!");
            InputStream inputStream = am.open("rlf_lng_lat.txt");
            Log.d("Debugggger", "file after");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(",");
                String a = tmp[0];
                String b = tmp[1];
                Log.d("Debugggger", "a" + a +"b" + b);
                Parameter_Convenient_Store store = new Parameter_Convenient_Store(a, b, "萊爾富");
                Convenient_Store_List.add(store);
            }
            br.close();

            //OK
            Log.d("Debugggger", "Reader!!");
            inputStream = am.open("OK_LAT_LONG.txt");
            Log.d("Debugggger", "file after");
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(",");
                String a = tmp[0];
                String b = tmp[1];
                Log.d("Debugggger", "a" + a +"b" + b);
                Parameter_Convenient_Store store = new Parameter_Convenient_Store(a, b, "OK");
                Convenient_Store_List.add(store);
            }
            br.close();

            //義美
            Log.d("Debugggger", "Reader!!");
            inputStream = am.open("IMEI_LAT_LONG.txt");
            Log.d("Debugggger", "file after");
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(",");
                String a = tmp[0];
                String b = tmp[1];
                Log.d("Debugggger", "a" + a +"b" + b);
                Parameter_Convenient_Store store = new Parameter_Convenient_Store(a, b, "義美");
                Convenient_Store_List.add(store);
            }
            br.close();

            //7-11
            Log.d("Debugggger", "Reader!!");
            inputStream = am.open("7_11_LAT_LONG.txt");
            Log.d("Debugggger", "file after");
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(",");
                String a = tmp[0];
                String b = tmp[1];
                Log.d("Debugggger", "a" + a +"b" + b);
                Parameter_Convenient_Store store = new Parameter_Convenient_Store(a, b, "7-11");
                Convenient_Store_List.add(store);
            }
            br.close();

        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            Log.e("", "", e);
        }
        return Convenient_Store_List;
    }
}
