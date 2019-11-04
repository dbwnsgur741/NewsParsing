package com.example.crawaling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList<>(  );
    private ImageView leftImg;
    private ImageView rightImg;
    private static int cnt = 1;
    private static String category;
    private TextView page;
     //파싱할 홈페이지의 URL주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById( R.id.recyclerview );
        leftImg = (ImageView)findViewById( R.id.left_img );
        rightImg = (ImageView)findViewById( R.id.right_img );
        page = (TextView)findViewById( R.id.page_num );

        page.setText( cnt+" PAGE" );

        Intent intent = getIntent();
        this.category = intent.getStringExtra( "index" );
        Log.d("#####",category);

        new Description().execute();

        leftImg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt >1){
                    Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                    intent.putExtra( "index",category );
                    cnt -= 1;
                    page.setText( cnt+" PAGE" );
                    startActivity( intent );
                    finish();
                }else{
                    finish();
                    /*
                    Toast.makeText(getApplicationContext(),"첫 페이지 입니다.",Toast.LENGTH_SHORT).show();
                    */
                }
            }
        });

        rightImg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                intent.putExtra( "index",category );
                cnt += 1;
                page.setText( cnt+" PAGE" );
                startActivity( intent );
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;
        private String htmlPageUrl ="http://search.daum.net/search?nil_suggest=btn&w=news&DA=PGD&cluster=y&q="+category+"&p="+cnt;

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Document doc = Jsoup.connect(htmlPageUrl).get();
                //테스트1
                Elements titles= doc.select("div[class=type_fulltext wid_n] div[class=coll_cont]").select("li");
                int size = titles.size();
                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    String desc = e.select( "li p[class=f_eb desc]" ).text();
                    String title =e.select( "li a" ).text();
                    String imgUrl = e.select( "li div[class=inner] a img" ).attr( "src" );
                    if(!title.equals( "" )){
                        String link_url = e.select( "li div[class=inner] a" ).attr( "href" );
                        Log.d("!!!!!!!!",link_url);
                        list.add(new ItemObject( title,desc,imgUrl,link_url ));
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            MyAdapter myAdapter = new MyAdapter(list,getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
            progressDialog.dismiss();
        }
    }
}
