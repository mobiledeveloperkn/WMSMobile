package addon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bl.tUserLoginBL;
import library.common.mSPMDetailData;
import library.common.tUserLoginData;
import library.dal.clsHardCode;
import service.WMSMobileService;
import wms.mobile.R;
import wms.mobile.TabsTaskHeader;
import wms.mobile.clsMainActivity;

/**
 * Created by Ravi on 13/05/15.
 */
public class SwipeListAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater;
    private List<mSPMDetailData> mSPMDetailDataList;
    private ListView mListView;
    private String[] bgColors;
    private PackageInfo pInfo =null;
    private String versionName = "";

    public static triggerOnOfflineConnection triggerOnOfflineConnection;
    public static  triggerProgressDialog triggerProgressDialog;

    public SwipeListAdapter(Activity activity, List<mSPMDetailData> mSPMDetailDataList, ListView mListView) {
        this.activity = activity;
        this.mSPMDetailDataList = mSPMDetailDataList;
        this.mListView = mListView;
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
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);
        ImageView iv_status = (ImageView) convertView.findViewById(R.id.imageViewStatus);

        title.setText("Locator " + mSPMDetailDataList.get(position).getTxtLocator());
        desc.setText("Item Code " + mSPMDetailDataList.get(position).getTxtItemCode() + "\n" + "Qty " + mSPMDetailDataList.get(position).getIntQty());

        if (mSPMDetailDataList.get(position).getBitStatus().equals("0") && mSPMDetailDataList.get(position).getBitSync().equals("0")) {
            iv_status.setVisibility(View.INVISIBLE);
            if (position == 0) {
                convertView.setBackgroundResource(R.color.white);
                rl_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewTaskDetail(activity, position);
                    }
                });
            } else if (position>0){
                convertView.setBackgroundResource(R.color.gray);
            }

        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("1") && mSPMDetailDataList.get(position).getBitSync().equals("1")) {
            iv_status.setVisibility(View.VISIBLE);
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPopupSuccess(activity, position);
                }
            });
        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("1") && mSPMDetailDataList.get(position).getBitSync().equals("0")){
            iv_status.setVisibility(View.INVISIBLE);
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPopupSuccess(activity, position);
                }
            });
        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("2") && mSPMDetailDataList.get(position).getBitSync().equals("1")) {
            iv_status.setVisibility(View.VISIBLE);
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPopupCancel(activity, position);
                }
            });
        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("2") && mSPMDetailDataList.get(position).getBitSync().equals("0")) {
            iv_status.setVisibility(View.INVISIBLE);
            rl_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPopupCancel(activity, position);
                }
            });
        }
        return convertView;
    }

    private void viewPopupCancel(final Activity activity, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = (TextView) promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = (TextView) promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = (TextView) promptView.findViewById(R.id.tv_itemname);
        final TextView tvTitleLocator = (TextView) promptView.findViewById(R.id.tv_title_locator);
        final TextView tvTitleItemCode = (TextView) promptView.findViewById(R.id.tv_title_itemcode);
        final TextView btnClose = (TextView) promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = (Button) promptView.findViewById(R.id.btn_confirm);
        final Button btnCancel = (Button) promptView.findViewById(R.id.btn_cancelDetail);
        final TextView qty = (TextView) promptView.findViewById(R.id.tv_qty);

        btnCancel.setVisibility(View.GONE);
        tvLocator.setText(mSPMDetailDataList.get(position).getTxtLocator());
        tvItemCode.setText(mSPMDetailDataList.get(position).getTxtItemCode());
        tvItemName.setText(mSPMDetailDataList.get(position).getTxtItemName());
        qty.setText(mSPMDetailDataList.get(position).getIntQty());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnConfirm.setText("UNDO CANCEL");
        btnConfirm.setEnabled(true);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Revert");
                alertDialog.setMessage("Are you sure to revert this item?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
                        String txtNoSPM = mSPMDetailDataList.get(position).getTxtNoSPM();
                        tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                        String _intUserId = dataLogin.getIntUserId();
                        dataLogin.getIntUserId();
                        boolean status = false;
                        pInfo = new clsMainActivity().getPinfo(activity);
                        if(pInfo!=null){
                            versionName = pInfo.versionName;
                        }
                        if(triggerProgressDialog!=null){
                            addon.SwipeListAdapter.triggerProgressDialog.showProgressDialog(true);
                        }
                        status = new WMSMobileService().undoCancelSPMDetail(_intSPMDetailId, _intUserId, versionName);
                        if (!status) {
                            JSONObject jsonObject = new JSONObject();
                            String method = new clsHardCode().txtMethodServerUndoCancelSPMDetail;
                            String message = "Success";
                            try {
                                jsonObject.put("_intSPMDetailId", _intSPMDetailId);
                                jsonObject.put("_intUserId", _intUserId);
                                jsonObject.put("txtNoSPM", txtNoSPM);
                                jsonObject.put("strMethodName", method);
                                jsonObject.put("message", message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            new mSPMDetailBL().updateDataValueById(_intSPMDetailId, _intUserId);
                            new TabsTaskHeader().onNetworkConnectionChanged(true);
                            if(triggerOnOfflineConnection!=null){
                                addon.SwipeListAdapter.triggerOnOfflineConnection.onOfflineConnection(jsonObject);
                            }
                        }

//                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
//                        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending();
//
//                        if (_mSPMDetailData.size() == 0) {
//                            new TabsTaskHeader().switchTab();
//                        }
                        alertD.dismiss();
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
            }
        });
    }

    private void viewPopupSuccess(final Activity activity, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = (TextView) promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = (TextView) promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = (TextView) promptView.findViewById(R.id.tv_itemname);
        final TextView tvTitleLocator = (TextView) promptView.findViewById(R.id.tv_title_locator);
        final TextView tvTitleItemCode = (TextView) promptView.findViewById(R.id.tv_title_itemcode);
        final TextView qty = (TextView) promptView.findViewById(R.id.tv_qty);
        final TextView btnClose = (TextView) promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = (Button) promptView.findViewById(R.id.btn_confirm);
        final Button btnCancel = (Button) promptView.findViewById(R.id.btn_cancelDetail);

        btnCancel.setVisibility(View.GONE);
        tvLocator.setText(mSPMDetailDataList.get(position).getTxtLocator());
        tvItemCode.setText(mSPMDetailDataList.get(position).getTxtItemCode());
        tvItemName.setText(mSPMDetailDataList.get(position).getTxtItemName());
        qty.setText(mSPMDetailDataList.get(position).getIntQty());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnConfirm.setText("CONFIRMED");
        btnConfirm.setEnabled(false);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
            }
        });
    }

    private void viewTaskDetail(final Activity ctx, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final LinearLayout ll_background = (LinearLayout) promptView.findViewById(R.id.ll_background);
        final TextView tvLocator = (TextView) promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = (TextView) promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = (TextView) promptView.findViewById(R.id.tv_itemname);
        final TextView btnClose = (TextView) promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = (Button) promptView.findViewById(R.id.btn_confirm);
        final Button btnCancel = (Button) promptView.findViewById(R.id.btn_cancelDetail);
        final TextView qty = (TextView) promptView.findViewById(R.id.tv_qty);
        final TextView tvLotNumber = (TextView) promptView.findViewById(R.id.tv_lot_number);

//        ll_background.setBackgroundResource(R.color.color_primary_red);
//        ll_background.setAlpha(1);
        tvLocator.setText(mSPMDetailDataList.get(position).getTxtLocator());
        tvItemCode.setText(mSPMDetailDataList.get(position).getTxtItemCode());
        tvItemName.setText(mSPMDetailDataList.get(position).getTxtItemName());
        qty.setText(mSPMDetailDataList.get(position).getIntQty() + " " + mSPMDetailDataList.get(position).getTxtUOM());
        tvLotNumber.setText(mSPMDetailDataList.get(position).getTxtLotNumber());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure to Confirm?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
                        String txtNoSPM = mSPMDetailDataList.get(position).getTxtNoSPM();
                        tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                        String _intUserId = dataLogin.getIntUserId();
                        dataLogin.getIntUserId();
                        boolean status = false;
                        pInfo = new clsMainActivity().getPinfo(ctx);
                        if(pInfo!=null){
                            versionName = pInfo.versionName;
                        }
                        if(triggerProgressDialog!=null){
                            addon.SwipeListAdapter.triggerProgressDialog.showProgressDialog(true);
                        }
                        status = new WMSMobileService().confirmSPMDetail(_intSPMDetailId, _intUserId, versionName);
                        if (!status) {
                            JSONObject jsonObject = new JSONObject();
                            String method = "confirmDetail";
                            String message = "Success";
                            try {
                                jsonObject.put("_intSPMDetailId", _intSPMDetailId);
                                jsonObject.put("_intUserId", _intUserId);
                                jsonObject.put("txtNoSPM", txtNoSPM);
                                jsonObject.put("strMethodName", method);
                                jsonObject.put("message", message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            new mSPMDetailBL().updateDataValueById(_intSPMDetailId, _intUserId);
                            new TabsTaskHeader().onNetworkConnectionChanged(true);
                            if(triggerOnOfflineConnection!=null){
                                addon.SwipeListAdapter.triggerOnOfflineConnection.onOfflineConnection(jsonObject);
                            }
                        }

//                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
//                        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending();
//
//                        if (_mSPMDetailData.size() == 0) {
//                            new TabsTaskHeader().switchTab();
//                        }
                        alertD.dismiss();
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
                showPopupReasonCancel(ctx, position);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
            }
        });

