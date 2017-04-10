package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import addon.SwipeListAdapter;
import addon.cropper.RecyclerAdapter;
import library.common.mSPMDetailData;
import wms.mobile.R;

public class PartThreeFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    private static View v;
    public static SwipeListAdapter adapterProgress;
    private static ListView mListView;

    public static PartThreeFragment createInstance(int itemsCount) {
        PartThreeFragment partThreeFragment = new PartThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) inflater.inflate(
                R.layout.fragment_part_three, container, false);
//        setupRecyclerView(recyclerView);
        setupListView(coordinatorLayout);
        return coordinatorLayout;
    }

    private void setupListView(CoordinatorLayout coordinatorLayout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) coordinatorLayout.findViewById(R.id.partThree_swipe_refresh_layout);
        mSwipeRefreshLayout.setRefreshing(false);

        List<mSPMDetailData> mSPMDetailDataListPending=new ArrayList<>();
//        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending();
        mListView = (ListView) coordinatorLayout.findViewById(R.id.partThree_lv);
        adapterProgress = new SwipeListAdapter(getActivity(), mSPMDetailDataListPending, mListView);
        mListView.setAdapter(adapterProgress);
        mListView.setEmptyView(coordinatorLayout.findViewById(R.id.LayoutEmpty_partthree));


    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
    }

    private List<mSPMDetailData> createItemList() {
        List<mSPMDetailData> itemList = new ArrayList<>();
//        itemList = new mSPMDetailBL().getAllDataTaskPending();
        return itemList;
    }
}
