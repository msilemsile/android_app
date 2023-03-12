package me.msile.app.androidapp.test;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.ui.adapter.CommonRecyclerAdapter;
import me.msile.app.androidapp.common.ui.fragment.BaseRecyclerFragment;

public class TabWidgetFragment extends BaseRecyclerFragment {

    private RecyclerView rvContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_widget;
    }

    @Override
    protected void initViews(View rootView) {
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
    }

    @Override
    protected void onFragmentResume(boolean isFirstOnResume) {
        if (isFirstOnResume) {
            List<AppWidgetBean> dataList = new ArrayList<>();
            //测试数据
            for (int i = 0; i < 10; i++) {
                switch (i) {
                    //拖拽布局
                    case AppWidgetBean.WIDGET_TYPE_DRAG_LAY:
                        dataList.add(new AppWidgetBean(i, "DragLayout", "拖拽布局,参考类DragFrameLayout"));
                        break;
                    //底部划线布局
                    case AppWidgetBean.WIDGET_TYPE_UNDERLINE_LAY:
                        dataList.add(new AppWidgetBean(i, "LineLayout", "底部划线布局,参考类LineLayoutHelper"));
                        break;
                    //循环ViewPager
                    case AppWidgetBean.WIDGET_TYPE_LOOP_VIEWPAGER:
                        dataList.add(new AppWidgetBean(i, "LoopViewPager", "循环ViewPager,参考类LooperRecyclerViewPager"));
                        break;
                    //上下滚动播报控件
                    case AppWidgetBean.WIDGET_TYPE_NOTIFY_VIEW:
                        dataList.add(new AppWidgetBean(i, "LoopNotifyView", "上下滚动播报控件,参考类LooperNotifyView"));
                        break;
                    //viewpager指示器
                    case AppWidgetBean.WIDGET_TYPE_PAGER_INDICATOR:
                        dataList.add(new AppWidgetBean(i, "PageIndicator", "viewpager指示器,参考类ViewPagerIndicator"));
                        break;
                    //比例布局
                    case AppWidgetBean.WIDGET_TYPE_RATIO_LAY:
                        dataList.add(new AppWidgetBean(i, "RadioLayout", "比例布局,参考类RadioLayoutHelper"));
                        break;
                    //阴影布局
                    case AppWidgetBean.WIDGET_TYPE_SHADOW_LAY:
                        dataList.add(new AppWidgetBean(i, "ShadowLayout", "阴影布局,参考类ShadowLayoutHelper"));
                        break;
                    //自定义背景图形的布局
                    case AppWidgetBean.WIDGET_TYPE_SHAPE_LAY:
                        dataList.add(new AppWidgetBean(i, "ShapeLayout", "自定义背景图形的布局,参考类ShapeLayoutHelper"));
                        break;
                    //viewpager标签选择控件
                    case AppWidgetBean.WIDGET_TYPE_SLIDE_TAB_LAY:
                        dataList.add(new AppWidgetBean(i, "SlideTabLayout", "viewpager标签选择控件,参考类AppSlideTabLayout"));
                        break;
                }
            }
            CommonRecyclerAdapter recyclerAdapter = new CommonRecyclerAdapter(false);
            recyclerAdapter.addViewHolderFactory(new AppWidgetViewHolder.Factory());
            recyclerAdapter.addDataList(dataList);
            rvContent.setAdapter(recyclerAdapter);
        }
    }
}
