package com.dev.kaizen.restful;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.dev.kaizen.base.ProgressDialog2;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.URLConnection.guessContentTypeFromName;
import static java.text.MessageFormat.format;

public class CallWebServiceTask2 extends AsyncTask<String, String, String>{
	private Context applicationContext;
	private AsyncTaskCompleteListener<Object> callback;
	private ProgressDialog2 dialog;
	//private String message = "Retrieving data ...";
	
	private static final String CRLF = "\r\n";
    private static final String CHARSET = "UTF-8";

    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 10000;

    private HttpURLConnection connection;
    private OutputStream outputStream;
    private PrintWriter writer;
    private String boundary;

    // for log formatting only
    private URL url;
    private long start;
    
    private int paramCount = 0;
    
    private Map<String, Object> mData;
    
	public CallWebServiceTask2() {
		// TODO Auto-generated constructor stub
	}
		
	public CallWebServiceTask2(Context applicationContext,
							   AsyncTaskCompleteListener<Object> callback, LinkedHashMap<String, Object> mData) {
		this.applicationContext = applicationContext;
		this.callback = callback;
		this.mData = mData;
		/**try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new EasyX509TrustManager(null) }, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			// TODO Auto-generated catch block
            Log.i("Exception", e.toString());
		}*/
	}

	public CallWebServiceTask2(Context applicationContext, AsyncTaskCompleteListener<Object> callback, HashMap<String, Object> mData){
		this(applicationContext, callback, hashMapToLinked(mData));
	}

	private static LinkedHashMap<String, Object> hashMapToLinked(HashMap<String, Object> mData){
		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		for(Entry<String, Object> entry : mData.entrySet()){
			data.put(entry.getKey(), entry.getValue());
		}
		return data;
	}

	@Override
	protected void onPreExecute() {
		/*dialog = ProgressDialog.show(applicationContext,    
                "Please wait...", message, true, true, 
                new DialogInterface.OnCancelListener(){
	                @Override
	                public void onCancel(DialogInterface dialog) {
	                	cancel(true);
	                }
            }
        );*/
		if(!((Activity)applicationContext).isFinishing()) {
			dialog = new ProgressDialog2(applicationContext);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.show();
		}

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
	protected String doInBackground(String... params) {	
		String result = null;		
		try {			
			String[] fieldFile = {"file"};
			CallWebServiceTask2 rest = new CallWebServiceTask2(new URL(params[0]));
			for ( Entry<String, Object> param : mData.entrySet() ) {
				if ( param.getValue() != null ) {
					String tempFile = null;
					for(int i=0; i<fieldFile.length; i++) {
						if(param.getKey().equals(fieldFile[i])) {
							tempFile = fieldFile[i];
						}
					}
					
					if(tempFile!=null) {
						rest.addFilePart( param.getKey(), param.getValue().toString() );
						Log.d("result", "add form field : " + param.getKey() + "   " + param.getValue().toString() );
					} else {
						rest.addFormField( param.getKey(), param.getValue().toString() );
						Log.d("result", "add file part : " + param.getKey() + "   " + param.getValue().toString() );
					}
					
					/*if ( param.getValue() instanceof String ) {
						rest.addFormField( param.getKey(), param.getValue().toString() );
						Log.d("result", "add form field : " + param.getKey() + "   " + param.getValue().toString() );
					} else if ( param.getValue() instanceof File ) {
						rest.addFilePart( param.getKey(), (File) param.getValue() );
						Log.d("result", "add file part : " + param.getKey() + "   " + param.getValue().toString() );
					}		*/				
				} else {
					rest.addFormField(param.getKey(), "");
					Log.d("result", "add form field: " + param.getKey() + "   " + param.getValue().toString() );
				}
			}

			rest.addHeaderField("Content-Type","application/json");
			rest.addHeaderField("Accept","application/json");

			result = rest.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return result;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		
		callback.onTaskComplete(result);
	}		
	
	private void addFormField(final String name, final String value) {
    	if (paramCount > 0) writer.append(CRLF);
    	
        writer.append("--").append(boundary).append(CRLF)
                .append("Content-Disposition: form-data; name=\"").append(name)
                .append("\"").append(CRLF)
                .append("Content-Type: text/plain; charset=").append(CHARSET)
                .append(CRLF).append(CRLF).append(value)/*.append(CRLF)*/;
        
        paramCount++;
    }

    private void addFilePart(final String fieldName, final String uploadFile) throws IOException {
    	if (paramCount > 0) writer.append(CRLF);
    	
    	String[] q = uploadFile.split("/");
        int idx = q.length - 1;
        
        final String fileName = q[idx];
        writer.append("--").append(boundary).append(CRLF)
                .append("Content-Disposition: form-data; name=\"")
                .append(fieldName).append("\"; filename=\"").append(fileName)
                .append("\"").append(CRLF).append("Content-Type: ")
                .append(guessContentTypeFromName(fileName)).append(CRLF)
                .append("Content-Transfer-Encoding: binary").append(CRLF)
                .append(CRLF);

        writer.flush();
        outputStream.flush();
        //try (  ) {
        	final FileInputStream inputStream = new FileInputStream(uploadFile);
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        //}

        //writer.append(CRLF);
        paramCount++;
    }

    protected void addHeaderField(String name, String value) {
        writer.append(name).append(": ").append(value).append(CRLF);
    }
    
    private CallWebServiceTask2(final URL url) throws IOException {
        start = currentTimeMillis();
        this.url = url;

        boundary = "-----------------" + currentTimeMillis();

		/**try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new EasyX509TrustManager(null) }, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("Exception", e.toString());
		}*/

		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setReadTimeout(READ_TIMEOUT);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Charset", CHARSET);
		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		outputStream = connection.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
    }
    
    private String send() throws IOException {
        writer.append(CRLF).append("--").append(boundary).append("--").append(CRLF);
        writer.close();

        final int status = connection.getResponseCode();
        if ( status != HTTP_OK ) {
            throw new IOException(format("{0} failed with HTTP status: {1}", url, status));
        }

        //try (  ) {
        	final InputStream is = connection.getInputStream();
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ( (bytesRead = is.read(buffer)) != -1 ) {
                bytes.write(buffer, 0, bytesRead);
            }

            connection.disconnect();
            return new String( bytes.toByteArray() );
            
        //} finally {
        	
         //   connection.disconnect();
        //}
    }

	public class NullHostNameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			Log.i("RestUtilImpl", 	"Approving certificate for " + hostname);
			return true;
		}

	}
}
