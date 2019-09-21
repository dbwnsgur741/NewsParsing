package com.example.crawaling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListViewActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_listview );

        mListView = findViewById( R.id.listView );

        /* 아이템 추가 및 어댑터 등록 */
        setAdapter();
    }

    private void setAdapter(){
        final ListViewAdapter mListViewAdapter = new ListViewAdapter();

        mListViewAdapter.addItem( "수질오염" );
        mListViewAdapter.addItem( "토양오염" );
        mListViewAdapter.addItem( "대기오염" );
        mListViewAdapter.addItem( "미세 플라스틱" );
        mListViewAdapter.addItem( "미세 먼지" );

        mListView.setAdapter( mListViewAdapter );
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", "수질오염");
                        startActivity( intent );
                        finish();
                        break;
                    case 1:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", "토양오염");
                        startActivity( intent );
                        finish();
                        break;
                    case 2:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", "대기오염");
                        startActivity( intent );
                        finish();
                        break;
                    case 3:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", "미세 플라스틱");
                        startActivity( intent );
                        finish();
                        break;
                    case 4:
                        intent = new Intent( getApplicationContext(), MainActivity.class );
                        intent.putExtra( "index", "미세 먼지");
                        startActivity( intent );
                        finish();
                        break;
                }
            }
        } );
    }
}
