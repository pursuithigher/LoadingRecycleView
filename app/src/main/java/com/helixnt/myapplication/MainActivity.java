package com.helixnt.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.helixnt.adapter.MyLoadingAdapter;
import com.helixnt.nestscroll.LoadLayout;

import java.util.ArrayList;

/**
 * Created by QZhu on 16-7-22.
 */
public class MainActivity extends FragmentActivity{

    RecyclerView recycleView;
    LoadLayout loadLayout = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_layout);

        TextView text = new TextView(this);
        text.setText("helloworld");
        text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
        loadLayout = (LoadLayout) findViewById(R.id.load_layout);
        text.setBackgroundColor(Color.RED);
        loadLayout.setHeadView(text);

        TextView text2 = new TextView(this);
        text2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160));
        text2.setTextColor(Color.WHITE);
        text2.setText("hello2");
        text2.setBackgroundColor(Color.BLACK);
        loadLayout.setFooterView(text2);

        recycleView = loadLayout.getRecycleView();
        if(recycleView == null)
            return ;
        ArrayList<String> datas = new ArrayList<String>();

        for(int i =0 ;i <55; i++)
        {
            datas.add(String.valueOf(i));
        }

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView.setLayoutManager(layoutManager);

        recycleView.setAdapter(new MyLoadingAdapter(datas));

        loadLayout.setPrecessChangeListener(new LoadLayout.onPrecessChangeListener() {
            @Override
            public void onLoad(View footer , int process) {
                Log.d("process",String.valueOf(process));
            }

            @Override
            public void onRefresh(View header ,int process) {
                Log.d("refresh",String.valueOf(process));
            }
        });
    }


}
