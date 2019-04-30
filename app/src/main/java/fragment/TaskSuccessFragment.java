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

import java.util.List;
import java.util.Objects;

import addon.SwipeListAdapter;
import bl.mSPMDetailBL;
import library.common.mSPMDetailData;
import wms.mobile.R;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TaskSuccessFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @SuppressLint("StaticFieldLeak")
    private static ListView mListView;
    @SuppressLint("StaticFieldLeak")
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    @SuppressLint("StaticFieldLeak")
    public static SwipeListAdapter adapterSuccess;
    private static String txtNoSPM;

    public TaskSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_success, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.success_swipe_refresh_layout);
        mListView = v.findViewById(R.id.lv_tasksuccess);

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

        List<mSPMDetailData> mSPMDetailDataList;
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        adapterSuccess = new SwipeListAdapter(activity, mSPMDetailDataList);
        mListView.setAdapter(adapterSuccess);
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
