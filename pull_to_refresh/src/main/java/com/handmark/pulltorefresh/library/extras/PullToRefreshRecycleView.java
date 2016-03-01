package com.handmark.pulltorefresh.library.extras;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * 可以刷新的RecycleView
 * Created by Hello on 2015/6/30.
 */
public class PullToRefreshRecycleView extends PullToRefreshBase<RecyclerView> {
    private Context context;
    private float density;
    private RecyclerView mRefreshableView;

    public PullToRefreshRecycleView(Context context) {
        super(context);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecycleView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecycleView(Context context, Mode mode, AnimationStyle animStyle) {

        super(context, mode, animStyle);
        this.context = context;
    }

    //重写4个方法
    //1 滑动方向
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    //重写4个方法
    //2  滑动的View
    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        mRefreshableView = new RecyclerView(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        return mRefreshableView;
    }

    //重写4个方法
    //3 是否滑动到底部
    @Override
    protected boolean isReadyForPullEnd() {
        return isLastItemVisible();
    }

    //重写4个方法
    //4 是否滑动到顶部
    @Override
    protected boolean isReadyForPullStart() {
        View view = mRefreshableView.getChildAt(0);

        if (view != null) {
            return view.getTop() >= mRefreshableView.getTop();
        }
        return false;
    }

    private boolean isLastItemVisible() {
        final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter) {
            return true;
        } else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRefreshableView.getLayoutManager();
            final int lastItemPosition = mRefreshableView.getAdapter().getItemCount() -1;
            final int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();


            /**
             * This check should really just be: lastVisiblePosition ==
             * lastItemPosition, but PtRListView internally uses a FooterView
             * which messes the positions up. For me we'll just subtract one to
             * account for it and rely on the inner condition which checks
             * getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - layoutManager.findFirstVisibleItemPosition();
                final View lastVisibleChild = mRefreshableView.getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= mRefreshableView.getBottom();
                }
            }
        }

        return false;
    }

    public void setSmoothToPosition(int position)
    {
        Log.i("setSmoothToPosition","高度   "+position+"   "+(mRefreshableView.getHeight() + (int)(65 * density))
        +"    "+mRefreshableView.getHeight());
        int sumHeight = 0;
        for(int i=0;i<mRefreshableView.getChildCount();i++)
        {
            sumHeight += mRefreshableView.getChildAt(i).getHeight();
        }
        Log.i("setSmoothToPosition","setSmoothToPosition   "+sumHeight+"    scrollY  "+mRefreshableView.getScrollY()
               +"    "+mRefreshableView.getScrollX());
        mRefreshableView.smoothScrollBy(0,(int)(50 * density));
//        Scroller mScroller;
//        mScroller = new Scroller(context,new LinearInterpolator(context,null));
//        mScroller.startScroll(0,mRefreshableView.getHeight(),0,(mRefreshableView.getHeight() + (int)(65 * density)),0);
//        invalidate();
    }
}
