package me.msile.app.androidapp.test;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.ui.adapter.CommonRecyclerAdapter;
import me.msile.app.androidapp.common.ui.dialog.BaseRecyclerDialog;
import me.msile.app.androidapp.common.ui.widget.slidetablayout.AppSlideTabLayout;

public class AppSlideTabTestDialog extends BaseRecyclerDialog {

    private AppSlideTabLayout stlSlideTab;
    private ViewPager2 vp2Content;

    @Override
    protected int getLayoutResId() {
        return me.msile.app.androidapp.R.layout.dialog_app_slide_tab_test;
    }

    @Override
    protected void initViews(View rootView) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        stlSlideTab = (AppSlideTabLayout) rootView.findViewById(R.id.stl_slide_tab);
        vp2Content = (ViewPager2) rootView.findViewById(R.id.vp2_content);

        CommonRecyclerAdapter pageAdapter = new CommonRecyclerAdapter(true);
        pageAdapter.addViewHolderFactory(new AppSlideTabTestPageViewHolder.Factory());
        vp2Content.setAdapter(pageAdapter);

        //init tab info list
        List<String> tabInfoList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tabInfoList.add("TAB_" + (i + 1));
        }
        stlSlideTab.addTabStringList(tabInfoList);
        pageAdapter.addDataList(stlSlideTab.getTabInfoList());

        stlSlideTab.setOnTabChangeListener(new AppSlideTabLayout.OnTabChangeListener() {
            @Override
            public void onTabSelected(int tabIndex) {
                vp2Content.setCurrentItem(tabIndex, false);
            }
        });

        vp2Content.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                stlSlideTab.setCurrentTab(position);
            }
        });
    }
}
