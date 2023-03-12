package me.msile.app.androidapp.common.rx;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * Observable(根据生命周期自动解绑)工具类
 */
public class AutoDisposeUtils {

    /**
     * 在onDestroy生命周期解绑
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> AutoDisposeConverter<T> onDestroyDispose(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY));
    }

}
