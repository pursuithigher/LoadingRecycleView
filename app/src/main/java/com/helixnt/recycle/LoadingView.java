package com.helixnt.recycle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.helixnt.adapter.DecorRecycleAdapter;
import com.helixnt.adapter.ItemGetCallback;

/**
 * Created by QZhu on 16-7-22.
 */
public class LoadingView extends RecyclerView implements ItemGetCallback{

    private static final String TAG = "LoadingRecycleView";

    DecorRecycleAdapter adapter = null;

    //between 0f ~ 1f1
    private float factor = 0.5f;

    private int currentPointId = NO_POSITION;

    private int initialTouchX,initialTouchY;
    private int lastTouchX,lastTouchY;

    LayoutManager layout ;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

//        initListener();
    }

    public void setDecorAdapter(DecorRecycleAdapter adapter){
        this.setAdapter(adapter);
        this.adapter = adapter;
        this.adapter.setLastItemListener(this);

//        scrollToPosition(10);
//        smoothScrollToPosition(10);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        this.layout = layout;
    }

    private void initListener() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                boolean shouldAdjustMotion = shouldAdjustMotion();
//                Log.d("shouldAdjustMotion", String.valueOf(shouldAdjustMotion));
                int action = MotionEventCompat.getActionMasked(event);

                int newPointId = MotionEventCompat.getPointerId(event, 0);

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEventCompat.ACTION_POINTER_DOWN:
                        currentPointId = MotionEventCompat.getPointerId(event, 0);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (newPointId < 0) {
                            Log.e(TAG, "Error processing scroll; pointer index for id " +
                                    currentPointId + " not found. Did any MotionEvents get skipped?");
                            return false;
                        }
                        if (startHeadAdjust) {
                            //set up first position
                            if (initialTouchX == 0 && initialTouchY == 0) {
                                initialTouchX = (int) (event.getX());
                                initialTouchY = (int) (event.getY());
                                return false;
                            }
                            if (newPointId == currentPointId) {
                                lastTouchX = (int) (MotionEventCompat.getX(event, newPointId));
                                lastTouchY = (int) (MotionEventCompat.getY(event, newPointId));
                                int processY = getCalculatorY2(initialTouchY, lastTouchY);
                                event.setLocation(lastTouchX, processY);
                                Log.d("end = ",initialTouchY+","+processY);
                                Log.d("event",event.getX()+","+event.getY());
                            } else {
                                //only support one finger
                                return true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEventCompat.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        if (newPointId == currentPointId) {
                            soomthScrollToHide();
                            currentPointId = NO_POSITION;
                            lastTouchX = 0;
                            lastTouchY = 0;
                            initialTouchX = 0;
                            initialTouchY = 0;
                        }
                    }
                    break;
                }

                return false;
            }
        });
    }

    boolean shouldAdjustMotion(){
        int mincount = (this.adapter.hasFooter()?1:0) + (this.adapter.hasHeader()?1:0);
        if(this.adapter != null && this.adapter.getItemCount() > mincount)
        {
            if(this.layout ==null )
            {
                this.layout = getLayoutManager();
            }

            if(this.layout!=null && this.layout instanceof LinearLayoutManager)  //linear layout
            {
                LinearLayoutManager layoutTypeNew = ((LinearLayoutManager)layout);
                return layoutTypeNew.findFirstVisibleItemPosition() == (this.adapter.hasHeader()?0:-2) ;
            }

        }
        return false;
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

    private void hideHeader() {
        startHeadAdjust = false;
        if(this.layout ==null )
        {
            this.layout = getLayoutManager();
        }

        if(this.layout!=null && this.layout instanceof LinearLayoutManager)  //linear layout
        {
            LinearLayoutManager layoutTypeNew = ((LinearLayoutManager)layout);
            Log.d("complete itemHeight = ",String.valueOf(layoutTypeNew.findViewByPosition(layoutTypeNew.findFirstCompletelyVisibleItemPosition()).getHeight()));

            if(layoutTypeNew.getOrientation() == LinearLayoutManager.VERTICAL)  //vertical
            {
                int firstItem = layoutTypeNew.findFirstVisibleItemPosition();
                Log.d("firstItem position = ",String.valueOf(firstItem));
                if(firstItem == 0)
                {
                    smoothScrollToPosition(10);
//                    layoutTypeNew.smoothScrollToPosition(this,null,10);
                }
            }
        }
    }

    private void hideFooter(){
        if(this.layout ==null )
        {
            this.layout = getLayoutManager();
        }

        if(this.layout!=null && this.layout instanceof LinearLayoutManager)  //linear layout
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
        Log.d("initial = ",initialTouchY+","+lastTouchY);
        int newY2 = initialTouchY + distant;
        Log.d("distY = ",String.valueOf(distant));
        return newY2;
    }


    private boolean startHeadAdjust = false;
    @Override
    public void onGetLastItem(int position) {
        if(adapter.hasHeader() && position == 0)
        {
            Log.d("shouldAdjustMotion", String.valueOf(true));
            startHeadAdjust = true;
        }
    }

    @Override
    public void onItemClicked(int position) {
        Log.d("click","true");
        smoothScrollToPosition(1);
    }
}
