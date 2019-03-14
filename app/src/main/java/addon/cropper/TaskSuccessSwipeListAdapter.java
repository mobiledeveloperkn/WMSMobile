package addon.cropper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import library.common.mSPMDetailData;
import wms.mobile.R;

/**
 * Created by ASUS ZE on 21/12/2016.
 */

public class TaskSuccessSwipeListAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater;
    private List<mSPMDetailData> mSPMDetailDataList;
    private String[] bgColors;

    public TaskSuccessSwipeListAdapter(Activity activity, List<mSPMDetailData> mSPMDetailDataList) {
        this.activity = activity;
        this.mSPMDetailDataList = mSPMDetailDataList;
//        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.lis_row_task_success, null);

        RelativeLayout rl_row = (RelativeLayout) convertView.findViewById(R.id.rl_lis_row);
//        TextView serial = (TextView) convertView.findViewById(serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);

//        serial.setText(String.valueOf(mSPMDetailDataList.get(position).getIntSPMDetailId()));
        title.setText("Locator " + mSPMDetailDataList.get(position).getTxtLocator());
        desc.setText("Item Code " + mSPMDetailDataList.get(position).getTxtItemCode()+"\n"+ "Qty " + mSPMDetailDataList.get(position).getIntQty());

//        String color = bgColors[position % bgColors.length];
//        serial.setBackgroundColor(Color.parseColor(color));

//        if(mSPMDetailDataList.get(position).getBitStatus().equals("0")){
//            rl_row.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    viewTaskDetail(activity, position);
//
//                }
//            });
//        } else {
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPopupSuccess(activity, position);

                }
            });
//        }



        return convertView;
    }
    private void viewPopupSuccess(Activity activity, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = (TextView) promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = (TextView) promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = (TextView) promptView.findViewById(R.id.tv_itemname);
        final TextView tvTitleLocator = (TextView) promptView.findViewById(R.id.tv_title_locator);
        final TextView tvTitleItemCode = (TextView) promptView.findViewById(R.id.tv_title_itemcode);
        final TextView btnClose = (TextView) promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = (Button) promptView.findViewById(R.id.btn_confirm);

        tvLocator.setVisibility(View.INVISIBLE);
        tvItemCode.setVisibility(View.INVISIBLE);
        tvTitleLocator.setVisibility(View.INVISIBLE);
        tvTitleItemCode.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        tvItemName.setText("SUCCESS");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(true);


//                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
            }
        });
    }
}
