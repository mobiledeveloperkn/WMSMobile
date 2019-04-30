package addon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private PackageInfo pInfo = null;
    private String versionName = "";

    public static triggerOnOfflineConnection triggerOnOfflineConnection;
    public static triggerProgressDialog triggerProgressDialog;

    public SwipeListAdapter(Activity activity, List<mSPMDetailData> mSPMDetailDataList) {
        this.activity = activity;
        this.mSPMDetailDataList = mSPMDetailDataList;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.list_row, null);
            }

        RelativeLayout rl_row = null;
        if (convertView != null) {
            rl_row = convertView.findViewById(R.id.rl_lis_row);
        }
        TextView title = null;
        if (convertView != null) {
            title = convertView.findViewById(R.id.title);
        }
        TextView desc = null;
        if (convertView != null) {
            desc = convertView.findViewById(R.id.desc);
        }
        ImageView iv_status = null;
        if (convertView != null) {
            iv_status = convertView.findViewById(R.id.imageViewStatus);
        }

        if (title != null) {
            title.setText(String.format("Locator %s", mSPMDetailDataList.get(position).getTxtLocator()));
        }
        if (desc != null) {
            desc.setText(String.format("Item Code %s\nQty %s", mSPMDetailDataList.get(position).getTxtItemCode(), mSPMDetailDataList.get(position).getIntQty()));
        }

        if (mSPMDetailDataList.get(position).getBitStatus().equals("0") && mSPMDetailDataList.get(position).getBitSync().equals("0")) {
            if (iv_status != null) {
                iv_status.setVisibility(View.INVISIBLE);
            }
//            if (position == 0) {
            if (convertView != null) {
                convertView.setBackgroundResource(R.color.white);
            }
            if (rl_row != null) {
                rl_row.setOnClickListener(view -> viewTaskDetail(activity, position));
            }

        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("1") && mSPMDetailDataList.get(position).getBitSync().equals("1")) {
            if (iv_status != null) {
                iv_status.setVisibility(View.VISIBLE);
            }
            if (rl_row != null) {
                rl_row.setOnClickListener(view -> viewPopupSuccess(activity, position));
            }
        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("1") && mSPMDetailDataList.get(position).getBitSync().equals("0")) {
            if (iv_status != null) {
                iv_status.setVisibility(View.INVISIBLE);
            }
            if (rl_row != null) {
                rl_row.setOnClickListener(view -> viewPopupSuccess(activity, position));
            }
        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("2") && mSPMDetailDataList.get(position).getBitSync().equals("1")) {
            if (iv_status != null) {
                iv_status.setVisibility(View.VISIBLE);
            }
            if (rl_row != null) {
                rl_row.setOnClickListener(view -> viewPopupCancel(activity, position));
            }
        } else if (mSPMDetailDataList.get(position).getBitStatus().equals("2") && mSPMDetailDataList.get(position).getBitSync().equals("0")) {
            if (iv_status != null) {
                iv_status.setVisibility(View.INVISIBLE);
            }
            if (rl_row != null) {
                rl_row.setOnClickListener(view -> viewPopupCancel(activity, position));
            }
        }
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    private void viewPopupCancel(final Activity activity, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        @SuppressLint("InflateParams") final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = promptView.findViewById(R.id.tv_itemname);
        final TextView btnClose = promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = promptView.findViewById(R.id.btn_confirm);
        final Button btnCancel = promptView.findViewById(R.id.btn_cancelDetail);
        final TextView qty = promptView.findViewById(R.id.tv_qty);

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

        btnConfirm.setOnClickListener(view -> {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Revert");
            alertDialog.setMessage("Are you sure to revert this item?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", (dialog, which) -> {
                String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
                String txtNoSPM = mSPMDetailDataList.get(position).getTxtNoSPM();
                tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                String _intUserId = dataLogin.getIntUserId();
                boolean status;
                pInfo = new clsMainActivity().getPinfo(activity);
                if (pInfo != null) {
                    versionName = pInfo.versionName;
                }
                if (triggerProgressDialog != null) {
                    SwipeListAdapter.triggerProgressDialog.showProgressDialog(true);
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
                    if (triggerOnOfflineConnection != null) {
                        SwipeListAdapter.triggerOnOfflineConnection.onOfflineConnection(jsonObject);
                    }
                }

//                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
//                        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending();
//
//                        if (_mSPMDetailData.size() == 0) {
//                            new TabsTaskHeader().switchTab();
//                        }
                alertD.dismiss();
            });
            alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        btnClose.setOnClickListener(view -> alertD.dismiss());
    }

    @SuppressLint("SetTextI18n")
    private void viewPopupSuccess(final Activity activity, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        @SuppressLint("InflateParams") final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = promptView.findViewById(R.id.tv_itemname);
        final TextView qty = promptView.findViewById(R.id.tv_qty);
        final TextView btnClose = promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = promptView.findViewById(R.id.btn_confirm);
        final Button btnCancel = promptView.findViewById(R.id.btn_cancelDetail);

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

        btnClose.setOnClickListener(view -> alertD.dismiss());
    }

    private void viewTaskDetail(final Activity ctx, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        @SuppressLint("InflateParams") final View promptView = layoutInflater.inflate(R.layout.activity_task_detail, null);

        final TextView tvLocator = promptView.findViewById(R.id.tv_locator);
        final TextView tvItemCode = promptView.findViewById(R.id.tv_itemcode);
        final TextView tvItemName = promptView.findViewById(R.id.tv_itemname);
        final TextView btnClose = promptView.findViewById(R.id.btn_close);
        final Button btnConfirm = promptView.findViewById(R.id.btn_confirm);
        final Button btnCancel = promptView.findViewById(R.id.btn_cancelDetail);
        final TextView qty = promptView.findViewById(R.id.tv_qty);
        final TextView tvLotNumber = promptView.findViewById(R.id.tv_lot_number);

//        ll_background.setBackgroundResource(R.color.color_primary_red);
//        ll_background.setAlpha(1);
        tvLocator.setText(mSPMDetailDataList.get(position).getTxtLocator());
        tvItemCode.setText(mSPMDetailDataList.get(position).getTxtItemCode());
        tvItemName.setText(mSPMDetailDataList.get(position).getTxtItemName());
        qty.setText(String.format("%s %s", mSPMDetailDataList.get(position).getIntQty(), mSPMDetailDataList.get(position).getTxtUOM()));
        tvLotNumber.setText(mSPMDetailDataList.get(position).getTxtLotNumber());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();

        btnConfirm.setOnClickListener(view -> {

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            alertDialog.setTitle("Confirm");
            alertDialog.setMessage("Are you sure to Confirm?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", (dialog, which) -> {
                String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
                String txtNoSPM = mSPMDetailDataList.get(position).getTxtNoSPM();
                tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                String _intUserId = dataLogin.getIntUserId();
                boolean status;
                pInfo = new clsMainActivity().getPinfo(ctx);
                if (pInfo != null) {
                    versionName = pInfo.versionName;
                }
                if (triggerProgressDialog != null) {
                    SwipeListAdapter.triggerProgressDialog.showProgressDialog(true);
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
                    if (triggerOnOfflineConnection != null) {
                        SwipeListAdapter.triggerOnOfflineConnection.onOfflineConnection(jsonObject);
                    }
                }

//                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
//                        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending();
//
//                        if (_mSPMDetailData.size() == 0) {
//                            new TabsTaskHeader().switchTab();
//                        }
                alertD.dismiss();
            });
            alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        btnCancel.setOnClickListener(view -> {
            alertD.dismiss();
            showPopupReasonCancel(ctx, position);
        });

        btnClose.setOnClickListener(view -> alertD.dismiss());

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
        if (pInfo != null) {
            versionName = pInfo.versionName;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        @SuppressLint("InflateParams") final View promptView = layoutInflater.inflate(R.layout.activity_popup_cancel, null);

        final EditText et_reason = promptView.findViewById(R.id.et_reason);
        final TextView btnClose = promptView.findViewById(R.id.btn_closeCancel);
        final Button btnConfirm = promptView.findViewById(R.id.btn_cancelconfirm2);
        final Button btnCancel = promptView.findViewById(R.id.btn_cancelDetail2);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnClose.setOnClickListener(view -> alertD.dismiss());

        btnCancel.setOnClickListener(view -> alertD.dismiss());

        btnConfirm.setOnClickListener(view -> {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Confirm");
            alertDialog.setMessage("Are you sure to Confirm?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", (dialog, which) -> {
                String reason = et_reason.getText().toString();
                if (reason.length() > 0) {
                    String _intSPMDetailId = mSPMDetailDataList.get(position).getIntSPMDetailId();
                    String txtNoSPM = mSPMDetailDataList.get(position).getTxtNoSPM();
                    tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                    String _intUserId = dataLogin.getIntUserId();
                    boolean status;
                    status = new WMSMobileService().cancelSPMDetail(_intSPMDetailId, _intUserId, reason, versionName);
                    if (!status) {
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
                        if (triggerOnOfflineConnection != null) {
                            SwipeListAdapter.triggerOnOfflineConnection.onOfflineConnection(jsonObject);
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

            });
            alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

            alertDialog.show();
        });
    }

    public interface triggerOnOfflineConnection {
        void onOfflineConnection(JSONObject jsonObject);
    }

    public interface triggerProgressDialog {
        void showProgressDialog(boolean valid);
    }

}