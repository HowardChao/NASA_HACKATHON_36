package com.example.chaokuan_hao.nasa_app.CheckList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.chaokuan_hao.nasa_app.Coreport.QueryUtils_Air_Quality;
import com.example.chaokuan_hao.nasa_app.Coreport.QueryUtils_Request_Accident;
import com.example.chaokuan_hao.nasa_app.MainActivity;
import com.example.chaokuan_hao.nasa_app.MapActivity;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_AirQuality;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Convenient_Store;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Coreport_Point;
import com.example.chaokuan_hao.nasa_app.Parameters.Parameter_Place;
import com.example.chaokuan_hao.nasa_app.Parsor.Parser;
import com.example.chaokuan_hao.nasa_app.Parsor.Parsing_Convenient_Store;
import com.example.chaokuan_hao.nasa_app.Parsor.Parsing_Emergency_Place;
import com.example.chaokuan_hao.nasa_app.R;
import com.example.chaokuan_hao.nasa_app.models.Adapter_PlaceInfo;
import com.example.chaokuan_hao.nasa_app.models.PlaceInfo;
import com.example.chaokuan_hao.nasa_app.models.Place_AutoComplete_Adapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Event_Map extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private List<Parameter_AirQuality> AirQualityPoints = new ArrayList<Parameter_AirQuality>();
    private static final String AIR_QUALITY_API = "http://114.34.123.174:5000/airquality";

    private static final LatLng VOLCANO_POSITION = new LatLng(25.17611, 121.52138);

    private static final String TAG = Event_Map.class.getSimpleName();
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = Event_Map.this;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mInfo, mMainMenu;
    private DrawerLayout mDrawer;
    private ImageView mPolice, mFireDep, mEmergencyPlace, mHydrant, mAED, mMonitor, mConvenientStore, mHospital;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Place_AutoComplete_Adapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;


    private static List<Parameter_Place> FireDep_Result = new ArrayList<Parameter_Place>();
    private static List<Parameter_Place> Police_Result = new ArrayList<Parameter_Place>();
    private static List<Parameter_Place> EmergencyPlace_Result = new ArrayList<Parameter_Place>();
    //    private static List<Parameter_Hydrant> Hydrant_Result = new ArrayList<Parameter_Hydrant>();
    private static List<Parameter_Place>  AED_Result = new ArrayList<Parameter_Place>();
    //    private static List<Parameter_Monitor> Monitor_Result = new ArrayList<Parameter_Monitor>();
    private static List<Parameter_Convenient_Store> Convenient_Store_Result = new ArrayList<Parameter_Convenient_Store>();
    private static List<Parameter_Place> Hospital_Result = new ArrayList<Parameter_Place>();
    //
//    private JSON_Parsing_FireDep json_parsing_fireDep = new JSON_Parsing_FireDep();
//    private JSON_Parsing_Police json_parsing_police = new JSON_Parsing_Police();
//    private JSON_Parsing_EmergencyPlace json_parsing_emergencyPlace = new JSON_Parsing_EmergencyPlace();
//    private JSON_Parsing_Hydrant json_parsing_hydrant = new JSON_Parsing_Hydrant();
//    private JSON_Parsing_AED json_parsing_aed = new JSON_Parsing_AED();
    private Parsing_Convenient_Store parsing_convenient_store = new Parsing_Convenient_Store();
//    private JSON_Parsing_Hospital json_parsing_hospital = new JSON_Parsing_Hospital();

    private static boolean FireDep_Buttom_is_clicked = false;
    private static boolean Police_Buttom_is_clicked = false;
    private static boolean Accident_Buttom_is_clicked = false;
    private static boolean EmergencyPlace_Buttom_is_clicked = false;
    private static boolean Hydrant_Buttom_is_clicked = false;
    private static boolean AED_Buttom_is_clicked = false;
    private static boolean Monitor_Buttom_is_clicked = false;
    private static boolean Convenient_Buttom_is_clicked = false;
    private static boolean Hospital_Buttom_is_clicked = false;

    private List<Marker> markers_fireDep = new ArrayList<Marker>();
    private List<Marker> markers_police = new ArrayList<Marker>();
    private List<Marker> markers_accident_point = new ArrayList<Marker>();
    private List<Marker> markers_emergency_place_point = new ArrayList<Marker>();
    private List<Marker> markers_hydrant_point = new ArrayList<Marker>();
    private List<Marker> markers_aed_point = new ArrayList<Marker>();
    private List<Marker> markers_convenience_store = new ArrayList<Marker>();
    private List<Marker> markers_ruleshot_point = new ArrayList<Marker>();
    private List<Marker> markers_hospital_point = new ArrayList<Marker>();
    private List<Marker> markers_airquality_station = new ArrayList<Marker>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_action_gps);
        mInfo = (ImageView) findViewById(R.id.place_info);
