package com.huhuijia.simpleweather.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.huhuijia.simpleweather.MainActivity;
import com.huhuijia.simpleweather.R;
import com.huhuijia.simpleweather.entity.Constans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrendActivity extends BaseActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mMaxChart,mMinChart;
    private int mcolor = 0xFF2aa0e7;
    private ArrayList<Entry> Maxvalues = new ArrayList<>();
    private ArrayList<Entry> Minvalues = new ArrayList<>();
    private int mBack=0,first,Second,mm,ss,add;
    private String firstdate,Seconddate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        initMaxChart();
        initMinChart();
        Maxvalues=Constans.Maxvalues;
        Minvalues=Constans.Minvalues;
        setMaxData(Maxvalues);
        setMinData(Minvalues);


    }
    //初始化最高温度
    private void initMaxChart() {
        mMaxChart = (LineChart) findViewById(R.id.MaxChart);
        mMaxChart.setOnChartGestureListener(this);//设置手势滑动事件
        mMaxChart.setOnChartValueSelectedListener(this);//设置数值选择监听
        mMaxChart.setDrawGridBackground(false);//设置后台绘制
        // 不需要描述文本
        mMaxChart.getDescription().setEnabled(false);
        // 支持触摸手势
        mMaxChart.setTouchEnabled(true);
        // 设置缩放/滑动
        mMaxChart.setDragEnabled(true);
        mMaxChart.setScaleEnabled(true);
        mMaxChart.setPinchZoom(true);

        XAxis xAxis = mMaxChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 10f);
        //设置最大值
        xAxis.setAxisMaximum(7);
        //设置最小值
        xAxis.setAxisMinimum(1);
        //设置间隔1天
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        LimitLine ll1 = new LimitLine(30, "高温");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 10f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(0, "低温");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 10f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mMaxChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(60f);
        leftAxis.setAxisMinimum(-30f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        leftAxis.setDrawLimitLinesBehindData(true);

        mMaxChart.getAxisRight().setEnabled(false);
        mMaxChart.animateY(2500);

    }


    //设置最高温度数据
    private void setMaxData(ArrayList<Entry> values) {
        LineDataSet set1;
        if (mMaxChart.getData() != null &&
                mMaxChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mMaxChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mMaxChart.getData().notifyDataChanged();
            mMaxChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "一周最高温度趋势");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(mcolor);
            set1.setCircleColor(mcolor);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(mcolor);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mMaxChart.setData(data);
        }
    }
    //初始化最低温度
    private void initMinChart() {
        mMinChart = (LineChart) findViewById(R.id.MinChart);
        mMinChart.setOnChartGestureListener(this);//设置手势滑动事件
        mMinChart.setOnChartValueSelectedListener(this);//设置数值选择监听
        mMinChart.setDrawGridBackground(false);//设置后台绘制
        // 不需要描述文本
        mMinChart.getDescription().setEnabled(false);
        // 支持触摸手势
        mMinChart.setTouchEnabled(true);
        // 设置缩放/滑动
        mMinChart.setDragEnabled(true);
        mMinChart.setScaleEnabled(true);
        mMinChart.setPinchZoom(true);

        XAxis xAxis = mMinChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 10f);
        //设置最大值
        xAxis.setAxisMaximum(7);
        //设置最小值
        xAxis.setAxisMinimum(1);
        //设置间隔1天
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置

        LimitLine ll1 = new LimitLine(30, "高温");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 10f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(0, "低温");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 10f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mMinChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(60f);
        leftAxis.setAxisMinimum(-30f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        leftAxis.setDrawLimitLinesBehindData(true);

        mMinChart.getAxisRight().setEnabled(false);
        mMinChart.animateY(2500);

    }


    //设置最低温度数据
    private void setMinData(ArrayList<Entry> values) {
        LineDataSet set1;
        if (mMinChart.getData() != null &&
                mMinChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mMinChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mMinChart.getData().notifyDataChanged();
            mMinChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "一周最低温度趋势");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(mcolor);
            set1.setCircleColor(mcolor);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(mcolor);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mMinChart.setData(data);
        }
    }

    //获取当前时间
    private int NowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        SimpleDateFormat simpleDate = new SimpleDateFormat("ss");
        mm= Integer.parseInt(simpleDateFormat.format(new Date()));
        ss= Integer.parseInt(simpleDate.format(new Date()));
        add=mm*60+ss;
        return add;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBack++;
        if(mBack==1){
            first=NowTime();
        }else if(mBack==2){
            Second=NowTime();
        }
        switch (mBack){
            case 1:
                startActivity(new Intent(TrendActivity.this,MainActivity.class));
                Maxvalues.clear();
                Minvalues.clear();
                finish();
                break;

        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
