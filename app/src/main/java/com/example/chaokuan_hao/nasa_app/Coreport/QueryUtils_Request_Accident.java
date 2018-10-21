package com.example.chaokuan_hao.nasa_app.Coreport;
import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Coreport_Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 2017/7/15.
 */

//the helper function relating to requesting nad receiving the data from Azure
public final class QueryUtils_Request_Accident {
    private static final String LOG_TAG = QueryUtils_Request_Accident.class.getSimpleName();
    private QueryUtils_Request_Accident(){
    }

    public static List<Parameter_Coreport_Point> request_Accident_Point(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Parameter_Coreport_Point> parameter_coreport_point = extractFeatureFormJson(jsonResponse);
        return  parameter_coreport_point;
    }

    private  static  URL createUrl (String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000000);
            urlConnection.setConnectTimeout(1500000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error response code:"+ urlConnection.getResponseCode());
            }
        }
        catch (IOException e ){
            Log.e(LOG_TAG, "Problem retrieving from the JSON results", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static List<Parameter_Coreport_Point> extractFeatureFormJson (String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        List<Parameter_Coreport_Point> accident_point_coreports_list = new ArrayList<Parameter_Coreport_Point>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
//            Log.d("RRRRR", "Fuckeeeee");
//            Log.d("RRRRR", "Fuckeeeeee");
            for ( int j = 1; j < root.length(); j++ ){
                JSONArray accident_inside = root.getJSONArray(Integer.toString(j));
                for ( int i = 0; i < accident_inside.length(); i++ ) {
                    JSONArray elementsWrapper = accident_inside.getJSONArray(i);
                    String Latitude = elementsWrapper.getString(0);
                    String Longitude = elementsWrapper.getString(1);
                    Log.d("RRRRR", Latitude);
                    Log.d("RRRRR", Longitude);
                    Log.d("RRRRR", Integer.toString(j));
                    Parameter_Coreport_Point accident_point_coreports = new Parameter_Coreport_Point( Latitude, Longitude, Integer.toString(j));
                    accident_point_coreports_list.add(accident_point_coreports);
                }
            }

        }catch (JSONException e ){
            Log.e(LOG_TAG, "Problem parsing the JSON results" + e);
        }
        return accident_point_coreports_list;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while ( line !=  null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return  output.toString();
    }
}