//        mPlacePicker = (ImageView) findViewById( R.id.place_picker);
        mMainMenu = (ImageView) findViewById( R.id.main_menu);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);

        mPolice = (ImageView) findViewById(R.id.police_station);
        mFireDep = (ImageView) findViewById(R.id.fireDep);
        mEmergencyPlace = (ImageView) findViewById(R.id.emergencyPlace);
        mAED = (ImageView) findViewById(R.id.AED);
        mConvenientStore = (ImageView) findViewById( R.id.convenientstore);
        mHospital = (ImageView) findViewById(R.id.hospital);

//        Place information
//        Police_Result = json_parsing_police.extractJsonfrom(mContext);
//        FireDep_Result = json_parsing_fireDep.extractJsonfrom(mContext);
//        EmergencyPlace_Result = json_parsing_emergencyPlace.extractJsonfrom(mContext);
//        Hydrant_Result = json_parsing_hydrant.extractJsonfrom(mContext);
//        AED_Result = json_parsing_aed.extractJsonfrom(mContext);
////        Monitor_Result =
//        Hospital_Result = json_parsing_hospital.extractJsonfrom(mContext);

        getLocationPermission();

//        setupBottomNavigationView();
        Convenient_Store_Result = parsing_convenient_store.extractFromTxt(mContext);
        Police_Result = Parser.extractFromTxt(mContext, "police_station.txt", "警察局", ' ');
        EmergencyPlace_Result = Parsing_Emergency_Place.extractFromTxt(mContext);
        FireDep_Result = Parser.extractFromTxt(mContext, "FireDep.txt", "消防局", ' ');
        AED_Result = Parser.extractFromTxt(mContext, "AED.txt", "AED", -1);
        Hospital_Result = Parser.extractFromTxt(mContext, "hospital.txt", "醫院", ' ');

        mMainMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_go_back_normal));



        //update muti-thread
        mHandlerThread = new HandlerThread("mRunnable");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mUIHandler = new Handler();
        mHandler.post(mRunnable);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar_map);         // this one is for old version
//        setSupportActionBar(toolbar);
        // so that toolbar can replace the function of actionbar.
        // return an actionbar that can control the given toolbar
//        toolbar.setTitle("新竹市地圖");
        //~~Important ! Manifest > java > xml


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//         this one is find the main menu

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        AirQualityAsyncTask task1 = new AirQualityAsyncTask();
        task1.execute(AIR_QUALITY_API);
        //        PoliceDepAsyncTask task02 = new PoliceDepAsyncTask();
//        task02.execute(USGS_REQUEST_URL_FIREDEP_POLICE);
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(Event_Map.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new Place_AutoComplete_Adapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked place info");
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{
                        Log.d(TAG, "onClick: place info: " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
                }
            }
        });

        mMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Go back to normal map !!");
                Intent intent = new Intent();
                intent.setClass( Event_Map.this, MapActivity.class);
                startActivity(intent);
                Event_Map.this.finish();
