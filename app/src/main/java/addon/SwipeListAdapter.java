package addon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import library.common.mSPMDetailData;
import wms.mobile.R;

/**
 * Created by Ravi on 13/05/15.
 */
public class SwipeListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<mSPMDetailData> mSPMDetailDataList;
    private String[] bgColors;

    public SwipeListAdapter(Activity activity, List<mSPMDetailData> mSPMDetailDataList) {
        this.activity = activity;
        this.mSPMDetailDataList = mSPMDetailDataList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public int getCount() {
        return mSPMDetailDataList.size();
    }

    @Override
    public Object getItem(int location) {
        return mSPMDetailDataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        serial.setText(String.valueOf(mSPMDetailDataList.get(position).getIntSPMDetailId()));
        title.setText(mSPMDetailDataList.get(position).getTxtItemName());

        String color = bgColors[position % bgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

}