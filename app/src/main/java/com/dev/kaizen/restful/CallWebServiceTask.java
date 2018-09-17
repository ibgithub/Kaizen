package com.dev.kaizen.restful;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import com.dev.kaizen.base.ProgressDialog2;
import com.dev.kaizen.util.Constant;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CallWebServiceTask extends AsyncTask<Object, String, String>{
	private Context applicationContext;
	private AsyncTaskCompleteListener<Object> callback;	
	private ProgressDialog2 dialog;
	private String message = "Mengakses NgeLAB";
	private Integer idCaller = null;
	private boolean hideDialog = false;

	public CallWebServiceTask() {
		// TODO Auto-generated constructor stub
	}
	
	public CallWebServiceTask(Context applicationContext,
			AsyncTaskCompleteListener<Object> callback, boolean hideDialog) {
		this.applicationContext = applicationContext;
		this.callback = callback;
		this.hideDialog = hideDialog;
	}
	
	public CallWebServiceTask(Context applicationContext,
			AsyncTaskCompleteListener<Object> callback) {
		this.applicationContext = applicationContext;
		this.callback = callback;
	}
	
	public CallWebServiceTask(Context applicationContext,
			AsyncTaskCompleteListener<Object> callback, String message) {
		this.applicationContext = applicationContext;
		this.callback = callback;
		this.message = message;
	}

	@Override
	protected void onPreExecute() {
//		if (!hideDialog && !((Activity)applicationContext).isFinishing()) {
//			dialog = new ProgressDialog2(applicationContext);
//			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//			dialog.show();
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.isi.setText(message);
//		}
		
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
				    synchronized (this) {
					wait(1000);
				    }
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}.start();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Object... params) {
		String url = (String) params[0];
		int method = (Integer) params[1];
		if(params.length > 2)
			idCaller = (Integer) params[2];
		
		String result = Constant.JSON_TIMEOUT;
		try {
			result = RestfulHttpMethod.connect(url, method);
			if(result.equalsIgnoreCase("null") || result.equalsIgnoreCase("[]"))
				result = "{\"errorCode\":\"IB-0002\"}";
		} catch (ClientProtocolException e) {
			result = Constant.JSON_TIMEOUT;
			e.printStackTrace();
		} catch (SocketException e) {
			result = Constant.JSON_TIMEOUT;
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			result = Constant.JSON_TIMEOUT;
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			result = Constant.JSON_TIMEOUT;
			e.printStackTrace();
		} catch (Exception e) {
			result = Constant.JSON_TIMEOUT;
			e.printStackTrace();
		}		
			
		return result;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
//		if (dialog != null && dialog.isShowing()) {
//			try {
//				dialog.dismiss();
//			} catch (IllegalArgumentException e) {
//				//do nothing
//				e.printStackTrace();
//			}
//		}
		
		if(idCaller != null)
			callback.onTaskComplete(result, idCaller);
		else
			callback.onTaskComplete(result);
	}		
}
