package com.weather.fixyoo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private String cLatitude;
    private String cLongitude;

    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;
    private Context context;

    private String cTemperature;
    private String cHumidity;
    private String cImg;
    private String cMaxTemp;
    private String cMinTemp;
    private String cWeather;
    private String cDescription;
    private String cNation;
    private String cCountry;

    private TextView TV_Location;
    private TextView TV_CTEMP;
    private TextView TV_MAIN;
    private TextView TV_DESC;
    private TextView TV_Humidity;
    private TextView TV_MAXTEMP;
    private TextView TV_MINTEMP;

    private ImageView IV_weather;
    private LineChart lineChart;
    private XAxis xAxis;
    private YAxis yAxis;

    String[] data  = new String[6];
    String[] temperature = new String[6];
    String[] icon = new String[6];

    String[] data2 = {"3시간 후","6시간 후","9시간 후","12시간 후","15시간 후","18시간 후"};

    Handler handler = new Handler( Looper.getMainLooper() );

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.weater_layout );

        context = this;

        sharedPreferences = getSharedPreferences( "Location",MODE_PRIVATE );
        cLatitude = sharedPreferences.getString( "latitude","" );
        cLongitude = sharedPreferences.getString( "longitude","" );

        Log.d("@@@@",cLatitude);
        Log.d("@@@@",cLongitude);

        IV_weather = findViewById( R.id.IV_weather );
        TV_Location = findViewById( R.id.TV_Location );
        TV_CTEMP = findViewById( R.id.TV_CTEMP );
        TV_DESC = findViewById( R.id.TV_DESC );
        TV_Humidity = findViewById( R.id.TV_Humidity );
        TV_MAXTEMP = findViewById( R.id.TV_MAXTEMP );
        TV_MINTEMP = findViewById( R.id.TV_MINTEMP );
        recyclerView = findViewById( R.id.recyclerview );
        /*
        lineChart = (LineChart)findViewById( R.id.weather_chart );
        lineChart.getDescription().setEnabled( false );

        xAxis = lineChart.getXAxis();
        yAxis = lineChart.getAxisLeft();
        lineChart.getAxisRight().setEnabled( false );
        */

        new GetWeatherTask().execute(  );
    }

    public class GetWeatherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Create URL

            String str;
            URL temp = null;

            try {
                temp = new URL( "https://api.openweathermap.org/data/2.5/" +
                        "weather?lat=" + cLatitude + "&lon=" + cLongitude + "&units=metric&" +
                        "appid=40efb10b3ddbcfc23121f9403dbb1bb9" );

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) temp.openConnection();
                httpsURLConnection.setRequestMethod( "GET" );

                InputStreamReader tmp = new InputStreamReader( httpsURLConnection.getInputStream(), "UTF-8" );
                BufferedReader reader = new BufferedReader( tmp );
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append( str );
                }

                Log.d( "buffer", buffer.toString() );

                JSONObject jsonObject = new JSONObject( buffer.toString() );
                cImg = jsonObject.getJSONArray( "weather" ).getJSONObject( 0 ).getString("icon");
                cTemperature = jsonObject.getJSONObject( "main" ).getString("temp");
                cHumidity = jsonObject.getJSONObject( "main" ).getString("humidity");
                cMaxTemp = jsonObject.getJSONObject( "main" ).getString("temp_max");
                cMinTemp = jsonObject.getJSONObject( "main" ).getString("temp_min");
                cWeather = jsonObject.getJSONArray( "weather" ).getJSONObject( 0 ).getString("main");
                cDescription = jsonObject.getJSONArray( "weather" ).getJSONObject( 0 ).getString("description");
                cNation = jsonObject.getJSONObject( "sys" ).getString("country");
                cCountry = jsonObject.getString( "name" );

                reader.close();
            }

            catch(MalformedURLException e){
                Log.d( "!!!!!!!", " !! 날씨 api 접속 오류 !!" );
                e.printStackTrace();
            }

            catch(IOException e){
                Log.d( "!!!!!!!!", "!! 날씨 데이터 제대로 못가져옴 !!" );
                e.printStackTrace();
            }

            catch(JSONException e){
                Log.d( "!!!!!!!!", "!! JSON 객체로 변환 오류  !!" );
                    e.printStackTrace();
                }

            return null;
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
        protected void onPostExecute(Void aVoid) {

            setWeatherImage();

            TV_Location.setText( cNation + "-" + cCountry );
            TV_CTEMP.setText( cTemperature+ "\u2103" );
            TV_DESC.setText( cDescription );
            TV_Humidity.setText( "Humidity \n\n " + cHumidity + "%" );
            TV_MAXTEMP.setText( "Max TEMP \n\n" + cMaxTemp + "\u2103" );
            TV_MINTEMP.setText( "Min TEMP \n\n" + cMinTemp + "\u2103" );

        }
    }

    private void setWeatherImage(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    URL url = new URL(" http://openweathermap.org/img/w/" + cImg + ".png");
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            IV_weather.setImageBitmap(bm);
                        }
                    },0);
                    IV_weather.setImageBitmap(bm); //비트맵 객체로 보여주기
                } catch(Exception e){

                }

            }
        });
        t.start();
        progressDialog.dismiss();
        new GetChartApi().execute(  );
    }

    public class GetChartApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Create URL

            String str;
            URL temp = null;

            try {
                temp = new URL( "https://api.openweathermap.org/data/2.5/forecast?" +
                        "lat="+ cLatitude +"&lon="+ cLongitude +"&cnt=6&" +
                        "appid=40efb10b3ddbcfc23121f9403dbb1bb9" );

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) temp.openConnection();
                httpsURLConnection.setRequestMethod( "GET" );

                InputStreamReader tmp = new InputStreamReader( httpsURLConnection.getInputStream(), "UTF-8" );
                BufferedReader reader = new BufferedReader( tmp );
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append( str );
                }

                Log.d( "buffer", buffer.toString() );

                JSONObject jsonObject = new JSONObject( buffer.toString() );


                for(int i = 0 ; i < jsonObject.getJSONArray( "list" ).length() ; i ++){

                    // 켈빈온도 --> 섭씨 온도로 변환

                    String tt = jsonObject.getJSONArray( "list" ).getJSONObject(i).getJSONObject( "main" ).getString( "temp" );
                    float aa = Float.parseFloat( tt );
                    aa -= 273.15;
                    tt = String.valueOf( aa );
                    if( aa < 10 ){
                        tt = tt.substring(0,3);
                    }else{
                        tt = tt.substring(0,4);
                    }

                    // 온도 설정 완료


                   // 날짜 자르기

                    String tt2 = jsonObject.getJSONArray( "list" ).getJSONObject( i ).getString( "dt_txt" );
                    String[] temp1 = tt2.split( " " );
                    String[] temp2 = temp1[1].split( ":" );
                    tt2 = temp2[0];

                    // 날짜 자르기 완료 ..

                    Log.d("$$$$$",tt);
                    Log.d("%%%%%",tt2);
                    data[i] = tt2;
                    temperature[i] = tt;
                    icon[i] = jsonObject.getJSONArray( "list" ).getJSONObject( i ).getJSONArray( "weather" ).getJSONObject(0).getString("icon");
                    Log.d("%%%%%%%%%55",icon[i]);
                }

                /* 차트 만들 때 사용 .

                setLineChart(data,temperature);
                */
                reader.close();
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        MyAdapter2 myAdapter2 = new MyAdapter2();
                        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
                        recyclerView.setLayoutManager( layoutManager );
                        recyclerView.setAdapter( myAdapter2 );
                    }
                } );

            }

            catch(MalformedURLException e){
                Log.d( "!!!!!!!", " !! Daily api 접속 오류 !!" );
                e.printStackTrace();
            }

            catch(IOException e){
                Log.d( "!!!!!!!!", "!! Daily 데이터 제대로 못가져옴 !!" );
                e.printStackTrace();
            }

            catch(JSONException e){
                Log.d( "!!!!!!!!", "!! JSON 객체로 변환 오류  !!" );
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    progressDialog2 = new ProgressDialog( context );
                    progressDialog2.setProgressStyle( ProgressDialog.STYLE_SPINNER );
                    progressDialog2.setMessage( "차트 생성중 입니다.." );
                    progressDialog2.show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog2.dismiss();

        }
    }


    /*
    private void setLineChart(String[] data, String[] temperature) {
        ArrayList<Entry> values = new ArrayList<Entry>( );
        LineDataSet set;

        for(int i = 0 ; i < data.length ; i ++){

            float date = Float.valueOf( data[i] );
            float value = Float.parseFloat( temperature[i] );

            values.add(new Entry( i,value ));
        }

        set = new LineDataSet( values, "" );
        set.setAxisDependency( YAxis.AxisDependency.LEFT );

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>(  );
        dataSets.add( set );

        final String[] finalTime = data;

        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(  ){
            @Override
            public String getFormattedValue(float value) {
                return finalTime[(int) value];
            }
        };

        xAxis.setGranularity( 1f );
        xAxis.setValueFormatter( formatter );

        LineData lineData = new LineData(dataSets);
        lineChart.setData( lineData );
        lineChart.invalidate();
    }

    */
    public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder>{

        @NonNull
        @Override
        public MyAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.itemlist2,viewGroup,false );
            final ViewHolder holder = new ViewHolder( view );
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter2.ViewHolder viewHolder, int i) {

            viewHolder.textView_top.setText( data2[i] );
            viewHolder.textView_bottom.setText( temperature[i]+"\u2103" );

            String temp = "http://openweathermap.org/img/w/"+icon[i]+".png";

            GlideApp.with(viewHolder.itemView).load(temp)
                    .override(300,300)
                    .into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return icon.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textView_top , textView_bottom;

            public ViewHolder(@NonNull View itemView) {
                super( itemView );

                imageView = (ImageView) itemView.findViewById( R.id.imageview_middle );
                textView_top = itemView.findViewById( R.id.textView_Top );
                textView_bottom = itemView.findViewById( R.id.textView_bottom );

            }
        }
    }
}

