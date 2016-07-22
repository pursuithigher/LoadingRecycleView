package com.helixnt.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.helixnt.recycle.LoadingView;
import com.helixnt.adapter.MyLoadingAdapter;

import java.util.ArrayList;

/**
 * Created by QZhu on 16-7-22.
 */
public class MainActivity3 extends FragmentActivity{

//
LoadingView recycleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = (LoadingView) findViewById(R.id.loading_recycleview);

        ArrayList<String> datas = new ArrayList<String>();

        for(int i =0 ;i <60; i++)
        {
            datas.add(String.valueOf(i));
        }

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView.setLayoutManager(layoutManager);

        recycleView.setDecorAdapter(new MyLoadingAdapter(datas));
//        recycleView.setAdapter(new MyLoadingAdapter(datas));
    }


}
