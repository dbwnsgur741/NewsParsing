package com.example.crawaling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    private String currentLocation;
    private String dataTime;
    private String cityName;
    private String so2Value;
    private String o3Value;
    private String no2Value;
    private String coValue;
    private String pm10Value;
    private String pm25Value;

    private TextView mCurrentLocation;
    private TextView mdataTime;
    private TextView mcityName;
    private TextView mso2Value;
    private TextView mcoValue;
    private TextView mo3Value;
    private TextView mno2Value;
    private TextView mpm10Value;
    private TextView mpm25Value;

    private LayoutInflater inflater;
    private Context context;

    public SliderAdapter(Context context, String currentLocation1,
                         String dataTime1,String cityName1,
                         String so2Value1,String o3Value1,String no2Value1,
                         String coValue1,String pm10Value1,String pm25Value1){

        currentLocation = currentLocation1;
        dataTime = dataTime1;
        cityName = cityName1;
        so2Value = so2Value1;
        o3Value = o3Value1;
        no2Value = no2Value1;
        coValue = coValue1;
        pm10Value = pm10Value1;
        pm25Value = pm25Value1;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate( R.layout.slider,container,false );

        mCurrentLocation = (TextView)v.findViewById( R.id.CurrentLocation );
        mdataTime = (TextView)v.findViewById( R.id.dataTime );
        mcityName = (TextView)v.findViewById( R.id.cityName );
        mso2Value = (TextView)v.findViewById( R.id.so2Value );
        mcoValue = (TextView)v.findViewById( R.id.coValue );
        mo3Value = (TextView)v.findViewById( R.id.o3Value );
        mno2Value = (TextView)v.findViewById( R.id.no2Value );
        mpm10Value = (TextView)v.findViewById( R.id.pm10Value );
        mpm25Value = (TextView)v.findViewById( R.id.pm25Value );

        setData();

        container.addView(v);

        return v;
    }

    public void setData(){

        mCurrentLocation.setText( currentLocation );
        mdataTime.setText( dataTime );
        mcityName.setText( cityName );
        mso2Value.setText( so2Value );
        mcoValue.setText( coValue);
        mo3Value.setText( o3Value );
        mno2Value.setText( no2Value );
        mpm10Value.setText( pm10Value );
        mpm25Value.setText( pm25Value );
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }

}
