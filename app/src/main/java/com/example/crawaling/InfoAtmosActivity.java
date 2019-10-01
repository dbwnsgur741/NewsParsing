package com.example.crawaling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class InfoAtmosActivity extends AppCompatActivity {

    private SliderAdapter sliderAdapter;
    private ViewPager viewPager;
    private Context context;
    private static GetXMLTask getXMLTask;
    private String SIDO;
    private String DONGU;
    private String currentLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.infoatmoslayout );

        Intent intent = getIntent();
        currentLocation = intent.getStringExtra( "CurrentLocation" );
        SIDO = intent.getStringExtra( "first" );
        DONGU = intent.getStringExtra( "second" );

        context = this;
        viewPager = (ViewPager) findViewById( R.id.ViewPager );
        new GetXMLTask(  ).execute(  );
    }

    public class GetXMLTask extends AsyncTask<String, Void, Document> {

        public String mDataTime;
        public String mCityName;
        public String mSo2Value;
        public String mO3Value;
        public String mNo2Value;
        public String mCoValue;
        public String mPm10Value;
        public String mPm25Value;
        public org.w3c.dom.Document doc;


        private ProgressDialog progressDialog;

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
            mDataTime = "관측 시간 : " + dataTime.item( 0 ).getChildNodes().item( 0 ).getNodeValue();

            NodeList cityName = fstElmnt.getElementsByTagName( "cityName" );
            mCityName = "관측 장소 : " + cityName.item( 0 ).getChildNodes().item( 0 ).getNodeValue();

            NodeList so2Value = fstElmnt.getElementsByTagName( "so2Value" );
            mSo2Value = "아황산가스(SO2) : " + so2Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList o3Value = fstElmnt.getElementsByTagName( "o3Value" );
            mO3Value = "오존(O3) : " + o3Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList no2Value = fstElmnt.getElementsByTagName( "no2Value" );
            mNo2Value = "이산화질소(NO2) : " + no2Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList coValue = fstElmnt.getElementsByTagName( "coValue" );
            mCoValue = "일산화탄소(CO) : " + coValue.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(ppm)";

            NodeList pm10Value = fstElmnt.getElementsByTagName( "pm10Value" );
            mPm10Value = "미세먼지(PM10) : " + pm10Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(㎍/㎥)";

            NodeList pm25Value = fstElmnt.getElementsByTagName( "pm25Value" );
            mPm25Value = "미세먼지(PM2.5) : " + pm25Value.item( 0 ).getChildNodes().item( 0 ).getNodeValue()+"(㎍/㎥)";


            sliderAdapter = new SliderAdapter(context,currentLocation,mDataTime,mCityName,mSo2Value,mO3Value,mNo2Value,mCoValue,mPm10Value,mPm25Value);
            viewPager.setAdapter( sliderAdapter );
            progressDialog.dismiss();
        }
    }
}
