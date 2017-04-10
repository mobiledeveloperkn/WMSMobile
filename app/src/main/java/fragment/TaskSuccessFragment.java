package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import addon.SwipeListAdapter;
import bl.mSPMDetailBL;
import library.common.mSPMDetailData;
import wms.mobile.R;

import static fragment.TaskOnProgressFragment.adapterProgress;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TaskSuccessFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static ListView mListView;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    Adapter mAdapter;
    public static SwipeListAdapter adapterSuccess;
    private List<mSPMDetailData> mSPMDetailDataList;

    private static View v;
    private static String txtNoSPM;
    public TaskSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_task_success, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.success_swipe_refresh_layout);
        mListView = (ListView) v.findViewById(R.id.lv_tasksuccess);

        Bundle extra = getActivity().getIntent().getExtras();
        txtNoSPM = extra.getString("txtNoSPM");

//        mSPMDetailDataList = new ArrayList<>();
//        adapterSuccess = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
//        mListView.setAdapter(adapterSuccess);
//        mListView.setEmptyView(v.findViewById(R.id.LayoutEmpty));
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                adapterSuccess.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//
//            }
//        });

        fetchData(getActivity());

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

        mSPMDetailDataList=new ArrayList<>();
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        adapterSuccess = new SwipeListAdapter(activity, mSPMDetailDataList, mListView);
        mListView.setAdapter(adapterSuccess);
//        mListView.setEmptyView(v.findViewById(R.id.LayoutEmpty));
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        fetchData(getActivity());
        adapterProgress.notifyDataSetChanged();
        new TaskOnProgressFragment().fetchData(getActivity());
    }

    @Override
    public void onDestroy() {
        mListView=null;
        mSwipeRefreshLayout=null;
        adapterSuccess=null;
        v=null;
        super.onDestroy();
    }
}
