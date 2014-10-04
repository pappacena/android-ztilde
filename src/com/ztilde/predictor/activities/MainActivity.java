package com.ztilde.predictor.activities;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.ztilde.client.Classifier;
import com.ztilde.client.UnauthorizedException;
import com.ztilde.client.ZTilde;
import com.ztilde.predictor.R;
import com.ztilde.predictor.utils.CredentialManager;
import com.ztilde.predictor.utils.Listener;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {
	private String[] menuItens;
	private final String title = "ZTilde";
	private ZTilde[] ztildes;
	
	@ViewById
	ProgressBar progressBar;

	@ViewById
	DrawerLayout drawerLayout;

	@ViewById
	ListView leftDrawer;

	ActionBarDrawerToggle drawerToggle;

	@AfterViews
	public void init() {
		this.ztildes = new ZTilde[0];
		makeMenu();
		this.updateZTildesMenu();
	}

	@Background
	public void updateZTildesMenu() {
		Context context = getApplicationContext();
		CredentialManager m = CredentialManager.getInstance(context);
		String apiKey = m.getApiKey();
		
		try {
			this.ztildes = ZTilde.getList(apiKey);
			this.makeMenu();
		} catch (UnauthorizedException e) {
			String msg = "You are not authorized to access ZTIlde.com.\n";
			msg += "Please, check your account.";
			this.displayMessage(msg);
		} catch (Exception e) {
			String msg = "An unexpected error has occoured. Please, try again latter";
			this.displayMessage(msg);
		}
	}
	
	@UiThread
	public void updateProgressBar(int value) {
		this.progressBar.setProgress(value);
	}
	
	@UiThread
	public void displayMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	

	@UiThread
	public void makeMenu() {
		menuItens = new String[this.ztildes.length + 1];
		Log.e("num", "" + this.ztildes.length);
		menuItens[0] = "Your ZTildes";
		for (int i = 0; i < this.ztildes.length; i++) {
			String name = ztildes[i].getName();
			if(ztildes[i] instanceof Classifier) {
				name += " (classifier)";
			} else {
				name += " (clustering)";
			}
			menuItens[i + 1] = name;
		}

		// Set the adapter for the list view
		leftDrawer.setAdapter(new ArrayAdapter<String>(this,
				R.layout.main_menu_list_item, menuItens));
		// Set the list's click listener
		leftDrawer.setOnItemClickListener(new MenuItemClickListener());

		this.initActionMenuIcon();
		// this.activateFragment(0);
	}

	private boolean isFirstLogin() {
		boolean firstLogin;
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			firstLogin = false;
		} else {
			firstLogin = extras.getBoolean("firstLogin");
		}
		return firstLogin;
	}

	/**
	 * Initialize stuff to make the menu icon work
	 */
	private void initActionMenuIcon() {
		drawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.open_menu, /* "open drawer" description */
		R.string.close_menu /* "close drawer" description */
		);

		drawerLayout.setDrawerListener(drawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		// drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	public void activateFragment(int position) {
		Fragment fragment = null;
		String title = this.title + " - ";
		switch (position) {
		case 0:
			// fragment = this.getFastTransactionFragment();
			title += "Despesa rápida";
			break;
		case 1:
			// fragment = this.getCreateTransactionFragment();
			title += "Criar movimentação";
			break;
		case 2:
			// fragment = this.getListTransactionFragment();
			title += "Ver movimentações";
			break;
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.contentFrame, fragment)
				.commit();

		leftDrawer.setItemChecked(position, true);
		drawerLayout.closeDrawer(leftDrawer);
		getActionBar().setTitle(title);
	}

	class MenuItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			activateFragment(position);
		}

	}

}
