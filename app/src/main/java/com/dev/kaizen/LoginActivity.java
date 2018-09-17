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
import com.dev.kaizen.restful.CallWebServiceTask;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;
import com.dev.kaizen.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class LoginActivity extends BaseActivity implements View.OnClickListener, AsyncTaskCompleteListener<Object> {
    EditText usernameEdit, passwordEdit;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinear);

        View v = null;
        if (v == null) {
            LayoutInflater vi = getLayoutInflater();
            v = vi.inflate(R.layout.activity_login, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        layout.addView(v);

        Button btn = (Button) v.findViewById(R.id.masukButton);
        btn.setTypeface(FontUtils.loadFontFromAssets(this));
        btn.setOnClickListener(this);

        usernameEdit = (EditText) v.findViewById(R.id.usernameEdit);
        passwordEdit = (EditText) v.findViewById(R.id.passwordEdit);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.masukButton) {
            //bypass
//            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//            startActivityForResult(intent, 1);

            if (usernameEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(LoginActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Username masih kosong");
            } else if (passwordEdit.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(LoginActivity.this);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Password masih kosong");
            } else {
                choice = "Login";
                StringBuilder url = new StringBuilder();
                try {
                    url.append(Constant.BASE_URL).append("authenticate");

                    JSONObject json = new JSONObject();
                    json.put("username", usernameEdit.getText().toString().trim().toLowerCase());
                    json.put("password", passwordEdit.getText().toString().trim());

                    if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());

                    final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
                    task.execute(url.toString(), Constant.REST_POST, json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTaskComplete(Object... params) {
        String result = (String) params[0];
        if (Utility.cekValidResult(result, this)) {
            if(Constant.SHOW_LOG) Log.d("tst", "========== response " + result);
            if(choice.equals("Login")) {
                try {
                    JSONObject obj = new JSONObject(result);
                    GlobalVar.getInstance().setIdToken(obj.getString("id_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                this.getAccount();

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivityForResult(intent, 1);
            }

//            else if(choice.equals("Account")) {
//                GlobalVar.getInstance().setAccount(result);
//                this.getPrograms();
//            } else if(choice.equals("Programs")) {
//                GlobalVar.getInstance().setProgram(result);
//                this.getProfile();
//            } else if(choice.equals("Participants")) {
//                GlobalVar.getInstance().setProfile(result);
//                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//                startActivityForResult(intent, 1);
//            }
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

            final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
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

            final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
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

            final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
            task.execute(url.toString(), Constant.REST_GET, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
