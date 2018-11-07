/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private TextView nameText, emailText, addressText, schoolText, classText;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.INVISIBLE);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.GONE);

        nameText = (TextView) v.findViewById(R.id.nameText);
        emailText = (TextView) v.findViewById(R.id.emailText);
        addressText = (TextView) v.findViewById(R.id.addressText);
        schoolText = (TextView) v.findViewById(R.id.schoolText);
        classText = (TextView) v.findViewById(R.id.classText);

        Button updateBtn = (Button) v.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);

        setValue();

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.updateBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "tutorial");

            ProfileEditFragment fragment2 = new ProfileEditFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("profile");
            fragmentTransaction.commit();

        }
    }

    private void setValue() {
        try {
            if(GlobalVar.getInstance().getProfile() == null) {
                getParticipant();
                if(GlobalVar.getInstance().getAccount() != null) {
                    JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
                    emailText.setText(account.getString("email"));
                }
            } else {
                JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
                addressText.setText(profile.getString("address"));
                classText.setText(profile.getString("schoolClass"));

                JSONObject user = profile.getJSONObject("user");
                nameText.setText(user.getString("firstName") + " " + user.getString("lastName"));
                emailText.setText(user.getString("email"));

                JSONObject school = profile.getJSONObject("school");
                schoolText.setText(school.getString("schoolName"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

//    private void getAccount() {
//        String url = Constant.BASE_URL + "account";
//        RequestQueue queue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("VOLLEY", response);
//                GlobalVar.getInstance().setAccount(response);
//                getParticipant();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VOLLEY", error.toString());
////                NetworkResponse response = error.networkResponse;
////                if (error instanceof ServerError && response != null) {
////                    try {
////                        String res = new String(response.data,
////                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
////                        JSONObject obj = new JSONObject(res);
////                        Log.d("obj", "" + obj);
////
////                        final CustomDialogClass2 cd = new CustomDialogClass2(context);
////                        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                        cd.show();
////                        cd.setCanceledOnTouchOutside(false);
////                        cd.header.setText("Message");
////                        cd.isi.setText(obj.getString("title"));
////                    } catch (UnsupportedEncodingException e1) {
////                        e1.printStackTrace();
////                    } catch (JSONException e2) {
////                        e2.printStackTrace();
////                    }
////                }
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
//                Log.d("mapheader", map.toString());
//                return map;
//            }
//        };
//        queue.add(stringRequest);
//    }

    private void getParticipant() {
        try {
            JSONObject obj = new JSONObject(GlobalVar.getInstance().getAccount());
            String url = Constant.BASE_URL + "participantsByUserId/" + obj.getString("id");
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    GlobalVar.getInstance().setProfile(response);

                    setValue();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
//                    NetworkResponse response = error.networkResponse;
//                    if (error instanceof ServerError && response != null) {
//                        try {
//                            String res = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                            JSONObject obj = new JSONObject(res);
//                            Log.d("obj", "" + obj);
//
//                            final CustomDialogClass2 cd = new CustomDialogClass2(context);
//                            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            cd.show();
//                            cd.setCanceledOnTouchOutside(false);
//                            cd.header.setText("Message");
//                            cd.isi.setText(obj.getString("title"));
//                        } catch (UnsupportedEncodingException e1) {
//                            e1.printStackTrace();
//                        } catch (JSONException e2) {
//                            e2.printStackTrace();
//                        }
//                    }
                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                    Log.d("mapheader", map.toString());
                    return map;
                }
            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setValue();
    }
}
