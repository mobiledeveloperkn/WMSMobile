package wms.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import bl.SignalRBL;
import bl.clsHelperBL;
import bl.clsMainBL;
import bl.tDeviceInfoUserBL;
import bl.tTimerLogBL;
import library.common.clsPushData;
import library.common.tDeviceInfoUserData;
import library.common.tTimerLogData;
import service.WMSMobileService;

//import com.kalbe.bl.mCounterNumberBL;

public class clsMainActivity extends Activity implements WMSMobileService.updateSnackbar{
    private static final String TAG_UUID = "id";
    private static Snackbar snackbarOffline;
    String months[] = {"", "January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    public void onClickHome(View v) {
//	    goHome (this);
    }

    public void setSnackBarClose(WMSMobileService.updateSnackbar snackBarClose) {
        WMSMobileService.updateSnackbar = snackBarClose;
    }

    public void saveTimerLog(String txtTimerStatus, String txtTimerType, String txtStarNo){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dtExecute = dateFormat.format(cal.getTime());

        tTimerLogData dtTimer = new tTimerLogData();
        dtTimer.setBitActive("1");
        dtTimer.setTxtTimerLogId(new clsMainBL().GenerateGuid());
        dtTimer.setTxtStarNo(txtStarNo);
        dtTimer.setTxtTimerStatus(txtTimerStatus);
        dtTimer.setTxtTimerType(txtTimerType);
        dtTimer.setDtExecute(dtExecute);
        new tTimerLogBL().insertData(dtTimer);
    }

    public String getTxtSTatus(String intSubmit, String intSync) {
        String txtStatus = "";
        if (intSubmit.equals("1") && intSync.equals("1")) {
            txtStatus = "Sync";
        } else if (intSubmit.equals("1") && intSync.equals("0")) {
            txtStatus = "Submit";
        } else if (intSubmit.equals("0") && intSync.equals("0")) {
            txtStatus = "Open";
        }
        return txtStatus;
    }

    public static void showToastStatic(Context ctx, String str) {
        Toast toast = Toast.makeText(ctx, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

//	public void goHome(Context context) {
//		finish();
//	    final Intent intent = new Intent(context, Home.class);
//	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    context.startActivity (intent);
//	}

    public String convertNumberDec(double dec) {
        double harga = dec;
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        String hsl = df.format(harga);

        return hsl;
    }

    public String splitDateTime(String dateTime) {
        String dateTimeView = "";
        String datetimeSplit[] = dateTime.split("T");
        String date[] = datetimeSplit[0].split("-");
        String year = date[0];
        String month = date[1];
        String day = date[2];
        String date_view = day + "-" + month + "-" + year;
        String timeSplit[] = datetimeSplit[1].split("\\.");
        String time = timeSplit[0];
        dateTimeView = date_view + " " + time;
        return dateTimeView;
    }

    public String convertNumberInt(int intNilai) {
        int value = intNilai;
        DecimalFormat myFormatter = new DecimalFormat("#,###");
        String output = myFormatter.format(value);

        return output;
    }

    public String convertcurrencyidn(double value) {

        return convertcurrencyidn(value);
    }

    @SuppressLint("NewApi")
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static <T, E> int getSpinnerPositionByValue(Map<T, E> map, E value, Spinner spn) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                int spinnerPosition = 0;
                String myString = (String) entry.getKey();
                ArrayAdapter myAdap = (ArrayAdapter) spn.getAdapter();
                spinnerPosition = myAdap.getPosition(myString);

                return spinnerPosition;
            }
        }
        return 0;
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
//	public OnClickListener listener(final Context _ctx, final String MenuName){
//		OnClickListener listener = new OnClickListener() {
//			@SuppressLint("NewApi")
//			@Override
//			public void onClick(View v) {
//				Context wrapper = wrapper=new ContextThemeWrapper(_ctx, R.style.PopupMenu);
//			    PopupMenu popup = new PopupMenu(wrapper, v);
//			    final mMenuData _dataMenu=new mMenuBL().getMenuDataByMenuName(MenuName);
//				if(MenuName.contains("mnPurchaseOrderKBN")){
//					popup.getMenuInflater().inflate(R.menu.popup_menu_and_menu_online, popup.getMenu());
//				}else if(MenuName.contains("InventoryInAddData")){
//					popup.getMenuInflater().inflate(R.menu.add_data_grn, popup.getMenu());
//				}else{
//					popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
//				}
//				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//					@Override
//					public boolean onMenuItemClick(MenuItem item) {
//						if(MenuName.contains("mnInventoryKBN")){
//							new InventoryInHeader().doStuff(_ctx, item.getTitle().toString());
//						}else if(MenuName.contains("mnStockopname")){
//							new OpnameHeader().doStuff(_ctx, item.getTitle().toString());
//						}else if(MenuName.contains("mnInventoryOut")){
//							new InventoryOutHeader().doStuff(_ctx, item.getTitle().toString());
//						}else if(MenuName.contains("mnPengeluaranKBN")){
//							new PengeluaranHeader().doStuff(_ctx, item.getTitle().toString());
//						}else if(MenuName.contains("mnPurchaseOrderKBN")){
//							new PurchaseOrderHeader().doStuff(_ctx, item.getTitle().toString());
//						}else if(MenuName.contains("InventoryInAddData")){
//							new InventoryIn().doStuff(_ctx, item.getTitle().toString());
//						}
//						return true;
//					}
//				});
//
//				popup.show();
//			}
//		};
//		return listener;
//	}


