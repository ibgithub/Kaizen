package com.dev.kaizen.menu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.dev.kaizen.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class SampleListActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_list);

        Uri uri = Uri.parse("http://156.67.221.248:2082/kaizen/menucontent/quotes/Slide1.PNG");
        SimpleDraweeView draweeView = findViewById(R.id.sample_image);
        draweeView.setImageURI(uri);
    }
}
