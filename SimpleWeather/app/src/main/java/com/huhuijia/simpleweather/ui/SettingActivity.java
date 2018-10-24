package com.huhuijia.simpleweather.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.huhuijia.simpleweather.R;
import com.huhuijia.simpleweather.adapter.ListAdapter;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {
    private ListView mListView;
    private ArrayList<String> mList;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mList = new ArrayList<>();
        mList.add("版本号：" + getVersion());
        mList.add("意见反馈");
        mList.add("关于我们");
        mAdapter = new ListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        try {
                            Toast.makeText(SettingActivity.this,"跳转至手机QQ",Toast.LENGTH_LONG);
                            //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=2393022053";//uin是发送过去的qq号码
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this,"请检查是否安装QQ",Toast.LENGTH_LONG);
                        }
                       // startActivity(new Intent(SettingActivity.this,BackActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(SettingActivity.this,AboutActivity.class));
                        break;
                }
            }
        });
    }

    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "无法获取版本号";
        }
    }
}
