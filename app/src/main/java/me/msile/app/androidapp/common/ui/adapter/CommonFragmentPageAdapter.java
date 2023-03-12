package me.msile.app.androidapp.common.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * {@link FragmentStateAdapter}
 * 二次封装 提供一些公用方法
 */
public abstract class CommonFragmentPageAdapter extends FragmentStateAdapter {
    public CommonFragmentPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public CommonFragmentPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public CommonFragmentPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * {@link FragmentStateAdapter}
     * FragmentManager.beginTransaction().add(fragment, "f" + holder.getItemId())
     */
    public Fragment findPageFragment(@NonNull FragmentActivity fragmentActivity, int index) {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag("f" + index);
    }
}
