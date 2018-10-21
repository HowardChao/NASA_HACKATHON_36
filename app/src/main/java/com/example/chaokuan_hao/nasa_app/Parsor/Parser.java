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

public class Parser {

    static BufferedReader br;

    public static ArrayList<Parameter_Place> extractFromTxt(Context context,String FileName, String type, int lastSplit) {
        Log.v("MyTag", "Parse/extractFromTxt: init");
        AssetManager am = context.getAssets();

        //Read text from file
        ArrayList<Parameter_Place> result = new ArrayList<Parameter_Place>();
        try {

            InputStream inputStream = am.open(FileName);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String temp;
            while ((temp = readUntil(',', false)) != null) {
                String lat = temp;
                String lng = readUntil(',', false);
                Log.v("MyTag" + type, lat + " " + lng);
                Parameter_Place place = new Parameter_Place(type, lat, lng);
                if(lastSplit != -1){
                    place.setAddress(readUntil(',', true));
                    place.setDetail(readUntil((char)lastSplit, true));
                }else place.setAddress(readUntil(' ', true));

                result.add(place);
            }
            br.close();



        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            Log.e("", "", e);
        }
        return result;
    }

    private static String readUntil(char c, boolean above128) throws IOException {
        StringBuilder result = new StringBuilder();
        boolean start = false;

        int temp;
        while((temp =  br.read()) != -1){
            if(temp >= 128 && !above128)continue;
            if(temp == 10)temp = (int)' ';

            if((char)temp ==  ' ' && !start)continue;
            else if ((char)temp == c){
                Log.v("MyTag", "Parser/readUntil: result: "+result.toString());
                while(result.charAt(result.length()-1) == ' ')result.deleteCharAt(result.length()-1);
                return result.toString();
            }
            else{
                start = true;
                result.append((char)temp);
            }
        }

        Log.v("MyTagError", "Parser/readUntil/last: result: "+result.toString());
        return start ? result.toString() : null;
    }
}
