package com.example.crawaling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class InfoAtmosActivity extends AppCompatActivity {

    private TextView mCurrentLocation;
    private TextView mdataTime;
    private TextView mcityName;
    private TextView mso2Value;
    private TextView mcoValue;
    private TextView mo3Value;
    private TextView mno2Value;
    private TextView mpm10Value;
    private TextView mpm25Value;

    private String currentLocation;
    public String mDataTime;
    public String mCityName;
    public String mSo2Value;
    public String mO3Value;
    public String mNo2Value;
    public String mCoValue;
    public String mPm10Value;
    public String mPm25Value;

    private Context context;
    private String SIDO;
    private String DONGU;

    private double wedo;
    private double kyeongdo;

    private String tmX;
    private String tmY;

    private String staionName;
    private String farFrom;
    private ProgressDialog progressDialog;
    private LineChart lineChart;
    private XAxis xAxis;
    private YAxis yAxis;

    private static String theValue = "pm10Value";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.infoatmoslayout );

        // <!-- chart layout setting --!> //

        lineChart = (LineChart)findViewById( R.id.chart );
        lineChart.getDescription().setEnabled( false );


        xAxis = lineChart.getXAxis();
        // xAxis.enableAxisLineDashedLine( 10f,10f,0f );


        yAxis = lineChart.getAxisLeft();
        lineChart.getAxisRight().setEnabled( false );

        /*
        yAxis.enableAxisLineDashedLine( 10f,10f,0f );
        yAxis.setAxisMaximum( 100f );
        yAxis.setAxisMinimum( 0f );
        */

        // <!-- chart layout setting --!> //

        Intent intent = getIntent();
        currentLocation = intent.getStringExtra( "CurrentLocation" );
        SIDO = intent.getStringExtra( "first" );
        DONGU = intent.getStringExtra( "second" );

        wedo = (double) intent.getDoubleExtra( "wedo",0 );
        kyeongdo = (double) intent.getDoubleExtra( "kyeongdo",0 );

        mCurrentLocation = (TextView)findViewById( R.id.CurrentLocation );
        mdataTime = (TextView)findViewById( R.id.dataTime );
        mcityName = (TextView)findViewById( R.id.cityName );
        mso2Value = (TextView)findViewById( R.id.so2Value );
        mcoValue = (TextView)findViewById( R.id.coValue );
        mo3Value = (TextView)findViewById( R.id.o3Value );
        mno2Value = (TextView)findViewById( R.id.no2Value );
        mpm10Value = (TextView)findViewById( R.id.pm10Value );
        mpm25Value = (TextView)findViewById( R.id.pm25Value );

        mpm10Value.setOnClickListener( this.onClickListener );
        mpm25Value.setOnClickListener( this.onClickListener );
        mno2Value.setOnClickListener( this.onClickListener );
        mo3Value.setOnClickListener( this.onClickListener );
        mcoValue.setOnClickListener( this.onClickListener );
        mso2Value.setOnClickListener( this.onClickListener );

        context = this;
        new GetXMLTask( ).execute( );
    }

    public class GetXMLTask extends AsyncTask<String, Void, Document> {


        public org.w3c.dom.Document doc;

        @Override
        protected Document doInBackground(String... urls) {
            Log.d("!!!!!","def");
            URL url;
            try {
                url = new URL( "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst" +
                        "?sidoName="+SIDO+"&searchCondition=DAILY&pageNo=1&numOfRows=40&" +
                        "ServiceKey=%2BDQd8pShYLPqi01nFPYY8nkzSSmY1ZdloIu2WEwO%2BnWnPWI1frIATtnp%2FB3hwvUJVvMimjhngiunQZRl7S%2BCyA%3D%3D" );
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse( new InputSource( url.openStream() ) );
                doc.getDocumentElement().normalize();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog( context );
            progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
            progressDialog.setMessage( "잠시 기다려 주세요." );
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Document doc) {

            NodeList citycity = doc.getElementsByTagName( "cityName" );

            String[] city = new String[40];
            int index = 0;

            for(int i = 0 ; i<citycity.getLength(); i++){
                city[i] = citycity.item( i ).getFirstChild().getNodeValue();
            }

            for(int j=0 ; j<citycity.getLength(); j++){
                if(city[j].equals( DONGU )){
                    index = j;
                    break;
                }
            }

            NodeList nodeList = doc.getElementsByTagName( "item" );
            Node node = nodeList.item( index );
            Element fstElmnt = (Element) node;


            NodeList dataTime = fstElmnt.getElementsByTagName( "dataTime" );
            mDataTime = "관측 시간  \n" + dataTime.item( 0 ).getChildNodes().item( 0 ).getNodeValue();

            NodeList cityName = fstElmnt.getElementsByTagName( "cityName" );
            mCityName = "관측 장소  \n" + cityName.item( 0 ).getChildNodes().item( 0 ).getNodeValue();

            NodeList so2Value = fstElmnt.getElementsByTagName( "so2Value" );
            mSo2Value = "아황산가스(SO2)  \n" + so2Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList o3Value = fstElmnt.getElementsByTagName( "o3Value" );
            mO3Value = "오존(O3)  \n" + o3Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList no2Value = fstElmnt.getElementsByTagName( "no2Value" );
            mNo2Value = "이산화질소(NO2)  \n" + no2Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList coValue = fstElmnt.getElementsByTagName( "coValue" );
            mCoValue = "일산화탄소(CO)  \n" + coValue.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList pm10Value = fstElmnt.getElementsByTagName( "pm10Value" );
            mPm10Value = "미세먼지(PM10)  \n" + pm10Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(㎍/㎥)";

            NodeList pm25Value = fstElmnt.getElementsByTagName( "pm25Value" );
            mPm25Value = "미세먼지(PM2.5)  \n" + pm25Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(㎍/㎥)";

            mCurrentLocation.setText( currentLocation );
            mdataTime.setText( mDataTime );
            mcityName.setText( mCityName );
            mso2Value.setText( mSo2Value );
            mcoValue.setText( mCoValue);
            mo3Value.setText( mO3Value );
            mno2Value.setText( mNo2Value );
            mpm10Value.setText( mPm10Value );
            mpm25Value.setText( mPm25Value );

            getWeNKyeong();

        }
    }

    private void getWeNKyeong(){

        AsyncTask.execute( new Runnable() {
            @Override
            public void run() {
                // Create URL
                BufferedReader br;
                String str;
                URL temp = null;

                try {
                    temp = new URL("https://dapi.kakao.com/v2/local/geo/transcoord.json?" +
                            "x="+wedo+"&y="+kyeongdo+"&input_coord=WGS84&output_coord=TM");
                    // Create connection
                    HttpsURLConnection myConnection = (HttpsURLConnection) temp.openConnection();
                    myConnection.setRequestMethod( "GET" );
                    myConnection.setRequestProperty( "Authorization","KakaoAK 7df147ac625003bcaa849f0f113341be");

                    Log.d("^^^^^^^^^^^",myConnection.getContent().toString());

                    if (200 <= myConnection.getResponseCode() && myConnection.getResponseCode() <= 299) {

                        InputStreamReader tmp = new InputStreamReader( myConnection.getInputStream(),"UTF-8" );
                        BufferedReader reader = new BufferedReader( tmp );
                        StringBuffer buffer = new StringBuffer(  );

                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }

                        Log.d("buffer",buffer.toString());

                        JSONObject jsonObject = new JSONObject( buffer.toString() );

                        JSONArray jsonArray = jsonObject.getJSONArray("documents");

                        for(int i =  0 ; i <jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject( i );

                            // tmX 좌표 & tmY 좌표를 구한다.
                            tmX = jsonObject1.optString("x");
                            tmY = jsonObject1.optString("y");
                            getStationName();
                        }
                        reader.close();

                    } else {

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getStationName(){

        AsyncTask.execute( new Runnable() {
            @Override
            public void run() {

                org.w3c.dom.Document document;
                URL url;

                try {
                    url = new URL( "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?" +
                            "tmX="+tmX+"&tmY="+tmY+"&"+
                            "ServiceKey=%2BDQd8pShYLPqi01nFPYY8nkzSSmY1ZdloIu2WEwO%2BnWnPWI1frIATtnp%2FB3hwvUJVvMimjhngiunQZRl7S%2BCyA%3D%3D" );
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    document = db.parse( new InputSource( url.openStream() ) );
                    document.getDocumentElement().normalize();

                    NodeList station = document.getElementsByTagName( "item" );
                    Node node = station.item(0);
                    Element element = (Element) node;

                    // 관측소 이름 구하기 --> stationName , 관측소와 거리 --> farFrom
                    NodeList nodeList = element.getElementsByTagName( "stationName" );
                    staionName = nodeList.item( 0 ).getChildNodes().item( 0 ).getNodeValue();
                    farFrom = nodeList.item( 0 ).getChildNodes().item( 0 ).getNodeValue();

                    getDailyInfo();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } );

    }

    private void getDailyInfo(){

        AsyncTask.execute( new Runnable() {
            @Override
            public void run() {

                org.w3c.dom.Document document;
                URL url;

                try {
                    url = new URL( "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?" +
                            "stationName="+staionName+"&dataTerm=daily&pageNo=1&numOfRows=10&" +
                            "ServiceKey=%2BDQd8pShYLPqi01nFPYY8nkzSSmY1ZdloIu2WEwO%2BnWnPWI1frIATtnp%2FB3hwvUJVvMimjhngiunQZRl7S%2BCyA%3D%3D&ver=1.3" );

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    document = db.parse( new InputSource( url.openStream() ) );
                    document.getDocumentElement().normalize();

                    NodeList nodeList = document.getElementsByTagName( "dataTime" );
                    NodeList nodeList1 = document.getElementsByTagName( theValue );

                    String[] station_theValue = new String[10];
                    String[] station_dataTime = new String[10];

                    for(int i =0 ; i<nodeList.getLength(); i++){

                        Log.d("mathmath",nodeList.item( i ).getFirstChild().getNodeValue()+"\n");
                        String temp = nodeList.item( i ).getFirstChild().getNodeValue();
                        String[] temp1 = temp.split( " " );
                        String[] temp2 = temp1[1].split( ":" );
                        station_dataTime[i] = temp2[0];

                    }
                    for(int i =0 ; i<nodeList.getLength(); i++){

                        Log.d("mathmath",nodeList1.item( i ).getFirstChild().getNodeValue()+"\n");
                        station_theValue[i] = nodeList1.item( i ).getFirstChild().getNodeValue();
                    }

                    setLineChart(station_dataTime,station_theValue);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } );

    }

    private void setLineChart(String[] time, String[] value_10) {

        List<String> list = Arrays.asList(time);
        Collections.reverse(list);
        time = list.toArray(new String[list.size()]);

        List<String> list1 = Arrays.asList(value_10);
        Collections.reverse(list1);
        value_10 = list1.toArray(new String[list1.size()]);

        ArrayList<Entry> values = new ArrayList<Entry>();
        LineDataSet set;

        for (int i = 0; i < time.length; i++) {

            float value = Float.parseFloat( value_10[i] );
            float times = Float.parseFloat( time[i] );

            Log.d("valuevalue", String.valueOf( value ) );
            Log.d("timestimes",String.valueOf( times ));

            values.add(new Entry( i , value ) );
        }

        set = new LineDataSet( values,theValue );

        set.setAxisDependency( YAxis.AxisDependency.LEFT );

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>(  );
        dataSets.add( set );

        final String[] finalTime = time;

        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(  ){
            @Override
            public String getFormattedValue(float value) {
                return finalTime[(int) value];
            }
        };

        xAxis.setGranularity( 1f );
        xAxis.setValueFormatter( formatter );

        LineData data = new LineData(dataSets);
        lineChart.setData( data );
        lineChart.invalidate();

        // End of progress
        progressDialog.dismiss();

    }

    private TextView.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
         switch (v.getId()){
             case R.id.pm10Value:
                 theValue = "pm10Value";
                 getDailyInfo();
                 break;
             case R.id.pm25Value:
                 theValue = "pm25Value";
                 getDailyInfo();
                 break;
             case R.id.no2Value:
                 theValue = "no2Value";
                 getDailyInfo();
                 break;
             case R.id.o3Value:
                 theValue = "o3Value";
                 getDailyInfo();
                 break;
             case R.id.so2Value:
                 theValue = "so2Value";
                 getDailyInfo();
                 break;
             case R.id.coValue:
                 theValue = "coValue";
                 getDailyInfo();
                 break;
         }
        }
    };
}
