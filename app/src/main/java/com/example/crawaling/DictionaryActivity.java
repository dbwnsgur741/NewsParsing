package com.example.crawaling;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DictionaryActivity extends AppCompatActivity {

    private Animation translateLeftAnim;
    private Animation translateRightAnim;
    private LinearLayout slidingPanel;
    private Button button;
    private TextView TV_title, TV_desc, TV_pollution;
    private boolean isPageOpen=false;

    private Spinner spinner1,spinner2,spinner3,spinner4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);

        spinner1 = (Spinner)findViewById( R.id.spinner1 );
        spinner2 = (Spinner)findViewById( R.id.spinner2 );
        spinner3 = (Spinner)findViewById( R.id.spinner3 );
        spinner4 = (Spinner)findViewById( R.id.spinner4 );

        translateLeftAnim =AnimationUtils.loadAnimation(this,R.anim.translate_left);
        translateRightAnim =AnimationUtils.loadAnimation(this,R.anim.translate_right);

        translateLeftAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    slidingPanel.setVisibility(View.INVISIBLE);
                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        translateRightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    slidingPanel.setVisibility(View.INVISIBLE);
                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slidingPanel=(LinearLayout)findViewById(R.id.slidingPanel);
        button = (Button)findViewById( R.id.button);

        TV_title = (TextView)findViewById( R.id.dic_title );
        TV_desc = (TextView)findViewById( R.id.dic_desc );
        TV_pollution = (TextView)findViewById( R.id.TV_pollution );

        TV_pollution.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_title.setText( "환경오염이란?" );
                TV_desc.setText( "'환경오염'이란 사업활동 및 그 밖의 사람의 활동에 의하여 발생하는 대기오염, 수질오염, 토양오염, 해양오염, 방사능 오염, 소음 및 진동, 악취, 일조 방해, 인공조명에 의한 빛공해 등으로서 사람의 건강이나 환경에 피해를 주는 상태를 말한다. \n\n-환경정책기본법 제 3조의 4" );
                slidingPanel.startAnimation(translateRightAnim);
                slidingPanel.setVisibility(View.GONE);
            }
        } );


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    slidingPanel.startAnimation(translateRightAnim);
                    slidingPanel.setVisibility(View.GONE);
                }else{
                    slidingPanel.startAnimation(translateLeftAnim);
                    slidingPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        addItemsOnSpinner();
        addEventListener();
    }

    public void addItemsOnSpinner(){

        List<ArrayList<String>> list = new ArrayList<ArrayList<String>>(  );

        List<String> list1 = new ArrayList<String>(  );
        List<String> list2 = new ArrayList<String>(  );
        List<String> list3 = new ArrayList<String>(  );
        List<String> list4 = new ArrayList<String>(  );

        list1.add("대기오염");
        list1.add("미세먼지");
        list1.add("지구온난화");
        list1.add("황사");

        list2.add("수질오염");
        list2.add("부영양화");
        list2.add("기름유출");
        list2.add("미세 플라스틱");

        list3.add("토양오염");
        list3.add("농약");
        list3.add("사막화");

        list4.add("기타오염");
        list4.add("빛공해");
        list4.add("원자력사고");

        list.add( (ArrayList<String>) list1 );
        list.add( (ArrayList<String>) list2 );
        list.add( (ArrayList<String>) list3 );
        list.add( (ArrayList<String>) list4 );

        for(int i = 0; i < list.size(); i++){

            Log.d("!!!!","!!!!!!");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( this,
                    android.R.layout.simple_spinner_item, list.get( i ) );
            dataAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

            switch (i){
                case 0:
                    Log.d("!!!!","@@@@@@");
                    spinner1.setAdapter( dataAdapter );
                    break;
                case 1:
                    spinner2.setAdapter( dataAdapter );
                    break;
                case 2:
                    spinner3.setAdapter( dataAdapter );
                    break;
                case 3:
                    spinner4.setAdapter( dataAdapter );
                    break;
            }
        }
    }

    public void addEventListener(){

        final String[] array1 = getResources().getStringArray( R.array.대기오염 );
        final String[] array2 = getResources().getStringArray( R.array.수질오염 );
        final String[] array3 = getResources().getStringArray( R.array.토양오염 );
        final String[] array4 = getResources().getStringArray( R.array.기타오염 );

        spinner1.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    /*
                    case 0:
                        textView.setText( array1[0] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                        */
                    case 1:
                        TV_title.setText( array1[1] );
                        TV_desc.setText( array1[2] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                    case 2:
                        TV_title.setText( array1[3] );
                        TV_desc.setText( array1[4] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                    case 3:
                        TV_title.setText( array1[5] );
                        TV_desc.setText( array1[6] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
            }
            spinner1.setSelection( 0 );
        }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TV_desc.setText( array1[0] );
                slidingPanel.startAnimation(translateRightAnim);
                slidingPanel.setVisibility(View.GONE);
            }
        });

        spinner2.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    /*
                    case 0:
                        textView.setText( array1[0] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                        */
                    case 1:
                        TV_title.setText( array2[1] );
                        TV_desc.setText( array2[2] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                    case 2:
                        TV_title.setText( array2[3] );
                        TV_desc.setText( array2[4] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                    case 3:
                        TV_title.setText( array2[5] );
                        TV_desc.setText( array2[6] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                }
                spinner2.setSelection( 0 );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TV_desc.setText( array3[0] );
                slidingPanel.startAnimation(translateRightAnim);
                slidingPanel.setVisibility(View.GONE);
            }
        } );

        spinner3.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    /*
                    case 0:
                        textView.setText( array1[0] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                        */
                    case 1:
                        TV_title.setText( array3[1] );
                        TV_desc.setText( array3[2] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                    case 2:
                        TV_title.setText( array3[3] );
                        TV_desc.setText( array3[4] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                }
                spinner3.setSelection( 0 );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TV_desc.setText( array3[0] );
                slidingPanel.startAnimation(translateRightAnim);
                slidingPanel.setVisibility(View.GONE);
            }
        } );

        spinner4.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    /*
                    case 0:
                        textView.setText( array1[0] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                        */
                    case 1:
                        TV_title.setText( array4[1] );
                        TV_desc.setText( array4[2] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                    case 2:
                        TV_title.setText( array4[3] );
                        TV_desc.setText( array4[4] );
                        slidingPanel.startAnimation(translateRightAnim);
                        slidingPanel.setVisibility(View.GONE);
                        break;
                }
                spinner4.setSelection( 0 );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TV_desc.setText( array4[0] );
                slidingPanel.startAnimation(translateRightAnim);
                slidingPanel.setVisibility(View.GONE);
            }
        } );
    }
    @Override
    public void onBackPressed() {
        if(isPageOpen){
            slidingPanel.startAnimation(translateRightAnim);
            slidingPanel.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    // if you use Naver dictionary API --> Use this !! ** Liam Martin 11.16 **
    private void getAPI() {

        AsyncTask.execute( new Runnable() {
            org.w3c.dom.Document document;

            @Override
            public void run() {
                // Create URL
                BufferedReader br;
                String str;
                URL temp = null;

                try {
                    String text = URLEncoder.encode("환경오염", "UTF-8");

                    temp = new URL( "https://openapi.naver.com/v1/search/encyc.json?" +
                            "query="+text+"&display=10&start=1&sort=sim" );

                    // Create connection
                    HttpsURLConnection myConnection = (HttpsURLConnection) temp.openConnection();
                    myConnection.setRequestMethod( "GET" );
                    myConnection.setRequestProperty("X-Naver-Client-Id","OfgdWlIRiEeccfkDwpAg");
                    myConnection.setRequestProperty("X-Naver-Client-Secret","r_U0mvxpqT");

                    InputStreamReader tmp = new InputStreamReader( myConnection.getInputStream(),"UTF-8" );
                    BufferedReader reader = new BufferedReader( tmp );
                    StringBuffer buffer = new StringBuffer(  );

                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    Log.d("buffer",buffer.toString());

                    JSONObject jsonObject = new JSONObject( buffer.toString() );
                    for(int i = 0 ; i < 3 ; i ++ ){
                        String tt = jsonObject.getJSONArray( "items" ).getJSONObject( i ).getString( "description" );
                        Log.d("zvzvzvzvzvzv",tt.replace( "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "" ));
                    }

                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {

                        }
                    } );


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}