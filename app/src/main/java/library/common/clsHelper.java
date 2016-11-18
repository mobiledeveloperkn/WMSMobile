package library.common;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;
import java.util.UUID;

import bl.clsMainBL;
import library.dal.clsHardCode;
import library.dal.mRoleDA;
import library.dal.mSPMDetailDA;
import library.dal.mSPMHeaderDA;
import library.dal.mconfigDA;
import library.dal.tUserLoginDA;

public class clsHelper {
//	public void InitlizeDB(){
//		SQLiteDatabase db;
//		 clsHardCode clsdthc= new clsHardCode();
//		db=SQLiteDatabase.openOrCreateDatabase(clsdthc.txtDatabaseName, null);
//		mconfigDA _mconfigDA=new mconfigDA(db);
//		tUserLoginDA _tUserLoginDA=new tUserLoginDA(db);
//		tDisplayPictureDA _tDisplayPictureDA = new tDisplayPictureDA(db);
//		_mconfigDA.DropTable(db);
//		_tUserLoginDA.DropTable(db);
//		_mconfigDA=new mconfigDA(db);
//		_tUserLoginDA=new tUserLoginDA(db);
//		_tDisplayPictureDA= new tDisplayPictureDA(db);
//		db.close();
//	}
	public String GenerateGuid(){
		 UUID uuid = UUID.randomUUID();
		 String randomUUIDString = uuid.toString();
		 return randomUUIDString;
	 }
//	public String PushDataWithFile(String urlToRead,String DataJson,Integer intTimeOut,HashMap<String,String> ListOfDataFile){
//		String charset = "UTF-8";
//
//        String requestURL = urlToRead;
//        String Result="";
//        clsHelper _clsClsHelper = new clsHelper();
//        try {
//            MultipartUtility multipart = new MultipartUtility(requestURL, charset,intTimeOut);
//
//            //multipart.addHeaderField("User-Agent", "CodeJava");
//            //multipart.addHeaderField("DataHeader", DataJson);
//
//            multipart.addFormField("dataField",DataJson);
//            //multipart.addFormField("keywords", "Java,upload,Spring");
//            for(Entry<String, String> entry : ListOfDataFile.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                multipart.addFilePart(key, new File(value));
//            }
//            List<String> response = multipart.finish();
//            //System.out.println("SERVER REPLIED:");
//
//            for (String line : response) {
//            	Result+=line;
//                System.out.println(line);
//            }
//        } catch (IOException ex) {
//            System.err.println(ex);
//        }
//		return _clsClsHelper.ResultJsonData(Result);
//	}
//	public String PushDataWithFile(String urlToRead,String DataJson,Integer intTimeOut,String File1,String File2){
//		String charset = "UTF-8";
//		File uploadFile1 = null;
//		File uploadFile2 = null;
//		if(File1.contains("file:")){
//			uploadFile1 = new File(File1.substring(7));
//		}
//
//        if(File2.contains("file:")){
//        	uploadFile2 = new File(File2.substring(7));
//        }
//
//        String requestURL = urlToRead;
//        String Result="";
//        clsHelper _clsClsHelper = new clsHelper();
//        try {
//            MultipartUtility multipart = new MultipartUtility(requestURL, charset,intTimeOut);
//
//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("DataHeader", DataJson);
//
//            multipart.addFormField("dataField",DataJson);
//            multipart.addFormField("keywords", "Java,upload,Spring");
//            if(uploadFile1 != null){
//            	if(uploadFile1.exists()){
//                	multipart.addFilePart("fileUpload1", uploadFile1);
//                }
//            }
//            if(uploadFile2 != null){
//            	if(uploadFile2.exists()){
//                	multipart.addFilePart("fileUpload2", uploadFile2);
//                }
//            }
//            List<String> response = multipart.finish();
//
//            System.out.println("SERVER REPLIED:");
//
//            for (String line : response) {
//            	Result+=line;
//                System.out.println(line);
//            }
//        } catch (IOException ex) {
//            System.err.println(ex);
//        }
//		return _clsClsHelper.ResultJsonData(Result);
//	}
	public String pushtData(String urlToRead,String DataJson,Integer intTimeOut) {
		  //notify("asa","asda","asdas");
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      clsHelper _clsClsHelper = new clsHelper();
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setConnectTimeout(intTimeOut);
	         conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
	         conn.setRequestProperty("Accept","*/*");
	         String param=DataJson;
	         conn.setDoOutput(true);
	         conn.setDoInput(true);
	         conn.setRequestMethod("POST");
	         conn.setFixedLengthStreamingMode(param.getBytes().length);
	         conn.setRequestProperty("Content-Type",
	                    "application/x-www-form-urlencoded");
	         conn.setRequestProperty("charset", "utf-8");
	         //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	         PrintWriter out = new PrintWriter(conn.getOutputStream());
	         out.print(param);
	         out.close();
	         String response= "";
	         Scanner inStream = new Scanner(conn.getInputStream());
	         while(inStream.hasNextLine())
	         {
	        	 response+=(inStream.nextLine());
	         }
	         conn.disconnect();
	         result=_clsClsHelper.ResultJsonData(response);
	      } catch (IOException e) {
	    	  result=e.getMessage();
	      } catch (Exception e) {
	    	  result=e.getMessage();
	      }
	      return result;
	   }
	
	public void DeleteAllDB(SQLiteDatabase db){
//		tUserLoginDA _tUserLoginDA=new tUserLoginDA(db);
//		tSalesProductHeaderDA _tSalesProductHeaderDA=new tSalesProductHeaderDA(db);
//		tSalesProductDetailDA _tSalesProductDetailDA=new tSalesProductDetailDA(db);
//		tDeviceInfoUserDA _tDeviceInfoUserDA=new tDeviceInfoUserDA(db);
//		mProductBrandHeaderDA _mProductBrandHeaderDA=new mProductBrandHeaderDA(db);
//		mNotificationDA _mNotificationDA=new mNotificationDA(db);
//		mEmployeeAreaDA _mEmployeeAreaDA=new mEmployeeAreaDA(db);
//		mEmployeeBranchDA _mEmployeeBranchDA=new mEmployeeBranchDA(db);
//		mEmployeeSalesProductDA _mEmployeeSalesProductDA=new mEmployeeSalesProductDA(db);
//		mCounterNumberDA _mCounterNumberDA=new mCounterNumberDA(db);
//		tNotificationDA _tNotificationDA=new tNotificationDA(db);
//		tActivityDA _tActivityDA=new tActivityDA(db);
//		tAbsenUserDA _tAbsenUserDA=new tAbsenUserDA(db);
//		tLeaveMobileDA _tLeaveMobileDA=new tLeaveMobileDA(db);
//		mMenuDA _mMenuDA=new mMenuDA(db);
//		mTypeLeaveMobileDA _mTypeLeaveMobileDA=new mTypeLeaveMobileDA(db);
//		mRoleDA _mUserRoleDA=new mRoleDA(db);
//		mPriceInOutletDA _mPriceInOutletDA=new mPriceInOutletDA(db);
//		mProductBarcodeDA _mProductBarcodeDA=new mProductBarcodeDA(db);
//		tDisplayPictureDA _tDisplayPictureDA = new tDisplayPictureDA(db);
//		mBranchDA _mBranchDA = new mBranchDA(db);
//		mRegionDA _mRegionDA = new mRegionDA(db);
//		mBannerDA _mBannerDA = new mBannerDA(db);
//		mTypeSumberDA _mTypeSumberDA = new mTypeSumberDA(db);
//		tCustomerBasedMobileHeaderDA _tCustomerBasedMobileHeaderDA = new tCustomerBasedMobileHeaderDA(db);
//		tCustomerBasedMobileDetailDA _tCustomerBasedMobileDetailDA = new tCustomerBasedMobileDetailDA(db);
//		tCustomerBasedMobileDetailProductDA _tCustomerBasedMobileDetailProductDA = new tCustomerBasedMobileDetailProductDA(db);
//		_tDisplayPictureDA = new tDisplayPictureDA(db);
//		tUserCheckinDA _tUserCheckinDA = new tUserCheckinDA(db);
		tUserLoginDA _tUserLoginDA = new tUserLoginDA(db);
		mRoleDA _mRoleDA = new mRoleDA(db);
		mSPMHeaderDA _mSPMHeaderDA = new mSPMHeaderDA(db);
		mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);


		//_tDisplayPictureDA.DropTable(db);
