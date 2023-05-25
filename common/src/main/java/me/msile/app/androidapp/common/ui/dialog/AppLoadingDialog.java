package me.msile.app.androidapp.common.ui.dialog;

import android.view.View;
import android.widget.TextView;

import me.msile.app.androidapp.common.R;

/**
 * 应用统一loading弹窗
 */
public class AppLoadingDialog extends BaseRecyclerDialog {

    private TextView tvLoading;
    private String mLoadingText;

    public AppLoadingDialog() {
        setStyle(STYLE_NORMAL, R.style.LoadingDialogTheme);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_app_loading;
    }

    @Override
    protected void initViews(View rootView) {
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        tvLoading.setText(mLoadingText);
    }

    public AppLoadingDialog setLoadingText(String loadingText) {
        mLoadingText = loadingText;
        if (tvLoading != null) {
            tvLoading.setText(loadingText);
        }
        return this;
    }

    public static AppLoadingDialog build() {
        return new AppLoadingDialog();
    }

}
