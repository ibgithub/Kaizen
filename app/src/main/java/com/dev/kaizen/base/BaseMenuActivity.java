package com.dev.kaizen.base;

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int check = getIntent().getIntExtra("check", R.id.navigation_home);

        if (savedInstanceState != null) {
            navigation.setSelectedItemId(saveState);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (check) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content, HomeFragment.newInstance(), "home");
                    break;
                case R.id.navigation_program:
                    transaction.replace(R.id.content, ProgramFragment.newInstance(), "program");
                    break;
                case R.id.navigation_profile:
                    transaction.replace(R.id.content, ProfileFragment.newInstance(), "profile");
                    break;
                case R.id.navigation_more:
                    transaction.replace(R.id.content, MoreFragment.newInstance(), "more");
                    break;

            }
            transaction.commit();
            navigation.setSelectedItemId(check);
        }

        BottomNavigationViewHelper.disableShiftMode(navigation);
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
}