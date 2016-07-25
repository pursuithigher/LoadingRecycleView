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
public class MyLoadingAdapter extends RecyclerView.Adapter<MyLoadingAdapter.ViewHolder> {

    List<?> datas = null ;

    public MyLoadingAdapter(List<?> datas){
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item,parent,false);

            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.attachedToUI(position);
    }

    @Override
    public final int getItemCount() {
        return this.datas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
        }
        public void attachedToUI(int positon){
            textView.setText(String.valueOf(positon));
        }
    }
}
