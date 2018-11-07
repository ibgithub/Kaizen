/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.MainActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.base.BaseMenuActivity;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.menu.QuotesListActivity;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private TextView welcomeText;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.activity_main, container, false);

//        welcomeText = (TextView) v.findViewById(R.id.welcomeText);

        Button btn = (Button) v.findViewById(R.id.loginBtn);
        btn.setVisibility(Button.GONE);

        btn = (Button) v.findViewById(R.id.youtubeBtn);
        btn.setVisibility(Button.GONE);

        ImageView imgV = (ImageView) v.findViewById(R.id.sampleBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.cornerBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.quotesBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.toolsBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.tutorialBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.testimonialBtn);
        imgV.setOnClickListener(this);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Home");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.INVISIBLE);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        AppBarLayout appBar = (AppBarLayout) getActivity().findViewById(R.id.appBar);
//        appBar.setVisibility(AppBarLayout.GONE);

        welcomeText = (TextView) v.findViewById(R.id.welcomeText);
        if(GlobalVar.getInstance().getAccount() == null) {
            getAccount();
        } else {
            setWelcome();
        }

        return v;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//        toolbar.setVisibility(Toolbar.GONE);
//
//        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sampleBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "sample");

            QuotesFragment fragment2 = new QuotesFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.cornerBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "corner");

            QuotesFragment fragment2 = new QuotesFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.quotesBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "quotes");

            QuotesFragment fragment2 = new QuotesFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.toolsBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "tools");

            QuotesFragment fragment2 = new QuotesFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.tutorialBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "tutorial");

            QuotesFragment fragment2 = new QuotesFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.testimonialBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("menu", "testimonial");

            QuotesFragment fragment2 = new QuotesFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        }
    }

    public void setWelcome() {
        try {
            if ( GlobalVar.getInstance().getProfile() != null ) {
                JSONObject obj = new JSONObject(GlobalVar.getInstance().getProfile());
                welcomeText.setText("Welcome, " + obj.getString("fullName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAccount() {
        String url = Constant.BASE_URL + "account";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                GlobalVar.getInstance().setAccount(response);
                getParticipant();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
//                NetworkResponse response = error.networkResponse;
//                if (error instanceof ServerError && response != null) {
//                    try {
//                        String res = new String(response.data,
//                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                        JSONObject obj = new JSONObject(res);
//                        Log.d("obj", "" + obj);
//
//                        final CustomDialogClass2 cd = new CustomDialogClass2(context);
//                        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        cd.show();
//                        cd.setCanceledOnTouchOutside(false);
//                        cd.header.setText("Message");
//                        cd.isi.setText(obj.getString("title"));
//                    } catch (UnsupportedEncodingException e1) {
//                        e1.printStackTrace();
//                    } catch (JSONException e2) {
//                        e2.printStackTrace();
//                    }
//                }
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
    }

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

                    setWelcome();
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
}
