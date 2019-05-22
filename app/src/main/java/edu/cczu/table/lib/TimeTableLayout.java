package edu.cczu.table.lib;

import android.content.Context;
import android.database.Observable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.gridlayout.widget.GridLayout;

import edu.cczu.table.R;
import edu.cczu.table.databinding.LibTableLayoutBinding;

public class TimeTableLayout extends LinearLayout {
    private final TableLayoutViewDataObserver mObserver = new TableLayoutViewDataObserver();

    private LibTableLayoutBinding binding;
    private Adapter adapter;
    private int colCount, rowCount;
    private ViewHolder[][] viewHolders;

    public TimeTableLayout(Context context, Adapter<ViewHolder> adapter) {
        super(context);
        setAdapter(adapter);
        initView();
    }
    // auto construction
    public TimeTableLayout(Context context) {
        super(context);
        initView();
    }

    public TimeTableLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TimeTableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setAdapter(Adapter adapter) {
        adapter.registerAdapterDataObserver(mObserver);
        this.adapter = adapter;
        initTableItem();
    }


    public TimeTableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.lib_table_layout, this, true);
    }

    private void initTableItem() {
        binding.mainGridContent.removeAllViews();
        colCount = adapter.getColCount();
        rowCount = adapter.getRowCount();
        binding.mainGridContent.setRowCount(rowCount);
        binding.mainGridContent.setColumnCount(colCount);

        viewHolders = new ViewHolder[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(i),
                        GridLayout.spec(j)
                );
                params.height = adapter.getItemHeight(i, j);
                params.width = adapter.getItemWidth(i, j);
                ViewHolder viewHolder = adapter.onCreateViewHolder(this, adapter.getItemViewType(i, j));
                viewHolders[i][j] = viewHolder;
                params.setGravity(Gravity.FILL);
                binding.mainGridContent.addView(viewHolder.itemView, params);
            }
        }
        bindTableItem();
    }

    private void bindTableItem() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                ViewHolder holder = viewHolders[i][j];
                adapter.onBindViewHolder(holder, i, j);
                if (holder.rowSpanSize != -1 && holder.colSpanSize != -1) {
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                            GridLayout.spec(i, holder.rowSpanSize),
                            GridLayout.spec(j, holder.colSpanSize)
                    );
                    params.setGravity(Gravity.FILL);
                    params.width = adapter.getItemWidth(i, j) * holder.colSpanSize;
                    params.height = adapter.getItemHeight(i, j) * holder.rowSpanSize;
                    binding.mainGridContent.removeView(holder.itemView);
                    binding.mainGridContent.addView(holder.itemView, params);
                }
            }
        }
    }

    public abstract static class ViewHolder {
        public final View itemView;
        public int rowSpanSize, colSpanSize;

        protected ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        public void setSpanSize(int rowSpanSize, int colSpanSize) {
            this.rowSpanSize = rowSpanSize;
            this.colSpanSize = colSpanSize;
        }
    }

    public abstract static class Adapter<VH extends ViewHolder> {
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public final void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }

        public final boolean hasObservers() {
            return mObservable.hasObservers();
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        public int getItemViewType(int row, int col) {
            return 0;
        }
        public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);
        public abstract void onBindViewHolder(VH holder, int row, int col);
        public abstract int getRowCount();
        public abstract int getColCount();
        public abstract int getItemWidth(int row, int col);
        public abstract int getItemHeight(int row, int col);

    }

    public abstract static class AdapterDataObserver {
        public void onChanged() {
            // Do nothing
        }
    }


    private class TableLayoutViewDataObserver extends AdapterDataObserver {
        TableLayoutViewDataObserver() {
        }

        @Override
        public void onChanged() {
            initTableItem();
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

}
