package com.dev.kaizen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dev.kaizen.base.BaseActivity;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.restful.AsyncTaskCompleteListener;
import com.dev.kaizen.restful.CallWebService2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;
import com.dev.kaizen.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener, AsyncTaskCompleteListener<Object> {
    EditText emailUserEdit;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinear);

        View v = null;
        if (v == null) {
            LayoutInflater vi = getLayoutInflater();
            v = vi.inflate(R.layout.activity_forgot_password, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        layout.addView(v);

        Button btn = (Button) v.findViewById(R.id.resetButton);
        btn.setTypeface(FontUtils.loadFontFromAssets(this));
        btn.setOnClickListener(this);

        emailUserEdit = (EditText) v.findViewById(R.id.emailUserEdit);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.resetButton) {
            //bypass
//            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//            startActivityForResult(intent, 1);

            if (emailUserEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(ForgotPasswordActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Email masih kosong");
            } else {
                choice = "Login";
                StringBuilder url = new StringBuilder();
                try {
                    url.append(Constant.BASE_URL).append("reset-password/init");

                    JSONObject json = new JSONObject();
                    json.put("mail", emailUserEdit.getText().toString().trim().toLowerCase());

                    if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());

                    final CallWebService2 task = new CallWebService2(ForgotPasswordActivity.this, this);
                    task.execute(url.toString(), Constant.REST_POST, json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if(v.getId() == R.id.signUp) {

        } else if (v.getId() == R.id.forgotPassword) {

        }

    }

    @Override
    public void onTaskComplete(Object... params) {
        String result = (String) params[0];
        try {
            String msg = "Email Ganti Password sudah terkirim";
            JSONObject obj = new JSONObject(result);
            if (obj.getString("errorCode") != null) {
                msg = "Ganti password gagal, silahkan cek lagi";
            }
            final CustomDialogClass2 cd = new CustomDialogClass2(ForgotPasswordActivity.this);
            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cd.show();
            cd.setCanceledOnTouchOutside(false);
            cd.header.setText("Pesan");
            cd.isi.setText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
