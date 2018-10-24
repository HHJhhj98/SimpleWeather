package com.huhuijia.simpleweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.github.mikephil.charting.data.Entry;
import com.huhuijia.simpleweather.data.WeatherDataBean;
import com.huhuijia.simpleweather.entity.Constans;
import com.huhuijia.simpleweather.entity.IsInternet;
import com.huhuijia.simpleweather.impl.WeatherImpl;
import com.huhuijia.simpleweather.ui.BaseActivity;
import com.huhuijia.simpleweather.ui.SettingActivity;
import com.huhuijia.simpleweather.ui.SplashActivity;
import com.huhuijia.simpleweather.ui.TrendActivity;
import com.huhuijia.simpleweather.ui.WelcomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvCity;
    private ImageView mIvWeatherIcon;
    private TextView mTvWeather;
    //定位服务
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener;
    //提示框
    private ProgressDialog dialog;
    private SwipeRefreshLayout mSrl;
    private WeatherImpl impl;
    private TextView mTvTemp;
    private TextView mTvAddr;
    private TextView mTvWindDirection;
    private TextView mTvWindStrength;
    private TextView mTvHumidity;
    private TextView mTvDate1;
    private ImageView mIvWeatherIc1;
    private TextView mTvWeather1;
    private TextView mTvMaxMin1;
    private TextView mTvDate2;
    private ImageView mIvWeatherIc2;
    private TextView mTvWeather2;
    private TextView mTvMaxMin2;
    private TextView mTvDate3;
    private ImageView mIvWeatherIc3;
    private TextView mTvWeather3;
    private TextView mTvMaxMin3;
    private TextView mTvUvIndex;
    private TextView mTvWashIndex;
    private TextView mTvTravelIndex;
    private TextView mTvExerciseIndex;
    private TextView mTvDressingIndex;
    private Button mBtnTrend;
    private String city;
    private String district;
    private String street;
    private double latitude;
    private double longitude;
    private int mipmap, mipmap1, mipmap2, mipmap3;
    private ArrayList<Entry> Maxvalues = new ArrayList<>();
    private ArrayList<Entry> Minvalues = new ArrayList<>();
    private TextView mTvUv;
    private TextView mTvWash;
    private TextView mTvTravel;
    private TextView mTvExercise;
    private TextView mTvDressing;
    private boolean mboolean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1003:
                    mboolean = IsInternet.isNetworkAvalible(MainActivity.this);
                    if (mboolean) {
                       // Toast.makeText(MainActivity.this, city + "gh", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        //requstWeather(city);
                    } else {
                        showNormalDialog(MainActivity.this, R.mipmap.ic_launcher, "网络错误", "请检查网络是否连接？", "退出", "前往");
                    }
                    break;
                case 1001:
                    mboolean = IsInternet.isNetworkAvalible(MainActivity.this);
                    if (mboolean) {
                        mLocationClient.start();
                        handler.sendEmptyMessageDelayed(1003, 0);
                    } else {
                        showNormalDialog(MainActivity.this, R.mipmap.ic_launcher, "网络错误", "请检查网络是否连接？", "退出", "前往");
                    }
                    break;
                case 1002:
                    mboolean = IsInternet.isNetworkAvalible(MainActivity.this);
                    if (mboolean) {
                        initView();
                        // requstWeather(city);
                        handler.sendEmptyMessageDelayed(1002, 1000 * 30 * 60);
                        break;
                    } else {
                        showNormalDialog(MainActivity.this, R.mipmap.ic_launcher, "网络错误", "请检查网络是否连接？", "退出", "前往");
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Refresh();
        initView();
        handler.sendEmptyMessageDelayed(1001, 0);
        handler.sendEmptyMessageDelayed(1002, 1000 * 30 * 60);
        //以下代码用于去除阴影，兼容5.0以上版本
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
        //showNormalDialog();

    }

    private void initView() {
        initLocation();
        initDialog();
        dialog.show();
        initRetrofit();
        //开始定位
        Log.i("ghg", "开始定位");
        mLocationClient.start();
        //dialog.dismiss();
        mTvCity = (TextView) findViewById(R.id.tvCity);
        mIvWeatherIcon = (ImageView) findViewById(R.id.ivWeatherIcon);
        mTvWeather = (TextView) findViewById(R.id.tvWeather);
        mTvTemp = (TextView) findViewById(R.id.tvTemp);
        mTvAddr = (TextView) findViewById(R.id.tvAddr);
        mTvWindDirection = (TextView) findViewById(R.id.tvWinddirection);
        mTvWindStrength = (TextView) findViewById(R.id.tvWindstrength);
        mTvHumidity = (TextView) findViewById(R.id.tvHumidity);
        mTvDate1 = (TextView) findViewById(R.id.tvDate1);
        mIvWeatherIc1 = (ImageView) findViewById(R.id.ivWeatherIc1);
        mTvWeather1 = (TextView) findViewById(R.id.tvWeather1);
        mTvMaxMin1 = (TextView) findViewById(R.id.tvMaxMin1);
        mTvDate2 = (TextView) findViewById(R.id.tvDate2);
        mIvWeatherIc2 = (ImageView) findViewById(R.id.ivWeatherIc2);
        mTvWeather2 = (TextView) findViewById(R.id.tvWeather2);
        mTvMaxMin2 = (TextView) findViewById(R.id.tvMaxMin2);
        mTvDate3 = (TextView) findViewById(R.id.tvDate3);
        mIvWeatherIc3 = (ImageView) findViewById(R.id.ivWeatherIc3);
        mTvWeather3 = (TextView) findViewById(R.id.tvWeather3);
        mTvMaxMin3 = (TextView) findViewById(R.id.tvMaxMin3);
        mTvUvIndex = (TextView) findViewById(R.id.tvUvIndex);
        mTvWashIndex = (TextView) findViewById(R.id.tvWashIndex);
        mTvTravelIndex = (TextView) findViewById(R.id.tvTravelIndex);
        mTvExerciseIndex = (TextView) findViewById(R.id.tvExerciseIndex);
        mTvDressingIndex = (TextView) findViewById(R.id.tvDressingIndex);
        mBtnTrend = (Button) findViewById(R.id.btnTrend);
        mTvUv = (TextView) findViewById(R.id.tvUv);
        mTvWash = (TextView) findViewById(R.id.tvWash);
        mTvTravel = (TextView) findViewById(R.id.tvTravel);
        mTvExercise = (TextView) findViewById(R.id.tvExercise);
        mTvDressing = (TextView) findViewById(R.id.tvDressing);
        mTvDate1.setOnClickListener(this);
        mIvWeatherIc1.setOnClickListener(this);
        mTvWeather1.setOnClickListener(this);
        mTvMaxMin1.setOnClickListener(this);
        mTvDate2.setOnClickListener(this);
        mIvWeatherIc2.setOnClickListener(this);
        mTvWeather2.setOnClickListener(this);
        mTvMaxMin2.setOnClickListener(this);
        mTvDate3.setOnClickListener(this);
        mIvWeatherIc3.setOnClickListener(this);
        mTvWeather3.setOnClickListener(this);
        mTvMaxMin3.setOnClickListener(this);
        mTvHumidity.setOnClickListener(this);
        mTvWeather.setOnClickListener(this);
        mTvDressing.setOnClickListener(this);
        mBtnTrend.setOnClickListener(this);
        mTvTemp.setOnClickListener(this);
        mTvCity.setOnClickListener(this);
        mTvAddr.setOnClickListener(this);
        mIvWeatherIcon.setOnClickListener(this);
        mTvWindDirection.setOnClickListener(this);
        mTvWindStrength.setOnClickListener(this);
        mTvUv.setOnClickListener(this);
        mTvUvIndex.setOnClickListener(this);
        mTvWash.setOnClickListener(this);
        mTvWashIndex.setOnClickListener(this);
        mTvTravel.setOnClickListener(this);
        mTvTravelIndex.setOnClickListener(this);
        mTvExercise.setOnClickListener(this);
        mTvExerciseIndex.setOnClickListener(this);
        mTvDressingIndex.setOnClickListener(this);


    }


    //刷新
    private void Refresh() {
        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(1001, 0);
                new Handler().postDelayed(new Runnable() {//模拟耗时操作
                    @Override
                    public void run() {
                        mSrl.setRefreshing(false);//取消刷新
                    }
                }, 1000);
            }
        });
