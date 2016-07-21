package com.helixnt.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;

import com.helixnt.viewgroup.Utils;

/**
 * Created by QZhu on 16-7-20.
 */
public class MainActivity2 extends FragmentActivity{

    final String TAG = "activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rootview);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, Utils.getActionToString(event.getAction()));
        return super.onTouchEvent(event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispatchTouchEvent \t"+Utils.getActionToString(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }
}
