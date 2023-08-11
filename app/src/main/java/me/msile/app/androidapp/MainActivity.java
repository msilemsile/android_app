package me.msile.app.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.msile.app.androidapp.common.core.AppManager;
import me.msile.app.androidapp.common.extend.OpenFileProxyHelper;
import me.msile.app.androidapp.common.storage.CacheFileInfo;
import me.msile.app.androidapp.common.ui.activity.ImmerseFullScreenActivity;
import me.msile.app.androidapp.common.ui.dialog.AppAlertDialog;
import me.msile.app.androidapp.test.HomeTabInfo;
import me.msile.app.androidapp.test.HomeTabLayout;
import me.msile.app.androidapp.test.HomeTabPageAdapter;

public class MainActivity extends ImmerseFullScreenActivity {

    ViewPager2 vp2Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBarMode(true);
        setContentView(R.layout.activity_main);
        AppManager.INSTANCE.setMainActivity(this);
        initViews();
        handleOpenFileProxy();
    }

    private void handleOpenFileProxy() {
        List<CacheFileInfo> cacheFileList = OpenFileProxyHelper.INSTANCE.getCacheFileList();
        if (!cacheFileList.isEmpty()) {
            AppAlertDialog.build()
                    .setTitleText("外部打开的文件")
                    .setContentText(Arrays.toString(cacheFileList.toArray()))
                    .setConfirmText("确定")
                    .show(this);
        }
        OpenFileProxyHelper.INSTANCE.clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleOpenFileProxy();
    }

    private void initViews() {
        vp2Content = (ViewPager2) findViewById(R.id.vp2_content);
        HomeTabLayout htlTab = (HomeTabLayout) findViewById(R.id.htl_tab);
        HomeTabPageAdapter homeTabPageAdapter = new HomeTabPageAdapter(this);
        //禁止viewPager左右滑动切换tab页
        vp2Content.setUserInputEnabled(false);
        vp2Content.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                htlTab.setCurrentTab(position);
            }
        });
        vp2Content.setAdapter(homeTabPageAdapter);
        vp2Content.setOffscreenPageLimit(2);
        ArrayList tabInfoList = new ArrayList<>();
        HomeTabInfo<String> comTabInfo = new HomeTabInfo<>();
        comTabInfo.setExtraInfo("组件");
        tabInfoList.add(comTabInfo);
        HomeTabInfo<String> widgetTabInfo = new HomeTabInfo<>();
        widgetTabInfo.setExtraInfo("控件");
        tabInfoList.add(widgetTabInfo);
        HomeTabInfo<String> descTabInfo = new HomeTabInfo<>();
        descTabInfo.setExtraInfo("说明");
        tabInfoList.add(descTabInfo);
        htlTab.setOnTabChangeListener(new HomeTabLayout.OnTabChangeListener() {
            @Override
            public void onTabSelected(int tabIndex) {
                vp2Content.setCurrentItem(tabIndex, false);
            }
        });
        htlTab.addTabList(tabInfoList);
        htlTab.setCurrentTab(saveStateCurrentTabPage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.INSTANCE.clearMainActivity();
    }

    //当前TAB界面
    private static final String TAB_CURRENT_PAGE = "currentTabPage";
    private int saveStateCurrentTabPage;

    @Override
    protected void onSaveInstanceState(@androidx.annotation.NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_CURRENT_PAGE, vp2Content.getCurrentItem());
    }

    private void readSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            saveStateCurrentTabPage = savedInstanceState.getInt(TAB_CURRENT_PAGE, 0);
        }
    }
}