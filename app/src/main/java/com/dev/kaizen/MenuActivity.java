package com.dev.kaizen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.kaizen.program.DetailProgramActivity;
import com.dev.kaizen.util.FontUtils;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextMessage, belumText;
    private LinearLayout mainLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
//                case R.id.navigation_timeline:
//                    mTextMessage.setText(R.string.title_timeline);
//                    belumText.setVisibility(TextView.VISIBLE);
//                    mainLayout.setVisibility(LinearLayout.GONE);
//                    return true;
                case R.id.navigation_program:
                    mTextMessage.setText(R.string.title_program);
                    belumText.setVisibility(TextView.GONE);
                    mainLayout.setVisibility(LinearLayout.VISIBLE);

//                    LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);

                    mainLayout.removeAllViews();
                    View v = null;
                    if (v == null) {
                        LayoutInflater vi = getLayoutInflater();
                        v = vi.inflate(R.layout.layout_program, null);
                        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                    mainLayout.addView(v);

                    Button detailBtn = (Button) v.findViewById(R.id.detailBtn);
                    detailBtn.setOnClickListener(MenuActivity.this);

                    detailBtn = (Button) v.findViewById(R.id.detailBtn2);
                    detailBtn.setOnClickListener(MenuActivity.this);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    belumText.setVisibility(TextView.VISIBLE);
                    mainLayout.setVisibility(LinearLayout.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setTypeface(FontUtils.loadFontFromAssets(this));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        belumText = (TextView) findViewById(R.id.belumText);
        belumText.setTypeface(FontUtils.loadFontFromAssets(this));
        belumText.setVisibility(TextView.VISIBLE);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.detailBtn || v.getId() == R.id.detailBtn2) {
            Intent intent = new Intent(MenuActivity.this, DetailProgramActivity.class);
            startActivityForResult(intent, 1);
        }
    }
}
