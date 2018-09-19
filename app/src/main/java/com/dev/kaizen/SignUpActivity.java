package com.dev.kaizen;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class SignUpActivity extends BaseActivity implements View.OnClickListener, AsyncTaskCompleteListener<Object> {
    EditText usernameEdit, emailEdit, passwordEdit, confirmPasswordEdit;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinear);

        View v = null;
        if (v == null) {
            LayoutInflater vi = getLayoutInflater();
            v = vi.inflate(R.layout.activity_sign_up, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        layout.addView(v);

        Button btn = (Button) v.findViewById(R.id.masukButton);
        btn.setTypeface(FontUtils.loadFontFromAssets(this));
        btn.setOnClickListener(this);

        usernameEdit = (EditText) v.findViewById(R.id.usernameEdit);
        emailEdit = (EditText) v.findViewById(R.id.emailEdit);
        passwordEdit = (EditText) v.findViewById(R.id.passwordEdit);
        confirmPasswordEdit = (EditText) v.findViewById(R.id.confirmPasswordEdit);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.masukButton) {
            if (usernameEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Username masih kosong");
            } else if (emailEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Email masih kosong");
            } else if (passwordEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Password masih kosong");
            } else if (confirmPasswordEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Konfirmasi Password masih kosong");
            } else if (!passwordEdit.getText().toString().trim().equals(confirmPasswordEdit.getText().toString().trim())) {
                final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Konfirmasi Password harus sama");
            } else {
                choice = "Login";
                StringBuilder url = new StringBuilder();
                try {
                    url.append(Constant.BASE_URL).append("register");

                    JSONObject json = new JSONObject();
                    json.put("login", usernameEdit.getText().toString().trim().toLowerCase());
                    json.put("email", emailEdit.getText().toString().trim().toLowerCase());
                    json.put("password", passwordEdit.getText().toString().trim());
                    json.put("langKey", "en");

                    if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());

                    final CallWebService2 task = new CallWebService2(SignUpActivity.this, this);
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
            String msg = "Registrasi berhasil, silahkan login";
            JSONObject obj = new JSONObject(result);
            if (obj.getString("errorCode") != null) {
                msg = "Registrasi gagal, silahkan cek lagi";
            }
            final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cd.show();
            cd.setCanceledOnTouchOutside(false);
            cd.header.setText("Pesan");
            cd.isi.setText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAccount () {
        choice = "Account";
        StringBuilder url = new StringBuilder();
        try {
            url.append(Constant.BASE_URL).append("account");

            JSONObject json = new JSONObject();
            json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());

            if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());

            final CallWebService2 task = new CallWebService2(SignUpActivity.this, this);
            task.execute(url.toString(), Constant.REST_GET, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPrograms () {
        choice = "Programs";
        StringBuilder url = new StringBuilder();
        try {
            url.append(Constant.BASE_URL).append("programs");

            JSONObject json = new JSONObject();
            json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());

            if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());

            final CallWebService2 task = new CallWebService2(SignUpActivity.this, this);
            task.execute(url.toString(), Constant.REST_GET, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProfile () {
        choice = "Participants";
        StringBuilder url = new StringBuilder();
        try {
            url.append(Constant.BASE_URL).append("participants");

            JSONObject json = new JSONObject();
            json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());

            if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());

            final CallWebService2 task = new CallWebService2(SignUpActivity.this, this);
            task.execute(url.toString(), Constant.REST_GET, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
