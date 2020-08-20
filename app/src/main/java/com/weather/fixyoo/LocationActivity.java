package com.weather.fixyoo;

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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity
{
    private GpsTracker gpsTracker;
    private Context context;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.location_activity);
        context = this;
        gpsTracker = new GpsTracker(context);

        sharedPreferences = getSharedPreferences( "Location",MODE_PRIVATE );
        editor = sharedPreferences.edit();

        Log.d("111111111111111111","!!!!!!!!!!!!!!!1");
        if (!checkLocationServicesStatus()) {
            Log.d("22222222222222222","2222222222222222222");
            showDialogForLocationServiceSetting();
        }else {
            Log.d("3333333333333333333","33333333333333333333");
            checkRunTimePermission();
        }

    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        Log.d("aaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaa");
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            Log.d("bbbbbbbbbbbbbbb","bbbbbbbbbbbbbbbbbbbb");
            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    Log.d("cccccccccccccccc","ccccccccccccccccccccc");
                    break;
                }
            }


            if ( check_result ) {

                checkRunTimePermission();

            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(LocationActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(LocationActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        Log.d("888888888","88888888888888888888");
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            Log.d("99999999999999","999999999999999999");


            double latitude = Double.parseDouble( sharedPreferences.getString( "latitude","" ) );
            double longitude = Double.parseDouble( sharedPreferences.getString( "longitude","" ) );

            Log.d("ㅇㅇㅇㅇㅇ경도", String.valueOf( latitude ) );
            Log.d("ㅇㅇㅇㅇㅇ경도",String.valueOf( longitude ));

            String address = getCurrentAddress(latitude, longitude);

            if(address == "주소 미발견"){
                Toast.makeText( this,"위치 정보를 제대로 받아오지 못했습니다.\n다시 시도해주세요.",Toast.LENGTH_LONG ).show();
                finish();
                return;
            }

            String[] array = address.split( " " );
            Log.d("mmmmmmmvvmsavmkslml",array[0]);

            String currentLocation = array[2] + array[3];
            String first_temp = array[1];
            first_temp =first_temp.substring( 0,2 );
            String second_temp = array[2];

            Intent intent = new Intent( getApplicationContext(),InfoAtmosActivity.class );
            intent.putExtra( "CurrentLocation",currentLocation );
            intent.putExtra( "first",first_temp );
            intent.putExtra( "second",second_temp);
            intent.putExtra( "wedo", longitude);
            intent.putExtra( "kyeongdo",latitude );
            startActivity( intent );
            finish();
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            Log.d("10101001010101010","10101010101010");
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(LocationActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LocationActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                Log.d("afafafafafaf","afafafafafaf");
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LocationActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
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


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        Log.d("5555555555555555555555","5555555555555555555555555555");

        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정해주세요.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                Log.d("777777777777777777","7777777777777777777777777");

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();

                        return;
                    }
                }

                break;
        }
        */

        if(requestCode == GPS_ENABLE_REQUEST_CODE){
            if(checkLocationServicesStatus()){
                Log.d("777777777777777777","7777777777777777777777777");
                checkRunTimePermission();
            }
            else{
                Toast.makeText( this,"위치 서비스를 활성화해야합니다.", Toast.LENGTH_LONG).show();
                Log.d("...................",".................");
                finish();
            }
        }


    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.d("444444444444444444444","444444444444444444444444444");
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) // GPS 프로바이더 사용가능 여부
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // 네트워크 프로바이더 사용가능여부
    }

}
