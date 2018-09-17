package com.dev.kaizen.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.dev.kaizen.base.CustomDialogClass2;

import org.json.JSONObject;

public class Utility {	
	private Utility() {
		// TODO Auto-generated constructor stub
	}
	
//	public static final String getDbPassword(Context context){
//		return getTokenId(context).substring(2,10);
//	}
//
//	private static final String getImei(Context context){
//		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//		//di hardcode dulu buat test di simulator
//	    String imei = manager.getDeviceId();
//		//String imei = "121156789015477";
//	    if (imei == null || imei.trim().length() == 0) {
//	    	imei = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
//	    }
//		return imei;
//	}
//
//	public static final String getTokenId(Context context){
//		String imei = getImei(context);
//		if (imei == null || imei.trim().length() == 0) return null;
//	    String tokenId =  imei + Constant.ANDROID_ID;
//	    tokenId = tokenId.toUpperCase();
//		return tokenId;
//	}
//
//	public static boolean cekValidResultTransaction(String result, Activity activity){
//		Log.d("##CEK RESULT ", result);
//		try{
//			if((result.indexOf("errorCode") < 0) || result.equalsIgnoreCase(Constant.TRUE)){
//				return true;
//			}else{
//				JSONObject jsonObject = new JSONObject(result);
//				if(jsonObject.has("errorCode")){
//					String errorCode = jsonObject.getString("errorCode");
//					String message = jsonObject.getString("fullMessage");
//
//					if(errorCode.equalsIgnoreCase(Constant.ERROR_COD_SESSION_EXPIRED) || errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_SESSION)){
//						logoutProcess(activity, message);
//					}else if(errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_AUTHENTICATION) || errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_SMS_TOKEN)){
//						final CustomDialogClass2 cd = new CustomDialogClass2(activity);
//						cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//						cd.show();
//						cd.header.setText("Pesan");
//						cd.isi.setText(message);
//					} else{
//						Intent intent = new Intent(activity, TransactionFailedActivity.class);
//						intent.putExtra("message", message);
//						activity.startActivityForResult(intent, 1);
//					}
//					return false;
//				}
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			final CustomDialogClass2 cd = new CustomDialogClass2(activity);
//			cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//			cd.show();
//			cd.header.setText("Pesan");
////			if (result == null) {
////			if (e.getMessage() != null) {
////				cd.isi.setText("Gagal terima respon dari server "+ result + " " + e.getMessage());
////			} else {
////				cd.isi.setText("Gagal terima respon dari server " + result);
////			}
//			cd.isi.setText("Terjadi kesalahan karena masalah Jaringan Telekomunikasi. Harap ulangi beberapa saat lagi.");
//			return false;
//		}
//		return true;
//	}
	
	public static boolean cekValidResult(String result, Activity activity){
		/**
		 * Error invalid Tin tetap di halaman confirm selain itu ke jurnal transaksi dengan status gagal + message error
		 */		 
//		Log.d("##CEK RESULT COY  ", result);
		try{
			if((result.indexOf("errorCode") < 0) || result.equalsIgnoreCase(Constant.TRUE)){
				return true;
			}else{
				Log.d("##CEK ERROR ", "##CEK ERROR");
				JSONObject jsonObject = new JSONObject(result);
				if(jsonObject.has("errorCode")){
					String errorCode = jsonObject.getString("errorCode");
					String message = (jsonObject.has("fullMessage"))?jsonObject.getString("fullMessage"):"Gangguan koneksi. Mohon cek koneksi internet anda.";
					if(errorCode.equalsIgnoreCase(Constant.ERROR_COD_SESSION_EXPIRED) || errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_SESSION)){						
//						logoutProcess(activity, message);
					}else if(errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_AUTHENTICATION)){
						final CustomDialogClass2 cd = new CustomDialogClass2(activity);
						cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
						cd.show();
						cd.header.setText("Pesan");
						cd.isi.setText(message);
					} else{
						final CustomDialogClass2 cd = new CustomDialogClass2(activity);
						cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
						cd.show();
						cd.header.setText("Pesan");
						cd.isi.setText(message);
					}
					return false;
				}
			}	
		}catch (Exception e) {
			e.printStackTrace();
			final CustomDialogClass2 cd = new CustomDialogClass2(activity);
			cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			cd.show();
			cd.header.setText("Pesan");
//			if (e.getMessage() != null) {
//				cd.isi.setText("Gagal terima respon dari server "+ result + " " + e.getMessage());
//			} else {
//				cd.isi.setText("Gagal terima respon dari server " + result);
//			}
			cd.isi.setText("Terjadi kesalahan karena masalah Jaringan Telekomunikasi. Harap ulangi beberapa saat lagi.");
			return false;			
		}
		return true;
	}
		
