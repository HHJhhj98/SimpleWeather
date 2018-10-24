package com.huhuijia.simpleweather.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity2 extends AppCompatActivity {
    public String[] permissions = new String[]{ //申请的权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 判断系统版本是否是Android6.0以上
     *
     * @return
     */
    protected boolean isSystemVersionLarge23() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查多个权限是否被授予
     *
     * @param permissions 被检查权限名
     * @return
     */
    protected boolean checkMorePermission(String[] permissions) {
        boolean isAllCheck = false;
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                isAllCheck = true;
                break;
            }
        }
        if (isAllCheck) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查单个权限是否被授予
     *
     * @param permission 被检查权限名
     * @return
     */
    protected boolean checkOnePermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 请求权限一个或多个
     *
     * @param permissions
     * @param requestCode
     */
    protected void requestOneOrMorePermissions(String[] permissions, int requestCode, AppCompatActivity activity) {
        boolean isAllCheck = false;
        for (int i = 0; i < permissions.length; i++) {
            if (!checkOnePermission(permissions[i])) {
                isAllCheck = true;
                break;
            }
        }
        if (isAllCheck) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }


    /**
     * 获取应用版本号
     *
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    protected float getVersionCode() {
        float versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void requestSDcardPermission(AppCompatActivity activity, int resultcode) {
        //检查SDcard权限
        if (activity.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && activity.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, resultcode);
        }


    }

    public void requestSensorPermission(AppCompatActivity activity, int resultcode) {
        //检查传感器权限
        if (activity.checkCallingOrSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BODY_SENSORS}, resultcode);
        }
    }

    public void requestPositionPermission(AppCompatActivity activity, int resultcode) {
        //检查位置权限
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, resultcode);
        }
    }
}
