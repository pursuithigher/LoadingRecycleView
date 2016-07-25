package com.helixnt.nestscroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by QZhu on 16-7-22.
 */
public class LoadLayout extends RelativeLayout implements NestedScrollingParent{//NestedScrollingChild

    public final static String HEADERALIAS = "load_header";
    public final static String FOOTERALIAS = "load_footer";

    private RecyclerView mTarget = null; // the target of the gesture

    private int offSet_Y = 0;
    private int loading_Y = 0;

    private int RAWX = -1;
    private int RAWY = -1;

    private boolean refreshEnabled = true;
    private boolean loadEnabled = true;

    private View header = null;
    private View footer = null;

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

    public LoadLayout(Context context) {
        this(context,null);
    }

    public LoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
                if(child.getContentDescription().equals(HEADERALIAS)) {
                    header = child;
                }
                if(child.getContentDescription().equals(FOOTERALIAS)){
                    footer = child;
                }
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
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
                mTarget.setY(RAWY + changeOffsetY(offSet_Y));//getCalculatorY(offSet_Y));
                nodifyRefreshProcess(offSet_Y);
                return ;
            }
        }
        if(loadEnabled)
        {
            if(dyUnconsumed > 0)
            {
                offSet_Y -= dyUnconsumed;
                mTarget.setY(RAWY + changeOffsetY(offSet_Y));
                nodifyLoadProcess(offSet_Y);
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
                    mTarget.setY(RAWY + changeOffsetY(offSet_Y));//getCalculatorY(offSet_Y));
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
                        mTarget.setY(RAWY + changeOffsetY(offSet_Y));
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
        setHeaderHeight(header.getHeight());
        setFooterHeight(footer.getHeight());
        setListEnabled(true);
    }


    private int headerHeight = 100;
    private int footerHeight = 100;

    //private final int FACTX = 600;//max distance that move to height when complete visible
//    private int getCalculatorY(int disy){
//        Log.d("distant disy = ",String.valueOf(disy));
//        int distant = (int) Math.sqrt(headerHeight*headerHeight*disy/FACTX);
//        if(distant >= headerHeight)
//            distant = headerHeight;
//        Log.d("distant height = ",String.valueOf(distant));
//        return distant;
//    }

    private void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        Log.d("header height = ",String.valueOf(headerHeight));
    }

    private void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
        Log.d("footer height = ",String.valueOf(footerHeight));
    }

    private int changeOffsetY(int disy){
        if(disy > headerHeight)
        {
            return headerHeight;
        }else if(-disy > footerHeight){
            return -footerHeight;
        }
        return disy;
    }

    public interface onPrecessChangeListener{
        void onLoad(int process);
        void onRefresh(int process);
    }

    onPrecessChangeListener precessChangeListener;

    public void setPrecessChangeListener(onPrecessChangeListener precessChangeListener) {
        this.precessChangeListener = precessChangeListener;
    }

    private void nodifyRefreshProcess(int offsety){
        if(precessChangeListener != null)
        {
            int percent = (int) ((offsety*1.0f/headerHeight)*100);
            precessChangeListener.onRefresh(percent > 100 ? 100:percent);
        }
    }

    private void nodifyLoadProcess(int offsety){
        if(precessChangeListener != null)
        {
            int percent = (int) ((offsety*1.0f/headerHeight)*100);
            precessChangeListener.onLoad(percent > 100 ? 100:percent);
        }

    }

    public RecyclerView getRecycleView(){
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {
                    return (RecyclerView) child;
                }
            }
        }
        return mTarget;
    }

}
