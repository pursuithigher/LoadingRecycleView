package com.helixnt.myapplication;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.helixnt.custom.MyRecycleView;
import com.helixnt.viewgroup.Utils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class LoadingRecyleView extends FrameLayout{

    public interface onPullListener<R>{
        /**
         * this is run on other thread
         * @return
         * @throws Exception
         */
        R onLoad()throws Exception;

        /**
         * this is run on main thread and with return arguments
         * @param result
         */
        void onLoadComplete(R result);

        /**
         * this is run on other thread
         * @return
         * @throws Exception
         */
        R onRefresh()throws Exception;

        /**
         * this is run on main thread and with return arguments
         * @param result
         */
        void onRefreshComplete(R result);
    }

    SwipeRefreshLayout mswipe_refresh;
    RecyclerView mrecycleView;

    private boolean isRefresh = false;
    private boolean isLoading = false;

    public LoadingRecyleView(Context context) {
        this(context,null);
    }

    public LoadingRecyleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingRecyleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial(context);
    }

    private void initial(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View group = inflater.inflate(R.layout.recycle_group, this, true);
        initialView(group);
    }

    private void initialView(View group) {
        mswipe_refresh = (SwipeRefreshLayout) group.findViewById(R.id.swipe_refresh);
        mswipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(ionPullListener != null)
                {
                    runRefreshing();
                }
            }
        });
        mrecycleView = (MyRecycleView) group.findViewById(R.id.recycleView);

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mrecycleView.setLayoutManager(layoutManager);
        //setLayoutParam();
        setRecycleAdapter(new ArrayList<String>());

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

    }

//    private void setLayoutParam(){
//        mrecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
//    }

    onPullListener ionPullListener;

    public void setOnPullListener(onPullListener ionPullListener) {
        this.ionPullListener = ionPullListener;
    }

    private void onRefreshComplete(){
        mswipe_refresh.setRefreshing(false);
    }

    private void onLoadComplete(){
        isLoading = false;
    }

    public SwipeRefreshLayout getRefreshLsyout(){
        return this.mswipe_refresh;
    }

    public void setRecycleAdapter(final List<?> datas){
        mrecycleView.setAdapter(new LoadingRecycleAdapter(datas).setLastItemListener(new LoadingRecycleAdapter.LastItemListener() {
            @Override
            public void onGetLastItem(int position) {
                if(position == datas.size())
                {
                    if(! isLoading) {
                        if(ionPullListener != null)
                        {
                            runLoading();
                            isLoading = true;
                        }

                    }
                }
            }
        }));
    }

    private void runLoading(){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    subscriber.onNext(ionPullListener.onLoad());
                } catch (Exception e) {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object result) {
                        ionPullListener.onLoadComplete(result);
                        onLoadComplete();
                    }
                });
    }

    private void runRefreshing(){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    subscriber.onNext(ionPullListener.onRefresh());
                } catch (Exception e) {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object result) {
                        ionPullListener.onRefreshComplete(result);
                        onRefreshComplete();
                    }
                });
    }


    final String TAG = "contentView";

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        Log.d(TAG,"onInterceptTouchEvent \t"+String.valueOf(isIntercept)+"\t"+Utils.getActionToString(ev.getAction()));
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isTouchEvent = super.onTouchEvent(ev);
        Log.d(TAG,"onTouchEvent \t"+ String.valueOf(isTouchEvent)+"\t"+Utils.getActionToString(ev.getAction()));
        return isTouchEvent;
    }

}
