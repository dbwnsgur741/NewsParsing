package com.weather.fixyoo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
        private Context context;

        private ProgressDialog progressDialog;

        public GetXMLTask(Context context1){
            context = context1;
        }
        @Override
        protected Document doInBackground(String... urls) {
            Log.d("!!!!!","def");
            URL url;
            try {
                url = new URL( "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst" +
                        "?sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY&pageNo=1&numOfRows=40&" +
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

            NodeList nodeList = doc.getElementsByTagName( "item" );

            Node node = nodeList.item( 22 );
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

            Log.d("dataTime",mDataTime);
            Log.d("cityName",mCityName);
            Log.d("mSo2Value",mSo2Value);
            Log.d("mO3value",mO3Value);
            progressDialog.dismiss();
        }



        public String getmSo2Value() {
            return mSo2Value;
        }

        public String getmDataTime() {
            return mDataTime;
        }

        public String getmCityName() {
            return mCityName;
        }

        public String getmO3Value() {
            return mO3Value;
        }

        public String getmNo2Value() {
            return mNo2Value;
        }

        public String getmCoValue() {
            return mCoValue;
        }

        public String getmPm10Value() {
            return mPm10Value;
        }

        public String getmPm25Value() {
            return mPm25Value;
        }

    }

