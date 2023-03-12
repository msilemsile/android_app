package me.msile.app.androidapp.common.web.helper;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

public class X5WebViewInit {

    public static void init(Application application) {
        try {
            // 在调用TBS初始化、创建WebView之前进行如下配置
            QbSdk.setDownloadWithoutWifi(true);
            HashMap map = new HashMap();
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
            QbSdk.initTbsSettings(map);
            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean arg0) {
                    Log.d("X5WebViewInit", " onViewInitFinished is " + arg0);
                }

                @Override
                public void onCoreInitFinished() {
                    Log.d("X5WebViewInit", " onCoreInitFinished");
                }
            };
            //x5内核初始化接口
            QbSdk.initX5Environment(application, cb);
            Log.d("X5WebViewInit", " initX5Environment");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
