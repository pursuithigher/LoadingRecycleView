package com.helixnt.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by QZhu on 16-7-20.
 */
public class MFrame extends FrameLayout{
    final String TAG = "parentView2";

    public MFrame(Context context) {
        super(context);
    }

    public MFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispatchTouchEvent \t"+Utils.getActionToString(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent \t"+Utils.getActionToString(ev.getAction()));
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.w(TAG,"onTouchEvent \t"+Utils.getActionToString(ev.getAction()));
        return super.onTouchEvent(ev);
    }
}