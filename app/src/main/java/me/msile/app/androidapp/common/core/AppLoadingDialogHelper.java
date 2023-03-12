package me.msile.app.androidapp.common.core;

import android.util.SparseArray;

import androidx.fragment.app.FragmentActivity;

import me.msile.app.androidapp.common.ui.dialog.AppLoadingDialog;
import me.msile.app.androidapp.common.ui.dialog.BaseDialog;

/**
 * 应用通用loading dialog处理
 * 与BaseActivity配合使用
 */
public class AppLoadingDialogHelper {

    private final SparseArray<AppLoadingDialog> loadingDialogSA = new SparseArray<>();

    public void showLoadingDialog(FragmentActivity activity, int tag) {
        showLoadingDialog(activity, tag, "");
    }

    public void showLoadingDialog(FragmentActivity activity, int tag, String loadingText) {
        AppLoadingDialog loadingDialog = loadingDialogSA.get(tag);
        if (loadingDialog != null) {
            loadingDialog.setLoadingText(loadingText);
            return;
        }
        AppLoadingDialog newLoadingDialog = AppLoadingDialog.build().setLoadingText(loadingText);
        newLoadingDialog.setDialogLifecycleListener(new BaseDialog.DialogLifecycleListener() {
            @Override
            public void onDialogDestroy() {
                loadingDialogSA.remove(tag);
            }
        });
        newLoadingDialog.show(activity);
        loadingDialogSA.put(tag, newLoadingDialog);
    }

    public void hideLoadingDialog(int tag) {
        AppLoadingDialog loadingDialog = loadingDialogSA.get(tag);
        if (loadingDialog != null) {
            loadingDialogSA.remove(tag);
            loadingDialog.dismiss();
        }
    }

    public void clear() {
        loadingDialogSA.clear();
    }
}
