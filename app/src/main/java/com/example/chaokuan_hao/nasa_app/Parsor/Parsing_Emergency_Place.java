package com.example.chaokuan_hao.nasa_app.Parsor;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Convenient_Store;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Place;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parsing_Emergency_Place {

    public static ArrayList<Parameter_Place> extractFromTxt(Context context) {
        Log.d("Debugggger", "file before");
//        File file = new File("/Users/chaokuan-hao/Documents/NASA_Hackathon/NASA_APP/app/src/main/res/raw/rlf_lng_lat.txt");
        AssetManager am = context.getAssets();
        Log.d("Debugggger", "file after");
        //Read text from file
        ArrayList<Parameter_Place> Convenient_Store_List = new ArrayList<Parameter_Place>();
        try {

            Log.d("Debugggger", "Reader!!");
            InputStream inputStream = am.open("EmergencyPlace_LAT_LONG.txt");
            Log.d("Debugggger", "file after");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(",");
                String a = tmp[0];
                String b = tmp[1];
                Log.d("Debugggger", "a" + a +"b" + b);
                Parameter_Place store = new Parameter_Place("避難地點",a, b);
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
