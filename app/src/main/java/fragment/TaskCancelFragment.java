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
import wms.mobile.TabsTaskHeader;

/**
 * Created by ASUS ZE on 04/01/2017.
 */

public class TaskCancelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static ListView mListView;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    Adapter mAdapter;
    public static SwipeListAdapter adapterCancel;
    private List<mSPMDetailData> mSPMDetailDataList;

    private static View v;
    private static String txtNoSPM;
    public TaskCancelFragment() {
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
        v = inflater.inflate(R.layout.fragment_task_cancel, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.cancel_swipe_refresh_layout);
        mListView = (ListView) v.findViewById(R.id.lv_taskcancel);

        Bundle extra = getActivity().getIntent().getExtras();
        txtNoSPM = extra.getString("txtNoSPM");
//        mSPMDetailDataList = new ArrayList<>();
//        adapterSuccess = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
//        mListView.setAdapter(adapterSuccess);
//        mListView.setEmptyView(v.findViewById(R.id.LayoutEmpty));

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


        new TabsTaskHeader().updateTitleTabs(data);

        mSPMDetailDataList=new ArrayList<>();
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskCancel(txtNoSPM);
        adapterCancel = new SwipeListAdapter(activity, mSPMDetailDataList, mListView);
        mListView.setAdapter(adapterCancel);
//        mListView.setEmptyView(v.findViewById(R.id.LayoutEmpty));
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        fetchData(getActivity());
        adapterCancel.notifyDataSetChanged();
        new TaskOnProgressFragment().fetchData(getActivity());
    }

    @Override
    public void onDestroy() {
        mListView=null;
        mSwipeRefreshLayout=null;
        adapterCancel=null;
        v=null;
        super.onDestroy();
    }
}
