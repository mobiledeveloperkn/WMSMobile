package fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import addon.SwipeListAdapter;
import bl.mSPMDetailBL;
import library.common.mSPMDetailData;
import wms.mobile.R;
import wms.mobile.TabsTaskHeader;

/**
 * Created by ASUS ZE on 04/01/2017.
 */

public class TaskCancelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @SuppressLint("StaticFieldLeak")
    private static ListView mListView;
    @SuppressLint("StaticFieldLeak")
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    @SuppressLint("StaticFieldLeak")
    public static SwipeListAdapter adapterCancel;

    private static String txtNoSPM;
    public TaskCancelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_cancel, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.cancel_swipe_refresh_layout);
        mListView = v.findViewById(R.id.lv_taskcancel);

        Bundle extra = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extra != null) {
            txtNoSPM = extra.getString("txtNoSPM");
        }
        fetchData(getActivity());

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);

            fetchData(getActivity());
        }
        );

        return v;
    }

    public void fetchData(Activity activity) {
        mSwipeRefreshLayout.setRefreshing(true);

        List<mSPMDetailData> mSPMDetailDataListPending;
        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);
        List<mSPMDetailData> mSPMDetailDataListSuccess;
        mSPMDetailDataListSuccess = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        List<mSPMDetailData> mSPMDetailDataListCancel;
        mSPMDetailDataListCancel = new mSPMDetailBL().getAllDataTaskCancel(txtNoSPM);

        ArrayList<String> data = new ArrayList<>();

        data.add("OnProgress(" + mSPMDetailDataListPending.size() + ")");
        data.add("Confirm(" + mSPMDetailDataListSuccess.size() + ")");
        data.add("Cancel(" + mSPMDetailDataListCancel.size() + ")");


        new TabsTaskHeader().updateTitleTabs(data);

        List<mSPMDetailData> mSPMDetailDataList;
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskCancel(txtNoSPM);
        adapterCancel = new SwipeListAdapter(activity, mSPMDetailDataList);
        mListView.setAdapter(adapterCancel);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        Objects.requireNonNull(getActivity()).recreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