    public void doStuff(Context _ctx, String Item) {
        Toast.makeText(_ctx, Item, Toast.LENGTH_SHORT).show();
    }

    public int getStringResourceByName(String aString, String packageName) {
        int resId = getResources().getIdentifier(aString, "drawable", packageName);
        return resId;
    }

    public void showToast(Context ctx, String str) {
        Toast toast = Toast.makeText(ctx,
                str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    public String getOutletCode(Intent _intent) {
        String OutletCode = "";
        if (_intent.hasExtra("OutletCode")) {
            OutletCode = (String) _intent.getSerializableExtra("OutletCode");
        }
        return OutletCode;
    }

    public String getBranchCode(Intent _intent) {
        String BranchCode = "";
        if (_intent.hasExtra("BranchCode")) {
            BranchCode = (String) _intent.getSerializableExtra("BranchCode");
        }
        return BranchCode;
    }

    public String getMenuID(Intent _intent) {
        String MenuID = "";
        if (_intent.hasExtra("MenuID")) {
            MenuID = (String) _intent.getSerializableExtra("MenuID");
        }
        return MenuID;
    }

    public String getStatusID(Intent _intent) {
        String MenuID = "";
        if (_intent.hasExtra("Status")) {
            MenuID = (String) _intent.getSerializableExtra("Status");
        }
        return MenuID;
    }

    public PackageInfo getPinfo(Activity activity) {
        PackageInfo pInfo = null;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        } catch (NameNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        return pInfo;
    }

    public tDeviceInfoUserData getDataDeviceActive() {
        List<tDeviceInfoUserData> dt = new tDeviceInfoUserBL().getData(1);
        if (dt.size() == 0) {
            return null;
        } else {
            return dt.get(0);
        }
    }

    public double qtySumAmount(double price, double item) {
        double total;
        total = price * item;

        return total;
    }

//    public void GenerateNewId(String txtId, enumCounterData dtCounter) {
//
//        clsHelper _clsHelper = new clsHelper();
//        String NewId = _clsHelper.generateNewId(txtId, "-", "6");
//        mCounterNumberBL _mCounterNumberBL = new mCounterNumberBL();
//        mCounterNumberData dtmCounterNumberData = _mCounterNumberBL.getDataByenumCounterData(dtCounter);
//        dtmCounterNumberData.set_txtValue(NewId);
//        _mCounterNumberBL.SaveData(dtmCounterNumberData);
//    }

//    public tUserLoginData getDataLoginActive() {
//        tUserLoginData dt = new tUserLoginBL().getUserActive();
//        return dt;
//    }

    public static String right(String value, int length) {
        // To get right characters from a string, change the begin index.
        return value.substring(value.length() - length);
    }

    public String giveFormatDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public String FormatDateDB() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public String giveFormatDateTime(String dateYYMMDD) {

        String date = dateYYMMDD;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date newdate = null;
        try {
            newdate = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = "";
        formattedDate = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss", Locale.getDefault()).format(newdate);
        //System.out.println(newdate); // Wed Mar 09 03:02:10 BOT 2011

        //String txtDate = dateFormat.format(dateYYMMDD);

        return formattedDate;
    }

    public String giveFormatDate(String DateYYMMDD) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat formatYY = new SimpleDateFormat("yyyy");
        DateFormat formatMM = new SimpleDateFormat("MM");
        DateFormat formatDD = new SimpleDateFormat("dd");
        String txtDate = "";
        try {
            Date dtdate = (Date) dateFormat.parse(DateYYMMDD);
            int year = Integer.valueOf(formatYY.format(dtdate));
            int month = Integer.valueOf(formatMM.format(dtdate));
            int day = Integer.valueOf(formatDD.format(dtdate));
            txtDate = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        } catch (ParseException e) {
            txtDate = DateYYMMDD;
        }

        return txtDate;
    }

    public String giveFormatDate2(String DateYYMMDD) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatYY = new SimpleDateFormat("yyyy");
        DateFormat formatMM = new SimpleDateFormat("MM");
        DateFormat formatDD = new SimpleDateFormat("dd");
        String txtDate = "";
        try {
            Date dtdate = (Date) dateFormat.parse(DateYYMMDD);
            int year = Integer.valueOf(formatYY.format(dtdate));
            int month = Integer.valueOf(formatMM.format(dtdate));
            int day = Integer.valueOf(formatDD.format(dtdate));
            txtDate = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        } catch (ParseException e) {
            txtDate = DateYYMMDD;
        }

        return txtDate;
    }

    public String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM, yyyy");
        return sdf.format(cal.getTime());
    }

