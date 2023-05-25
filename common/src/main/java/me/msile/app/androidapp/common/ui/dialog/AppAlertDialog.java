package me.msile.app.androidapp.common.ui.dialog;

import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import me.msile.app.androidapp.common.R;

/**
 * 应用警告统一弹窗
 */
public class AppAlertDialog extends BaseRecyclerDialog implements View.OnClickListener {

    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mConfirmButton;
    private TextView mCancelButton;
    private View mBottomDivider;

    private boolean mDialogCancelable;
    private String mTitleText;
    private CharSequence mContentText;
    private String mCancelText;
    private String mConfirmText;
    private MovementMethod mContentMovementMethod;

    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;

    public interface OnSweetClickListener {
        void onClick(AppAlertDialog appAlertDialog);
    }

    public AppAlertDialog() {
        super();
        setCancelable(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_app_alert;
    }

    @Override
    protected void initViews(View rootView) {
        if (mDialogCancelable) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        mTitleTextView = (TextView) rootView.findViewById(R.id.title_text);
        mContentTextView = (TextView) rootView.findViewById(R.id.content_text);
        mContentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mConfirmButton = (TextView) rootView.findViewById(R.id.confirm_button);
        mCancelButton = (TextView) rootView.findViewById(R.id.cancel_button);
        mBottomDivider = rootView.findViewById(R.id.bottom_divider);
        initData();
    }

    public AppAlertDialog setDialogCancelable(boolean cancelable) {
        mDialogCancelable = cancelable;
        return this;
    }

    private void initData() {
        setTitleText(mTitleText);
        setContentText(mContentText);
        setContentMovementMethod(mContentMovementMethod);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        mCancelButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    public AppAlertDialog setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mTitleTextView.setVisibility(View.VISIBLE);
            mTitleTextView.setText(text);
        } else {
            mTitleTextView.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialog setContentText(CharSequence text) {
        mContentText = text;
        if (mContentTextView == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mContentTextView.setVisibility(View.VISIBLE);
            mContentTextView.setText(text);
        } else {
            mContentTextView.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialog setContentMovementMethod(MovementMethod movementMethod) {
        if (movementMethod == null) {
            return this;
        }
        this.mContentMovementMethod = movementMethod;
        if (mContentTextView == null) {
            return this;
        }
        mContentTextView.setMovementMethod(movementMethod);
        return this;
    }

    public AppAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mCancelButton.setVisibility(View.VISIBLE);
            mCancelButton.setText(text);
        } else {
            mCancelButton.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mConfirmText)) {
            mBottomDivider.setVisibility(View.VISIBLE);
        } else {
            mBottomDivider.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mConfirmButton.setVisibility(View.VISIBLE);
            mConfirmButton.setText(text);
        } else {
            mConfirmButton.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mCancelText)) {
            mBottomDivider.setVisibility(View.VISIBLE);
        } else {
            mBottomDivider.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialog setCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public AppAlertDialog setConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.cancel_button) {
            dismiss();
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(AppAlertDialog.this);
            }
        } else if (viewId == R.id.confirm_button) {
            dismiss();
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(AppAlertDialog.this);
            }
        }
    }

    public static AppAlertDialog build() {
        AppAlertDialog appAlertDialog = new AppAlertDialog();
        return appAlertDialog;
    }
}
