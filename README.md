# LoadingRecycleView
	offer pull to refresh and push to load
# Principle
	使用NestedScrolling*嵌套滚动原理，再下也是初学者,请自己研究
# Commom
	see MainActivity.java

#修改视图
	recycle_item.xml ：测试使用的recyclerView的item文件，可以全部修改
	load_layout.xml：LoadLayout使用的XML文件(可以部分修改，如下：)
	
##修改load_layout.xml
	load_layout.xml中的android:contentDescription="load_header"与android:contentDescription="load_footer"
	分别对应头视图与尾部视图，这个属性必须，可以没有id，如果想修改头部尾部视图，务必加入这2个熟悉，值在LoadLayout.HEADERALIAS与
	LoadLayout.FOOTERALIAS，如果想修改，请务必保持一致

#使用方法
	得到LoadLayout：
	loadLayout = (LoadLayout) findViewById(R.id.load_layout);//该id可修改为自定义id，见load_layout.xml
	得到recycleView：
	RecyclerView recycleView = loadLayout.getRecycleView();//adapter请自定义
	
	**设置监听事件：
	loadLayout.setPrecessChangeListener(new LoadLayout.onPrecessChangeListener() {
            @Override
            public void onLoad(int process) {
                Log.d("process",String.valueOf(process));
            }

            @Override
            public void onRefresh(int process) {
                Log.d("refresh",String.valueOf(process));
            }
        });
	
	onload:下拉头部距离百分比：0~100[int]
	onRefresh:上滑头部距离百分比:0~100[int]
	//请在回调方法中调整您的头部尾部视图，比如根据百分比视图变化的，目前比较多的动态UI

###遗留问题，正在处理中的：
回调没有传入headview与footview，目前先在main里面使用findviewbyid引用，后续补充
代码还没有优化

#希望大家加入，协同修改