    public String DisplayDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(cal.getTime());
    }

    public String FormatDateComplete() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
        return sdf.format(cal.getTime());
    }

    public String GenerateGuid() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }

    public int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
//	 public void setTitleForm(String txtTitle){
//		 TextView tvTitle=(TextView) findViewById(R.id.tvTitle);
//		 tvTitle.setText(txtTitle);
//	 }
//	 public void setTitleGreeting(String txtTitle){
//		 TextView tvTitle=(TextView) findViewById(R.id.tvTittleGreeting);
//		 tvTitle.setText("HI, " + txtTitle);
//	 }

    //	 public void setConfirmForm(View _acty,String txtTitle,String txtDesc){
//		 TextView tvTitle=(TextView) _acty.findViewById(R.id.tvTitle);
//		 TextView tvDesc=(TextView) _acty.findViewById(R.id.tvDesc);
//		 tvTitle.setText(txtTitle);
//		 tvDesc.setText(txtDesc);
//	 }
    protected void onRestoreInstanceState1(Bundle savedInstanceState1) {
        // TODO Auto-generated method stub

    }


//    public static AppAdapter setList(Context _ctx, final List<clsSwipeList> swipeList) {
//        final AppAdapter mAdapter;
//        PullToRefreshSwipeMenuListView mListView;
//        Handler mHandler;
//
//        List<String> mAppList = new ArrayList<String>();
//
//        for (int i = 0; i < swipeList.size(); i++) {
//            clsSwipeList getswipeList = swipeList.get(i);
//            mAppList.add(getswipeList.get_txtTitle() + "\n" + getswipeList.get_txtDescription());
//        }
//
//        mAdapter = new AppAdapter(_ctx, mAppList);
//
//        return mAdapter;
//
//    }