//                mDrawer.openDrawer(Gravity.LEFT);
            }
        });


        mMap.addMarker(new MarkerOptions().position(VOLCANO_POSITION).title("Volcano")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.volcano)));
        /********
         *  Start information about all data show on map
         */


        mPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Event_Map.this));

                if( Police_Buttom_is_clicked == true ){
                    Police_Buttom_is_clicked = false;
                    TextView textView = (TextView) findViewById( R.id.police_station_textview);
                    textView.setText("警察局");

                    Toast.makeText( Event_Map.this, "關起警察局圖例", Toast.LENGTH_SHORT).show();

                }
                else if ( Police_Buttom_is_clicked == false ){
                    Police_Buttom_is_clicked = true;
                    TextView textView = (TextView) findViewById( R.id.police_station_textview);
                    textView.setText("關閉圖例");

                    Toast.makeText( Event_Map.this, "打開警察局圖例", Toast.LENGTH_SHORT).show();

                }
                try {
                    if ( Police_Buttom_is_clicked ){

//                        Log.d("Howard ~~~~~" , String.valueOf(Police_Result.size()));
                        for ( int i = 0; i < Police_Result.size(); i++ ){
                            LatLng latLng = new LatLng( Double.parseDouble( Police_Result.get(i).getLat()), Double.parseDouble( Police_Result.get(i).getLng()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng)
                                    .title(Police_Result.get(i).getDetail())
                                    .snippet( "地址：" + Police_Result.get(i).getAddress())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_onmap_police));
                            markers_police.add(mMap.addMarker(markerOptions));
                        }
                    }
                    else{
                        for ( int i = 0; i < markers_police.size(); i++ ){
                            markers_police.get(i).remove();
                        }
                    }
                }
                catch ( NullPointerException e){

                }
            }
        });
//
        mFireDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Event_Map.this));
                Log.d("Howard ~~~~~" , String.valueOf(FireDep_Result.size() ));

                if( FireDep_Buttom_is_clicked == true ){
                    FireDep_Buttom_is_clicked = false;
                    TextView textView = (TextView) findViewById(R.id.fireDep_textview);
                    textView.setText("消防局");

                    Toast.makeText( Event_Map.this, "關起消防局圖例", Toast.LENGTH_SHORT).show();

                }
                else if ( FireDep_Buttom_is_clicked == false ){
                    FireDep_Buttom_is_clicked = true;
                    TextView textView = (TextView) findViewById(R.id.fireDep_textview);
                    textView.setText("關閉圖例");

                    Toast.makeText( Event_Map.this, "打開消防局圖例", Toast.LENGTH_SHORT).show();

                }
                try {
                    if ( FireDep_Buttom_is_clicked ){
                        for ( int i = 0; i < FireDep_Result.size(); i++ ){
                            Log.d(TAG, '\n' + FireDep_Result.get(i).getLat() + '\t' +  FireDep_Result.get(i).getLng() + '\n' );
                            LatLng latLng = new LatLng( Double.parseDouble(FireDep_Result.get(i).getLat()), Double.parseDouble(FireDep_Result.get(i).getLng()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng)
                                    .title(FireDep_Result.get(i).getDetail())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_onmap_firedep));
                            markers_fireDep.add(mMap.addMarker(markerOptions));
                        }
                    }
                    else{
                        for ( int i = 0; i < markers_fireDep.size(); i++ ){
                            markers_fireDep.get(i).remove();
                        }
                    }
                }
                catch ( NullPointerException e){
                }
            }
        });

        mEmergencyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Event_Map.this));
                Log.d("Howard ~~~~~" , String.valueOf(EmergencyPlace_Result.size() ));

                if( EmergencyPlace_Buttom_is_clicked == true ){
                    EmergencyPlace_Buttom_is_clicked = false;
                    TextView textView = (TextView) findViewById(R.id.emergencyPlace_textview);
                    textView.setText("避難場所");

                    Toast.makeText( Event_Map.this, "關起緊急避難場所圖例", Toast.LENGTH_SHORT).show();

                }
                else if ( EmergencyPlace_Buttom_is_clicked == false ){
                    EmergencyPlace_Buttom_is_clicked = true;
                    TextView textView = (TextView) findViewById(R.id.emergencyPlace_textview);
                    textView.setText("關閉圖例");

                    Toast.makeText( Event_Map.this, "打開緊急避難場所圖例", Toast.LENGTH_SHORT).show();

                }
                try {
                    if ( EmergencyPlace_Buttom_is_clicked ){
                        for ( int i = 0; i < EmergencyPlace_Result.size(); i++ ){
                            Log.d(TAG, '\n' + EmergencyPlace_Result.get(i).getLat() + '\t' +  EmergencyPlace_Result.get(i).getLng() + '\n' );
                            LatLng latLng = new LatLng( Double.parseDouble(EmergencyPlace_Result.get(i).getLat()), Double.parseDouble(EmergencyPlace_Result.get(i).getLng()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng)
                                    .title(EmergencyPlace_Result.get(i).getType())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.emergencyplace));
                            markers_emergency_place_point.add(mMap.addMarker(markerOptions));
                        }
                    }
                    else{
                        for ( int i = 0; i < markers_emergency_place_point.size(); i++ ){
                            markers_emergency_place_point.get(i).remove();
                        }
                    }
                }
                catch ( NullPointerException e){
                }
            }
        });

