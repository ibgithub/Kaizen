/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.app.ProgressDialog;
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
import com.dev.kaizen.LoginActivity;
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
import java.util.List;
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

    private JSONArray arrProv, arrKota, arrSekolah;
    private JSONObject selectedProv, selectedKota, selectedSekolah;
    private ArrayList<String> provList = new ArrayList<String>();
    private ArrayList<String> kotaList = new ArrayList<String>();
    private ArrayList<String> sekolahList = new ArrayList<String>();
    private ArrayAdapter<String> provAdapter, kotaAdapter, sekolahAdapter;

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
                if (account.getString("firstName") != null && !account.getString("firstName").equals("null")) {
                    firstNameEdit.setText(account.getString("firstName"));
                }
                if (account.getString("lastName") != null && !account.getString("lastName").equals("null")) {
                    lastNameEdit.setText(account.getString("lastName"));
                }
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

        Button saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        getProv();
        provinceSpinner = (Spinner) v.findViewById(R.id.provinceSpinner);
        provAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, provList);
        provinceSpinner.setAdapter(provAdapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                try {
                    selectedProv = arrProv.getJSONObject(position);
                    Log.d("selectedProv:", "" + selectedProv.getInt("id"));
                    getKota(selectedProv.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        citySpinner = (Spinner) v.findViewById(R.id.citySpinner);
        kotaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, kotaList);
        citySpinner.setAdapter(kotaAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                try {
                    selectedKota = arrKota.getJSONObject(position);
                    Log.d("selectedKota:", "" + selectedKota.getInt("id"));
                    getSekolah(selectedKota.getInt("id"));
                } catch (JSONException e) {
                    Log.d("e=", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        schoolSpinner = (Spinner) v.findViewById(R.id.schoolSpinner);
        sekolahAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sekolahList);
        schoolSpinner.setAdapter(sekolahAdapter);
        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                try {
                    selectedSekolah = arrSekolah.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        return v;
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
            } else if(isTambah && schoolEdit.getText().toString().length() < 1) {
                Toast.makeText(getContext(), "Nama sekolah masih kosong", Toast.LENGTH_LONG).show();
                schoolEdit.requestFocus();
            } else if(!isTambah && schoolSpinner.getSelectedItem() == null) {
                Toast.makeText(getContext(), "Nama sekolah masih kosong", Toast.LENGTH_LONG).show();
                schoolSpinner.requestFocus();
            } else {
                int kelas = Integer.valueOf(classEdit.getText().toString());
                if(kelas < 10 || kelas > 12) {
                    Toast.makeText(getContext(), "Kelas tidak valid", Toast.LENGTH_LONG).show();
                    classEdit.requestFocus();
                } else {
                    putProfile();
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
            JSONObject json = new JSONObject();
            JSONObject userJson = new JSONObject();

            if ( GlobalVar.getInstance().getAccount() != null ) {
                userJson = new JSONObject(GlobalVar.getInstance().getAccount());
            }
            if ( GlobalVar.getInstance().getProfile() != null ) {
                json = new JSONObject(GlobalVar.getInstance().getProfile());

                userJson = json.getJSONObject("user");
            }

            json.put("fullName", firstNameEdit.getText().toString().trim() + " " + lastNameEdit.getText().toString().trim());
            json.put("schoolClass", classEdit.getText().toString().trim());
            json.put("address", addressEdit.getText().toString().trim());

            userJson.put("firstName", firstNameEdit.getText().toString().trim());
            userJson.put("lastName", lastNameEdit.getText().toString().trim());
            userJson.put("email", emailEdit.getText().toString().trim());

            json.put("user", userJson);

            JSONObject schoolObj = new JSONObject();
            if(!isTambah) {
                schoolObj.put("desc", "OLD");
                schoolObj.put("id", selectedSekolah.getInt("id"));
            } else {
                schoolObj.put("desc", "NEW");
                schoolObj.put("schoolName", schoolEdit.getText().toString().trim());

                JSONObject kotaObj = new JSONObject();
                kotaObj.put("id", selectedKota.getInt("id"));
                schoolObj.put("city", kotaObj);
                Log.d("schoolObj 1", schoolObj.toString());
            }

            json.put("school", schoolObj);
            Log.d("schoolObj 2", schoolObj.toString());
            Log.d("json", json.toString());

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, json,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response put user", response.toString());
                        GlobalVar.getInstance().setProfile(response.toString());

                        Toast.makeText(getContext(), "Data berhasil disimpan", Toast.LENGTH_LONG).show();
                        //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        Bundle bundle = new Bundle();
                        //bundle.putParcelable("item", groupTeam);

                        ProfileFragment fragment2 = new ProfileFragment();
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
            queue.add(postRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProv() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Mengambil data...");
        dialog.show();

        final String url = Constant.BASE_URL + "provinces";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Res provinces", response.toString());
                        try {
                            arrProv = new JSONArray(response.toString());
                            Long provId = null;
                            if (GlobalVar.getInstance().getProfile() != null ) {
                                JSONObject objProfile = new JSONObject(GlobalVar.getInstance().getProfile());
                                JSONObject objSchool = objProfile.getJSONObject("school");
                                JSONObject objCity = objSchool.getJSONObject("city");
                                JSONObject objProv = objCity.getJSONObject("province");

                                provId = objProv.getLong("id");
//                            getKota(objProv.getInt("id"), objCity.getInt("id"));
//                            getSekolah(objCity.getInt("id"), objSchool.getInt("id"));
                            } else {
                                provId = arrProv.getJSONObject(0).getLong("id");
                            }

                            Log.d("provId=", "" + provId);
                            for(int i=0; i<arrProv.length(); i++) {
                                provList.add(arrProv.getJSONObject(i).getString("provinceName"));
                                if(arrProv.getJSONObject(i).getInt("id") == provId.intValue()) {
                                    provinceSpinner.setSelection(i);
                                }
                            }
                            provAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
        queue.add(getRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                if (dialog !=  null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    private void getKota(int idProv) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Mengambil data...");
        dialog.show();

        final String url = Constant.BASE_URL + "citiesByProvinceId/" + idProv;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Res city", response.toString());

                        try {
                            kotaList.clear();

                            arrKota = new JSONArray(response.toString());
                            Long kotaId = null;
                            if (GlobalVar.getInstance().getProfile() != null ) {
                                JSONObject objProfile = new JSONObject(GlobalVar.getInstance().getProfile());
                                JSONObject objSchool = objProfile.getJSONObject("school");
                                JSONObject objCity = objSchool.getJSONObject("city");

                                kotaId = objCity.getLong("id");
                            } else {
                                kotaId = arrKota.getJSONObject(0).getLong("id");
                            }

                            Log.d("kotaId res=", "" + kotaId);

                            for(int i=0; i<arrKota.length(); i++) {
                                kotaList.add(arrKota.getJSONObject(i).getString("cityName"));
                                if(arrKota.getJSONObject(i).getInt("id") == kotaId.intValue()) {
                                    citySpinner.setSelection(i);
                                    selectedKota = arrKota.getJSONObject(i);
                                }
                            }
                            kotaAdapter.notifyDataSetChanged();

                            getSekolah(kotaId.intValue());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("city Err=", error.getMessage());
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
        queue.add(getRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                if (dialog !=  null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    private void getSekolah(int idKota) {
        Log.d("idKota=", "" + idKota);
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Mengambil data...");
        dialog.show();

        final String url = Constant.BASE_URL + "schoolsByCityId/" + idKota;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Res school", response.toString());

                        try {
                            sekolahList.clear();

                            arrSekolah = new JSONArray(response.toString());
                            Long schoolId = null;
                            if (GlobalVar.getInstance().getProfile() != null ) {
                                JSONObject objProfile = new JSONObject(GlobalVar.getInstance().getProfile());
                                JSONObject objSchool = objProfile.getJSONObject("school");

                                schoolId = objSchool.getLong("id");
//                            getSekolah(objCity.getInt("id"), objSchool.getInt("id"));
                            } else {
                                schoolId = arrSekolah.getJSONObject(0).getLong("id");
                            }

                            Log.d("schoolId=", "" + schoolId);

                            for(int i=0; i<arrSekolah.length(); i++) {
                                sekolahList.add(arrSekolah.getJSONObject(i).getString("schoolName"));
                                if(arrSekolah.getJSONObject(i).getInt("id") == schoolId.intValue()) {
                                    schoolSpinner.setSelection(i);
                                }
                            }
                            sekolahAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("school Err=", error.getMessage());
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
        queue.add(getRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                if (dialog !=  null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }
}
