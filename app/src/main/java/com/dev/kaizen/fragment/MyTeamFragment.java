/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.ForgotPasswordActivity;
import com.dev.kaizen.MainActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.adapter.Quotes;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.menu.QuotesListActivity;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyTeamFragment extends Fragment implements View.OnClickListener{
    private Context context;
    RequestQueue queue;
    ListView listMember;
    Button updateTeamBtn;

    public static MyTeamFragment newInstance() {
        MyTeamFragment fragment = new MyTeamFragment();
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
        View v = inflater.inflate(R.layout.fragment_team, container, false);

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.INVISIBLE);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.GONE);

        TextView schoolText = (TextView) v.findViewById(R.id.schoolText);
        listMember = (ListView) v.findViewById(R.id.listMember);

        updateTeamBtn = (Button) v.findViewById(R.id.updateTeamBtn);
        updateTeamBtn.setOnClickListener(this);

        queue = Volley.newRequestQueue(getActivity());

        if(GlobalVar.getInstance().getProfile() == null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Pesan")
                    .setMessage("Anda belum mengisi data Profile diri Anda.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        } else {
            try {
                JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
                JSONObject school = profile.getJSONObject("school");
                schoolText.setText(school.getString("schoolName"));

                JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
                Log.d("userId", "" + account.getLong("id"));
                getParticipants(account.getLong("id"));

            } catch (JSONException ex) {

            }

        }

        return v;
    }

    private void getParticipants (Long userId) {
        final String url = Constant.BASE_URL + "groupNamesByUserId/" + userId ;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());

                        try {
                            JSONArray responseArr = new JSONArray(response.toString());

                            ArrayList<String> sort = new ArrayList<String>();

                            for (int i = 0; i < responseArr.length(); i++) {
                                JSONObject jsonobject = responseArr.getJSONObject(i);
                                sort.add(jsonobject.getString("fullName"));
                            }
                            ArrayAdapter<String> sd = new ArrayAdapter<String>(getView().getContext(),
                                    android.R.layout.simple_list_item_1,
                                    sort);
                            listMember.setAdapter(sd);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

                                final CustomDialogClass2 cd = new CustomDialogClass2(getActivity());
                                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cd.show();
                                cd.setCanceledOnTouchOutside(false);
                                cd.header.setText(obj.getString("title"));
                                cd.isi.setText("Anda belum memiliki Tim, silahkan membuat Tim Baru");

                                updateTeamBtn.setText("Create Team");
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
        if(v.getId() == R.id.updateBtn) {

        }
    }
}
