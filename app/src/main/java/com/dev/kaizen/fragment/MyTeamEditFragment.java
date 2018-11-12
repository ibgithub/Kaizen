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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.GroupTeam;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyTeamEditFragment extends Fragment implements View.OnClickListener{
    private Context context;

    private GroupTeam groupTeam;

    private EditText teamName;
    private Gson gson;

    public static MyTeamEditFragment newInstance() {
        MyTeamEditFragment fragment = new MyTeamEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team_update, container, false);

        groupTeam = getArguments().getParcelable("item");

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        if(groupTeam != null) {
            headertext.setText("Ubah Tim");
        } else {
            headertext.setText("Tambah Tim");
        }
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        Button btnUpdate = (Button) v.findViewById(R.id.saveTeamBtn);
        btnUpdate.setOnClickListener(this);

        teamName = (EditText) v.findViewById(R.id.descEdit);

        setValue(v);

        return v;
    }

    private void setValue(View v) {
        if (groupTeam != null) {
            teamName.setText(groupTeam.getDesc());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStackImmediate();
        } else if(v.getId() == R.id.saveTeamBtn) {
            if (teamName.getText().toString().trim().equals("")) {
                final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.show();
                cd.setCanceledOnTouchOutside(false);
                cd.header.setText("Pesan");
                cd.isi.setText("Nama Tim masih kosong");
            } else {
                try {
                    JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());

                    JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
                    JSONObject school = profile.getJSONObject("school");

                    String url = Constant.BASE_URL + "groups";
                    RequestQueue queue = Volley.newRequestQueue(getContext());

                    if (groupTeam != null && groupTeam.getId() != null) {

                        String str = gson.toJson(groupTeam);
                        JSONObject json = new JSONObject(str);
                        json.put("desc", teamName.getText());

                        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, json,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("response put team", response.toString());
                                    Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_LONG).show();

                                    groupTeam = gson.fromJson(response.toString(), GroupTeam.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("item", groupTeam);

                                    MyTeamFragment fragment2 = new MyTeamFragment();
                                    fragment2.setArguments(bundle);

                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.content, fragment2);
                                    fragmentTransaction.addToBackStack("profile");  //diganti apa ya?
                                    fragmentTransaction.commit();
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

                                            final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
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
                        )
                        {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                                Log.d("mapheader", map.toString());
                                return map;
                            }
                        };
                        queue.add(putRequest);
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("desc", teamName.getText());
                        json.put("member1", account.getLong("id"));
                        json.put("member10", school.getLong("id")); //member10 untuk nyimpan asal sekolah

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_LONG).show();

                                        groupTeam = gson.fromJson(response.toString(), GroupTeam.class);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("item", groupTeam);

                                        MyTeamFragment fragment2 = new MyTeamFragment();
                                        fragment2.setArguments(bundle);

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.content, fragment2);
                                        fragmentTransaction.addToBackStack("profile");  //diganti apa ya?
                                        fragmentTransaction.commit();
                                    }
                                },
                                new Response.ErrorListener() {
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

                                                final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
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
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                                return map;
                            }
                        };
                        queue.add(postRequest);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
