package me.msile.app.androidapp.common.ui.widget.looperviewpager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.msile.app.androidapp.R;

/**
 * 循环recycler view pager adapter
 */

public class LooperRecyclerAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter realAdapter;

    public LooperRecyclerAdapterWrapper(RecyclerView.Adapter realAdapter) {
        this.realAdapter = realAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (realAdapter != null) {
            return realAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (realAdapter != null) {
            try {
                holder.itemView.setTag(R.id.lvp_vh_pos_tag, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            realAdapter.onBindViewHolder(holder, getRealPosition(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (realAdapter == null) {
            return super.getItemViewType(position);
        }
        int realAdapterCount = getRealAdapterCount();
        if (realAdapterCount <= 1) {
            return realAdapter.getItemViewType(position);
        }
        int realPos = position % realAdapterCount;
        return realAdapter.getItemViewType(realPos);
    }

    @Override
    public int getItemCount() {
        if (realAdapter == null) {
            return 0;
        }
        int realAdapterCount = getRealAdapterCount();
        if (realAdapterCount <= 1) {
            return realAdapterCount;
        }
        return realAdapterCount * 4;
    }

    public int getRealPosition(int position) {
        int realAdapterCount = getRealAdapterCount();
        if (realAdapterCount <= 1) {
            return position;
        }
        return position % realAdapterCount;
    }

    public int getRealAdapterCount() {
        if (realAdapter == null) {
            return 0;
        }
        return realAdapter.getItemCount();
    }

}