////show the dialog first
//        final AlertDialog dialog = new AlertDialog.Builder(ctx)
//                .setCancelable(false)
//                .setView(promptView)
//                .show();
////Grab the window of the dialog, and change the width
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = dialog.getWindow();
//        lp.copyFrom(window.getAttributes());
////This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.gravity = Gravity.CENTER;
//        window.setAttributes(lp);
//
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
//                alertDialog.setTitle("Confirm");
//                alertDialog.setMessage("Are you sure to Confirm?");
//                alertDialog.setCancelable(false);
//                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogs, int which) {
//                        String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
//                        tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
//                        String _intUserId = dataLogin.getIntUserId();
//                        dataLogin.getIntUserId();
//                        boolean status = false;
//                        TabsTaskHeader taskHeader = new TabsTaskHeader();
//                        status = new WMSMobileService().confirmSPMDetail(_intSPMDetailId, _intUserId);
//                        if (!status) {
//                            new mSPMDetailBL().updateDataValueById(_intSPMDetailId, _intUserId);
//                            new TabsTaskHeader().onNetworkConnectionChanged(true);
//                        }
//
////                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
////                        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending();
////
////                        if (_mSPMDetailData.size() == 0) {
////                            new TabsTaskHeader().switchTab();
////                        }
//                        dialogs.dismiss();
//                        dialog.dismiss();
//                    }
//                });
//                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alertDialog.show();
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                showPopupReasonCancel(ctx, position);
//            }
//        });
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });


    }

    private void showPopupReasonCancel(final Activity activity, final int position) {

        pInfo = new clsMainActivity().getPinfo(activity);
        if(pInfo!=null){
            versionName = pInfo.versionName;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        final View promptView = layoutInflater.inflate(R.layout.activity_popup_cancel, null);

        final EditText et_reason = (EditText) promptView.findViewById(R.id.et_reason);
        final TextView btnClose = (TextView) promptView.findViewById(R.id.btn_closeCancel);
        final Button btnConfirm = (Button) promptView.findViewById(R.id.btn_cancelconfirm2);
        final Button btnCancel = (Button) promptView.findViewById(R.id.btn_cancelDetail2);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure to Confirm?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reason = et_reason.getText().toString();
                        if (reason.length() > 0) {
                            String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
                            String txtNoSPM = mSPMDetailDataList.get(position).getTxtNoSPM();
                            tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                            String _intUserId = dataLogin.getIntUserId();
                            dataLogin.getIntUserId();
                            boolean status = false;
                            status = new WMSMobileService().cancelSPMDetail(_intSPMDetailId, _intUserId, reason, versionName);
                            if (!status) {
//                                new mSPMDetailBL().updateDataSPMCancelById(_intSPMDetailId, _intUserId, reason);
                                new TabsTaskHeader().onNetworkConnectionChanged(true);

                                JSONObject jsonObject = new JSONObject();
                                String method = "cancelDetail";
                                String message = "Success";
                                try {
                                    jsonObject.put("_intSPMDetailId", _intSPMDetailId);
                                    jsonObject.put("_intUserId", _intUserId);
                                    jsonObject.put("txtNoSPM", txtNoSPM);
                                    jsonObject.put("reason", reason);
                                    jsonObject.put("strMethodName", method);
                                    jsonObject.put("message", message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                            new mSPMDetailBL().updateDataValueById(_intSPMDetailId, _intUserId);
                                new TabsTaskHeader().onNetworkConnectionChanged(true);
                                if(triggerOnOfflineConnection!=null){
                                    addon.SwipeListAdapter.triggerOnOfflineConnection.onOfflineConnection(jsonObject);
                                }
                            }

//                            List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
//                            _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending();
//
//                            if (_mSPMDetailData.size() == 0) {
//                                new TabsTaskHeader().switchTab();
//                            }
                            alertD.dismiss();
                        } else {
                            new clsMainActivity().showCustomToast(activity, "Reason cannot empty..", false);
                        }

                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                alertDialog.show();
            }
        });
    }
    public interface triggerOnOfflineConnection {
        void onOfflineConnection(JSONObject jsonObject);
    }
    public interface triggerProgressDialog{
        void showProgressDialog(boolean valid);
    }

}