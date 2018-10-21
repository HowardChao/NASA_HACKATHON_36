package com.example.chaokuan_hao.nasa_app.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.chaokuan_hao.nasa_app.AdditionalKnowledge;
import com.example.chaokuan_hao.nasa_app.MapActivity;
import com.example.chaokuan_hao.nasa_app.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by chaokuanhao on 21/11/2017.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = BottomNavigationViewHelper.class.getSimpleName();


    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView : Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_list_volcano:
                        Intent intent1 = new Intent(context, AdditionalKnowledge.class);     //ACTIVITY_NUM = 0
                        intent1.putExtra("ACCESS", "volcano");
                        context.startActivity(intent1);
                        break;
                     case R.id.bottom_navigation_map_earthquake:
                        Intent intent2 = new Intent(context, AdditionalKnowledge.class);     //ACTIVITY_NUM = 1
                        intent2.putExtra("ACCESS", "earthquake");
                        context.startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }
}
