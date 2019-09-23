package com.example.crawaling;

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
        org.w3c.dom.Document doc;
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst" +
                        "?sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY&pageNo=1&numOfRows=40&" +
                        "ServiceKey=%2BDQd8pShYLPqi01nFPYY8nkzSSmY1ZdloIu2WEwO%2BnWnPWI1frIATtnp%2FB3hwvUJVvMimjhngiunQZRl7S%2BCyA%3D%3D");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";
            NodeList nodeList = doc.getElementsByTagName("item");

            Node node = nodeList.item(22);
            Element fstElmnt = (Element) node;

            NodeList dataTime = fstElmnt.getElementsByTagName("dataTime");
            s += "dataTime = "+  dataTime.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList cityName = fstElmnt.getElementsByTagName("cityName");
            s += "cityName = "+  cityName.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList o3Value  = fstElmnt.getElementsByTagName("o3Value");
            s += "o3Value = "+ o3Value.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList no2Value  = fstElmnt.getElementsByTagName("no2Value");
            s += "no2Value = "+ no2Value.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList coValue  = fstElmnt.getElementsByTagName("coValue");
            s += "coValue = "+ coValue.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList pm10Value = fstElmnt.getElementsByTagName("pm10Value");
            s += "pm10Value = "+  pm10Value.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList pm25Value = fstElmnt.getElementsByTagName("pm25Value");
            s += "pm25Value = "+  pm25Value.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            Log.d("@@@@@",s);
            super.onPostExecute(doc);
        }
    }

