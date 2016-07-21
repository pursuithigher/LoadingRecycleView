package com.helixnt.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    LoadingRecyleView loadingRecyleView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingRecyleView = (LoadingRecyleView) findViewById(R.id.loadingrecycleview);

        ArrayList<String> datas = new ArrayList<String>();

        for(int i =0 ;i <60; i++)
        {
            datas.add(String.valueOf(i));
        }




        loadingRecyleView.setRecycleAdapter(datas);


        loadingRecyleView.setOnPullListener(new LoadingRecyleView.onPullListener<String>() {

            @Override
            public String onLoad() throws Exception {
                Log.d("thread id = ",String.valueOf(Thread.currentThread().getId()));

                return "load ok";
            }

            @Override
            public void onLoadComplete(String result) {
                Log.d("thread id = ",String.valueOf(Thread.currentThread().getId()));
                Log.i("load","load callback");
            }

            @Override
            public String onRefresh() throws Exception {
                Log.d("thread id = ",String.valueOf(Thread.currentThread().getId()));

                return "refresh ok";
            }

            @Override
            public void onRefreshComplete(String result) {
                Log.d("thread id = ",String.valueOf(Thread.currentThread().getId()));
                Log.i("refresh","refresh callback");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
