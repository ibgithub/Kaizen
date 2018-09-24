package com.dev.kaizen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dev.kaizen.base.BaseActivity;
import com.dev.kaizen.menu.QuoteListActivity;
import com.dev.kaizen.menu.SampleListActivity;
import com.dev.kaizen.util.FontUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinear);

        View v = null;
        if (v == null) {
            LayoutInflater vi = getLayoutInflater();
            v = vi.inflate(R.layout.activity_main, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        layout.addView(v);

        copyright.setText("Copyright 2018. Toyota Astra Motor");

        Button btn = (Button) v.findViewById(R.id.programBtn);
        btn.setTypeface(FontUtils.loadFontFromAssets(this));
        btn.setOnClickListener(this);

        Button btnQuotes = (Button) v.findViewById(R.id.quotesBtn);
        btnQuotes.setTypeface(FontUtils.loadFontFromAssets(this));
        btnQuotes.setOnClickListener(this);

        Button btnSample = (Button) v.findViewById(R.id.sampleBtn);
        btnSample.setTypeface(FontUtils.loadFontFromAssets(this));
        btnSample.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.programBtn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.quotesBtn) {
            Intent intent = new Intent(MainActivity.this, QuoteListActivity.class);
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.sampleBtn) {
            Intent intent = new Intent(MainActivity.this, SampleListActivity.class);
            startActivityForResult(intent, 1);
        }
    }
}
