package me.msile.app.androidapp.common.core;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * 持有弱引用AC
 * 与BaseActivity配合使用
 */
public class ActivityHolderHelper {

    private final HashMap<Class, Object> weakRefHolderMap = new HashMap<>();

    public @Nullable
    <T extends ActivityWeakRefHolder> T get(@NonNull Class<T> tClass, @NonNull Activity activity) {
        Object weakRefHolder = weakRefHolderMap.get(tClass);
        if (weakRefHolder == null) {
            try {
                Constructor<T> constructor = tClass.getConstructor(Activity.class);
                weakRefHolder = constructor.newInstance(activity);
                weakRefHolderMap.put(tClass, weakRefHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) weakRefHolder;
    }

    public <T extends ActivityWeakRefHolder> void clear(@NonNull Class<T> tClass) {
        Object weakRefHolder = weakRefHolderMap.get(tClass);
        if (weakRefHolder != null) {
            weakRefHolderMap.remove(tClass);
        }
    }

    public void clear() {
        weakRefHolderMap.clear();
    }

}
