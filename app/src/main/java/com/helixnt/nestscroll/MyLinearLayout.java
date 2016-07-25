package com.helixnt.nestscroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.helixnt.recycle.LoadingView;

/**
 * Created by QZhu on 16-7-22.
 */
public class MyLinearLayout extends LinearLayout implements NestedScrollingParent{//NestedScrollingChild

    private RecyclerView mTarget = null; // the target of the gesture

    private int offSet_Y = 0;
    private int loading_Y = 0;

    private int RAWX = -1;
    private int RAWY = -1;

    private boolean refreshEnabled = true;
    private boolean loadEnabled = true;

    public boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    public void setRefreshEnabled(boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
    }

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public void setLoadEnabled(boolean loadEnabled) {
        this.loadEnabled = loadEnabled;
    }

    public void setListEnabled(boolean listEnabled){
        setRefreshEnabled(listEnabled);
        setLoadEnabled(listEnabled);
    }


    private final NestedScrollingParentHelper mNestedScrollingParentHelper;

    public MyLinearLayout(Context context) {
        this(context,null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {
                    mTarget = (RecyclerView) child;
                    break;
                }
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        Log.d("onStartNestedScroll",getNestString(nestedScrollAxes));
        return true;
    }

    private void setNestString(int type){
        switch(type)
        {
            case ViewCompat.SCROLL_AXIS_VERTICAL:
                setListEnabled(true);
//            case ViewCompat.SCROLL_AXIS_HORIZONTAL:
//                return "horizontal";
            default:
                setListEnabled(false);
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        setNestString(nestedScrollAxes);
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        // Dispatch up to the nested parent
        //startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    //dyUnconsumed < 0 means finger move to down else up
    //dyUnconsumed < 0
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if(refreshEnabled)
        {
            if(dyUnconsumed < 0)
            {
                offSet_Y -= dyUnconsumed;
                mTarget.setY(RAWY + offSet_Y);
                return ;
            }
        }
        if(loadEnabled)
        {
            if(dyUnconsumed > 0)
            {
                offSet_Y -= dyUnconsumed;
                mTarget.setY(RAWY + offSet_Y);
                return ;
            }
        }


    }

    //dy < 0 means finger move to down else up
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (refreshEnabled) {
            //up movement
            if (dy > 0) {
                if (offSet_Y > 0) {
                    if (offSet_Y > dy) {
                        offSet_Y -= dy;
                        consumed[1] = dy;
                    } else {
                        consumed[1] = offSet_Y;
                        offSet_Y = 0;
                    }
                    mTarget.setY(RAWY + offSet_Y);
                    return;
                }
            }
        }

        if (loadEnabled) {
            if (dy < 0) {
                if (isLastItemVisible()) {
                    if (offSet_Y < 0) {
                        if (offSet_Y < dy) {
                            offSet_Y -= dy;
                            consumed[1] = dy;
                        } else {
                            consumed[1] = -offSet_Y;
                            offSet_Y = 0;
                        }
                        mTarget.setY(RAWY + offSet_Y);
                        return;
                    }
                }
            }
        }
    }

    private boolean isLastItemVisible(){
        RecyclerView.LayoutManager layoutManager = mTarget.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager)
        {
            return ((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition() >= layoutManager.getItemCount()-1;
        }
        else{
            return false;
        }
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d("onStopNestedScroll","true");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        if(offSet_Y != 0)
        {
            mTarget.setY(RAWY);
            offSet_Y = 0;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.d("onNestedFling", String.valueOf(consumed));
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        RAWX = (int) mTarget.getX();
        RAWY = (int) mTarget.getY();
        Log.d("point = ",RAWX+","+RAWY);
    }

}
