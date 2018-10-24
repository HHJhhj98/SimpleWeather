package com.huhuijia.simpleweather.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;

import com.huhuijia.simpleweather.MainActivity;
import com.huhuijia.simpleweather.R;

public class WelcomeActivity extends BaseActivity2 {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //进入退出效果 注意这里 创建的效果对象是 Fade()
        getWindow().setEnterTransition(new Fade().setDuration(1000));
        getWindow().setExitTransition(new Fade().setDuration(1000));

        //判断系统版本是否是6.0
        if (isSystemVersionLarge23()){
            checkPermission();
        }else {
            prepare();
        }
    }
    private void prepare(){
        //操作
        handler.sendEmptyMessageDelayed(1001, 1500);
    }


    private void checkPermission(){

        if (checkMorePermission(permissions)){
            prepare();
        }else {
            requestOneOrMorePermissions(permissions,11,this);//一次请求多个权限
        }
    }


    //申请权限回调  Android6.0以上才会调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 11:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //所有权限通过才会
                    prepare();
                }else {
                    //拒绝一个就会这出现
                    showHintDialog("获取权限失败！");
                }
                break;
        }
    }

    /**
     * 拒绝权限弹出提示
     * @param messege
     */
    protected void showHintDialog(String messege){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(messege);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermission();
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
