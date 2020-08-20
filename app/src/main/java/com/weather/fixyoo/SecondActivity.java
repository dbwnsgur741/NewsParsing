package com.weather.fixyoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class SecondActivity extends AppCompatActivity {

    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.secondactivity );

        Toolbar toolbar = (Toolbar) findViewById(R.id.msq_detail_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        linearLayout1 = (LinearLayout)findViewById( R.id.num1 );
        linearLayout2 = (LinearLayout)findViewById( R.id.num2 );
        linearLayout3 = (LinearLayout)findViewById( R.id.num3 );
        linearLayout4 = (LinearLayout)findViewById( R.id.num4 );

        linearLayout1.setOnClickListener( this.onClickListener );
        linearLayout2.setOnClickListener( this.onClickListener );
        linearLayout3.setOnClickListener( this.onClickListener );
        linearLayout4.setOnClickListener( this.onClickListener );
    }

    private LinearLayout.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
          switch (v.getId()){
              case R.id.num1:
                  intent = new Intent( getApplicationContext(),ListViewActivity.class );
                  intent.putExtra( "List","대기오염" );
                  startActivity( intent );
                  break;
              case R.id.num2:
                  intent = new Intent( getApplicationContext(),ListViewActivity.class );
                  intent.putExtra( "List","수질오염" );
                  startActivity( intent );
                  break;
              case R.id.num3:
                  intent = new Intent( getApplicationContext(),ListViewActivity.class );
                  intent.putExtra( "List","토양오염" );
                  startActivity( intent );
                  break;
              case R.id.num4:
                  intent = new Intent( getApplicationContext(),ListViewActivity.class );
                  intent.putExtra( "List","기타오염" );
                  startActivity( intent );
                  break;
          }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
