package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import addon.SwipeListAdapter;
import bl.mSPMDetailBL;
import library.common.mSPMDetailData;
import wms.mobile.R;
import wms.mobile.TabsTaskHeader;

import static fragment.TaskSuccessFragment.adapterSuccess;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TaskOnProgressFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static ListView mListView;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    Adapter mAdapter;
    public static SwipeListAdapter adapterProgress;
    private List<mSPMDetailData> mSPMDetailDataList;
    private LinearLayout mToolbarContainer;
    private int mToolbarHeight;
    private static String txtNoSPM;

    private static View v,v2;

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

        Bundle extra = getActivity().getIntent().getExtras();

        txtNoSPM = extra.getString("txtNoSPM");

//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (mListView.getChildAt(0) != null) {
//                    mSwipeRefreshLayout.setEnabled(mListView.getFirstVisiblePosition() == 0 && mListView.getChildAt(0).getTop() == 0);
//                }
//            }
//        });

//        mSPMDetailDataList = new ArrayList<>();
//        adapterProgress = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
//        mListView.setAdapter(adapterProgress);
//        mListView.setEmptyView(v.findViewById(R.id.LayoutEmpty));

        fetchData(getActivity());

//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            int mLastFirstVisibleItem = 0;
//
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
////                if (absListView.getId() == mListView.getId()) {
////                    final int currentFirstVisibleItem = mListView.getFirstVisiblePosition();
////
////                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
////                        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
////                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
////                        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
////                    }
////                    mLastFirstVisibleItem = currentFirstVisibleItem;
////                }
//            }
//        });

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipeRefreshLayout.setRefreshing(true);

                                        fetchData(getActivity());
                                    }
                                }
        );

        return v;
    }

    public void fetchData(Activity activity) {
        mSwipeRefreshLayout.setRefreshing(true);

        List<mSPMDetailData> mSPMDetailDataListPending=new ArrayList<>();
        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);
        List<mSPMDetailData> mSPMDetailDataListSuccess=new ArrayList<>();
        mSPMDetailDataListSuccess = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        List<mSPMDetailData> mSPMDetailDataListCancel=new ArrayList<>();
        mSPMDetailDataListCancel = new mSPMDetailBL().getAllDataTaskCancel(txtNoSPM);

        ArrayList<String> data = new ArrayList<>();

        data.add(String.valueOf("OnProgress("+String.valueOf(mSPMDetailDataListPending.size())+")"));
        data.add(String.valueOf("Confirm("+String.valueOf(mSPMDetailDataListSuccess.size())+")"));
        data.add(String.valueOf("Cancel("+String.valueOf(mSPMDetailDataListCancel.size())+")"));

        new TabsTaskHeader().updateTitleTabs(data);

        mSPMDetailDataList=new ArrayList<>();
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);
//        adapter.notifyDataSetChanged();
        adapterProgress = new SwipeListAdapter(activity, mSPMDetailDataList, mListView);
        mListView.setAdapter(adapterProgress);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchData(getActivity());
        adapterSuccess.notifyDataSetChanged();
        new TaskSuccessFragment().fetchData(getActivity());
    }

    @Override
    public void onDestroy() {
        mListView=null;
        mSwipeRefreshLayout=null;
        adapterProgress=null;
        v=null;
        super.onDestroy();
    }
}
