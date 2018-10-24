package com.huhuijia.simpleweather.entity;

import android.content.Context;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class Constans {
    public static String CITY="";
    public static final String WEATHER_KEY = "1a2f905a5b215379c797ab7b703ca249";
//http://v.juhe.cn/weather/index?format=2&cityname=%E9%95%BF%E6%B2%99&key=1a2f905a5b215379c797ab7b703ca249
    public  static ArrayList<Entry> Maxvalues = new ArrayList<>();
    public static ArrayList<Entry> Minvalues = new ArrayList<>();
    public static void showToast(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_LONG);
    }
}
