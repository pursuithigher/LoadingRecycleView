package com.helixnt.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by QZhu on 16-7-21.
 */
public interface DecorInterf {
    boolean hasHeader();
    boolean hasFooter();
    RecyclerView.Adapter setLastItemListener(ItemGetCallback lastItemListener);

}
