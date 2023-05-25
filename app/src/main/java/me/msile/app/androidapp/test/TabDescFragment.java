package me.msile.app.androidapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.fragment.BaseRecyclerFragment;
import me.msile.app.androidapp.common.ui.widget.common.UnderlineTextView;
import me.msile.app.androidapp.common.web.WebManager;

public class TabDescFragment extends BaseRecyclerFragment {

    private static final String URL_GITHUB = "https://www.github.com/msilemsile/android_app";

    private UnderlineTextView tvGithub;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_desc;
    }

    @Override
    protected void initViews(View rootView) {
        tvGithub = (UnderlineTextView) findViewById(R.id.tv_github);
    }

    @Override
    protected void onFragmentResume(boolean isFirstOnResume) {
        if (isFirstOnResume) {
            tvGithub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commonWebIntent = WebManager.INSTANCE.getCommonWebIntent(mActivity, URL_GITHUB);
                    startActivity(commonWebIntent);
                }
            });
        }
    }
}
