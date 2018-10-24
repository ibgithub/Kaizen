package com.dev.kaizen.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.kaizen.R;
import com.dev.kaizen.fragment.HomeFragment;
import com.dev.kaizen.fragment.MoreFragment;
import com.dev.kaizen.fragment.ProfileFragment;
import com.dev.kaizen.fragment.ProgramFragment;
import com.dev.kaizen.util.BottomNavigationViewHelper;
import com.dev.kaizen.util.Constant;
import com.dev.kaizen.util.GlobalVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseMenuActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.headertext)
    TextView headertext;

    private int saveState;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment selectedFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    transaction.replace(R.id.content, selectedFragment, "home");
                    break;
                case R.id.navigation_program:
                    selectedFragment = ProgramFragment.newInstance();
                    transaction.replace(R.id.content, selectedFragment, "program");
                    break;
                case R.id.navigation_profile:
                    selectedFragment = ProfileFragment.newInstance();
                    transaction.replace(R.id.content, selectedFragment, "profile");
                    break;
                case R.id.navigation_more:
                    selectedFragment = MoreFragment.newInstance();
                    transaction.replace(R.id.content, selectedFragment, "more");
                    break;

            }
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_menu);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int check = getIntent().getIntExtra("check", R.id.navigation_home);

        if (savedInstanceState != null) {
            navigation.setSelectedItemId(saveState);
        } else {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            switch (check) {
//                case R.id.navigation_home:
//                    transaction.replace(R.id.content, HomeFragment.newInstance(), "home");
//                    break;
//                case R.id.navigation_program:
//                    transaction.replace(R.id.content, ProgramFragment.newInstance(), "program");
//                    break;
//                case R.id.navigation_profile:
//                    transaction.replace(R.id.content, ProfileFragment.newInstance(), "profile");
//                    break;
//                case R.id.navigation_more:
//                    transaction.replace(R.id.content, MoreFragment.newInstance(), "more");
//                    break;
//
//            }
//            transaction.commit();
            navigation.setSelectedItemId(check);

        }

        BottomNavigationViewHelper.disableShiftMode(navigation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(saveState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveState = navigation.getSelectedItemId();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        logout();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda yakin untuk keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobalVar.getInstance().setIdToken(null);
                GlobalVar.getInstance().setAccount(null);
                GlobalVar.getInstance().setProfile(null);
                GlobalVar.getInstance().setGrup(null);
                GlobalVar.getInstance().setProgram(null);

                GlobalVar.getInstance().setProvincies(null);
                GlobalVar.getInstance().setCities(null);
                GlobalVar.getInstance().setSchools(null);

                Intent resultData = new Intent();
                resultData.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                resultData.putExtra("wantLogin", "true");
                setResult(Activity.RESULT_OK, resultData);
                finish();
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