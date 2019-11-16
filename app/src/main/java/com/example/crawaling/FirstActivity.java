package com.example.crawaling;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FirstActivity extends AppCompatActivity {

    private static boolean loading = true;

    private LinearLayout linearLayout_dictionary;
    private LinearLayout linearLayout_news;
    private LinearLayout linearLayout_atmos;
    private LinearLayout linearLayout_weather;

    private ImageView imageView;
    private GpsTracker gpsTracker;

    private static int REQUEST_ACCESS_FINE_LOCATION = 1000;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Context context;

    private String cLatitude;
    private String cLongitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.real_first_activity );

        context =this;

        sharedPreferences = getSharedPreferences( "Location",MODE_PRIVATE );
        editor = sharedPreferences.edit();

        cLatitude = sharedPreferences.getString( "latitude","" );
        cLongitude = sharedPreferences.getString( "longitude","" );

        Log.d("CLatitude",cLatitude);
        Log.d("CLongitude",cLongitude);

        linearLayout_dictionary = findViewById( R.id.linear_dictionary );
        linearLayout_news = findViewById( R.id.linear_news );
        linearLayout_atmos = findViewById( R.id.linear_atmos );
        linearLayout_weather = findViewById( R.id.linear_weather );

        linearLayout_dictionary.setOnClickListener( this.onClickListener );
        linearLayout_news.setOnClickListener( this.onClickListener );
        linearLayout_atmos.setOnClickListener( this.onClickListener );
        linearLayout_weather.setOnClickListener( this.onClickListener );

        Toolbar toolbar = (Toolbar) findViewById( R.id.msq_detail_toolbar );
        setSupportActionBar( toolbar );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled( false );
            getSupportActionBar().setDisplayHomeAsUpEnabled( false );
            getSupportActionBar().setDisplayShowTitleEnabled( false );
        }

        showDialogForLocationServiceSetting();

    }

    private LinearLayout.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()){
                case R.id.linear_dictionary:
                    intent = new Intent( context , DictionaryActivity.class );
                    startActivity( intent );
                    break;
                case R.id.linear_news:
                    intent = new Intent( context, SecondActivity.class );
                    startActivity( intent );
                    break;
                case R.id.linear_atmos:
                    intent = new Intent( context, LocationActivity.class );
                    startActivity( intent );
                    break;
                case R.id.linear_weather:
                    intent = new Intent( context, WeatherActivity.class );
                    startActivity( intent );
                    break;
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            saveCurrentLocation();

        } else {
            Toast.makeText( this, "위치설정하세요.", Toast.LENGTH_LONG ).show();
            finish();
        }

    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCheck = ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION );

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                // 권한 없음
                ActivityCompat.requestPermissions( this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION );

            } else {

                saveCurrentLocation();// ACCESS_FINE_LOCATION 에 대한 권한이 이미 있음.
            }
        }

// OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
        else {

        }
    }

    private void showDialogForLocationServiceSetting() {


        if(checkLocationServicesStatus()){

            checkPermission();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder( FirstActivity.this );
            builder.setTitle( "위치 서비스 비활성화" );
            builder.setMessage( "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                    + "위치 설정을 수정해주세요." );
            builder.setCancelable( true );
            builder.setPositiveButton( "설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent callGPSSettingIntent
                            = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                    startActivityForResult( callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE );
                }
            } );
            builder.setNegativeButton( "취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                }
            } );
            builder.create().show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GPS_ENABLE_REQUEST_CODE) {
            if (checkLocationServicesStatus()) {
                checkPermission();
            } else {
                Toast.makeText( this, "위치 서비스를 활성화해야합니다.", Toast.LENGTH_LONG ).show();
                Log.d( "...................", "................." );
                finish();
            }
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
        Log.d( "444444444444444444444", "444444444444444444444444444" );
        return locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) // GPS 프로바이더 사용가능 여부
                || locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ); // 네트워크 프로바이더 사용가능여부
    }

    private void saveCurrentLocation(){

        Log.d("afafafa","Afafaffaf");
        Log.d("ccccccc",cLatitude);

        if(cLatitude.equals( "" )){

            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            builder.setTitle( "위치 정보를 불러오지 못했습니다." );
            builder.setMessage( "다시 불러오시겠습니까?" );
            builder.setCancelable( false );
            builder.setPositiveButton( "불러오기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    gpsTracker = new GpsTracker(context);

                    double latitude = gpsTracker.getLatitude( );
                    double longitude = gpsTracker.getLongitude();

                    if(latitude == 0 ){
                        saveCurrentLocation();
                        return;
                    }
                    else{
                        editor.putString( "latitude", String.valueOf( latitude ) );
                        editor.putString("longitude", String.valueOf( longitude ) );
                        editor.commit();
                        Log.d("ㅇㅇㅇㅇㅇ경도", String.valueOf( latitude ) );
                        Log.d("ㅇㅇㅇㅇㅇ경도",String.valueOf( longitude ));
                    }
                }
            } );
            builder.setNegativeButton( "취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            } );
            builder.create().show();

        }

        else{

            gpsTracker = new GpsTracker( context );
            double mlatitude = gpsTracker.getLatitude();
            double mlongitude = gpsTracker.getLongitude();

            if(!cLatitude.equals( String.valueOf( mlatitude ))){
                editor.putString( "latitude",String.valueOf( mlatitude ) );
                editor.putString( "longitude",String.valueOf( mlongitude ) );
                editor.commit();
            }
        }

    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견 다시 시도해주세요.", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        Log.d("@@@@@@@@", String.valueOf( address ) );
        return address.getAddressLine(0).toString()+"\n";

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
