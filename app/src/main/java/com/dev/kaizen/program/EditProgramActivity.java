package com.dev.kaizen.program;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dev.kaizen.R;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.restful.AsyncTaskCompleteListener;
import com.dev.kaizen.restful.CallWebService2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.GlobalVar;
import com.dev.kaizen.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EditProgramActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener<Object> {
    private static final int REQUEST_CODE_CHOOSE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout baseView = (LinearLayout) getLayoutInflater().inflate(R.layout.program_edit_layout, null);
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
        } else if (v.getId() == R.id.beforeBtn){
            Intent intent = new Intent(EditProgramActivity.this, UploadPhotoActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

        }
    }

    @Override
    public void onTaskComplete(Object... params) {
        String result = (String) params[0];
        if (Utility.cekValidResult(result, this)) {
            if(Constant.SHOW_LOG) Log.d("tst", "========== response " + result);

        }
    }

}
