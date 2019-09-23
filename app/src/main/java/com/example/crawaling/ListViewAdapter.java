package com.example.crawaling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> mitems = new ArrayList<>(  );

    @Override
    public int getCount() {
        return mitems.size();
    }

    @Override
    public Object getItem(int position) {
        return mitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate( R.layout.listview_custom,parent,false );
        }

        TextView textView = (TextView) convertView.findViewById( R.id.listView_textView );
        TextView textView1 = (TextView) convertView.findViewById( R.id.listView_desc_TextView );
        ImageView imageView = (ImageView)convertView.findViewById( R.id.listView_imageView );

        ListViewItem listViewItem = (ListViewItem) getItem( position );

        textView.setText( listViewItem.getTitle() );
        textView1.setText( listViewItem.getDesc() );

        return convertView;
    }

    public void addItem(String title,String desc){
        ListViewItem listViewItem = new ListViewItem();
        listViewItem.setTitle(title);
        listViewItem.setDesc(desc);
        mitems.add(listViewItem);
    }
}
