package me.msile.app.androidapp.common.rx;

import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class RxHelper {

    public static void init() {
        try {
            RxJavaPlugins.setErrorHandler(Functions.emptyConsumer());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
