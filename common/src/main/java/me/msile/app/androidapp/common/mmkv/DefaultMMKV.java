package me.msile.app.androidapp.common.mmkv;

import com.tencent.mmkv.MMKV;

public class DefaultMMKV {

    public static MMKV getMMKV() {
        return MMKV.defaultMMKV();
    }
}
