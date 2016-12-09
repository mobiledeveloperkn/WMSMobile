package fragment;

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

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TaskSuccessFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Adapter mAdapter;
    private SwipeListAdapter adapter;
    private List<mSPMDetailData> mSPMDetailDataList;

    View v;
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

        mSPMDetailDataList = new ArrayList<>();
        adapter = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
        mListView.setAdapter(adapter);

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
        mSPMDetailDataList = new mSPMDetailBL().getAllDataTaskSuccess();
//        adapter.notifyDataSetChanged();
        adapter = new SwipeListAdapter(getActivity(), mSPMDetailDataList);
        mListView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