//	public static boolean cekValidResult(String result, Activity activity, String input){
//		/**
//		 * Error invalid Tin tetap di halaman confirm selain itu ke jurnal transaksi dengan status gagal + message error
//		 */
//		Log.d("##CEK RESULT COY  ", result);
//		try{
//			if((result.indexOf("errorCode") < 0) || result.equalsIgnoreCase(Constant.TRUE)){
//				return true;
//			}else{
//				Log.d("##CEK ERROR ", "##CEK ERROR");
//				JSONObject jsonObject = new JSONObject(result);
//				if(jsonObject.has("errorCode")){
//					String errorCode = jsonObject.getString("errorCode");
//					String message = (jsonObject.has("fullMessage"))?jsonObject.getString("fullMessage"):ResourceUtil.getBundle().getString(errorCode);
//					if(errorCode.equalsIgnoreCase(Constant.ERROR_COD_SESSION_EXPIRED) || errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_SESSION)){
//						logoutProcess(activity, message);
//					}else if(errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_AUTHENTICATION)){
//						final CustomDialogClass2 cd = new CustomDialogClass2(activity);
//						cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//						cd.show();
//						cd.header.setText("Pesan");
//						cd.isi.setText(message);
//					} else if(errorCode.equalsIgnoreCase(Constant.ERROR_UNDEFINED_USER) && input.equals("sesama")){
//						return true;
//					} else if(errorCode.equalsIgnoreCase(Constant.ERROR_UNDEFINED_USER_LAIN) && input.equals("nomor")){
//						return true;
//					} else{
//						final CustomDialogClass2 cd = new CustomDialogClass2(activity);
//						cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//						cd.show();
//						cd.header.setText("Pesan");
//						cd.isi.setText(message);
//					}
//					return false;
//				}
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			final CustomDialogClass2 cd = new CustomDialogClass2(activity);
//			cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//			cd.show();
//			cd.header.setText("Pesan");
//			cd.isi.setText("Terjadi kesalahan karena masalah Jaringan Telekomunikasi. Harap ulangi beberapa saat lagi.");
////			if (e.getMessage() != null) {
////				cd.isi.setText("Gagal terima respon dari server "+ result + " " + e.getMessage());
////			} else {
////				cd.isi.setText("Gagal terima respon dari server " + result);
////			}
//			return false;
//		}
//		return true;
//	}
	
//	public static boolean cekValidResult(String result, Activity activity, MobileErrorListener mobileErrorListener){
//		/**
//		 * Error invalid Tin tetap di halaman confirm selain itu ke jurnal transaksi dengan status gagal + message error
//		 */
//		try{
//			if(!result.contains("errorCode") || result.equalsIgnoreCase(Constant.TRUE)){
//				return true;
//			}else{
//				JSONObject jsonObject = new JSONObject(result);
//				if(jsonObject.has("errorCode")){
//					String errorCode = jsonObject.getString("errorCode");
//					String message = (jsonObject.has("fullMessage"))?jsonObject.getString("fullMessage"):ResourceUtil.getBundle().getString(errorCode);
//					if(errorCode.equalsIgnoreCase(Constant.ERROR_COD_SESSION_EXPIRED) || errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_SESSION) ||
//							errorCode.equalsIgnoreCase(Constant.ERROR_UNREGISTERED_USER)){
//						logoutProcess(activity, message);
//					}
//					else{
//						if(errorCode.equalsIgnoreCase(Constant.ERROR_INVALID_AUTHENTICATION)){
//							final CustomDialogClass2 cd = new CustomDialogClass2(activity);
//							cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//							cd.show();
//							cd.header.setText("Pesan");
//							cd.isi.setText(message);
//						}else{
//							mobileErrorListener.errorListener(message);
//						}
//					}
//					return false;
//				}
//			}
//		}catch (JSONException e) {
//			return false;
//		}catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
	
//	public static void logoutProcess(Activity activity, String message){
//		//Intent intent = new Intent(activity, LoginActivity.class);
//		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		//LoginActivity.logoutOrExit(activity, true, GlobalVar.getInstance().getAppContexts());
//		clearGlobalVariable();
//		Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
//		//activity.startActivity(intent);
//		//activity.finish();
//
//		Intent resultData = new Intent();
//		resultData.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////		resultData.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//    	resultData.putExtra("wantLogin", "true");
//    	activity.setResult(Activity.RESULT_OK, resultData);
//    	activity.finish();
//	}
//
//	public static boolean isNetworkAvailable(Context context) {
//	    ConnectivityManager connectivityManager
//	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//	    return activeNetworkInfo != null;
//	}
//
//	public static String getRootCause(Throwable t) {
//		Throwable cause = t;
//		Throwable subCause = cause.getCause();
//		while (subCause != null && !subCause.equals(cause)) {
//			cause = subCause;
//			subCause = cause.getCause();
//		}
//		return cause.getMessage();
//	}
//
//	public static void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }
//
//	public static String realUrl(String encryptedUrl) {
////        String[] urlArr = encryptedUrl.split(Constant.SUB_BASE_URL);
////        String realUrl = AesCrypto.decrypt("dsi12345", urlArr[0]);
////    	return realUrl + Constant.SUB_BASE_URL + urlArr[1];
//        return encryptedUrl;
//    }
//
//	public static String splitAmount(String amount){
//		CharSequence comma = ",";
//		CharSequence dot = ".";
//
//		if(amount.contains(comma)){
//			return amount.replace(",", "");
//		}else if(amount.contains(dot)){
//			return amount.replace(".", "");
//		}
//
//		return amount;
//	}
}