//    public static SwipeMenuCreator setCreator(final Context _ctx, final Map<String, HashMap> map) {
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//
//                HashMap<String, String> map2 = new HashMap<String, String>();
//
//                for (int i = 0; i < map.size(); i++) {
//                    map2 = map.get(String.valueOf(i));
//
//                    // create "open" item
//                    SwipeMenuItem menuItem = new SwipeMenuItem(_ctx);
//                    // set item background
//                    menuItem.setBackground(new ColorDrawable(Color.parseColor(map2.get("bgColor"))));
//                    // set item width
//                    menuItem.setWidth(dp2px(_ctx, 90));
//                    // set item title
//
//                    if (map2.get("name") == "View") {
//                        int icon = R.drawable.ic_view;
//                        menuItem.setIcon(icon);
//                    } else if (map2.get("name") == "Edit") {
//                        int icon = R.drawable.ic_edit;
//                        menuItem.setIcon(icon);
//                    } else if (map2.get("name") == "Delete") {
//                        int icon = R.drawable.ic_delete;
//                        menuItem.setIcon(icon);
//                    }
//                    // add to menu
//                    menu.addMenuItem(menuItem);
//                }
//                // create "delete" item
//                // SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
//                // set item background
//                // deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
//                // set item width
//                // deleteItem.setWidth(dp2px(90));
//                // set a icon
//                // deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                // menu.addMenuItem(deleteItem);
//            }
//        };
//
//        return creator;
//
//    }

    private static int dp2px(Context _ctx, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, _ctx.getResources().getDisplayMetrics());
    }

    public OnItemLongClickListener longListener() {
        OnItemLongClickListener listener = new OnItemLongClickListener() {
            @SuppressLint("NewApi")

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        return listener;
    }

//    public static edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener menuSwipeListener(final Context _ctx, final String action, final Map<String, HashMap> mapMenu, final List<clsSwipeList> swipeList) {
//        edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener listener = new edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                HashMap<String, String> selectedMenu = mapMenu.get(String.valueOf(index));
//
//                clsSwipeList getswipeList = swipeList.get(position);
//                String id = getswipeList.get_txtId();
//            }
//
//        };
//        return listener;
//    }

//    public OnSwipeListener swipeListener() {
//        OnSwipeListener listener = new OnSwipeListener() {
//
//            @Override
//            public void onSwipeStart(int position) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onSwipeEnd(int position) {
//                // TODO Auto-generated method stub
//
//            }
//        };
//        return listener;
//
//    }

//    public static AppAdapterNotif setListA(Context context, List<clsRowItem> items) {
//        final AppAdapterNotif mAdapter;
//        PullToRefreshSwipeMenuListView mListView;
//        Handler mHandler;
//
//        List<clsRowItem> mAppList = new ArrayList<clsRowItem>();
//
//        if (items != null) {
//            for (int i = 0; i < items.size(); i++) {
//                clsRowItem getswipeList = items.get(i);
//                mAppList.add(i, getswipeList);
//            }
//        }
//        mAdapter = new AppAdapterNotif(context, items);
//
//        return mAdapter;
//    }


//    public edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener mmenuSwipeListener(final Context _ctx, final String action, final Map<String, HashMap> mapMenu, final List<clsRowItem> swipeList) {
//        edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener listener = new edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                HashMap<String, String> selectedMenu = mapMenu.get(String.valueOf(index));
//
//                clsRowItem getswipeList = swipeList.get(position);
//                if (action == "LNotifi") {
//                    String uuid = getswipeList.get_txtId();
////                    Intent intent = new Intent(getApplicationContext(), FragmentNotifcation.class);
////                    intent.putExtra("From", "Notif");
////                    intent.putExtra(TAG_UUID, String.valueOf(uuid));
////                    startActivity(intent);
//                }
//            }
//
//        };
//        return listener;
//    }

    public void setHeaderFull() {
        ImageView imgV = (ImageView) findViewById(R.id.header);
        imgV.setAdjustViewBounds(true);
        imgV.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public PackageInfo getpInfo(){
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        return pInfo;
    }


    public void showCustomToast(Context context, String message, Boolean status) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View promptView = mInflater.inflate(R.layout.custom_toast, null);

        TextView tvTextToast = (TextView) promptView.findViewById(R.id.custom_toast_message);
        ImageView icon = (ImageView) promptView.findViewById(R.id.custom_toast_image);
        tvTextToast.setText(message);

        GradientDrawable bgShape = (GradientDrawable)promptView.getBackground();

        if (status) {
            bgShape.setColor(Color.parseColor("#6dc066"));
            icon.setImageResource(R.drawable.ic_checklist);

        } else {
            bgShape.setColor(Color.parseColor("#e74c3c"));
            icon.setImageResource(R.drawable.ic_error);
        }

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(promptView);
        toast.show();
    }

    public void showSnackbar(CoordinatorLayout coordinatorLayout, String message, Boolean status, final String action) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        if(status){
            snackBarView.setBackgroundResource(R.color.color_primary_blue);
        } else {
            snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
            snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.red);
        }
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void snackbarWithActionRetry(final CoordinatorLayout coordinatorLayout, String message, Boolean status, final String action){
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(action.equalsIgnoreCase("RETRY")){
//                            boolean status = new SignalRBL().buildingConnection();
//                            if(status){
//                                snackbarWithNoAction(coordinatorLayout,"Connected", status, "");
//                            } else {
//                                snackbarWithActionRetry(coordinatorLayout,"Connecting Failed", status, "RETRY");
//                            }
                            AsyncTestConnection task = new AsyncTestConnection(coordinatorLayout);
                            task.execute();
                        }
                    }
                });
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.red);
        snackbar.setActionTextColor(Color.WHITE);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                snackbar.getView().getViewTreeObserver().removeOnPreDrawListener(this);
                ((CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams()).setBehavior(null);
                return true;
            }
        });
        snackbar.show();
    }

    public void snackbarWithNoAction(CoordinatorLayout coordinatorLayout, String message, Boolean status, final String action){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        if(status){
            snackBarView.setBackgroundResource(R.color.color_primary_blue);
        } else {
            snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
            snackBarView = snackbar.getView();
            snackBarView.setBackgroundResource(R.color.red);
            final Snackbar finalSnackbar = snackbar;
            snackbar.getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    finalSnackbar.getView().getViewTreeObserver().removeOnPreDrawListener(this);
                    ((CoordinatorLayout.LayoutParams) finalSnackbar.getView().getLayoutParams()).setBehavior(null);
                    return true;
                }
            });
        }
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }

    public void snackbarProgress(CoordinatorLayout coordinatorLayout, String message, Boolean status, final String action){
        Snackbar bar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) bar.getView();
        snack_view.addView(new ProgressBar(getApplicationContext()));
        bar.show();

    }

    public void checkConnection(CoordinatorLayout coordinatorLayout, ConnectivityManager connectivityManager){

        boolean status = false;
        //mobile
        NetworkInfo.State mobile = connectivityManager.getNetworkInfo(0).getState();
        //wifi
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(1).getState();

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
        {
//            snackbarWithNoAction(coordinatorLayout, "connecting...", status, "");
            AsyncTestConnection task = new AsyncTestConnection(coordinatorLayout);
            task.execute();
//            status = new SignalRBL().buildingConnection();
//            if(!status){
//                snackbarWithActionRetry(coordinatorLayout,"Connecting Failed", status, "RETRY");
//            } else {
//                snackbarWithNoAction(coordinatorLayout,"Connected", status, "");
//            }
        }
        else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
        {
//            snackbarWithNoAction(coordinatorLayout, "connecting...", status, "");
            AsyncTestConnection task = new AsyncTestConnection(coordinatorLayout);
            task.execute();
//            status = new SignalRBL().buildingConnection();
//            if(!status){
//                snackbarWithActionRetry(coordinatorLayout,"Connecting Failed", status, "RETRY");
//            } else {
//                snackbarProgress(coordinatorLayout,"Connected", status, "");
//            }
        }
        else {
            snackbarWithNoAction(coordinatorLayout,"No Connectivity", status, "");
        }
    }

    @Override
    public void onUpdateSnackBar(boolean info) {
        if(info){
            snackbarOffline.dismiss();
        }
    }

    private class AsyncTestConnection extends AsyncTask<Boolean, Void, Boolean> {


        public AsyncTestConnection(CoordinatorLayout coordinatorLayout) {
            super();
            coordinatorLayoutTask = coordinatorLayout;
            snackbar = Snackbar.make(coordinatorLayoutTask, "Connecting...", Snackbar.LENGTH_INDEFINITE);
            snackBarView = snackbar.getView();
        }

        private CoordinatorLayout coordinatorLayoutTask;
        private Snackbar snackbar;
        private View snackBarView;

        @Override
        protected void onCancelled() {
            snackbar.dismiss();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            clsPushData dtJson = new clsHelperBL().pushData();
            if(status){
                snackbar.dismiss();
                snackbarWithNoAction(coordinatorLayoutTask,"Connected", status, "");
                String versionName = "";
                if(dtJson.getDtdataJson()!=null){
                    try {
                        snackbarOffline = Snackbar.make(coordinatorLayoutTask, "Sync Data...", Snackbar.LENGTH_INDEFINITE);
                        View snackBarView = snackbarOffline.getView();
                        snackBarView.setBackgroundResource(R.color.color_primary_blue);
                        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        final Snackbar finalSnackbar = snackbarOffline;
                        snackbarOffline.getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                finalSnackbar.getView().getViewTreeObserver().removeOnPreDrawListener(this);
                                ((CoordinatorLayout.LayoutParams) finalSnackbar.getView().getLayoutParams()).setBehavior(null);
                                return true;
                            }
                        });
                        snackbarOffline.show();
                        setSnackBarClose(clsMainActivity.this);
                        status = new WMSMobileService().pushFromOfflineAct(dtJson.getDtdataJson().txtJSON().toString(), versionName);
                        if (status){
                            snackbar.dismiss();
                            snackbarOffline.dismiss();
                            new TabsTaskHeader().updateListView();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            } else {
                snackbar.dismiss();
                snackbarWithActionRetry(coordinatorLayoutTask,"Connecting Failed", status, "RETRY");
            }
        }

        @Override
        protected Boolean doInBackground(Boolean... booleen) {
//            android.os.Debug.waitForDebugger();
            boolean status;
            status = new SignalRBL().buildingConnection();
            return status;
        }

        @Override
        protected void onPreExecute() {
            snackBarView.setBackgroundResource(R.color.red);
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            snackbar.dismiss();
        }
    }

    public boolean isMyServiceRunning(Context mContext) {
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (WMSMobileService.class.getName().equals(
            service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public Bitmap resizeImageForBlob(Bitmap photo){
        int width = photo.getWidth();
        int height = photo.getHeight();

        int maxHeight = 800;
        int maxWidth = 800;

        Bitmap bitmap;

        if(height > width){
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        }
        else if(height < width){
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        }
        else{
            width = maxWidth;
            height = maxHeight;
        }

        bitmap = Bitmap.createScaledBitmap(photo, width, height, true);

        return bitmap;
    }
//    public void zoomImage (Bitmap bitmap, Context context){
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        final View promptView = layoutInflater.inflate(R.layout.custom_zoom_image, null);
//        final TextView tv_desc = (TextView) promptView.findViewById(R.id.desc_act);
//
////        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
////        params.gravity = Gravity.BOTTOM;
////        params.gravity = Gravity.CENTER;
//
//        CustomZoomView customZoomView ;
//        customZoomView = (CustomZoomView)promptView.findViewById(R.id.customImageVIew1);
//        customZoomView.setBitmap(bitmap);
////        customZoomView.setLayoutParams(params);
//
////        tv_desc.setText(description);
//
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(promptView);
//        alertDialogBuilder
//                .setCancelable(false)
//                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        final AlertDialog alertD = alertDialogBuilder.create();
//        alertD.show();
//
//
//
//    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NetworkOnMainThreadException e){
            e.printStackTrace();
            return null;
        }
    }
    public List<String> getArrayMonth(){
        List <String> arrMonths;
        arrMonths = new ArrayList<>();

        arrMonths.add("January");
        arrMonths.add("February");
        arrMonths.add("March");
        arrMonths.add("April");
        arrMonths.add("May");
        arrMonths.add("June");
        arrMonths.add("July");
        arrMonths.add("August");
        arrMonths.add("September");
        arrMonths.add("October");
        arrMonths.add("November");
        arrMonths.add("December");

        return arrMonths;
    }

    public List<String> getArrayYears(){
        List <String> arrYears;
        arrYears = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int i = 5;
        for (i=0;i<5;i++) {
            arrYears.add(String.valueOf(year));
            year--;
        }

        return arrYears;
    }

    public String monthNow(int month){
        List <String> monthNames = getArrayMonth();
            return monthNames.get(month);
    }

    public int getSelectedMoth(List<String> arrMonths){
        Calendar calendar = Calendar.getInstance();
        int monthInt = calendar.get(Calendar.MONTH);
        int indexSelectedMonth = 0;
        String monthString = new clsMainActivity().monthNow(monthInt);

        for (String months : arrMonths) {
            if(String.valueOf(monthString).equalsIgnoreCase(months)){
                indexSelectedMonth = monthInt;
            }
        }

        return indexSelectedMonth;
    }

    public void timerDelayRemoveDialog(long time, final ProgressDialog d){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);
    }

