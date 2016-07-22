package com.helixnt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helixnt.myapplication.R;

import java.util.List;

/**
 * Created by QZhu on 2016/7/19 0019.
 */
public class MyLoadingAdapter extends DecorRecycleAdapter<MyLoadingAdapter.ViewHolder> {

    protected static int NONE = 0;
    protected static int LOADING = 1;

    List<?> datas = null ;

    public MyLoadingAdapter(List<?> datas){
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOADING)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_loading,parent,false);
            return new ViewHolder(view,LOADING);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item,parent,false);
            view.setOnClickListener(clickListener);

            return new ViewHolder(view,NONE);
        }
    }

    final View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(lastItemListener != null)
                lastItemListener.onItemClicked(-1);
        }
    };

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.attachedToUI(position);
    }

    @Override
    public final int getItemCount() {
        return this.datas.size()+1;
    }

    @Override
    public final int getItemViewType(int position) {
//        Log.d("getItemViewType",String.valueOf(position));
        boolean isAtEnd = position == this.datas.size();
//
        if(lastItemListener != null){
            lastItemListener.onGetLastItem(position);
        }
        return isAtEnd ? LOADING : NONE;
    }

    @Override
    public boolean hasHeader() {
        return false;
    }

    @Override
    public boolean hasFooter() {
        return false;
    }

    ItemGetCallback lastItemListener = null;

    @Override
    public MyLoadingAdapter setLastItemListener(ItemGetCallback lastItemListener) {
        this.lastItemListener = lastItemListener;
        return this ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        private int type = NONE;
        public ViewHolder(View itemView,int type) {
            super(itemView);
            this.type = type;
            if(type == NONE){
                textView = (TextView) itemView.findViewById(R.id.item);
            }
            else{
                textView = (TextView) itemView.findViewById(R.id.loading);
            }
        }
        public int getType() {
            return type;
        }

        public void attachedToUI(int positon){
//            Log.d("attachedToUI",String.valueOf(positon));
            String mtext ;
            if(positon == 0)
            {
                mtext = "it is header";
            }else {
                mtext = "type = " + String.valueOf(type) + "\t" + String.valueOf(positon);
            }
            textView.setText(mtext);

        }
    }
}
