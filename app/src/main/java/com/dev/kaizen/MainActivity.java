package com.dev.kaizen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.kaizen.base.BaseActivity;
import com.dev.kaizen.base.CustomDialogClass2;
import com.dev.kaizen.menu.QuotesListActivity;
import com.dev.kaizen.util.Constant;
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

        bgImg.setVisibility(ImageView.INVISIBLE);

        copyright.setText("Copyright 2018. Toyota Astra Motor");

        TextView tv = (TextView) v.findViewById(R.id.welcomeText);
        tv.setTypeface(FontUtils.loadFontFromAssets(this, Constant.FONT_BOLD));
        
        tv = (TextView) v.findViewById(R.id.descText);
        tv.setTypeface(FontUtils.loadFontFromAssets(this));
        
        Button btn = (Button) v.findViewById(R.id.loginBtn);
        btn.setTypeface(FontUtils.loadFontFromAssets(this));
        btn.setOnClickListener(this);

        btn = (Button) v.findViewById(R.id.youtubeBtn);
        btn.setTypeface(FontUtils.loadFontFromAssets(this));
        btn.setOnClickListener(this);
        
        ImageView imgV = (ImageView) v.findViewById(R.id.sampleBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.cornerBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.quotesBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.toolsBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.tutorialBtn);
        imgV.setOnClickListener(this);

        imgV = (ImageView) v.findViewById(R.id.testimonialBtn);
        imgV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.sampleBtn) {
            Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
            intent.putExtra("menu", "sample");
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.cornerBtn) {
            Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
            intent.putExtra("menu", "corner");
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.quotesBtn) {
            Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
            intent.putExtra("menu", "quotes");
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.toolsBtn) {
            Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
            intent.putExtra("menu", "tools");
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.tutorialBtn) {
            Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
            intent.putExtra("menu", "tutorial");
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.testimonialBtn) {
            Intent intent = new Intent(MainActivity.this, QuotesListActivity.class);
            intent.putExtra("menu", "testimonial");
            startActivityForResult(intent, 1);
        } else if(v.getId() == R.id.youtubeBtn) {
            final CustomDialogClass2 cd = new CustomDialogClass2(MainActivity.this);
            cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cd.show();
            cd.setCanceledOnTouchOutside(false);
            cd.header.setText("Pesan");
            cd.isi.setText("Menu belum tersedia");
        }
    }
}
