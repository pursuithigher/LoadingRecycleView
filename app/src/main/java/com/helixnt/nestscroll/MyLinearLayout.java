package com.helixnt.nestscroll;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.helixnt.recycle.LoadingView;

/**
 * Created by QZhu on 16-7-22.
 */
public class MyLinearLayout extends LinearLayout implements NestedScrollingParent{//NestedScrollingChild

    private View mTarget = null; // the target of the gesture

    private int refresh_Y = 0;
    private int loading_Y = 0;

    private int RAWX = -1;
    private int RAWY = -1;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
//    private final NestedScrollingChildHelper mNestedScrollingChildHelper;

    public MyLinearLayout(Context context) {
        this(context,null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

//        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof LoadingView) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d("onStartNestedScroll",getNestString(nestedScrollAxes));
        return true;
    }

    private String getNestString(int type){
        switch(type)
        {
            case ViewCompat.SCROLL_AXIS_VERTICAL:
                return "vertical";
            case ViewCompat.SCROLL_AXIS_HORIZONTAL:
                return "horizontal";
            default:
                return "null";
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.d("onNestedScrollAccepted",getNestString(nestedScrollAxes));
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        // Dispatch up to the nested parent
        //startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    //dyUnconsumed < 0 means finger move to down else up
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d("onNestedScroll","dxConsumed = "+dxConsumed+"\t"+"dyConsumed = "+dyConsumed+"\t"+"dxUnconsumed = "+dxUnconsumed+"\t"+"dyUnconsumed = "+dyUnconsumed);
        if(dyUnconsumed == 0)
        {
            refresh_Y = 0;
        }else{
            refresh_Y -= dyUnconsumed;
            mTarget.setY(RAWX + refresh_Y);
            Log.d("dyUnconsumed = ","refreshY = "+refresh_Y);
        }

    }

    //dy < 0 means finger move to down else up
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        if(refresh_Y != 0)
//        {
//            mTarget.offsetTopAndBottom(-dy);
//            refresh_Y -= dy;
//            consumed[1] = -dy;
//        }

        //this case Y must be consumed
        if(refresh_Y>0 && dy <0 )
        {
            refresh_Y -= dy;
            mTarget.setY(RAWY + refresh_Y);
            consumed[1] = dy;
        }
        else if(refresh_Y<0 && dy > 0){
            refresh_Y -= dy;
            mTarget.setY(RAWY + refresh_Y);
            consumed[1] = dy;
        }
        Log.d("onNestedPreScroll","dx = "+dx+"\t"+"dy = "+dy+"\t"+"consumed x= "+consumed[0]+"\t"+"consumed y= "+consumed[1]);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d("onStopNestedScroll","true");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        if(refresh_Y > 0)
        {
            mTarget.setY(RAWY);
            refresh_Y = 0;
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
        RAWX = (int) mTarget.getY();
        Log.d("point = ",RAWX+","+RAWY);
    }

    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        ensureTarget();
//        if(canChildScrollUp())
//        {
//            return false;
//        }
//        return false;
//    }

    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }
}
