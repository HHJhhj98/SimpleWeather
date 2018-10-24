package com.huhuijia.simpleweather.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.huhuijia.simpleweather.R;
import com.huhuijia.simpleweather.adapter.ListAdapter;

import java.util.ArrayList;

public class AboutActivity extends BaseActivity {
    private ListView mListView;
    private ArrayList<String> mList = new ArrayList<>();
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mList.add("单位：湖南信息学院");
        mList.add("作者：hhj");
        mList.add("QQ群：682455648");
        mList.add("CSDN博客：https://blog.csdn.net/qq_41445849");
        mAdapter = new ListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }
}
