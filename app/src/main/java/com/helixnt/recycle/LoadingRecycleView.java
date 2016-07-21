package com.helixnt.recycle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by QZhu on 16-7-21.
 */
public class LoadingRecycleView extends RecyclerView{

    DecorRecycleAdapter adapter = null;
//    LayoutManager layoutManager= null;

    private int currentPointId = 0;

    public LoadingRecycleView(Context context) {
        this(context,null);
    }

    public LoadingRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public LoadingRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initListener();
    }

    @Override
    public final void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    public void setDecorAdapter(DecorRecycleAdapter adapter){
        this.setAdapter(adapter);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return super.getLayoutManager();
    }

    private void initListener() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(adapter != null)
                {

//                    if(adapter.hasFooter()){
//
//                    }

                    int action = MotionEventCompat.getActionMasked(event);
                    switch(action)
                    {
                        case MotionEvent.ACTION_DOWN:
                            currentPointId = MotionEventCompat.getPointerId(event, 0);
                            break;
                        case MotionEventCompat.ACTION_POINTER_DOWN:
                            currentPointId = MotionEventCompat.getPointerId(event, 0);
                            break;
                        case MotionEvent.ACTION_MOVE:

                            break;
                        case MotionEvent.ACTION_UP:

                            break;
                        case MotionEventCompat.ACTION_POINTER_UP:

                            break;
                        case MotionEvent.ACTION_CANCEL:

                            break;
                    }

                }
                return false;
            }
        });
    }


}
