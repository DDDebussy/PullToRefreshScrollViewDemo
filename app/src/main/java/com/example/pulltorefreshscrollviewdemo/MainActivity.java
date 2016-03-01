package com.example.pulltorefreshscrollviewdemo;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //这几个刷新Label的设置
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("lastUpdateLabel");
        mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("PULLLABLE");
        mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("refreshingLabel");
        mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("releaseLabel");

        //上拉、下拉设定
        mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);

        //上拉监听函数
        mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //执行刷新函数
                new GetDataTask().execute();
            }
        });

        //获取ScrollView布局，此文中用不到
        mScrollView = mPullRefreshScrollView.getRefreshableView();
    }

    private class GetDataTask extends AsyncTask<Void, Void, LinearLayout> {

        @Override
        protected LinearLayout doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
                LinearLayout lin=viewSingleItem();
                return lin;
            } catch (InterruptedException e) {
                Log.e("msg","GetDataTask:" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(LinearLayout result) {
            // Do some stuff here

            LinearLayout sub_root_lin=(LinearLayout) findViewById(R.id.sub_root_lin);
            LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            sub_root_lin.addView(result, 0, LP_FW);

            mPullRefreshScrollView.setMode(Mode.DISABLED);

            // Call onRefreshComplete when the list has been refreshed.
            //在更新UI后，无需其它Refresh操作，系统会自己加载新的listView
            mPullRefreshScrollView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    /**
     * 下拉刷新后，动态产生的一条布局
     * @return
     */
    private LinearLayout viewSingleItem()
    {
        LinearLayout layout_root_lin=new LinearLayout(this);
        layout_root_lin.setOrientation(LinearLayout.VERTICAL);

        //添加第一个子布局集
        RelativeLayout layout_sub_relative=new RelativeLayout(this);

        ImageView relative_sub_IV=new ImageView(this);
        relative_sub_IV.setPadding(5, 5, 5, 5);
        relative_sub_IV.setClickable(true);
        relative_sub_IV.setImageResource(R.drawable.list_item_detail_part_navi_edit);
        RelativeLayout.LayoutParams RL_IM = new RelativeLayout.LayoutParams(50,50);//尤其注意这个位置，用的是父容器的布局参数
        RL_IM.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//这里要注意设置方法！！！---靠父容器右侧对齐
        layout_sub_relative.addView(relative_sub_IV, RL_IM);

        TextView relative_sub_TV=new TextView(this);
        relative_sub_TV.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        relative_sub_TV.setText("(一)");
        relative_sub_TV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        relative_sub_TV.setTextSize(20);
        RelativeLayout.LayoutParams RL_TV = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,50);
        layout_sub_relative.addView(relative_sub_TV, RL_TV);

        LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_root_lin.addView(layout_sub_relative, LP_FW);

        //添加第二个子布局
        TextView lin_sub_TV=new TextView(this);
        lin_sub_TV.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        lin_sub_TV.setText("四月十七，正是去年今日，别君时。忍泪佯低面，含羞半敛眉。" +
                "不知魂已断，空有梦相随。除却天边月，没人知。");
        lin_sub_TV.setTextSize(20);
        layout_root_lin.addView(lin_sub_TV, LP_FW);

        return layout_root_lin;


    }
}
