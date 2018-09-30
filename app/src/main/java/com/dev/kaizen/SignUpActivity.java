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
import android.widget.TextView;

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
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    EditText usernameEdit, emailEdit, passwordEdit, confirmPasswordEdit;

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

        bgImg.setImageResource(R.drawable.bg_down2);

        TextView tv = (TextView) v.findViewById(R.id.signText);
        tv.setTypeface(FontUtils.loadFontFromAssets(this, Constant.FONT_BOLD));

        tv = (TextView) v.findViewById(R.id.kaizenText);
        tv.setTypeface(FontUtils.loadFontFromAssets(this));

        Button btn = (Button) v.findViewById(R.id.masukButton);
        btn.setTypeface(FontUtils.loadFontFromAssets(this, Constant.FONT_BOLD));
        btn.setOnClickListener(this);

        usernameEdit = (EditText) v.findViewById(R.id.usernameEdit);
        usernameEdit.setTypeface(FontUtils.loadFontFromAssets(this));

        emailEdit = (EditText) v.findViewById(R.id.emailEdit);
        emailEdit.setTypeface(FontUtils.loadFontFromAssets(this));

        passwordEdit = (EditText) v.findViewById(R.id.passwordEdit);
        passwordEdit.setTypeface(FontUtils.loadFontFromAssets(this));

        confirmPasswordEdit = (EditText) v.findViewById(R.id.confirmPasswordEdit);
        confirmPasswordEdit.setTypeface(FontUtils.loadFontFromAssets(this));
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
                String url = Constant.BASE_URL + "register";

                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

                // prepare the Request
                JSONObject json = new JSONObject();
                try {
                    json.put("login", usernameEdit.getText().toString().trim().toLowerCase());
                    json.put("email", emailEdit.getText().toString().trim().toLowerCase());
                    json.put("password", passwordEdit.getText().toString().trim());
                    json.put("langKey", "en");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                //ga masuk sini ketika sukses register
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
                                NetworkResponse response = error.networkResponse;
                                if (response == null) {

                                    new AlertDialog.Builder(SignUpActivity.this)
                                        .setTitle("Pesan")
                                        .setMessage("Sukses mendaftar, silahkan login")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                GlobalVar.getInstance().setIdToken(null);

                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivityForResult(intent, 1);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();

                                }else if (error instanceof ServerError && response != null) {
                                    try {
                                        String res = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        JSONObject obj = new JSONObject(res);
                                        Log.d("obj", "" + obj);

                                        final CustomDialogClass2 cd = new CustomDialogClass2(SignUpActivity.this);
                                        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        cd.show();
                                        cd.setCanceledOnTouchOutside(false);
                                        cd.header.setText("Message");
                                        cd.isi.setText(obj.getString("title"));
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                        }
                )
                        ;
                queue.add(postRequest);
            }
        }
    }
}
