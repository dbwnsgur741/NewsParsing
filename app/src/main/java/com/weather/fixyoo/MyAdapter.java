package com.weather.fixyoo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<ItemObject> mList;
    private Context context;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private TextView textView_title, textView_desc;

        public ViewHolder(final View itemView) {
            super(itemView);

            imageView_img = (ImageView) itemView.findViewById(R.id.imageView_img);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title);
            textView_desc = (TextView) itemView.findViewById(R.id.textView_desc);
        }
    }

    //생성자
    public MyAdapter(ArrayList<ItemObject> list,Context context) {
        this.mList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, parent, false);
        final ViewHolder holder = new ViewHolder( view );
        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = mList.get( holder.getAdapterPosition() ).getLink_url();
                Intent intent = new Intent(context , WebViewActivity.class );
                intent.putExtra( "url",URL );
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
        holder.textView_desc.setText(String.valueOf(mList.get(position).getDesc()));

        //다 해줬는데도 GlideApp 에러가 나면 rebuild project를 해주자.
        Log.d("#######",mList.get(position).getImg_url());

        if(mList.get( position ).getImg_url().equals( "" )){
            holder.imageView_img.setImageResource( R.drawable.logo );
        }
        else{
            GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                    .override(300,400)
                    .into(holder.imageView_img);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
