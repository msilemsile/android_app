package me.msile.app.androidapp.test;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.ui.adapter.CommonRecyclerAdapter;
import me.msile.app.androidapp.common.ui.fragment.BaseRecyclerFragment;

public class TabComFragment extends BaseRecyclerFragment {

    private RecyclerView rvContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_com;
    }

    @Override
    protected void initViews(View rootView) {
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
    }

    @Override
    protected void onFragmentResume(boolean isFirstOnResume) {
        if (isFirstOnResume) {
            List<AppComBean> dataList = new ArrayList<>();
            //测试数据
            for (int i = 0; i < 11; i++) {
                switch (i) {
                    case AppComBean.COM_TYPE_NET:
                        dataList.add(new AppComBean(i, "网络框架", "参考类OkHttpManager + RetrofitManager", false));
                        break;
                    case AppComBean.COM_TYPE_PIC_LOADER:
                        dataList.add(new AppComBean(i, "图片框架", "参考类AppGlideModule", false));
                        break;
                    case AppComBean.COM_TYPE_LOCAL_DATA:
                        dataList.add(new AppComBean(i, "存储框架", "MMKV + GreenDAO + 参考类StorageHelper", false));
                        break;
                    case AppComBean.COM_TYPE_WEB_VIEW:
                        dataList.add(new AppComBean(i, "WebView框架", "参考类BaseWebView + BaseX5WebView", true));
                        break;
                    case AppComBean.COM_TYPE_PICKER:
                        dataList.add(new AppComBean(i, "文件选择器框架", "参考类FilePickerHelper", true));
                        break;
                    case AppComBean.COM_TYPE_PLAYER:
                        dataList.add(new AppComBean(i, "播放器框架", "参考类MediaPlayerVideoView + ExoPlayerView", true));
                        break;
                    case AppComBean.COM_TYPE_PERMISSION:
                        dataList.add(new AppComBean(i, "权限授权框架", "参考类PermissionHelper", true));
                        break;
                    case AppComBean.COM_TYPE_QR_CODE:
                        dataList.add(new AppComBean(i, "条码扫描框架", "参考类QrCodeActivity", true));
                        break;
                    case AppComBean.COM_TYPE_ROUTER:
                        dataList.add(new AppComBean(i, "路由框架", "参考类RouterManager", false));
                        break;
                    case AppComBean.COM_TYPE_CAMERA:
                        dataList.add(new AppComBean(i, "相机框架", "参考类CameraHelper+CameraXHelper", true));
                        break;
                    case AppComBean.COM_TYPE_DOWNLOAD:
                        dataList.add(new AppComBean(i, "下载框架", "参考类DownloadHelper", true));
                        break;
                }
            }
            CommonRecyclerAdapter recyclerAdapter = new CommonRecyclerAdapter(false);
            recyclerAdapter.addViewHolderFactory(new AppComViewHolder.Factory());
            recyclerAdapter.addDataList(dataList);
            rvContent.setAdapter(recyclerAdapter);
        }
    }
}
