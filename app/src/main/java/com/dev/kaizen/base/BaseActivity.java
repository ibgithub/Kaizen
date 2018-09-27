package com.dev.kaizen.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.kaizen.R;
import com.dev.kaizen.util.FontUtils;

public class BaseActivity extends AppCompatActivity {
    protected RelativeLayout baseView;
    public TextView copyright;
    public ImageView bgImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseView = (RelativeLayout) getLayoutInflater().inflate(R.layout.base_page, null);
        setContentView(baseView);

        copyright = (TextView) baseView.findViewById(R.id.copyright);
        copyright.setTypeface(FontUtils.loadFontFromAssets(this));

        bgImg = (ImageView) baseView.findViewById(R.id.bgImg);
    }
}
