package com.example.chaokuan_hao.nasa_app.CheckList;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.chaokuan_hao.nasa_app.CoreportActivity;
import com.example.chaokuan_hao.nasa_app.MainActivity;
import com.example.chaokuan_hao.nasa_app.MapActivity;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_CheckList;
import com.example.chaokuan_hao.nasa_app.Parameters.CheckListParameterAdaptor;
import com.example.chaokuan_hao.nasa_app.R;
import com.example.chaokuan_hao.nasa_app.VolcanoAlarm.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Earthquake_Check_List_2 extends AppCompatActivity {
    private static final String LOG_TAG = Earthquake_Check_List_2.class.getSimpleName();
    private static final String USGS_REQUEST_URL = "http://192.168.0.110:5000/time/";
    private CheckListParameterAdaptor mAdapter;
    private List<Parameter_CheckList> result = new ArrayList<Parameter_CheckList>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Integer COUNTER_STATE = 1;

        super.onCreate(savedInstanceState);
//        Do after merge
        setContentView(R.layout.list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_volcano_1);
        setSupportActionBar(toolbar);               // this one is for old version
//        getActionBar().setHomeButtonEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.id_check_list_navigate);
        ImageView imageView_reverse = (ImageView) findViewById(R.id.id_check_list_navigate_reverse);

        ListView CheckListListView = findViewById(R.id.id_check_list);

        mAdapter = new CheckListParameterAdaptor(this, new ArrayList<Parameter_CheckList>());
        CheckListListView.setAdapter(mAdapter);
        String Main_title = "";
        try {
            JSONObject checkListJsonObj = new JSONObject(loadJSONFromAsset(this));
            JSONArray volcanoJsonArray = checkListJsonObj.getJSONArray("earthquake");
            JSONObject elementsInWrapper = volcanoJsonArray.getJSONObject(COUNTER_STATE);
            Main_title = elementsInWrapper.getString("title");
            JSONArray contextlist = elementsInWrapper.getJSONArray("list");
            for (int i = 0; i < contextlist.length(); i++) {
                String context = contextlist.getString(i);
                Parameter_CheckList route = new Parameter_CheckList(context);
                result.add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTitle(Main_title);
//        setTitleColor("red");
        mAdapter.addAll(result);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( Earthquake_Check_List_2.this, Earthquake_Check_List_3.class);
                startActivity(intent);
                Earthquake_Check_List_2.this.finish();
            }
        });

        imageView_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( Earthquake_Check_List_2.this, Earthquake_Check_List_1.class);
                startActivity(intent);
                Earthquake_Check_List_2.this.finish();
            }
        });
    }


    public String loadJSONFromAsset(Context context) {
        String json = null;
        InputStream is = null;
        try {
            Log.v("MyTag", "Volcano_Check_List_1/loadJSONFromAsset: dynamic file init");
            is = context.openFileInput(UserData.CheckListFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            Log.v("MyTag", "Volcano_Check_List_1/loadJSONFromAsset: dynamic file content:" + new String(buffer));
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.v("MyTagErr", "Volcano_Check_List_1/loadJSONFromAsset: fail dynamic file " + ex);

            try {
                is = context.getAssets().open("check_list.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }
        return json;
    }
}

