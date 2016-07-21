package com.helixnt.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.helixnt.viewgroup.Utils;

/**
 * Created by QZhu on 16-7-21.
 */
public class MyRecycleView extends RecyclerView{
    final String TAG = "MyRecycleView";

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyRecycleView(Context context) {
        super(context);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispatchTouchEvent \t"+Utils.getActionToString(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        Log.d(TAG,"onInterceptTouchEvent \t"+String.valueOf(isIntercept)+"\t"+Utils.getActionToString(ev.getAction()));
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isTouchEvent = super.onTouchEvent(ev);
        Log.d(TAG,"onTouchEvent \t"+ String.valueOf(isTouchEvent)+"\t"+Utils.getActionToString(ev.getAction()));
        return isTouchEvent;
    }
}
