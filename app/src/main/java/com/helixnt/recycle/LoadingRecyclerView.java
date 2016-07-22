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

import com.helixnt.adapter.DecorRecycleAdapter;

/**
 * Created by QZhu on 16-7-21.
 */
public class LoadingRecyclerView extends RecyclerView{

//    private int headViewHeight = 100;
//    private int footerViewHeight = 100;
//    private int MAXMOVE_X = 500;

    private static final String TAG = "LoadingRecycleView";

    DecorRecycleAdapter adapter = null;

    //between 0f ~ 1f1
    private float factor = 0.5f;

    private int currentPointId = 0;

    private int initialTouchX,initialTouchY;
    private int lastTouchX,lastTouchY;

    LayoutManager layout ;

    public LoadingRecyclerView(Context context) {
        this(context,null);
    }

    public LoadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public LoadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        if(isInEditMode())
//        {
//            return ;
//        }
//        initListener();
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

    private void initListener() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(shouldAdjustMotion())
                {

//                    if(adapter.hasFooter()){
//
//                    }

                    int action = MotionEventCompat.getActionMasked(event);

                    int newPointId = MotionEventCompat.getPointerId(event, 0);

                    switch(action) {
                        case MotionEvent.ACTION_DOWN:
//                            currentPointId = MotionEventCompat.getPointerId(event, 0);
//                            lastTouchX = initialTouchX = (int) (event.getX()+0.5f);
//                            lastTouchY = initialTouchY = (int) (event.getY()+0.5f);
//                            break;
                        case MotionEventCompat.ACTION_POINTER_DOWN:
                            if(currentPointId == NO_POSITION) {
                                currentPointId = newPointId;
                                lastTouchX = initialTouchX = (int) (event.getX());
                                lastTouchY = initialTouchY = (int) (event.getY());
                            }
                            else if(currentPointId != newPointId)
                            {
                                    return true;
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (newPointId < 0) {
                                Log.e(TAG, "Error processing scroll; pointer index for id " +
                                        currentPointId + " not found. Did any MotionEvents get skipped?");
                                return false;
                            }
                            if(newPointId == currentPointId)
                            {
                                lastTouchX = (int) (MotionEventCompat.getX(event, newPointId));
                                lastTouchY = (int) (MotionEventCompat.getY(event, newPointId));
                                Log.d("move raw Y=",String.valueOf(lastTouchY));
                                event.setLocation(event.getX(),getCalculatorY2(initialTouchY,lastTouchY));
                                Log.d("move process Y=",String.valueOf(lastTouchY));
                            }else{
                                //only support one finger
                                return true;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEventCompat.ACTION_POINTER_UP:
                        case MotionEvent.ACTION_CANCEL:
                            {
                                if(newPointId == currentPointId)
                                {
                                    soomthScrollToHide();
                                    currentPointId = NO_POSITION;
                                }
                            }
                            break;
                    }

                }
                return false;
            }
        });
    }

    boolean shouldAdjustMotion(){
        int mincount = (this.adapter.hasFooter()?1:0) + (this.adapter.hasHeader()?1:0);
        if(this.adapter != null && this.adapter.getItemCount() > mincount)
        {
            Log.d("shouldAdjustMotion == ","true");
            if(this.layout ==null )
            {
                this.layout = getLayoutManager();
            }

            if(this.layout!=null && this.layout instanceof LinearLayoutManager)  //linear layout
            {
                LinearLayoutManager layoutTypeNew = ((LinearLayoutManager)layout);
                return layoutTypeNew.findFirstVisibleItemPosition() <= (this.adapter.hasHeader()?1:0) ;
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
        if(this.layout ==null )
        {
            this.layout = getLayoutManager();
        }

        if(this.layout!=null && this.layout instanceof LinearLayoutManager)  //linear layout
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
        Log.d("movedistant = ",String.valueOf(distant));
        int newY2 = initialTouchY + distant;
        return newY2;
    }

}
