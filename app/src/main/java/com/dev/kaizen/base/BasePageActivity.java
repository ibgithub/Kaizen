package com.dev.kaizen.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dev.kaizen.R;
import com.dev.kaizen.util.FontUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BasePageActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.headertext)
    public TextView headertext;
    @BindView(R.id.backBtn)
    Button backBtn;
    @BindView(R.id.nextBtn)
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_page2);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headertext.setTypeface(FontUtils.loadFontFromAssets(this));

        backBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backBtn) {
            super.onBackPressed();
        }
    }
}