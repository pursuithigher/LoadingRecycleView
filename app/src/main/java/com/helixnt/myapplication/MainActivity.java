package com.helixnt.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.helixnt.adapter.MyLoadingAdapter;
import com.helixnt.nestscroll.LoadLayout;

import java.util.ArrayList;

/**
 * Created by QZhu on 16-7-22.
 */
public class MainActivity extends FragmentActivity{

//
    RecyclerView recycleView;
    LoadLayout loadLayout = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_layout);

        loadLayout = (LoadLayout) findViewById(R.id.load_layout);
        recycleView = loadLayout.getRecycleView();
        ArrayList<String> datas = new ArrayList<String>();

        for(int i =0 ;i <30; i++)
        {
            datas.add(String.valueOf(i));
        }

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView.setLayoutManager(layoutManager);

        recycleView.setAdapter(new MyLoadingAdapter(datas));

        loadLayout.setPrecessChangeListener(new LoadLayout.onPrecessChangeListener() {
            @Override
            public void onLoad(int process) {
                Log.d("process",String.valueOf(process));
            }

            @Override
            public void onRefresh(int process) {
                Log.d("refresh",String.valueOf(process));
            }
        });
    }


}
