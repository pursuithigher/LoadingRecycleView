package com.helixnt.recycle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by QZhu on 16-7-21.
 */
public class LoadingRecycleView extends RecyclerView{

    private static final String TAG = "LoadingRecycleView";

    private int headViewHeight = 100;
    private int footerViewHeight = 100;
    private int MAXMOVE_X = 500;


    DecorRecycleAdapter adapter = null;
//    LayoutManager layoutManager= null;

    //between 0f ~ 1f1
    private float factor = 0.5f;

    private int currentPointId = 0;

    private int initialTouchX,initialTouchY;
    private int lastTouchX,lastTouchY;

    LayoutManager layout ;

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
        this.adapter = adapter;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        this.layout = layout;
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
//                            currentPointId = MotionEventCompat.getPointerId(event, 0);
//                            lastTouchX = initialTouchX = (int) (event.getX()+0.5f);
//                            lastTouchY = initialTouchY = (int) (event.getY()+0.5f);
//                            break;
                        case MotionEventCompat.ACTION_POINTER_DOWN:
                            currentPointId = MotionEventCompat.getPointerId(event, 0);
                            lastTouchX = initialTouchX = (int) (event.getX()+0.5f);
                            lastTouchY = initialTouchY = (int) (event.getY()+0.5f);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int indexmove = MotionEventCompat.getPointerId(event, 0);
                            if (indexmove < 0) {
                                Log.e(TAG, "Error processing scroll; pointer index for id " +
                                        currentPointId + " not found. Did any MotionEvents get skipped?");
                                return false;
                            }
                            if(indexmove == currentPointId)
                            {
                                lastTouchX = (int) (MotionEventCompat.getX(event, indexmove) + 0.5f);
                                lastTouchY = (int) (MotionEventCompat.getY(event, indexmove) + 0.5f);
                                Log.d("move raw Y=",String.valueOf(lastTouchY));
                                event.setLocation(event.getX(),getCalculatorY2(initialTouchY,lastTouchY));
                                Log.d("move process Y=",String.valueOf(lastTouchY));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEventCompat.ACTION_POINTER_UP:
                            int indexup = MotionEventCompat.getPointerId(event, 0);
                            {
                                if(indexup == currentPointId)
                                {
                                    soomthScrollToHide();
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            int indexcancel = MotionEventCompat.getPointerId(event, 0);
                            {
                                if(indexcancel == currentPointId)
                                {
                                    soomthScrollToHide();
                                }
                            }
                            break;
                    }

                }
                return false;
            }
        });
    }

    /**
     * smooth hide header or footer
     */
    private void soomthScrollToHide(){
        if(this.adapter.hasHeader())    //has header
        {
            hideHeader();
        }
        if(this.adapter.hasFooter())
        {
            hideFooter();
        }
    }

    private void hideFooter() {
        if(this.layout instanceof LinearLayoutManager)  //linear layout
        {
            LinearLayoutManager layoutTypeNew = ((LinearLayoutManager)layout);
            if(layoutTypeNew.getOrientation() == LinearLayoutManager.VERTICAL)  //vertical
            {
                int firstItem = layoutTypeNew.findFirstVisibleItemPosition();
                Log.d("firstItem position = ",String.valueOf(firstItem));
                if(firstItem == 0)
                {
                    this.smoothScrollToPosition(1);
                }
            }
        }
    }

    private void hideHeader(){
        if(this.layout instanceof LinearLayoutManager)  //linear layout
        {
            LinearLayoutManager layoutTypeNew = ((LinearLayoutManager)layout);
            if(layoutTypeNew.getOrientation() == LinearLayoutManager.VERTICAL)  //vertical
            {
                int lastItem = layoutTypeNew.findLastVisibleItemPosition();
                Log.d("lastitem position = ",String.valueOf(lastItem));
                if(adapter!= null && lastItem == adapter.getItemCount())
                {
                    //should load ?
                }
            }
        }
    }

    /**
     * based on MoveY = 500 DisY = 100;
     * @param initialTouchY
     * @param lastTouchY
     * @return
     */
    private int getCalculatorY2(int initialTouchY,int lastTouchY){
        int distant = (int) Math.sqrt(20*(lastTouchY - initialTouchY));
        Log.d("movedistant = ",String.valueOf(distant));
        int newY2 = initialTouchY + distant;
        return newY2;
    }

}
