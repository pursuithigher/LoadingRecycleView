package com.helixnt.nestscroll;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * Created by QZhu on 16-7-22.
 */
public class LoadLayout extends FrameLayout implements NestedScrollingParent{//NestedScrollingChild
    //
    public final static int BounceTime = 1000;

    private RecyclerView mTarget = null; // the target of the gesture

    private int offSet_Y = 0;
    private int RAWY = -1;  //offSet_Y -> RAWY add a animator

    private int RAWX = -1;

    private boolean refreshEnabled = false;
    private boolean loadEnabled = false;

    /**
     * 是否位于Recyler上方作为头部
     * 否则被Recyler覆盖
     */
    private static boolean LoadUnderFrame = false;
    /**
     * 是否位于Recyler下方作为底部
     * 否则被Recyler覆盖
     */
    private static boolean RefreshUnderFrame = false;

    public final static String LEFT = "left";
    public final static String RIGHT = "right";
    public final static String TOP = "header";
    public final static String BOTTOM = "footer";
    public final static String CENTER = "center";

    public enum POSITION{
        LEFT,RIGHT,TOP,BOTTOM
    }

    private int width = 0;
    private int height = 0;

//    private LinearLayout headGroup = null;
//    private LinearLayout footerGroup = null;
    private View sleft,sright,sheader,sfooter,scenter;
    private Point pleft,pright,pheader,pfooter;

    public boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    public void setRefreshEnabled(boolean refreshEnabled) {
        if(sheader != null)
            this.refreshEnabled = refreshEnabled;
    }

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public void setLoadEnabled(boolean loadEnabled) {
        if(sfooter != null)
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
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;

        addChild();
    }

    private void addChild(){
        removeAllViews();

        enableRecycleView();

        enableLoader();
    }

    private void enableLoader(){
//        headGroup = new LinearLayout(getContext());
//        headGroup.setOrientation(VERTICAL);
//        footerGroup = new LinearLayout(getContext());
//        footerGroup.setOrientation(VERTICAL);
//
//        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.TOP;
//        addView(this.headGroup, layoutParams);
//
//        FrameLayout.LayoutParams layoutParamsBottom = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParamsBottom.gravity = Gravity.BOTTOM;
//        addView(this.footerGroup, layoutParamsBottom);
    }

