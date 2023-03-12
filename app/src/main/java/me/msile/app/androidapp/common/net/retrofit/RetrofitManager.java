package me.msile.app.androidapp.common.net.retrofit;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

import me.msile.app.androidapp.common.constants.AppCommonConstants;
import me.msile.app.androidapp.common.net.okhttp.OkHttpManager;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public enum RetrofitManager {

    INSTANCE;

    private HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();

    public Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit = retrofitHashMap.get(baseUrl);
        if (retrofit == null) {
            retrofit = createRetrofit(baseUrl);
            retrofitHashMap.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    public Retrofit getDefaultRetrofit() {
        return getRetrofit(AppCommonConstants.APP_BASE_URL);
    }

    private Retrofit createRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        HttpUrl httpUrl = null;
        try {
            httpUrl = HttpUrl.parse(baseUrl);
        } catch (Exception e) {
            Log.d("RetrofitManager", "createRetrofit fail baseUrl error");
        }
        if (httpUrl == null) {
            httpUrl = HttpUrl.parse(AppCommonConstants.APP_BASE_URL);
        }
        builder.baseUrl(httpUrl)
                .client(OkHttpManager.INSTANCE.getAppHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(getJacksonConfig()))
                .build();
        return builder.build();
    }

    private ObjectMapper getJacksonConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}