//    public List<String> getArraytUserLoginData(tUserLoginData dt){
//        List<String> arrData;
//        arrData = new ArrayList<>();
//        arrData.add(dt.get_txtEmpId());
//        arrData.add(dt.get_txtUserName());
//        arrData.add(dt.get_txtRoleName());
//        arrData.add(dt.get_txtRegion());
//        arrData.add(dt.get_txtCabang());
//
//        return arrData;
//    }

//    public List<String> getArrayRegion(String region){
//        List<String> arrData;
//        arrData = new ArrayList<>();
//
//        List<mRegionData> _mRegionData = new mRegionBL().GetAllData();
//        tUserLoginData _tUserLoginData = new tUserLoginBL().getUserActive();
//        if(_tUserLoginData.get_txtRoleId().equals("101")){
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("102")){
//            for (mRegionData dat : _mRegionData){
//                arrData.add(dat.get_txtRegionName());
//            }
//        } else if (_tUserLoginData.get_txtRoleId().equals("103")) {
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("100")){
//            for (mRegionData dat : _mRegionData){
//                arrData.add(dat.get_txtRegionName());
//            }
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("106")){
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("104")){
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("105")){
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("116")){
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("117")){
//            arrData.add(region);
//
//        } else if (_tUserLoginData.get_txtRoleId().equals("119")){
//            arrData.add(region);
//
//        } else {
//            arrData.add(region);
//        }
//
//        return arrData;
//    }

//    public List<String> getArrayBranch(){
//        List<String> arrData;
//        arrData = new ArrayList<>();
//
//        List<mBranchData> _mBranchData = new mBranchBL().GetAllData();
//
//        for (mBranchData dat : _mBranchData){
//            arrData.add(dat.get_txtBranchName());
//        }
//
//        return arrData;
//    }
//
//    public List<String> getArrayBranchByRegion(String region){
//        List<String> arrData;
//        arrData = new ArrayList<>();
//
//        List<mBranchData> _mBranchData = new mBranchBL().GetArrayBranchByRegion(region);
//    if(_mBranchData!=null){
//        for (mBranchData dat : _mBranchData){
//            arrData.add(dat.get_txtBranchName());
//        }
//    } else {
//        arrData.add(region);
//    }
//        return arrData;
//    }

}
