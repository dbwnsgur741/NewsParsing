package com.example.crawaling;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class DictionaryActivity extends AppCompatActivity {

    Animation translateLeftAnim;
    Animation translateRightAnim;
    LinearLayout slidingPanel;
    Button button;

    boolean isPageOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);

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

        getAPI();
    }

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

    @Override
    public void onBackPressed() {
        if(isPageOpen){
            slidingPanel.startAnimation(translateRightAnim);
            slidingPanel.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}