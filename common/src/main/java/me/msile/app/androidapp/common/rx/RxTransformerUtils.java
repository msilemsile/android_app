package me.msile.app.androidapp.common.rx;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * RX java observable转换工具
 */
public class RxTransformerUtils {

    /**
     * IO线程--> MAIN线程回调
     */
    public static <T> ObservableTransformer<T, T> mainSchedulers() {
        return new ObservableTransformer<T, T>() {
            @NonNull
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