//		_tCustomerBasedMobileHeaderDA.DropTable(db);
//		_tCustomerBasedMobileDetailDA.DropTable(db);
//		_tCustomerBasedMobileDetailProductDA.DropTable(db);
//		_tNotificationDA.DropTable(db);
//		_mPriceInOutletDA.DropTable(db);
//		_mProductBarcodeDA.DropTable(db);
//		_mUserRoleDA.DropTable(db);
//		_tLeaveMobileDA.DropTable(db);
//		_mProductBrandHeaderDA.DropTable(db);
//		_tActivityDA.DropTable(db);
//		_mEmployeeAreaDA.DropTable(db);
//		_mNotificationDA.DropTable(db);
//		_mEmployeeBranchDA.DropTable(db);
//		_mEmployeeSalesProductDA.DropTable(db);
//		_tUserLoginDA.DropTable(db);
//		_tSalesProductHeaderDA.DropTable(db);
//		_tSalesProductDetailDA.DropTable(db);
//		_tDeviceInfoUserDA.DropTable(db);
//		_mMenuDA.DropTable(db);
//		_mCounterNumberDA.DropTable(db);
//		_tAbsenUserDA.DropTable(db);
//		_mTypeLeaveMobileDA.DropTable(db);
//		_mBranchDA.DropTable(db);
//		_mRegionDA.DropTable(db);
//		_mBannerDA.DropTable(db);
//		_mTypeSumberDA.DropTable(db);
//		_tUserCheckinDA.DropTable(db);
		_tUserLoginDA.DropTable(db);
		_mRoleDA.DropTable(db);
		_mSPMHeaderDA.DropTable(db);
		_mSPMDetailDA.DropTable(db);

