package com.example.chaokuan_hao.nasa_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.chaokuan_hao.nasa_app.Utils.BottomNavigationViewHelper;
import com.example.chaokuan_hao.nasa_app.VolcanoAlarm.DisasterService;
import com.example.chaokuan_hao.nasa_app.VolcanoAlarm.UserData;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by chaokuanhao on 21/11/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = MainActivity.this;

//    private enum appBarState {
//        EXPANDED,
//        COLLAPSED,
//        IDLE
//    }
//
//    private appBarState mCurrentState = appBarState.IDLE;


//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.bottom_navigation_list_child:
//
//                    return true;
//
//                case R.id.bottom_navigation_map_child:
//                    Intent intent = new Intent();
//                    intent.setClass( Information_List_Activity.this, Information_Map_Activity.class);
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        }
//
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point_left_menu);

        //Permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        Intent intent = new Intent(MainActivity.this, DisasterService.class);
        stopService(intent);
        startService(intent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_entry);
        setSupportActionBar(toolbar);               // this one is for old version

        // open drawerlayout before find navigationView
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_entry);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // this one is find the main menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        UserData u = new UserData(MainActivity.this);



//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_list);
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        setupBottomNavigationView();
//        initInstanceDrawer();
//        setupViewPager();

        // open drawerlayout before find navigationView
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_checklist);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        // this one is find the main menu
        //httprequest
//        QueryUtils q = new QueryUtils();
//        q.run();
    }

    /**
     * This function is to create the main menu on the toolbar. It is called option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_additional, menu);
        return true;
    }

    /**
     * This function is to send the MenuItem to the menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_additional_setting) {
            return true;
        }
        if ( id == R.id.menu_additional_rateUs){
            return true;
        }
        if ( id == R.id.menu_additional_help){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * set up the viewPager to add the fragment
     */
//    private void setupViewPager(){
//        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment( new Information_AED_Fragment());
//        adapter.addFragment( new Information_Hydrant_Fragment());
//        //adapter.addFragment( new Information_Police_Station_Fragment());
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_container);
//        viewPager.setAdapter(adapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_list);
//        tabLayout.setupWithViewPager(viewPager);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                switch (position){
////                    case 0:
////                        fab.show();
////                        break;
////                    case 1:
////                        fab.show();
////                        break;
////                    case 2:
////                        fab.show();
////                        break;
////                    default:
////                        fab.hide();
////                        break;
////                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        tabLayout.getTabAt(0).setText("緊急用具");
//        tabLayout.getTabAt(1).setText("緊急地點");
//        //tabLayout.getTabAt(2).setText("警察局");
//    }


    /**
     * Called when an item in the navigation menu is selected.~~ the main menu !!
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ( id == R.id.activity_main_menu_ic_main_page){
        } else if ( id == R.id.activity_main_menu_ic_basic_information){
            Intent intent = new Intent();
            intent.setClass( MainActivity.this, BasicInformation.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if (id == R.id.activity_main_menu_ic_addi_knowledge) {
            Intent intent = new Intent();
            intent.setClass( MainActivity.this, AdditionalKnowledge.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if ( id == R.id.activity_main_menu_ic_map ) {
            Intent intent = new Intent();
            intent.setClass( MainActivity.this, MapActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if (id == R.id.activity_main_menu_ic_coreport ) {
            Intent intent = new Intent();
            intent.setClass( MainActivity.this, CoreportActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_entry);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