    private void enableRecycleView(){
        this.mTarget = new RecyclerView(getContext());
        this.mTarget.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        addView(this.mTarget, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if(objectAnimator!=null && objectAnimator.isRunning())
        {
            objectAnimator.cancel();
            objectAnimator.removeAllListeners();
            objectAnimator = null;
        }

//        if recycleView item not fill the screen we will setFooter disabled
//        if(judgeFooterEnabled()){
//            setLoadEnabled(false);
//        }
        return true;
    }

//    private boolean judgeFooterEnabled(){
//        RecyclerView.LayoutManager layoutManager = mTarget.getLayoutManager();
//        if(layoutManager instanceof LinearLayoutManager)
//        {
//            return (((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition() == (layoutManager.getItemCount()-1))
//                    && (((LinearLayoutManager)layoutManager).findFirstCompletelyVisibleItemPosition() == 0);
//        }else{
//            return true;
//        }
//    }


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
                offSet_Y = changeOffsetY(offSet_Y);
                mTarget.setY(RAWY + offSet_Y);//getCalculatorY(offSet_Y));
                if(!RefreshUnderFrame)
                    sheader.setY(pheader.y + offSet_Y);
                notifyRefreshProcess(offSet_Y);
                return ;
            }
        }
        if(loadEnabled)
        {
            if(dyUnconsumed > 0)
            {
                offSet_Y -= dyUnconsumed;
                offSet_Y = changeOffsetY(offSet_Y);
                mTarget.setY(RAWY + offSet_Y);
                if(!LoadUnderFrame)
                    sfooter.setY(pfooter.y + offSet_Y);
                notifyLoadProcess(offSet_Y);
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
                    if(!RefreshUnderFrame)
                        sheader.setY(pheader.y + offSet_Y);
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
                        if(!LoadUnderFrame)
                            sfooter.setY(pfooter.y + offSet_Y);
                        return;
                    }
                }
            }
        }
    }

    /**
     * this is for loading more only valid when recyclerView use LinearLayoutManager
     * @return
     */
    private boolean isLastItemVisible(){
        RecyclerView.LayoutManager layoutManager = mTarget.getLayoutManager();
//        if(layoutManager instanceof LinearLayoutManager)
//        {
//            return ((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition() >= layoutManager.getItemCount()-1;
//        }
//        else{
//            return false;
//        }
        return true;
    }

    ObjectAnimator objectAnimator = null;

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        if(offSet_Y != 0)
        {
            objectAnimator = ObjectAnimator.ofFloat(mTarget,View.Y,offSet_Y,0).setDuration(BounceTime);
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    offSet_Y = (int) value;
                    mTarget.setY(RAWY + offSet_Y);
                    if(offSet_Y > 0)
                    {
                        if(refreshEnabled)
                        {
                            if(!RefreshUnderFrame)
                                sheader.setY(pheader.y + offSet_Y);
                        }
                    }else {
                        if(loadEnabled) {
                            if(!LoadUnderFrame)
                                sfooter.setY(pfooter.y + offSet_Y);
                        }
                    }
                }
            });
            objectAnimator.start();
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

        //if recycler == null then load from child
        //else it will add automatic
        if (mTarget == null) {
            ensureTarget();
            initialChild();
        }
        if (mTarget == null) {
            return;
        }
        alignChildPosition();
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    }

    /**
     * query the recycleView
     */
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

    /**
     * achieve child footer/header
     */
    private void initialChild(){
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(child.getContentDescription() == null)
            {
                continue;
            }
            String contentDes = child.getContentDescription().toString();

            if(contentDes.equals(LEFT)) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.START;
                child.setLayoutParams(lp);
                sleft = child;
            }
            if(contentDes.equals(RIGHT)){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.END;
                child.setLayoutParams(lp);
                sright = child;
            }
            if(contentDes.equals(TOP)){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.TOP;
                child.setLayoutParams(lp);
                sheader = child;
            }
            if(contentDes.equals(BOTTOM)){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.BOTTOM;
                child.setLayoutParams(lp);
                sfooter = child;
            }
            if(contentDes.equals(CENTER)){
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;
                child.setLayoutParams(lp);
                scenter = child;
            }
        }
    }


    /**
     * set child at top/bottom position out the window
     */
    private void alignChildPosition(){
        if(sleft != null)
        {
            sleft.setX(0 - sleft.getMeasuredWidth());
        }
        if(sright != null)
        {
            sright.setX(width + sright.getMeasuredWidth());
        }
        if(sheader != null)
        {
            if(!RefreshUnderFrame)
                sheader.setY(0 - sheader.getMeasuredHeight());
        }
        if(sfooter != null && mTarget != null)
        {
            if(!LoadUnderFrame)
                sfooter.setY(mTarget.getY() + mTarget.getMeasuredHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        RAWX = (int) mTarget.getX();
        RAWY = (int) mTarget.getY();
        initialOuterPoint();
        if(sheader != null)
        {
            setHeaderHeight(sheader.getHeight());
            setRefreshEnabled(true);
        }
        if(sfooter != null)
        {
            setFooterHeight(sfooter.getHeight());
            setLoadEnabled(true);
        }
    }

    /**
     * initial child point
     */
    private void initialOuterPoint(){
        if(sleft != null)
        {
            pleft = new Point((int)sleft.getX(), (int)sleft.getY());
        }
        if(sright != null)
        {
            pright = new Point((int)sright.getX(),(int)sright.getY());
        }
        if(sheader != null)
        {
            pheader = new Point((int)sheader.getX(),(int)sheader.getY());
        }
        if(sfooter != null)
        {
            pfooter = new Point((int)sfooter.getX(),(int)sfooter.getY());
        }
    }


    private int headerHeight = 100;
    private int footerHeight = 100;

    private void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
//        Log.d("header height = ",String.valueOf(headerHeight));
    }

    private void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
//        Log.d("footer height = ",String.valueOf(footerHeight));
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

    /**
     * this interface is to notify
     */
    public interface onPrecessChangeListener{
        /**
         * load status
         * @param footer footer view
         * @param process value -100~0
         */
        void onLoad(View footer ,int process);

        /**
         * refresh status
         * @param header header view
         * @param process value 0~100
         */
        void onRefresh(View header ,int process);
    }

    onPrecessChangeListener precessChangeListener;

    public void setPrecessChangeListener(onPrecessChangeListener precessChangeListener) {
        this.precessChangeListener = precessChangeListener;
    }

    private void notifyRefreshProcess(int offsety){
        if(precessChangeListener != null)
        {
            int percent = (int) ((offsety*1.0f/headerHeight)*100);
            precessChangeListener.onRefresh(sheader,percent > 100 ? 100:percent);
        }
    }

    private void notifyLoadProcess(int offsety){
        if(precessChangeListener != null)
        {
            int percent = (int) ((offsety*1.0f/footerHeight)*100);
            precessChangeListener.onLoad(sfooter,percent > 100 ? 100:percent);
        }

    }

    /**
     * get the recycleView in this view group
     * @return
     */
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

    /**
     * get header/footer view at right position
     * @param position const value
     * @return
     */
    public View getLoatedView(POSITION position){
        if(position == POSITION.BOTTOM)
        {
            return sfooter;
        }
        if(position == POSITION.LEFT)
        {
            return sleft;
        }
        if(position == POSITION.TOP)
        {
            return sheader;
        }
        if(position == POSITION.RIGHT)
        {
            return sright;
        }
        return null;
    }


    public boolean setHeadView(View view,LoadLayout.LayoutParams layoutParams){
        if(view!= null && view.getParent() != null)
        {
            return false;
        }else{
            if(layoutParams == null)
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.TOP;

            sheader = view;
            addView(this.sheader, layoutParams);

            refreshEnabled = true;
        }
        return true;
    }

    public boolean setFooterView(View view,LoadLayout.LayoutParams layoutParams){
        if(view!=null && view.getParent() != null)
        {
            return false;
        }else{
            if(layoutParams == null)
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;

            sfooter = view;
            addView(sfooter, layoutParams);
            loadEnabled = true;
        }
        return true;
    }
}
