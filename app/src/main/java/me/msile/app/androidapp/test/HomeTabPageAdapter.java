package me.msile.app.androidapp.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import me.msile.app.androidapp.common.ui.adapter.CommonFragmentPageAdapter;

/**
 * 首页全部界面
 */
public class HomeTabPageAdapter extends CommonFragmentPageAdapter {

    private static final int TAB_PAGE_SIZE = 3;

    public HomeTabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new TabComFragment();
        } else if (position == 1) {
            return new TabWidgetFragment();
        } else {
            return new TabDescFragment();
        }
    }

    @Override
    public int getItemCount() {
        return TAB_PAGE_SIZE;
    }

}
