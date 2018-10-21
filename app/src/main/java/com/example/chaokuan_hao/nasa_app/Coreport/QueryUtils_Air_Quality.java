package com.example.chaokuan_hao.nasa_app.Coreport;

import android.text.TextUtils;
import android.util.Log;

import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_AirQuality;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_AirQuality;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 2017/7/15.
 */

//the helper function relating to requesting nad receiving the data from Azure
public final class QueryUtils_Air_Quality {
    private static final String LOG_TAG = QueryUtils_Report_Accident.class.getSimpleName();
    private QueryUtils_Air_Quality(){
    }

    public static List<Parameter_AirQuality> report_AirQuality(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Parameter_AirQuality> Parameter_AirQuality = extractFeatureFormJson(jsonResponse);
        return  Parameter_AirQuality;
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

    private static List<Parameter_AirQuality> extractFeatureFormJson (String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        List<Parameter_AirQuality> AirQuality = new ArrayList<Parameter_AirQuality>();
        try {
            JSONArray root = new JSONArray(jsonResponse);
            Log.d("AirQuality: ", "Inside extractFeatureFormJson");
            for (int i=0; i < root.length(); i++) {
                JSONObject jsonObject = root.getJSONObject(i);

                String getName = jsonObject.getString("name");
                JSONArray getLocation = jsonObject.getJSONArray("location");
                Double getLong = getLocation.getDouble(0);
                Double getLat = getLocation.getDouble(1);
                Double getAqi = jsonObject.getDouble("aqi");
                if (getAqi == null) getAqi = 0.0;
//                Double getO3 = jsonObject.getDouble("o3");
//                if (getO3 == null) getAqi = 0.0;
                Double getPm25 = jsonObject.getDouble("pm2.5");
                if (getPm25 == null) getAqi = 0.0;
                Double getPm10 = jsonObject.getDouble("pm10");
                if (getPm10 == null) getAqi = 0.0;
                Double getCo = jsonObject.getDouble("co");
                if (getCo == null) getAqi = 0.0;
                Double getSo2 = jsonObject.getDouble("so2");
                if (getSo2 == null) getAqi = 0.0;
                Double getNo2 = jsonObject.getDouble("no2");
                if (getNo2 == null) getAqi = 0.0;
                Log.d("AirQuality: ", getName);
                Log.d("AirQuality: ", String.valueOf(getLong));
                Log.d("AirQuality: ", String.valueOf(getLat));
                Log.d("AirQuality: ", String.valueOf(getAqi));
//                Log.d("AirQuality: ", String.valueOf(getO3));
                Log.d("AirQuality: ", String.valueOf(getPm25));
                Log.d("AirQuality: ", String.valueOf(getPm10));
                Log.d("AirQuality: ", String.valueOf(getCo));
                Log.d("AirQuality: ", String.valueOf(getSo2));
                Log.d("AirQuality: ", String.valueOf(getNo2));

//                String Name, String Long, String Lat, String Aqi, String O3,
//                        String Pm25, String Pm10, String Co, String So2, String No2){
                Parameter_AirQuality parameter_airQuality = new Parameter_AirQuality(getName, getLong,
                        getLat, getAqi, 0.0, getPm25, getPm10, getCo, getSo2, getNo2);
                AirQuality.add(parameter_airQuality);
            }
            Log.d(LOG_TAG, "\n Successful in paring JSON file \n");

        }catch (JSONException e ){
            Log.e(LOG_TAG, "Problem parsing the JSON results" + e);
        }
        return AirQuality;
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
