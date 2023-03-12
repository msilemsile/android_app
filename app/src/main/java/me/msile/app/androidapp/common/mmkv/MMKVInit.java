package me.msile.app.androidapp.common.mmkv;

import android.app.Application;

import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVLogLevel;

public class MMKVInit {

    public static void init(Application application) {
        try {
            //腾讯的快速存储工具 替代sp
            MMKV.initialize(application);
            //如果你不希望 MMKV 打印日志，你可以关掉它（虽然我们强烈不建议你这么做）。
//            MMKV.setLogLevel(MMKVLogLevel.LevelNone);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
