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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.base.CustomDialogClass2;
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

    private LinearLayout dataLayout, tambahLayout;
    private Button tambahButton;
    private boolean isTambah;

    private JSONObject selectedProv, selectedKota, selectedSekolah;

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

        dataLayout = (LinearLayout) v.findViewById(R.id.dataLayout);
        tambahLayout = (LinearLayout) v.findViewById(R.id.tambahLayout);
        tambahButton = (Button) v.findViewById(R.id.tambahButton);
        tambahButton.setOnClickListener(this);

        String tambahText = "Nama sekolah tidak ada dalam daftar? Tambah Baru";
        SpannableString text = new SpannableString(tambahText);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkgray)), 0, tambahText.length()-12, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background)), tambahText.length()-12, tambahText.length(), 0);
        tambahButton.setText(text, TextView.BufferType.SPANNABLE);

        isTambah = false;

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
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (GlobalVar.getInstance().getProvincies() == null) {
                    getData("provinces", 0);
                } else {
                    try {
                        JSONArray arr = new JSONArray(GlobalVar.getInstance().getProvincies());
                        getData("citiesByProvinceId", Integer.valueOf(arr.getJSONObject(i).getString("id")));

                        selectedProv = arr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citySpinner = (Spinner) v.findViewById(R.id.citySpinner);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (GlobalVar.getInstance().getProvincies() == null) {
                    getData("provinces", 0);
                } else {
                    try {
                        JSONArray arr = new JSONArray(GlobalVar.getInstance().getCities());
                        selectedKota = arr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        schoolSpinner = (Spinner) v.findViewById(R.id.schoolSpinner);
        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (GlobalVar.getInstance().getSchools() == null) {
                    getData("schools", 0);
                } else {
                    try {
                        JSONArray arr = new JSONArray(GlobalVar.getInstance().getSchools());
                        selectedSekolah = arr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        getData("schools", 0);

//        if (GlobalVar.getInstance().getProvincies() == null) {

//        }

        return v;
    }

    private void spinnerData (final String type) {
        String str = "";
        if (type.equals("provinces")) {
            str = GlobalVar.getInstance().getProvincies();
        } else if (type.equals("citiesByProvinceId")) {
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
                } else if (type.equals("citiesByProvinceId")) {
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
            } else if (type.equals("citiesByProvinceId")) {
                citySpinner.setAdapter(sd);
            } else {
                schoolSpinner.setAdapter(sd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData (final String type, int province) {
        final String url = Constant.BASE_URL + type + ((type.equals("citiesByProvinceId"))? "/"+province:"");

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response type", response.toString());
                        if (type.equals("provinces")) {
                            GlobalVar.getInstance().setProvincies(response.toString());
                            try {
                                JSONArray responseArr = new JSONArray(GlobalVar.getInstance().getProvincies());
//                                if (GlobalVar.getInstance().getCities() == null)
                                    getData("citiesByProvinceId", Integer.valueOf(responseArr.getJSONObject(0).getString("id")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (type.equals("citiesByProvinceId")) {
                            GlobalVar.getInstance().setCities(response.toString());
                        } else {
                            GlobalVar.getInstance().setSchools(response.toString());
                            getData("provinces", 0);
                        }
                        spinnerData(type);
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
        } else if(v.getId() == R.id.saveBtn) {
            if(firstNameEdit.getText().toString().length() < 1) {
                Toast.makeText(getContext(), "Nama depan masih kosong", Toast.LENGTH_LONG).show();
                firstNameEdit.requestFocus();
            } else if(lastNameEdit.getText().toString().length() < 1) {
                Toast.makeText(getContext(), "Nama belakang masih kosong", Toast.LENGTH_LONG).show();
                lastNameEdit.requestFocus();
            } else if(emailEdit.getText().toString().length() < 1) {
                Toast.makeText(getContext(), "Email masih kosong", Toast.LENGTH_LONG).show();
                emailEdit.requestFocus();
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString().trim()).matches()) {
                Toast.makeText(getContext(), "Email tidak valid", Toast.LENGTH_LONG).show();
                emailEdit.requestFocus();
            } else if(addressEdit.getText().toString().length() < 1) {
                Toast.makeText(getContext(), "Alamat masih kosong", Toast.LENGTH_LONG).show();
                addressEdit.requestFocus();
            } else if(classEdit.getText().toString().length() < 1) {
                Toast.makeText(getContext(), "Kelas masih kosong", Toast.LENGTH_LONG).show();
                classEdit.requestFocus();
            } else {
                if(isTambah && schoolEdit.getText().toString().length() < 1) {
                    Toast.makeText(getContext(), "Nama sekolah masih kosong", Toast.LENGTH_LONG).show();
                    schoolEdit.requestFocus();
                } else {
                    int kelas = Integer.valueOf(classEdit.getText().toString());
                    if(kelas < 10 || kelas > 12) {
                        Toast.makeText(getContext(), "Kelas tidak valid", Toast.LENGTH_LONG).show();
                        classEdit.requestFocus();
                    } else {
                        putProfile();
                    }
                }
            }
        } else if(v.getId() == R.id.tambahButton) {
            isTambah = !isTambah;
            if(isTambah) {
                String tambahText = "Nama sekolah tidak ada dalam daftar? Kembali";
                SpannableString text = new SpannableString(tambahText);
                text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkgray)), 0, tambahText.length()-7, 0);
                text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background)), tambahText.length()-7, tambahText.length(), 0);
                tambahButton.setText(text, TextView.BufferType.SPANNABLE);

                tambahLayout.setVisibility(LinearLayout.VISIBLE);
                dataLayout.setVisibility(LinearLayout.GONE);
            } else {
                String tambahText = "Nama sekolah tidak ada dalam daftar? Tambah Baru";
                SpannableString text = new SpannableString(tambahText);
                text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkgray)), 0, tambahText.length()-12, 0);
                text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background)), tambahText.length()-12, tambahText.length(), 0);
                tambahButton.setText(text, TextView.BufferType.SPANNABLE);

                tambahLayout.setVisibility(LinearLayout.GONE);
                dataLayout.setVisibility(LinearLayout.VISIBLE);
            }
        }
    }

    private void putProfile() {
        String url = Constant.BASE_URL + "participantComplete";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        // prepare the Request
        try {
            JSONObject json = new JSONObject(GlobalVar.getInstance().getProfile());

            JSONObject userJson = json.getJSONObject("user");
            userJson.put("firstName", firstNameEdit.getText().toString().trim());
            userJson.put("lastName", lastNameEdit.getText().toString().trim());
            userJson.put("email", emailEdit.getText().toString().trim());

            json.put("fullName", firstNameEdit.getText().toString().trim() + " " + lastNameEdit.getText().toString().trim());
            json.put("schoolClass", classEdit.getText().toString().trim());
            json.put("address", addressEdit.getText().toString().trim());

//            json.remove("school");

            JSONObject schoolObj = new JSONObject();
            if(!isTambah) {
                schoolObj.put("desc", "OLD");
                schoolObj.put("id", selectedSekolah.getInt("id"));
            } else {
                schoolObj.put("desc", "NEW");
                schoolObj.put("schoolName", selectedSekolah.getString("schoolName"));

                JSONObject kotaObj = new JSONObject();
                kotaObj.put("id", selectedKota.getInt("id"));
                schoolObj.put("city", kotaObj);
            }
            json.put("school", schoolObj);

            Log.d("getprofile", GlobalVar.getInstance().getProfile());
            Log.d("json", json.toString());

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, json,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("response put user", response.toString());
                            GlobalVar.getInstance().setProfile(response.toString());
                            Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            queue.add(postRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
