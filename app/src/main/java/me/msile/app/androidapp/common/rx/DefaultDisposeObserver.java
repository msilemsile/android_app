package me.msile.app.androidapp.common.rx;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 可手动调用dispose方法 取消订阅
 * @param <T>
 */
public abstract class DefaultDisposeObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(@NonNull T t) {
        try {
            onSuccess(t);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(@NonNull T t);
}
