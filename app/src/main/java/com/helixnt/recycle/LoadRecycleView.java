package com.helixnt.recycle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by QZhu on 16-7-25.
 */
public class LoadRecycleView extends RecyclerView{
    public LoadRecycleView(Context context) {
        super(context);
    }

    public LoadRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
