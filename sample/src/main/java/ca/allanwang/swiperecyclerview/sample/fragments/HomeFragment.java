package ca.allanwang.swiperecyclerview.sample.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.allanwang.capsule.library.event.CFabEvent;
import ca.allanwang.capsule.library.fragments.CapsuleFragment;
import ca.allanwang.swiperecyclerview.library.SwipeRecyclerView;
import ca.allanwang.swiperecyclerview.library.adapters.FastAnimatedAdapter;
import ca.allanwang.swiperecyclerview.library.animations.SlideUpFadeInAnimator;
import ca.allanwang.swiperecyclerview.library.interfaces.ISwipeRecycler;
import ca.allanwang.swiperecyclerview.sample.R;
import ca.allanwang.swiperecyclerview.sample.items.CheckBoxItem;

/**
 * Created by Allan Wang on 2017-02-24.
 */

public class HomeFragment extends CapsuleFragment implements ISwipeRecycler.OnRefreshListener {
    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private FastItemAdapter<CheckBoxItem> mAdapter;

    @Nullable
    @Override
    protected CFabEvent updateFab() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.swipe_recycler_view, container, false);
        SwipeRecyclerView frame = (SwipeRecyclerView) v.findViewById(R.id.swipe_recycler);

        mAdapter = new FastAnimatedAdapter<>(new SlideUpFadeInAnimator());
        mAdapter.withOnPreClickListener(new FastAdapter.OnClickListener<CheckBoxItem>() {
            @Override
            public boolean onClick(View v, IAdapter<CheckBoxItem> adapter, CheckBoxItem item, int position) {
                // consume otherwise radio/checkbox will be deselected
                return true;
            }
        });
        mAdapter.withItemEvent(new CheckBoxItem.CheckBoxClickEvent());

        mAdapter.add(generateList());

        frame.setAdapter(mAdapter).setOnRefreshListener(this);
        return v;
    }

    private List<CheckBoxItem> generateList() {
        int x = 0;
        List<CheckBoxItem> items = new ArrayList<>();
        for (String s : ALPHABET) {
            int count = new Random().nextInt(20);
            for (int i = 1; i <= count; i++) {
                items.add(new CheckBoxItem().withName(s + " Test " + x).withIdentifier(100 + x));
                x++;
            }
        }
        return items;
    }

    @Override
    public int getTitleId() {
        return R.string.home;
    }

    @Override
    public void onRefresh(final ISwipeRecycler.OnRefreshStatus statusEmitter) {
        mAdapter.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.add(generateList());
                statusEmitter.onSuccess();
            }
        }, 2000);
    }
}