//		_tCustomerBasedMobileHeaderDA = new tCustomerBasedMobileHeaderDA(db);
//		_tCustomerBasedMobileDetailDA = new tCustomerBasedMobileDetailDA(db);
//		_tCustomerBasedMobileDetailProductDA = new tCustomerBasedMobileDetailProductDA(db);
//		_tDisplayPictureDA = new tDisplayPictureDA(db);
//		_mBranchDA = new mBranchDA(db);
//		_mRegionDA = new mRegionDA(db);
//		_mBannerDA = new mBannerDA(db);
//		_mTypeSumberDA = new mTypeSumberDA(db);
//		_tUserCheckinDA = new tUserCheckinDA(db);
//		_mPriceInOutletDA=new mPriceInOutletDA(db);
//		_mUserRoleDA=new mRoleDA(db);
//		_mTypeLeaveMobileDA=new mTypeLeaveMobileDA(db);
//		_tLeaveMobileDA=new tLeaveMobileDA(db);
//		_tUserLoginDA=new tUserLoginDA(db);
//		_tSalesProductHeaderDA=new tSalesProductHeaderDA(db);
//		_tSalesProductDetailDA=new tSalesProductDetailDA(db);
//		_tActivityDA=new tActivityDA(db);
//		_tDeviceInfoUserDA=new tDeviceInfoUserDA(db);
//		_mNotificationDA=new mNotificationDA(db);
//		_mEmployeeAreaDA=new mEmployeeAreaDA(db);
//		_mEmployeeBranchDA=new mEmployeeBranchDA(db);
//		_mEmployeeSalesProductDA=new mEmployeeSalesProductDA(db);
//		_mCounterNumberDA=new mCounterNumberDA(db);
//		_tAbsenUserDA=new tAbsenUserDA(db);
//		_mMenuDA=new mMenuDA(db);
//		_mProductBrandHeaderDA=new mProductBrandHeaderDA(db);
//		_tNotificationDA=new tNotificationDA(db);
		_tUserLoginDA = new tUserLoginDA(db);
		_mRoleDA = new mRoleDA(db);
		_mSPMHeaderDA = new mSPMHeaderDA(db);
		_mSPMDetailDA = new mSPMDetailDA(db);

//		int sumdata_mUserDA = _tUserLoginDA.getContactsCount(db);
//		if (sumdata_mUserDA == 0) {
//			_tUserLoginDA.InsertDefaultMUser(db);
//		}

		int sumdata_mRoleDA = _mRoleDA.getContactsCount(db);
		if (sumdata_mRoleDA == 0) {
			_mRoleDA.InsertDefaultMRole(db);
		}

