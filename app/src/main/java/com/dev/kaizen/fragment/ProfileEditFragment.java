/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileEditFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RequestQueue queue;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner schoolSpinner;

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText addressEdit;
    private EditText schoolEdit;
    private EditText classEdit;

    public static ProfileEditFragment newInstance() {
        ProfileEditFragment fragment = new ProfileEditFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("Edit Profile");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.VISIBLE);
        backBtn.setOnClickListener(this);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        queue = Volley.newRequestQueue(getActivity());

        firstNameEdit = (EditText) v.findViewById(R.id.firstNameEdit);
        lastNameEdit = (EditText) v.findViewById(R.id.lastNameEdit);
        emailEdit = (EditText) v.findViewById(R.id.emailEdit);
        addressEdit = (EditText) v.findViewById(R.id.addressEdit);
        schoolEdit = (EditText) v.findViewById(R.id.schoolEdit);
        classEdit = (EditText) v.findViewById(R.id.classEdit);

        try {
            if (GlobalVar.getInstance().getAccount() != null) {
                JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
                firstNameEdit.setText(account.getString("firstName"));
                lastNameEdit.setText(account.getString("lastName"));
                emailEdit.setText(account.getString("email"));
            }

            if (GlobalVar.getInstance().getProfile() != null) {
                JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
                addressEdit.setText(profile.getString("address"));
                classEdit.setText(profile.getString("schoolClass"));

                //JSONObject school = profile.getJSONObject("school");
                //schoolText.setText(school.getString("schoolName"));
            }
        } catch (JSONException ex) {

        }

        provinceSpinner = (Spinner) v.findViewById(R.id.provinceSpinner);
        citySpinner = (Spinner) v.findViewById(R.id.citySpinner);
        schoolSpinner = (Spinner) v.findViewById(R.id.schoolSpinner);

        Button saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        if (GlobalVar.getInstance().getProvincies() == null) getData("provinces");

        return v;
    }

    private void spinnerData (final String type) {
        String str = "";
        if (type.equals("provinces")) {
            str = GlobalVar.getInstance().getProvincies();
        } else if (type.equals("cities")) {
            str = GlobalVar.getInstance().getCities();
        } else {
            str = GlobalVar.getInstance().getSchools();
        }
        try {
            JSONArray responseArr = new JSONArray(str);

            ArrayList<String> sort = new ArrayList<String>();

            for (int i = 0; i < responseArr.length(); i++) {
                JSONObject jsonobject = responseArr.getJSONObject(i);

                if (type.equals("provinces")) {
                    sort.add(jsonobject.getString("provinceName"));
                } else if (type.equals("cities")) {
                    sort.add(jsonobject.getString("cityName"));
                } else {
                    sort.add(jsonobject.getString("schoolName"));
                }

            }
            ArrayAdapter<String> sd = new ArrayAdapter<String>(getView().getContext(),
                    android.R.layout.simple_list_item_1,
                    sort);
            if (type.equals("provinces")) {
                provinceSpinner.setAdapter(sd);
            } else if (type.equals("cities")) {
                citySpinner.setAdapter(sd);
            } else {
                schoolSpinner.setAdapter(sd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData (final String type) {
        final String url = Constant.BASE_URL + type;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        if (type.equals("provinces")) {
                            GlobalVar.getInstance().setProvincies(response.toString());
                        } else if (type.equals("cities")) {
                            GlobalVar.getInstance().setCities(response.toString());
                        } else {
                            GlobalVar.getInstance().setSchools(response.toString());
                        }
                        spinnerData(type);

                        if (GlobalVar.getInstance().getCities() == null) getData("cities");
                        if (GlobalVar.getInstance().getSchools() == null) getData("schools");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                Log.d("obj", "" + obj);
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + GlobalVar.getInstance().getIdToken());
                return map;
            }
        };

// add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
