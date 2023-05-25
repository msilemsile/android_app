package me.msile.app.androidapp.common.rx;

import io.reactivex.rxjava3.core.ObservableEmitter;

public class RxEmitterUtils {

    public static void tryOnError(ObservableEmitter emitter, Throwable throwable) {
        if (emitter != null && !emitter.isDisposed()) {
            emitter.onError(throwable);
        }
    }

    public static void tryOnEmptyError(ObservableEmitter emitter) {
        if (emitter != null && !emitter.isDisposed()) {
            emitter.onError(new Throwable());
        }
    }

}
