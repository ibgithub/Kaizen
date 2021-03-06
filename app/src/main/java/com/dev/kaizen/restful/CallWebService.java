package com.dev.kaizen.restful;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.dev.kaizen.util.Constant;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class CallWebService extends AsyncTask<Object, String, String> {
    private Context context;
    private AsyncTaskCompleteListener<Object> callback;
    private String message;
    private ProgressDialog dialog;


    public CallWebService(Context context, AsyncTaskCompleteListener<Object> callback) {
        this.context = context;
        this.callback = callback;
    }

    public CallWebService(Context context, AsyncTaskCompleteListener<Object> callback, String message) {
        this.context = context;
        this.callback = callback;
        this.message = message;
    }


    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "", message != null ? message : "Loading ...", true, false);
        if (!dialog.isShowing() && !((Activity) context).isFinishing()) {
            dialog.show();
        }
    }


    @Override
    protected String doInBackground(Object... params) {
        String url = (String) params[0];
        int method = (Integer) params[1];

        UrlEncodedFormEntity uefe = null;
        if (params.length > 2) {
            uefe = (UrlEncodedFormEntity) params[2];
        }

        String result = "{\"errorCode\":\"IB-0000\"}";

        try {

            if (uefe == null) {
                result = RestfulHttpMethod.connect(url, method);
            } else {
                result = RestfulHttpMethod.connect(url, method, uefe, Constant.TIMEOUT_CONN, context);
            }

            if (result.equalsIgnoreCase("null") || result.equalsIgnoreCase("[]"))
                result = "{\"errorCode\":\"IB-0002\"}";
        } catch (ClientProtocolException e) {
            result = "{\"errorCode\":\"IB-0000\"}";
            e.printStackTrace();
        } catch (SocketException e) {
            result = "{\"errorCode\":\"IB-0000\"}";
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            result = "{\"errorCode\":\"IB-1015\"}";
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            result = "{\"errorCode\":\"IB-1015\"}";
            e.printStackTrace();
        } catch (Exception e) {
            result = "{\"errorCode\":\"IB-0000\"}";
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        callback.onTaskComplete(result);
    }
}
