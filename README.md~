# LoadingRecycleView
	offer pull to refresh and push to load
## Principle
	使用NestedScrolling*嵌套滚动原理，再下也是初学者,请自己研究
## Commom
	see MainActivity.java

## Useage
1) 添加com.helixnt.nestscroll.LoadLayout到你的moudle下面<br/>
2) 编写你的xml文件：<br/>
	<com.helixnt.nestscroll.LoadLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_height="match_parent"
	    android:layout_width="match_parent"
	    android:id="@+id/load_layout"
	    >
	    <View
		....
		android:contentDescription="header"  *****这个代表这头视图
		/>
	    <View
		....
		android:contentDescription="footer" *****这个代表这尾视图
		/>
	    <android.support.v7.widget.RecyclerView
		....
		/>
	</com.helixnt.nestscroll.LoadLayout><br/>

	注意:<br/>
	头部视图可以是View,或者Viewgroup但是要有android:contentDescription="header"属性，方便LoadLayout寻找<br/>
	尾部视图可以是View,或者Viewgroup但是要有android:contentDescription="footer"属性，方便LoadLayout寻找<br/>

## 使用方法
	得到LoadLayout：<br/>
	loadLayout = (LoadLayout) findViewById(R.id.load_layout);//该id可修改为自定义id，见load_layout.xml<br/>
	得到recycleView：<br/>
	RecyclerView recycleView = loadLayout.getRecycleView();//adapter请自定义
	
	**设置监听事件：<br/>
	loadLayout.setPrecessChangeListener(new LoadLayout.onPrecessChangeListener() {
            @Override
            public void onLoad(View footer , int process) {
                Log.d("process",String.valueOf(process));
            }

            @Override
            public void onRefresh(View header ,int process) {
                Log.d("refresh",String.valueOf(process));
            }
        });
	<br/>
	onload:下拉头部距离百分比：-100~0[int]<br/>
	onRefresh:上滑头部距离百分比:0~100[int]<br/>
	请在回调方法中调整您的头部尾部视图，比如根据百分比视图变化的，目前比较多的动态UI

## 希望大家加入，协同修改


