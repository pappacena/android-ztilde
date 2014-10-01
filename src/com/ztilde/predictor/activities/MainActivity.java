package com.ztilde.predictor.activities;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.ztilde.predictor.R;
import com.ztilde.predictor.utils.Listener;

import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@AfterViews
	protected void init() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
