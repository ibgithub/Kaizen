package com.dev.kaizen.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev.kaizen.R;

public class ProgressDialog2 extends Dialog {
	public Context c;
	public Dialog d;
	public TextView isi;
	public ProgressBar progress;
	
	public ProgressDialog2(Context applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
		this.c = applicationContext;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress);
		
		isi = (TextView) findViewById(R.id.txt_isi);
		progress = (ProgressBar) findViewById(R.id.progressBar1);
	}
}