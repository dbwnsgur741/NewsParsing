package com.example.crawaling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListViewActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView ToolbarTextView;
    private TextView MainTitleTextView;
    private String string;
    private static String[] ListStringArray1;
    private static String[] ListStringArray2;
    private static String[] ListStringArray3;
    private static String[] ListStringArray4;
    private String[] strings = new String[2];
    private OpenApi openApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_listview );

        /* 툴바 세팅 */

        Toolbar toolbar = (Toolbar) findViewById(R.id.msq_detail_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        /* 툴바 세팅  끝 */

        ToolbarTextView = findViewById( R.id.listView_toolbar_text );
        MainTitleTextView = findViewById( R.id.listView_main_title );
        mListView = findViewById( R.id.listView );

        Intent intent = getIntent();
        string = intent.getStringExtra( "List" );
        SetListStringArray();
        ToolbarTextView.setText( string );
        MainTitleTextView.setText( string );

        /* 아이템 추가 및 어댑터 등록 */
        setAdapter();
    }

    private void SetListStringArray() {
       ListStringArray1 = getResources().getStringArray( R.array.대기오염 );
       ListStringArray2 = getResources().getStringArray( R.array.수질오염 );
       ListStringArray3 = getResources().getStringArray( R.array.토양오염 );
       ListStringArray4 = getResources().getStringArray( R.array.기타오염 );
    }

    private void setAdapter(){
        final ListViewAdapter mListViewAdapter = new ListViewAdapter();

        switch (string){
            case "대기오염":
                strings = ListStringArray1;
                new OpenApi().execute(  );
                break;
            case "수질오염":
                strings = ListStringArray2;
                break;
            case "토양오염":
                strings = ListStringArray3;
                break;
            case "기타오염":
                strings = ListStringArray4;
                break;
        }

        for(int i =1 ; i<strings.length; i++){
            mListViewAdapter.addItem( strings[i] );
        }

        mListView.setAdapter( mListViewAdapter );
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", strings[1]);
                        startActivity( intent );
                        finish();
                        break;
                    case 1:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", strings[2]);
                        startActivity( intent );
                        finish();
                        break;
                }
            }
        } );
    }

    private class OpenApi extends AsyncTask<Void,Void,Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //진행다일로그 시작
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListViewActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate( values );
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled( aVoid );
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BufferedReader bufferedReader = null;
            try{
                Log.d("1111","22222");
                String urlstr = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst" +
                        "?sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY&pageNo=1&numOfRows=40&" +
                        "ServiceKey=%2BDQd8pShYLPqi01nFPYY8nkzSSmY1ZdloIu2WEwO%2BnWnPWI1frIATtnp%2FB3hwvUJVvMimjhngiunQZRl7S%2BCyA%3D%3D";
                URL url = new URL(urlstr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod( "GET" );
                bufferedReader = new BufferedReader( new InputStreamReader( urlConnection.getInputStream(),"UTF-8" ) );
                String result = "";
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    result = result + line + "\n";
                }
                Log.d("@@@@@@@@2",result);
            } catch (Exception e) {
                Log.d("111111", String.valueOf( e ) );
                e.printStackTrace();
            }
            return null;
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
