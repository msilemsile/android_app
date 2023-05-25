package me.msile.app.androidapp.common.web.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.activity.ImmerseActivity;
import me.msile.app.androidapp.common.web.fragment.CommonWebFragment;

public class CommonWebActivity extends ImmerseActivity {

    public static final String EXTRA_WEB_URL = "web_url";
    public static final String EXTRA_WEB_X5 = "web_view_x5";

    private static final String TAG_FRAGMENT_WEB = "tag_fragment_web";
    private String mWebUrl;
    private boolean mIsX5WebView;
    private CommonWebFragment commonWebFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        getDataFromIntent();
        initViews(savedInstanceState);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mWebUrl = intent.getStringExtra(EXTRA_WEB_URL);
            mIsX5WebView = intent.getBooleanExtra(EXTRA_WEB_X5, false);
        }
    }

    private void initViews(Bundle saveInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (saveInstanceState != null) {
            commonWebFragment = (CommonWebFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_WEB);
        }
        if (commonWebFragment == null) {
            commonWebFragment = CommonWebFragment.newInstance(mWebUrl, mIsX5WebView);
            commonWebFragment.setOnWebListener(new CommonWebFragment.OnWebListener() {
                @Override
                public void onWebClose() {
                    finish();
                }
            });
        }
        try {
            fragmentManager.beginTransaction().replace(R.id.fl_content, commonWebFragment, TAG_FRAGMENT_WEB).commitNow();
        } catch (Exception e) {
            Log.d("CommonWebActivity", "add web error");
        }
    }

    @Override
    public void onBackPressed() {
        if (commonWebFragment.goBack()) {
            return;
        }
        super.onBackPressed();
    }
}
