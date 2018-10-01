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

        TextView nameText = (TextView) v.findViewById(R.id.nameText);
        TextView emailText = (TextView) v.findViewById(R.id.emailText);
        TextView addressText = (TextView) v.findViewById(R.id.addressText);
        TextView schoolText = (TextView) v.findViewById(R.id.schoolText);
        TextView classText = (TextView) v.findViewById(R.id.classText);

        Button updateBtn = (Button) v.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);

        try {
            JSONObject account = new JSONObject(GlobalVar.getInstance().getAccount());
            nameText.setText(account.getString("firstName") + " " + account.getString("lastName"));
            emailText.setText(account.getString("email"));

            JSONObject profile = new JSONObject(GlobalVar.getInstance().getProfile());
            addressText.setText(profile.getString("address"));
            classText.setText(profile.getString("schoolClass"));

            JSONObject school = profile.getJSONObject("school");
            schoolText.setText(school.getString("schoolName"));

        } catch (JSONException ex) {

        }


        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
