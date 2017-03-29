package com.example.next.coverflow;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener;
import com.example.next.coverflow.databinding.ActivityMainBinding;
import com.example.next.coverflow.databinding.ItemViewBinding;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_main);

      //  setSupportActionBar(binding.toolbar);

        final TestAdapter adapter = new TestAdapter();

        // create layout manager with needed params: vertical, cycle
        initRecyclerView(binding.listHorizontal, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), adapter);
        initRecyclerView(binding.listVertical, new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true), adapter);

        // fab button will add element to the end of the list
        binding.fabScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
/*
                final int itemToRemove = adapter.mItemsCount;
                if (10 != itemToRemove) {
                    adapter.mItemsCount++;
                    adapter.notifyItemInserted(itemToRemove);
                }
*/
                binding.listHorizontal.smoothScrollToPosition(adapter.getItemCount());
                binding.listVertical.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        // fab button will remove element from the end of the list
        binding.fabChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
/*
                final int itemToRemove = adapter.mItemsCount - 1;
                if (0 <= itemToRemove) {
                    adapter.mItemsCount--;
                    adapter.notifyItemRemoved(itemToRemove);
                }
*/
                binding.listHorizontal.smoothScrollToPosition(1);
                binding.listVertical.smoothScrollToPosition(1);
            }
        });
    }

    private void initRecyclerView(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, final TestAdapter adapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(2);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
        // enable center post touching on item and item click listener
        DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager carouselLayoutManager, @NonNull final View v) {
                final int position = recyclerView.getChildLayoutPosition(v);
                final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, recyclerView, layoutManager);

        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
                    final int value = adapter.mPosition[adapterPosition];
/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
                }
            }
        });
    }

    private static final class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

        @SuppressWarnings("UnsecureRandomNumberGeneration")
        private final Random mRandom = new Random();
        private final int[] mColors;
        private final int[] mPosition;
        private int mItemsCount = 10;

        TestAdapter() {
            mColors = new int[mItemsCount];
            mPosition = new int[mItemsCount];
            for (int i = 0; mItemsCount > i; ++i) {

                int c1 = mRandom.nextInt(256);
                Log.i("hi", "TestAdapter: "+c1);
                int c2=mRandom.nextInt(256);
                Log.i("hii", "TestAdapter: "+c2);

                int c3=mRandom.nextInt(256);
                Log.i("hii", "TestAdapter: "+c3);

                int c=255;
                //noinspection MagicNumber
                mColors[i] = Color.argb(c,c1, c2, c3);
                mPosition[i] = i;
            }
        }

        @Override
        public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            ItemViewBinding mItemViewBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_view,parent,false);

            return new TestViewHolder(mItemViewBinding);

        }

        @Override
        public void onBindViewHolder(final TestViewHolder holder, final int position) {
            holder.mItemViewBinding.cItem1.setText(String.valueOf(mPosition[position]));
            holder.mItemViewBinding.cItem2.setText(String.valueOf(mPosition[position]));
            holder.itemView.setBackgroundColor(mColors[position]);
        }

        @Override
        public int getItemCount() {
            return mItemsCount;
        }
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {

        public final ItemViewBinding mItemViewBinding;

        TestViewHolder(final ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());

            mItemViewBinding = itemViewBinding;
        }
    }
}