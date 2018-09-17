package com.dev.kaizen.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dev.kaizen.R;

public class CustomDialogClass2 extends Dialog implements View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes;
	public TextView header, isi;
	
	public CustomDialogClass2(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog2);
		yes = (Button) findViewById(R.id.btn_yes);
		yes.setOnClickListener(this);
		
		header = (TextView) findViewById(R.id.txt_header);
		isi = (TextView) findViewById(R.id.txt_isi);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
		  dismiss();
		  break;
		default:
		  break;
		}
		dismiss();
	}

}