//        mHydrant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Information_Map_Activity.this));
//                Log.d("Howard ~~~~~" , String.valueOf(Hydrant_Result.size() ));
//
//                if( Hydrant_Buttom_is_clicked == true ){
//                    Hydrant_Buttom_is_clicked = false;
//                    TextView textView = (TextView) findViewById(R.id.hydrant_textview);
//                    textView.setText("消防栓");
//                    Toast.makeText( Information_Map_Activity.this, "關起消防栓圖例", Toast.LENGTH_SHORT).show();
//                }
//                else if ( Hydrant_Buttom_is_clicked == false ){
//                    Hydrant_Buttom_is_clicked = true;
//                    TextView textView = (TextView) findViewById(R.id.hydrant_textview);
//                    textView.setText("關閉圖例");
//
//                    Toast.makeText( Information_Map_Activity.this, "打開消防栓圖例", Toast.LENGTH_SHORT).show();
//                }
//                try {
//                    if ( Hydrant_Buttom_is_clicked ){
//                        for ( int i = 0; i < Hydrant_Result.size(); i++ ){
//                            Log.d(TAG, '\n' + Hydrant_Result.get(i).getmLat() + '\t' +  Hydrant_Result.get(i).getmLng() + '\n' );
//                            LatLng latLng = new LatLng( Double.parseDouble(Hydrant_Result.get(i).getmLat()), Double.parseDouble(Hydrant_Result.get(i).getmLng()));
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.position(latLng)
//                                    .title(Hydrant_Result.get(i).getmNumber())
//                                    .snippet("型式：" + Hydrant_Result.get(i).getmType()
//                                            + "\n設置地點：" + Hydrant_Result.get(i).getmPlace())
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hydrant));
//                            markers_hydrant_point.add(mMap.addMarker(markerOptions));
//                        }
//                    }
//                    else{
//                        for ( int i = 0; i < markers_hydrant_point.size(); i++ ){
//                            markers_hydrant_point.get(i).remove();
//                        }
//                    }
//                }
//                catch ( NullPointerException e){
//                }
//            }
//        });
        mAED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Event_Map.this));
                Log.d("Howard ~~~~~", String.valueOf(AED_Result.size()));

                if (AED_Buttom_is_clicked == true) {
                    AED_Buttom_is_clicked = false;
                    TextView textView = (TextView) findViewById(R.id.aed_textview);
                    textView.setText("AED");
                    Toast.makeText(Event_Map.this, "關起AED圖例", Toast.LENGTH_SHORT).show();
                } else if (AED_Buttom_is_clicked == false) {
                    AED_Buttom_is_clicked = true;
                    TextView textView = (TextView) findViewById(R.id.aed_textview);
                    textView.setText("關閉圖例");
                    Toast.makeText(Event_Map.this, "打開AED圖例", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (AED_Buttom_is_clicked) {
                        for (int i = 0; i < AED_Result.size(); i++) {
                            Log.d(TAG, '\n' + AED_Result.get(i).getLat() + '\t' + AED_Result.get(i).getLng() + '\n');
                            LatLng latLng = new LatLng(Double.parseDouble(AED_Result.get(i).getLat()), Double.parseDouble(AED_Result.get(i).getLng()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng)
                                    .title(AED_Result.get(i).getType())
                                    .snippet("地址：" + AED_Result.get(i).getAddress())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.aed_small));
                            markers_aed_point.add(mMap.addMarker(markerOptions));
                        }
                    } else {
                        for (int i = 0; i < markers_aed_point.size(); i++) {
                            markers_aed_point.get(i).remove();
                        }
                    }
                } catch (NullPointerException e) {
                }
            }
        });


        mConvenientStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Convenient_Buttom_is_clicked == true) {
                    Convenient_Buttom_is_clicked = false;
                    TextView textView = (TextView) findViewById(R.id.convenient_textview);
                    textView.setText("便利商店");
                    Log.d("TOAST", "toast before");
                    Toast.makeText( Event_Map.this, "關起便利商店圖例", Toast.LENGTH_SHORT).show();
                } else if (Convenient_Buttom_is_clicked == false) {
                    Convenient_Buttom_is_clicked = true;
                    TextView textView = (TextView) findViewById(R.id.convenient_textview);
                    textView.setText("關閉圖例");
                    Log.d("TOAST", "toast before");
                    Toast.makeText( Event_Map.this, "打開便利商店圖例", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (Convenient_Buttom_is_clicked) {
                        for (int i = 0; i < Convenient_Store_Result.size(); i++) {
                            Log.d(TAG, '\n' + Convenient_Store_Result.get(i).getLat() + '\t' +  Convenient_Store_Result.get(i).getLng() + '\n' );
                            LatLng latLng = new LatLng( Double.parseDouble(Convenient_Store_Result.get(i).getLat()), Double.parseDouble(Convenient_Store_Result.get(i).getLng()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng)
                                    .title(Convenient_Store_Result.get(i).getType())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.convenient_store));
                            markers_convenience_store.add(mMap.addMarker(markerOptions));

                        }
                    } else {
                        for ( int i = 0; i < markers_convenience_store.size(); i++ ){
                            markers_convenience_store.get(i).remove();
                        }
                    }
                } catch (NullPointerException e) {

                }
            }
        });