//		int sumdata_mSPMHeaderDA = _mSPMHeaderDA.getContactsCount(db);
//		if (sumdata_mSPMHeaderDA == 0) {
//			_mSPMHeaderDA.InsertDefaultSPMHeader(db);
//		}
//
//		int sumdata_mSPMDetailDA = _mSPMDetailDA.getContactsCount(db);
//		if (sumdata_mSPMDetailDA == 0) {
//			_mSPMDetailDA.InsertDefaultmSPMDetail(db);
//		}

		clsHardCode clsdthc=new clsHardCode();
		clsHelper _clsHelper=new clsHelper();
//		File dir = new File(clsdthc.txtPathUserData);
//		_clsHelper.DeleteRecursive(dir);
		mconfigDA _mconfigDA = new mconfigDA(db);
		int sumdata = _mconfigDA.getContactsCount(db);
		if (sumdata == 0) {
			_mconfigDA.InsertDefaultMconfig(db);
		}

	}
	
	public String ResultJsonData(String dt){
		return dt.substring(16,dt.length()-2);
	}
//	public JSONArray ResultJsonArray(String dt) throws ParseException{
//		JSONParser jsonParser = new JSONParser();
//		Object obj = jsonParser.parse(dt);
//		JSONArray lang= (JSONArray) obj;
//		return lang;
//	}
//	public String linkAPI(SQLiteDatabase db){
//		//ambil linkapi Database sqllite
//		mconfigDA _mconfigDA=new mconfigDA(db);
//		String strVal2="";
//		mconfigData dataAPI = _mconfigDA.getData(db,
//				enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue() == "") {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		//ambil version dari webservices
//		linkAPI dtlinkAPI=new linkAPI();
//		return dtlinkAPI.QueryString(strVal2);
//	}
	void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);
	    fileOrDirectory.delete();
	}
	 public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		    int width = bm.getWidth();
		    int height = bm.getHeight();
		    float scaleWidth = ((float) newWidth) / width;
		    float scaleHeight = ((float) newHeight) / height;
		    // CREATE A MATRIX FOR THE MANIPULATION
		    Matrix matrix = new Matrix();
		    // RESIZE THE BIT MAP
		    matrix.postScale(scaleWidth, scaleHeight);

		    // "RECREATE" THE NEW BITMAP
		    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
		            matrix, false);

		    return resizedBitmap;
		}
	public Bitmap downloadFile(String fileUrl){
		  Bitmap bmImg=null;
	      URL myFileUrl =null;
	      try {
	           myFileUrl= new URL(fileUrl);
	      } catch (MalformedURLException e) {
	           e.printStackTrace();
	      }
	      try {
	           HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
	           conn.setDoInput(true);
	           conn.connect();
	           InputStream is = conn.getInputStream();
	           bmImg = BitmapFactory.decodeStream(is);
	      } catch (IOException e) {
	           e.printStackTrace();
	      }
	      return bmImg;
	 }
	public String getHTML(String urlToRead) throws Exception {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setConnectTimeout(5000);
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (IOException e) {
	    	  throw e;
	      } catch (Exception e) {
	    	  throw e;
	      }
	      return result;
	   }
	public void createFolderApp(){

		clsHardCode clsdthc= new clsHardCode();
		File appDir=new File(clsdthc.txtPathApp);
		if(!appDir.exists() && !appDir.isDirectory()) 
	    {
	        // create empty directory
	        if (appDir.mkdirs())
	        {
	            Log.i("CreateDir","App dir created");
	        }
	        else
	        {
	            Log.w("CreateDir","Unable to create app dir!");
	        }
	    }
	    else
	    {
	        Log.i("CreateDir","App dir already exists");
	    }
	}
	public String generateNewId(String OldId,String Separator,String Length){
		String itemStyle = OldId;
		String[] split = itemStyle.split(Separator,0);
		String itemID = split[1];
		Long num0x= (long) 0 ;
		if(itemID.contains("0")){
			num0x = Long.valueOf(itemID.substring(itemID.indexOf("0")));	
		}else{
			num0x = Long.valueOf(itemID);	
		}
		String second = split[0]+Separator+String.format("%0"+Length+"d", num0x + 1);
		return second;
	}
	public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return "-1";
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return "-1";
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

	public void createDb() throws ParseException {
		SQLiteDatabase db;
		clsHardCode clsdthc= new clsHardCode();
		db=SQLiteDatabase.openOrCreateDatabase(clsdthc.txtDatabaseName, null);
		if(new clsMainBL().checkUserActive()){
			new clsHelper().DeleteAllDB(db);
		}
	}
}
