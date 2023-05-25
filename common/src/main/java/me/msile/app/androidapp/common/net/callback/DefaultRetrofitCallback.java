package me.msile.app.androidapp.common.net.callback;

import io.reactivex.rxjava3.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultRetrofitCallback<T> implements Callback<T> {
    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

    }
}
