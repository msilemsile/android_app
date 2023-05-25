package me.msile.app.androidapp.common.utils;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import me.msile.app.androidapp.common.core.ApplicationHolder;

/**
 * 剪切板信息copy
 */
public class ClipboardUtils {

    public static void copyClipboardInfo(String textCase) {
        copyClipboardInfo("", textCase);
    }

    public static void copyClipboardInfo(String label, String textCase) {
        if (TextUtils.isEmpty(textCase)) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) ApplicationHolder.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            ClipData clipData = ClipData.newPlainText(label, textCase);
            cmb.setPrimaryClip(clipData);
        }
    }

    public static String getClipboardFirstLabel() {
        ClipboardManager manager = (ClipboardManager) ApplicationHolder.getAppContext().getSystemService(CLIPBOARD_SERVICE);
        if (manager == null) {
            return "";
        }
        ClipData primaryClip = manager.getPrimaryClip();
        if (primaryClip == null) {
            return "";
        }
        int clipDataSize = primaryClip.getItemCount();
        if (clipDataSize == 0) {
            return "";
        }
        ClipDescription clipDescription = primaryClip.getDescription();
        if (clipDescription == null || clipDescription.getLabel() == null) {
            return "";
        }
        return clipDescription.getLabel().toString();
    }

    public static String getClipboardFirstData() {
        ClipboardManager manager = (ClipboardManager) ApplicationHolder.getAppContext().getSystemService(CLIPBOARD_SERVICE);
        if (manager == null) {
            return "";
        }
        ClipData primaryClip = manager.getPrimaryClip();
        if (primaryClip == null) {
            return "";
        }
        int clipDataSize = primaryClip.getItemCount();
        if (clipDataSize == 0) {
            return "";
        }
        ClipData.Item firstClipItem = primaryClip.getItemAt(0);
        if (firstClipItem == null) {
            return "";
        }
        CharSequence clipCharSequence = firstClipItem.getText();
        if (clipCharSequence == null) {
            return "";
        }
        return clipCharSequence.toString();
    }

    public static void clearClipboardFirstData() {
        ClipboardManager manager = (ClipboardManager) ApplicationHolder.getAppContext().getSystemService(CLIPBOARD_SERVICE);
        if (manager != null) {
            manager.setPrimaryClip(ClipData.newPlainText("", ""));
        }
    }

}
