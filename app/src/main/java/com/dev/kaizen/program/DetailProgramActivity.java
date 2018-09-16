package com.dev.kaizen.program;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dev.kaizen.LoginActivity;
import com.dev.kaizen.MainActivity;
import com.dev.kaizen.R;
import com.dev.kaizen.base.BaseActivity;
import com.dev.kaizen.util.FontUtils;

public class DetailProgramActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout baseView = (LinearLayout) getLayoutInflater().inflate(R.layout.lapangan_layout, null);
        setContentView(baseView);

        Button button = (Button) findViewById(R.id.updateBtn);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.cancelBtn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancelBtn) {
            finish();
        } else if(v.getId() == R.id.updateBtn) {
            Intent intent = new Intent(DetailProgramActivity.this, UploadProgramActivity.class);
            startActivityForResult(intent, 1);
        }
    }
}
