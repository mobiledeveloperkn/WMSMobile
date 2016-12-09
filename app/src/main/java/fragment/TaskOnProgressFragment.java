package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import addon.SwipeListAdapter;
import bl.mSPMDetailBL;
import library.common.mSPMDetailData;
import wms.mobile.R;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TaskOnProgressFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Adapter mAdapter;
    private SwipeListAdapter adapter;
    private List<mSPMDetailData> mSPMDetailDataList;
    private LinearLayout mToolbarContainer;
    private int mToolbarHeight;

    View v,v2;

    public TaskOnProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_task_on_progress, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.onprogress_swipe_refresh_layout);
        mListView = (ListView) v.findViewById(R.id.lv_onprogres);

        mSPMDetailDataList = new ArrayList<>();
        adapter = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
        mListView.setAdapter(adapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                if (absListView.getId() == mListView.getId()) {
//                    final int currentFirstVisibleItem = mListView.getFirstVisiblePosition();
//
//                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//                        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//                        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//                    }
//                    mLastFirstVisibleItem = currentFirstVisibleItem;
//                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipeRefreshLayout.setRefreshing(true);

                                        fetchData();
                                    }
                                }
        );

        return v;
    }

    private void fetchData() {
        mSwipeRefreshLayout.setRefreshing(true);

        mSPMDetailDataList=new ArrayList<>();
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskPending();
//        adapter.notifyDataSetChanged();
        adapter = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
        mListView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (absListView.getId() == mListView.getId()) {
                    final int currentFirstVisibleItem = mListView.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                    }
                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