//        mRuleShot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Information_Map_Activity.this));
//                Log.d("Howard ~~~~~", String.valueOf(Ruleshot_Result.size()));
//
//                if (RuleShot_Buttom_is_clicked == true) {
//                    RuleShot_Buttom_is_clicked = false;
//                    TextView textView = (TextView) findViewById(R.id.ruleshot_textview);
//                    textView.setText("測速桿");
//                    Toast.makeText(Information_Map_Activity.this, "關起測速桿圖例", Toast.LENGTH_SHORT).show();
//                } else if (RuleShot_Buttom_is_clicked == false) {
//                    RuleShot_Buttom_is_clicked = true;
//                    TextView textView = (TextView) findViewById(R.id.ruleshot_textview);
//                    textView.setText("關閉圖例");
//                    Toast.makeText(Information_Map_Activity.this, "打開測速桿圖例", Toast.LENGTH_SHORT).show();
//                }
//                try {
//                    if (RuleShot_Buttom_is_clicked) {
//                        for (int i = 0; i < Ruleshot_Result.size(); i++) {
//                            Log.d(TAG, '\n' + Ruleshot_Result.get(i).getmLat() + '\t' + Ruleshot_Result.get(i).getmLng() + '\n');
//                            LatLng latLng = new LatLng(Double.parseDouble(Ruleshot_Result.get(i).getmLat()), Double.parseDouble(Ruleshot_Result.get(i).getmLng()));
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.position(latLng)
//                                    .title(Ruleshot_Result.get(i).getmPlace())
//                                    .snippet("場所編號：" + Ruleshot_Result.get(i).getmNumber())
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ruleshot_small));
//                            markers_ruleshot_point.add(mMap.addMarker(markerOptions));
//                        }
//                    } else {
//                        for (int i = 0; i < markers_ruleshot_point.size(); i++) {
//                            markers_ruleshot_point.get(i).remove();
//                        }
//                    }
//                } catch (NullPointerException e) {
//                }
//            }
//        });
//
        mHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Event_Map.this));
                Log.d("Howard ~~~~~", String.valueOf(Hospital_Result.size()));

                if (Hospital_Buttom_is_clicked == true) {
                    Hospital_Buttom_is_clicked = false;
                    TextView textView = (TextView) findViewById(R.id.hospital_textview);
                    textView.setText("醫院");
                    Toast.makeText(Event_Map.this, "關起醫院圖例", Toast.LENGTH_SHORT).show();
                } else if (Hospital_Buttom_is_clicked == false) {
                    Hospital_Buttom_is_clicked = true;
                    TextView textView = (TextView) findViewById(R.id.hospital_textview);
                    textView.setText("關閉圖例");
                    Toast.makeText(Event_Map.this, "打開醫院圖例", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (Hospital_Buttom_is_clicked) {
                        for (int i = 0; i < Hospital_Result.size(); i++) {
                            Log.d(TAG, '\n' + Hospital_Result.get(i).getLat() + '\t' + Hospital_Result.get(i).getLng() + '\n');
                            LatLng latLng = new LatLng(Double.parseDouble(Hospital_Result.get(i).getLat()), Double.parseDouble(Hospital_Result.get(i).getLng()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng)
                                    .title(Hospital_Result.get(i).getDetail())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_hospital_small));
                            markers_hospital_point.add(mMap.addMarker(markerOptions));
                        }
                    } else {
                        for (int i = 0; i < markers_hospital_point.size(); i++) {
                            markers_hospital_point.get(i).remove();
                        }
                    }
                } catch (NullPointerException e) {
                }
            }
        });

        hideSoftKeyboard();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        if (requestCode == PLACE_PICKER_REQUEST ){
            if ( resultCode == RESULT_OK ){
                Place place = PlacePicker.getPlace( this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(Event_Map.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        /**
         * RRRRRRRRRRRRRRemember here!!!
         */
//        mMap.setInfoWindowAdapter(new Adapter_PlaceInfo(Information_Map_Activity.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.transportation_coreport_map);

        mapFragment.getMapAsync(Event_Map.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

      /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };





    //update mutiple-thread
    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            AirQualityPoints = QueryUtils_Air_Quality.report_AirQuality(AIR_QUALITY_API);
            Log.v("MyRunnable", "mRunnable");
            mUIHandler.post(mUIRunnable);//update UI
            mHandler.postDelayed(this, 3000);
        }
    };

    final Runnable mUIRunnable = new Runnable() {
        @Override
        public void run() {
            Log.v("MyRunnable", "mUIRunnable");
            Log.v("MyRunnable", Integer.toString(AirQualityPoints.size()));
            for ( int i = 0; i < AirQualityPoints.size(); i++ ){
                Log.v("MyRunnable", AirQualityPoints.get(i).getmName());
//                if (AirQualityPoints.get(i).getmAccidentType() == "1" ) {

//                int icon_drawable_number = R.drawable.icon_house_collaspe;
//                switch (AirQualityPoints.get(i).getmAccidentType()){
//                    case "1" :
//                        icon_drawable_number = R.drawable.icon_house_collaspe;
//                        break;
//                    case "2" :
//                        icon_drawable_number = R.drawable.fire;
//                        break;
//                    case "3" :
//                        icon_drawable_number = R.drawable.casualty_2;
//                        break;
//                    case "4" :
//                        icon_drawable_number = R.drawable.icon_others;
//                        break;
//                                default:
//                                    icon_drawable_number = R.drawable.icon_house_collaspe;
//                                    break;
//                }

                Log.d(TAG, "\nHoward" + AirQualityPoints.get(i).getmName() + '\n');

                Log.d("AirQualityInsideMap", AirQualityPoints.get(i).getmName());
                Log.d("AirQualityInsideMap", String.valueOf(AirQualityPoints.get(i).getmLong()));
                Log.d("AirQualityInsideMap", String.valueOf(AirQualityPoints.get(i).getmLat()));

                LatLng latLng = new LatLng(AirQualityPoints.get(i).getmLong(), AirQualityPoints.get(i).getmLat());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng)
                        .title(AirQualityPoints.get(i).getmName())
                        .snippet( "SO2：" + AirQualityPoints.get(i).getmSo2())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_air_quality_station));
                markers_airquality_station.add(mMap.addMarker(markerOptions));
            }
        }
    };

    HandlerThread mHandlerThread;
    Handler mHandler, mUIHandler;



    /**
     ** ------------------------------ Asynctask to reload map -------------------------------------
     */
    private class AirQualityAsyncTask extends AsyncTask< String, Void, List<Parameter_AirQuality> > {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param urls The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<Parameter_AirQuality> doInBackground(String... urls) {

            if ( urls.length < 1 || urls[0] == null ){
                return null;
            }

            Log.d(TAG, "UpdateAsyncTask is activated!!");
            urls[0] = urls[0];
            Log.d("AirQuality", "urls[0] is "+ urls[0]);
            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param placeInfos The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<Parameter_AirQuality> placeInfos) {
            if (AirQualityPoints != null && !AirQualityPoints.isEmpty()){
                //do nothing~~ don't need to parse the code!!
            }
        }
    }
}