//        mSrl.post(new Runnable() {
//            @Override
//            public void run() {
//                mSrl.setRefreshing(true);
//                handler.sendEmptyMessageDelayed(1001, 1000);
//            }
//        });
    }


    //初始化dialog
    private void initDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在努力查询中...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);//屏幕外点击无效
    }

    //初始化Retrofit
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://v.juhe.cn/")
                .build();
        impl = retrofit.create(WeatherImpl.class);
    }

    //请求天气
    private void requstWeather(String city) {
        //异步
        impl.getWeather(city, Constans.WEATHER_KEY).enqueue(new Callback<WeatherDataBean>() {
            @Override
            public void onResponse(Call<WeatherDataBean> call, Response<WeatherDataBean> response) {
                WeatherDataBean weatherDataBean = response.body();
                try {
                    mTvTemp.setText(weatherDataBean.getResult().getSk().getTemp() + "°");
                    mipmap = getResources().getIdentifier("w" + weatherDataBean.getResult().getToday().getWeather_id().getFa(),
                            "mipmap", getPackageName());
                    mIvWeatherIcon.setImageResource(mipmap);
                    mTvWeather.setText(weatherDataBean.getResult().getToday().getWeather());
                    mTvWindDirection.setText("风向 | " + weatherDataBean.getResult().getSk().getWind_direction());
                    mTvWindStrength.setText("风力 | " + weatherDataBean.getResult().getSk().getWind_strength());
                    mTvHumidity.setText("相对湿度 | " + weatherDataBean.getResult().getSk().getHumidity());
                    mipmap1 = getResources().getIdentifier("w" + weatherDataBean.getResult().getFuture().get(0).getWeather_id().getFa(),
                            "mipmap", getPackageName());
                    mTvDate1.setText("今天");
                    mIvWeatherIc1.setImageResource(mipmap1);
                    mTvWeather1.setText(weatherDataBean.getResult().getFuture().get(0).getWeather());
                    mTvMaxMin1.setText(weatherDataBean.getResult().getFuture().get(0).getTemperature());
                    mTvDate2.setText("明天");
                    mipmap2 = getResources().getIdentifier("w" + weatherDataBean.getResult().getFuture().get(1).getWeather_id().getFa(),
                            "mipmap", getPackageName());
                    mIvWeatherIc2.setImageResource(mipmap2);
                    mTvWeather2.setText(weatherDataBean.getResult().getFuture().get(1).getWeather());
                    mTvMaxMin2.setText(weatherDataBean.getResult().getFuture().get(1).getTemperature());
                    String date = weatherDataBean.getResult().getFuture().get(2).getWeek();
                    switch (date) {
                        case "星期一":
                            date = "周一";
                            break;
                        case "星期二":
                            date = "周二";
                            break;
                        case "星期三":
                            date = "周三";
                            break;
                        case "星期四":
                            date = "周四";
                            break;
                        case "星期五":
                            date = "周五";
                            break;
                        case "星期六":
                            date = "周六";
                            break;
                        default:
                            date = "周日";
                            break;
                    }
                    mipmap3 = getResources().getIdentifier("w" + weatherDataBean.getResult().getFuture().get(2).getWeather_id().getFa(),
                            "mipmap", getPackageName());
                    mTvDate3.setText(date);
                    mIvWeatherIc3.setImageResource(mipmap2);
                    mTvWeather3.setText(weatherDataBean.getResult().getFuture().get(2).getWeather());
                    mTvMaxMin3.setText(weatherDataBean.getResult().getFuture().get(2).getTemperature());
                    mTvDressingIndex.setText("  天气" + weatherDataBean.getResult().getToday().getDressing_index() + "," + weatherDataBean.getResult().getToday().getDressing_advice()
                    );
                    mTvUvIndex.setText(weatherDataBean.getResult().getToday().getUv_index());
                    mTvExerciseIndex.setText(weatherDataBean.getResult().getToday().getExercise_index());
                    mTvTravelIndex.setText(weatherDataBean.getResult().getToday().getTravel_index());
                    mTvWashIndex.setText(weatherDataBean.getResult().getToday().getWash_index());
                    for (int i = 0; i < weatherDataBean.getResult().getFuture().size(); i++) {
                        String weatherCode = weatherDataBean.getResult().getFuture().get(i).getTemperature();
                        int MinCode = Integer.parseInt(weatherCode.substring(0, 2));
                        int MaxCode = Integer.parseInt(weatherCode.substring(4, 6));
                        Maxvalues.add(new Entry(i + 1, MaxCode));
                        Minvalues.add(new Entry(i + 1, MinCode));
                    }
                    dialog.dismiss();
                    mSrl.setRefreshing(false);
                    Log.i("hjcs", "关闭定位");
                    mLocationClient.stop();
                } catch (Exception e) {
                    mSrl.setRefreshing(false);
                    dialog.dismiss();
//                        showNormalDialog(MainActivity.this, R.mipmap.ic_launcher, "网络错误", "请检查网络是否连接？", "退出", "前往");
                }

            }

            @Override
            public void onFailure(Call<WeatherDataBean> call, Throwable t) {

            }
        });

    }

    //初始化定位
    private void initLocation() {
        myListener = new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        option.setScanSpan(30 * 60 * 1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true);
        //详细地址
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
    }

    //跳转至其他Activity、应用、网页
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTrend:
            case R.id.tvDate1:
            case R.id.tvWeather1:
            case R.id.ivWeatherIc1:
            case R.id.tvMaxMin1:
            case R.id.tvDate2:
            case R.id.tvWeather2:
            case R.id.ivWeatherIc2:
            case R.id.tvMaxMin2:
            case R.id.tvDate3:
            case R.id.tvWeather3:
            case R.id.ivWeatherIc3:
            case R.id.tvMaxMin3:
                Intent intent = new Intent(MainActivity.this, TrendActivity.class);
                Constans.Maxvalues = Maxvalues;
                Constans.Minvalues = Minvalues;
                startActivity(intent);
                //finish();
                break;
            case R.id.tvTemp:
            case R.id.ivWeatherIcon:
            case R.id.tvWeather:
            case R.id.tvWinddirection:
            case R.id.tvWindstrength:
            case R.id.tvHumidity:
            case R.id.tvUvIndex:
            case R.id.tvTravelIndex:
            case R.id.tvExerciseIndex:
            case R.id.tvDressingIndex:
            case R.id.tvWashIndex:
            case R.id.tvUv:
            case R.id.tvWash:
            case R.id.tvTravel:
            case R.id.tvExercise:
            case R.id.tvDressing:

                Intent it = new Intent();
                it.setData(Uri.parse("http://www.weather.com.cn/"));//Url 就是你要打开的网址
                it.setAction(Intent.ACTION_VIEW);
                this.startActivity(it); //启动浏览器
                break;
            case R.id.tvCity:
            case R.id.tvAddr:
                if (hasApplication(this, "com.baidu.BaiduMap")) {
                    Intent i = new Intent();
                    i.setData((Uri.parse("baidumap://map/show?center=" + latitude + "," + longitude + "&zoom=14&traffic=on&src=andr.baidu.openAPIdemo")));
                    startActivity(i);
                } else if (hasApplication(this, "com.autonavi.minimap")) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    //将功能Scheme以URI的方式传入data
                    Uri uri = Uri.parse("androidamap://myLocation?sourceApplication=简单天气");
                    i.setData(uri);
                    //启动该页面即可
                    startActivity(i);
                } else {
                    Intent i = new Intent();
                    i.setData(Uri.parse("http://map.baidu.com/mobile/"));//Url 就是你要打开的网址
                    i.setAction(Intent.ACTION_VIEW);
                    this.startActivity(i); //启动浏览器
                    //Toast.makeText(this, "不存在该应用", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    //获取定位
    public class MyLocationListener extends BDAbstractLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            /**tring addr = location.getAddrStr();    //获取详细地址信息
             String country = location.getCountry();    //获取国家
             String province = location.getProvince();    //获取省份
             **/
            city = location.getCity();    //获取城市
            district = location.getDistrict();    //获取区县
            street = location.getStreet();    //获取街道信息
            //Toast.makeText(MainActivity.this, city + "gh", Toast.LENGTH_LONG).show();
            //showNormalDialog();
            //请求
            //dialog.dismiss();
            String add = district + street;
            if (city == null) {
                //Toast.makeText(MainActivity.this,"yfyttf",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //showNormalDialog(MainActivity.this, R.mipmap.ic_launcher, "定位错误", "请检查GPS是否开启？", "退出", "设置");
                initLocation();
                mLocationClient.start();
            } else {
                dialog.dismiss();
                mTvCity.setText(city);
                mTvAddr.setText(district + street);
                //Toast.makeText(MainActivity.this, city + "gHh", Toast.LENGTH_LONG).show();
                requstWeather(city);

            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean hasApplication(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取系统中安装的应用包的信息
        List<PackageInfo> listPackageInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < listPackageInfo.size(); i++) {
            if (listPackageInfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;

    }
}
