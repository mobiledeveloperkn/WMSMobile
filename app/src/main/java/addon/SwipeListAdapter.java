package addon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        RelativeLayout rl_row = (RelativeLayout) convertView.findViewById(R.id.rl_lis_row);
//        TextView serial = (TextView) convertView.findViewById(serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);

//        serial.setText(String.valueOf(mSPMDetailDataList.get(position).getIntSPMDetailId()));
        title.setText("Locator " + mSPMDetailDataList.get(position).getTxtLocator());
        desc.setText("Item Code " + mSPMDetailDataList.get(position).getTxtItemCode()+"\n"+ "Qty " + mSPMDetailDataList.get(position).getIntQty());

        String color = bgColors[position % bgColors.length];
//        serial.setBackgroundColor(Color.parseColor(color));

        if(mSPMDetailDataList.get(position).getBitStatus().equals("0")){
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewTaskDetail(activity, position);

                }
            });
        } else {
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPopupSuccess(activity, position);

                }
            });
        }



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
        final ImageView btnClose = (ImageView) promptView.findViewById(R.id.btn_close);
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

    private void viewTaskDetail(Context ctx, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = (TextView) promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = (TextView) promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = (TextView) promptView.findViewById(R.id.tv_itemname);
        final ImageView btnClose = (ImageView) promptView.findViewById(R.id.btn_close);

        tvLocator.setText(mSPMDetailDataList.get(position).getTxtLocator());
        tvItemCode.setText(mSPMDetailDataList.get(position).getTxtItemCode());
        tvItemName.setText(mSPMDetailDataList.get(position).getTxtItemName());

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