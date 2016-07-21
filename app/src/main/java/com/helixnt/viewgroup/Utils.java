package com.helixnt.viewgroup;

import android.view.MotionEvent;

/**
 * Created by QZhu on 16-7-20.
 */
public class Utils {

    public static String getActionToString(int Action){
        if(Action == MotionEvent.ACTION_DOWN)
        {
            return "DOWN";
        }
        if(Action == MotionEvent.ACTION_MOVE)
        {
            return "MOVE";
        }
        if(Action == MotionEvent.ACTION_CANCEL)
        {
            return "CANCEL";
        }
        if(Action == MotionEvent.ACTION_UP)
        {
            return "UP";
        }
        return "unknown";
    }
}
