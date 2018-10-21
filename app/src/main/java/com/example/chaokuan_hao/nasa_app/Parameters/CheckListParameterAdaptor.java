package com.example.chaokuan_hao.nasa_app.Parameters;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//
//import java.util.ArrayList;
//
//public class CheckListParameterAdaptor extends ArrayList<Parameter_CheckList> {
//    public CheckListParameterAdaptor(Activity context, ArrayList<Parameter_CheckList> CheckLsit_List) {
//        super(context, 0, CheckLsit_List);
//    }
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View listItemView = convertView;
//        if(listItemView == null){
//            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
//        }
//    }
//}
//
//
//
//
//    public View getView(int position, View convertView, ViewGroup parent){
//        View listItemView = convertView;
//        if(listItemView == null){
//            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
//        }
//        BusParameter currentBus = getItem(position);
//
//        //ImageView busImage = (ImageView) listItemView.findViewById(R.id.IDBusImage);
//        //busImage.setImageResource(currentBus.getmImageResourceID());
//
//        TextView busNumber = listItemView.findViewById(R.id.IDbusNumber);
//        busNumber.setText(currentBus.getmBusNumber());
//
//        TextView busType = listItemView.findViewById(R.id.IDbusType);
//        busType.setText(currentBus.getmBusType());
//
//        TextView busStart = listItemView.findViewById(R.id.IDfrom);
//        busStart.setText(currentBus.getmStartStop());
//
//        TextView busEnd = listItemView.findViewById(R.id.IDto);
//        busEnd.setText(currentBus.getmEndStop());
//
//        TextView timeLeft = listItemView.findViewById(R.id.IDminute);
//        timeLeft.setText(currentBus.getmEstimateTimeOfTheUpComingBus());
//
//        TextView inWord = listItemView.findViewById((R.id.IDinWord));
//        inWord.setText("往");
//        return listItemView;
//}

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.chaokuan_hao.nasa_app.R;

import java.util.ArrayList;

/**
 * Created by Howard on 2017/7/15.
 */

public class CheckListParameterAdaptor extends ArrayAdapter<Parameter_CheckList> {
    public CheckListParameterAdaptor(Activity context, ArrayList<Parameter_CheckList> checkLists){
        super(context, 0, checkLists);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Parameter_CheckList currentCheckList = getItem(position);

        //ImageView busImage = (ImageView) listItemView.findViewById(R.id.IDBusImage);
        //busImage.setImageResource(currentBus.getmImageResourceID());

//        TextView titleAdaptor = listItemView.findViewById(R.id.id_title_checklist);
//        titleAdaptor.setText(currentCheckList.getmTitle());

        TextView busType = listItemView.findViewById(R.id.id_content_checklist);
        busType.setText(currentCheckList.getmContent());

        return listItemView;
    }

}
