package com.huhuijia.simpleweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huhuijia.simpleweather.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> mList;

    public ListAdapter(Context context,ArrayList<String> mList) {
    this.context=context;
    this.mList=mList;
    layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.list_itme,null);
            viewHolder.tv_item=convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item.setText(mList.get(position));
        return convertView;
    }
    class ViewHolder{
        private TextView tv_item;
    }
}
