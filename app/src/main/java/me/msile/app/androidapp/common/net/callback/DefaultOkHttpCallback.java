package me.msile.app.androidapp.common.net.callback;

import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DefaultOkHttpCallback implements Callback {
    @Override
    public void onFailure(@NonNull Call call, IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {

    }
}
