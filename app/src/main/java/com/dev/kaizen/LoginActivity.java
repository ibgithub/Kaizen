package com.dev.kaizen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.base.BaseActivity;
import com.dev.kaizen.base.BaseMenuActivity;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.restful.AsyncTaskCompleteListener;
import com.dev.kaizen.restful.CallWebService2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;
import com.dev.kaizen.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText usernameEdit, passwordEdit;

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

        TextView tv = (TextView) v.findViewById(R.id.signText);
        tv.setTypeface(FontUtils.loadFontFromAssets(this, Constant.FONT_BOLD));

        tv = (TextView) v.findViewById(R.id.kaizenText);
        tv.setTypeface(FontUtils.loadFontFromAssets(this));

        usernameEdit = (EditText) v.findViewById(R.id.usernameEdit);
        usernameEdit.setTypeface(FontUtils.loadFontFromAssets(this));

        passwordEdit = (EditText) v.findViewById(R.id.passwordEdit);
        passwordEdit.setTypeface(FontUtils.loadFontFromAssets(this));

        Button btn = (Button) v.findViewById(R.id.masukButton);
        btn.setTypeface(FontUtils.loadFontFromAssets(this, Constant.FONT_BOLD));
        btn.setOnClickListener(this);

        tv = (TextView) v.findViewById(R.id.forgotPassword);
        tv.setTypeface(FontUtils.loadFontFromAssets(this));

        tv = (TextView) v.findViewById(R.id.signUp);
        tv.setTypeface(FontUtils.loadFontFromAssets(this));
        String sourceString = "Belum memiliki akun daftar sekarang <b>disini</b> ";
        tv.setText(Html.fromHtml(sourceString));

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
                String url = Constant.BASE_URL + "authenticate";

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                // prepare the Request
                JSONObject json = new JSONObject();
                try {
                    json.put("username", usernameEdit.getText().toString().trim().toLowerCase());
                    json.put("password", passwordEdit.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    GlobalVar.getInstance().setIdToken(response.getString("id_token"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                usernameEdit.setText("");
                                passwordEdit.setText("");

                                Intent intent = new Intent(LoginActivity.this, BaseMenuActivity.class);
                                startActivityForResult(intent, 1);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
                                NetworkResponse response = error.networkResponse;
                                if (error instanceof AuthFailureError && response != null) {
                                    try {
                                        String res = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        Log.e("res", "" + res);
                                        JSONObject obj = new JSONObject(res);
                                        Log.d("obj", "" + obj);

                                        final CustomDialogClass2 cd = new CustomDialogClass2(LoginActivity.this);
                                        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        cd.show();
                                        cd.setCanceledOnTouchOutside(false);
                                        cd.header.setText("Message");
                                        String title = obj.getString("title");
                                        String detail = obj.getString("detail");

                                        cd.isi.setText(title + ": " + detail);
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                } else if (error instanceof ServerError && response != null) {

                                }
                            }
                        }
                );
                queue.add(postRequest);
            }
        } else if(v.getId() == R.id.signUp) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.forgotPassword) {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivityForResult(intent, 1);
        }

    }

//    private void getAccount () {
//        choice = "Account";
//        StringBuilder url = new StringBuilder();
//        try {
//            url.append(Constant.BASE_URL).append("account");
//
//            JSONObject json = new JSONObject();
//            json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//
//            if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());
//
//            final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
//            task.execute(url.toString(), Constant.REST_GET, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getPrograms () {
//        choice = "Programs";
//        StringBuilder url = new StringBuilder();
//        try {
//            url.append(Constant.BASE_URL).append("programs");
//
//            JSONObject json = new JSONObject();
//            json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//
//            if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());
//
//            final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
//            task.execute(url.toString(), Constant.REST_GET, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getProfile () {
//        choice = "Participants";
//        StringBuilder url = new StringBuilder();
//        try {
//            url.append(Constant.BASE_URL).append("participants");
//
//            JSONObject json = new JSONObject();
//            json.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//
//            if(Constant.SHOW_LOG) Log.d("test", "==== json req " + json.toString());
//
//            final CallWebService2 task = new CallWebService2(LoginActivity.this, this);
//            task.execute(url.toString(), Constant.REST_GET, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
