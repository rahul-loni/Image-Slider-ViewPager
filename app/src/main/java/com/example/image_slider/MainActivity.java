package com.example.image_slider;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImagePagerAdapter adapter;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrevious = findViewById(R.id.btnPrevious);

        // Handle Next button
        btnNext.setOnClickListener(v -> {
            int next = (viewPager.getCurrentItem() + 1) % adapter.getCount();
            viewPager.setCurrentItem(next, true);
        });

        // Handle Previous button
        btnPrevious.setOnClickListener(v -> {
            int prev = (viewPager.getCurrentItem() - 1 + adapter.getCount()) % adapter.getCount();
            viewPager.setCurrentItem(prev, true);
        });

        // Auto slide logic
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == adapter.getCount()) {
                    currentPage = 0; // restart from first image
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // 3 seconds delay
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // start auto-slide
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // stop auto-slide when not visible
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[]{
                R.drawable.processedmeat,
                R.drawable.pullups,
                R.drawable.pushups,
                R.drawable.runnig,
                R.drawable.squats
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = MainActivity.this;
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(mImages[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}
