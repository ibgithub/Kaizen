package com.dev.kaizen.program;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dev.kaizen.LoginActivity;
import com.dev.kaizen.MenuActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.restful.AsyncTaskCompleteListener;
import com.dev.kaizen.restful.CallWebService2;
import com.dev.kaizen.restful.CallWebServiceTask2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.GlobalVar;
import com.dev.kaizen.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class UploadProgramActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener<Object> {
    private static final int REQUEST_CODE_CHOOSE = 1001;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout baseView = (LinearLayout) getLayoutInflater().inflate(R.layout.upload_layout, null);
        setContentView(baseView);

        Button button = (Button) findViewById(R.id.beforeBtn);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.afterBtn);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.videoBtn);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.cancelBtn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancelBtn) {
            finish();
        } else {
            choice = "images";
            if(v.getId() == R.id.videoBtn) {
                choice = "video";
            }

//            Matisse.from(UploadProgramActivity.this)
//                    .choose(MimeType.ofAll())
//                    .countable(true)
//                    .maxSelectable(1)
//                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                    .thumbnailScale(0.85f)
//                    .imageEngine(new GlideEngine())
//                    .forResult(REQUEST_CODE_CHOOSE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            List<Uri> mSelected = Matisse.obtainResult(data);
//            Log.d("Matisse", "mSelected: " + mSelected + " ===== " + mSelected.get(0));
//            if(choice.equals("images") && !mSelected.toString().contains("/images/")) {
//                final CustomDialogClass2 cd = new CustomDialogClass2(UploadProgramActivity.this);
//                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                cd.show();
//                cd.setCanceledOnTouchOutside(false);
//                cd.header.setText("Pesan");
//                cd.isi.setText("Foto yang terpilih invalid");
//            } else if(choice.equals("video") && !mSelected.toString().contains("/video/")) {
//                final CustomDialogClass2 cd = new CustomDialogClass2(UploadProgramActivity.this);
//                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                cd.show();
//                cd.setCanceledOnTouchOutside(false);
//                cd.header.setText("Pesan");
//                cd.isi.setText("Video yang terpilih invalid");
//            } else {
//                StringBuilder url = new StringBuilder();
//                try {
//                    url.append(Constant.BASE_URL).append("fileUpload");
//
//                    JSONObject json = new JSONObject();
//                    json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//                    json.put("file", mSelected.get(0));
//
//                    if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());
//
//                    final CallWebService2 task = new CallWebService2(UploadProgramActivity.this, this);
//                    task.execute(url.toString(), Constant.REST_POST, json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

//                JSONObject paramsObj = new JSONObject();
//                try {
//                    paramsObj.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//                    paramsObj.put("file", "Bearer " + GlobalVar.getInstance().getIdToken());
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                //setup params
//                final LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
//
//                Iterator<String> it = paramsObj.keys();
//                while (it.hasNext()) {
//                    String key = it.next();
//                    try {
//                        params.put(key, paramsObj.getString(key));
//                        Log.i("result", "result: " + key + "   " + paramsObj.getString(key));
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//
//                Log.i("result", "result: " + "json" + "   " + paramsObj.toString());
//
//                CallWebServiceTask2 callWeb = new CallWebServiceTask2(UploadProgramActivity.this, this, params);
//                String url = Constant.BASE_URL + "fileUpload";
//                callWeb.execute(url);
//            }
//        }
    }

    @Override
    public void onTaskComplete(Object... params) {
        String result = (String) params[0];
        if (Utility.cekValidResult(result, this)) {
            if(Constant.SHOW_LOG) Log.d("tst", "========== response " + result);

        }
    }

//    private File savebitmap(Bitmap realImage) {
//        float ratio = Math.min(
//                (float) 800 / realImage.getWidth(),
//                (float) 800 / realImage.getHeight());
//        int width = Math.round((float) ratio * realImage.getWidth());
//        int height = Math.round((float) ratio * realImage.getHeight());
//
//        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
//                height, true);
//        OutputStream outStream = null;
//        File file = new File(this.getFilesDir(), System.currentTimeMillis() + ".jpg");
//
//        try {
//            outStream = new FileOutputStream(file);
//            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
//            outStream.flush();
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("file", "" + file);
//
//        return file;
//    }
}
