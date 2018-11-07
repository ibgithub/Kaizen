/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dev.kaizen.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.kaizen.MainActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.FontUtils;
import com.dev.kaizen.util.GlobalVar;


public class MoreFragment extends Fragment implements View.OnClickListener{
    private Context context;

    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
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
        View v = inflater.inflate(R.layout.fragment_more, container, false);

        TextView headertext = (TextView) getActivity().findViewById(R.id.headertext);
        headertext.setText("More");
        headertext.setTypeface(FontUtils.loadFontFromAssets(context, Constant.FONT_SEMIBOLD));

        Button backBtn = (Button) getActivity().findViewById(R.id.backBtn);
        backBtn.setVisibility(Button.INVISIBLE);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(Toolbar.VISIBLE);

        LinearLayout layout = (LinearLayout ) v.findViewById(R.id.teamLayout);
        layout.setOnClickListener(this);

//        layout = (LinearLayout ) v.findViewById(R.id.settingLayout);
//        layout.setOnClickListener(this);

        layout = (LinearLayout ) v.findViewById(R.id.contactLayout);
        layout.setOnClickListener(this);

        layout = (LinearLayout ) v.findViewById(R.id.logoutLayout);
        layout.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.teamLayout) {
            Bundle bundle = new Bundle();
            //bundle.putString("menu", "sample");

            MyTeamFragment fragment2 = new MyTeamFragment();
            fragment2.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
//        } else if(v.getId() == R.id.settingLayout) {
//            SettingFragment fragment2 = new SettingFragment();
//
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.content, fragment2);
//            fragmentTransaction.addToBackStack("home");
//            fragmentTransaction.commit();
        } else if(v.getId() == R.id.contactLayout) {
            ContactUsFragment fragment2 = new ContactUsFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment2);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
        } else if(v.getId() == R.id.logoutLayout) {
            this.logout();
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Apakah Anda yakin untuk keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobalVar.getInstance().setIdToken(null);
                GlobalVar.getInstance().setAccount(null);
                GlobalVar.getInstance().setProfile(null);
                GlobalVar.getInstance().setGrup(null);
                GlobalVar.getInstance().setProgram(null);

//                GlobalVar.getInstance().setProvincies(null);
//                GlobalVar.getInstance().setCities(null);
//                GlobalVar.getInstance().setSchools(null);

                Intent resultData = new Intent();
                resultData.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                resultData.putExtra("wantLogin", "true");
                getActivity().setResult(Activity.RESULT_OK, resultData);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.darkgray));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.darkgray));
            }
        });

        dialog.show();
    }
}
