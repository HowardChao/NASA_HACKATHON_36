package com.example.chaokuan_hao.nasa_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chaokuan_hao.nasa_app.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class AdditionalKnowledge extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = AdditionalKnowledge.class.getSimpleName();
    private Context mContext = AdditionalKnowledge.this;
    private static final int ACTIVITY_NUM = 1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_information_left_menu);
        String getIntentValue = getIntent().getStringExtra("ACCESS");
        String mType = "earthquake";
        if(getIntentValue != null) {
            Log.d("HowardTag", getIntentValue);
            if (getIntentValue.equals("volcano") || getIntentValue.equals("earthquake")) {
                mType = getIntentValue;
            }
        }
        Log.d("HowardTag", mType);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_additional_information);
        setSupportActionBar(toolbar);               // this one is for old version

        // open drawerlayout before find navigationView
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_additional_information);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // this one is find the main menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupBottomNavigationView();
        TextView textView = (TextView) findViewById(R.id.id_textview);
        ImageView imageView = (ImageView) findViewById(R.id.id_imageview);
        if (mType.equals("volcano")) {
            textView.setText("台北大屯火山以往一直被視為休火山或死火山，然而近來多項突破性研究，證實其在近ㄉ20萬年仍有噴發，實為活火山，必須多加防範．由於大屯火山長久未噴發，台灣民眾多半對火山爆發應對措施較不熟悉，但只要切記災害發生時勿驚慌、非必要勿隨意外出行動，並配合政府疏散逃離，及能將災害將到最低．");
        } else if (mType.equals("earthquake")){
            textView.setText("台灣位於環太平洋火山地震代，地震發生頻繁，為了保護個人與家人的生命安全，我們應當對地震有所了解．地震由板塊運動所造成，難以預警，平時應多熟悉逃生路線、逃生需備物品．遇到災害發生時方能冷靜以對，減少對生命財產的危害到最低．\n");
        }
    }

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
            Intent intent = new Intent();
            intent.setClass( AdditionalKnowledge.this, MainActivity.class);
            startActivity(intent);
            AdditionalKnowledge.this.finish();
        } else if ( id == R.id.activity_main_menu_ic_basic_information){
            Intent intent = new Intent();
            intent.setClass( AdditionalKnowledge.this, BasicInformation.class);
            startActivity(intent);
            AdditionalKnowledge.this.finish();
        } else if (id == R.id.activity_main_menu_ic_addi_knowledge) {
            // Do not do anything
        } else if ( id == R.id.activity_main_menu_ic_map ) {
            Intent intent = new Intent();
            intent.setClass( AdditionalKnowledge.this, MapActivity.class);
            startActivity(intent);
            AdditionalKnowledge.this.finish();
        } else if (id == R.id.activity_main_menu_ic_coreport ) {
            Intent intent = new Intent();
            intent.setClass( AdditionalKnowledge.this, CoreportActivity.class);
            startActivity(intent);
            AdditionalKnowledge.this.finish();
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_additional_information);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * this function is to control the bottom navigationview for Information_activity!! called in Oncreate
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation_map);
        BottomNavigationViewHelper bottomNavigationViewHelper = null;
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        bottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
