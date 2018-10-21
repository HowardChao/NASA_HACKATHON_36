package com.example.chaokuan_hao.nasa_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.chaokuan_hao.nasa_app.VolcanoAlarm.UserActivity;
import com.example.chaokuan_hao.nasa_app.VolcanoAlarm.UserData;

public class BasicInformation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    UserData mUserData;

    //view
    Switch[] mSwitch = new Switch[5];
    EditText mTelEdit, mNameEdit;
    Button mOKButton;
    CompoundButton.OnCheckedChangeListener mCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Switch s = (Switch)buttonView;

            if(s == mSwitch[0])mUserData.setHasChild(isChecked);
            else if(s == mSwitch[1])mUserData.setHasSenior(isChecked);
            else if(s == mSwitch[2])mUserData.setHasMedicine(isChecked);
            else if(s == mSwitch[3])mUserData.setHasPets(isChecked);
            else if(s == mSwitch[4])mUserData.setIsDisable(isChecked);
        }
    };

    View.OnClickListener mOKListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            save();
//            BasicInformation.this.finish();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information_left_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_basic_information);
        setSupportActionBar(toolbar);               // this one is for old version

        // open drawerlayout before find navigationView
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_basic_information);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // this one is find the main menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mUserData = new UserData(BasicInformation.this);

        //view
        Typeface microsoft = Typeface.createFromAsset(getResources().getAssets(),"fonts/microsoft.ttf");
        ((TextView)findViewById(R.id.user_tel_textView)).setTypeface(microsoft);
        ((TextView)findViewById(R.id.user_name_textView)).setTypeface(microsoft);
        mTelEdit = findViewById(R.id.user_tel_editText);
        mNameEdit = findViewById(R.id.user_name_editText);

        mOKButton = findViewById(R.id.user_check_button);
        mOKButton.setTypeface(microsoft);
        mOKButton.setOnClickListener(mOKListener);


        mSwitch[0] = findViewById(R.id.user_switch1);
        mSwitch[1] = findViewById(R.id.user_switch2);
        mSwitch[2] = findViewById(R.id.user_switch3);
        mSwitch[3] = findViewById(R.id.user_switch4);
        mSwitch[4] = findViewById(R.id.user_switch5);
        mSwitch[0].setChecked(mUserData.hasChild());
        mSwitch[1].setChecked(mUserData.hasSenior());
        mSwitch[2].setChecked(mUserData.hasMedicine());
        mSwitch[3].setChecked(mUserData.hasPets());
        mSwitch[4].setChecked(mUserData.isDisable());
        mTelEdit.setText(mUserData.getContact() );
        mNameEdit.setText(mUserData.getName());

        for (Switch aMSwitch : mSwitch) {
            aMSwitch.setTypeface(microsoft);
            aMSwitch.setOnCheckedChangeListener(mCheckListener);
        }

        setTitle("基本資料");

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ( id == R.id.activity_main_menu_ic_main_page){
            Intent intent = new Intent();
            intent.setClass( BasicInformation.this, MainActivity.class);
            startActivity(intent);
            BasicInformation.this.finish();
        } else if ( id == R.id.activity_main_menu_ic_basic_information){
            // Do not do anything
        } else if (id == R.id.activity_main_menu_ic_addi_knowledge) {
            Intent intent = new Intent();
            intent.setClass( BasicInformation.this, AdditionalKnowledge.class);
            startActivity(intent);
            BasicInformation.this.finish();
        } else if ( id == R.id.activity_main_menu_ic_map ) {
            Intent intent = new Intent();
            intent.setClass( BasicInformation.this, MapActivity.class);
            startActivity(intent);
            BasicInformation.this.finish();
        } else if (id == R.id.activity_main_menu_ic_coreport ) {
            Intent intent = new Intent();
            intent.setClass( BasicInformation.this, CoreportActivity.class);
            startActivity(intent);
            BasicInformation.this.finish();
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_basic_information);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void save(){
        String tel = mTelEdit.getText().toString();
        if(tel.equals(""))tel = "0";
        mUserData.save(tel, mNameEdit.getText().toString());
    }
}
