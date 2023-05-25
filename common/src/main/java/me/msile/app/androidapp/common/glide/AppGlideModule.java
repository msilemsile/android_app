package me.msile.app.androidapp.common.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public final class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        RequestOptions requestOptions = new RequestOptions().format(DecodeFormat.PREFER_RGB_565)
                .set(GifOptions.DECODE_FORMAT, DecodeFormat.DEFAULT);
        builder.setDefaultRequestOptions(requestOptions);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
    }

}