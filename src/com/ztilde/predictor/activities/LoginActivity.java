package com.ztilde.predictor.activities;

import java.io.IOException;

import org.json.JSONException;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.ztilde.client.Login;
import com.ztilde.client.UnauthorizedException;
import com.ztilde.predictor.R;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
	@ViewById
	ProgressBar progressBar;
	
	@ViewById
	TextView loginErrorMessage;

	@ViewById
	EditText emailLoginText;

	@ViewById
	EditText passwordLoginText;

	@ViewById
	Button loginButton;

	@UiThread
	public void updateProgressBar(int progress) {
		this.progressBar.setProgress(progress);
	}
	
	@UiThread
	public void setErrorText(String message) {
		this.loginErrorMessage.setText(message);
	}

	@Click
	public void loginButtonClicked() {
		this.updateProgressBar(10);
		
		Log.e("log",
				emailLoginText.getText() + " - " + passwordLoginText.getText());
		this.getCredentials(emailLoginText.getText().toString(),
				passwordLoginText.getText().toString());
	}

	@Background
	public void getCredentials(String email, String password) {
		try {
			String apiKey = Login.getApiKey(email, password);
			Log.e("apikey", apiKey);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnauthorizedException e) {
			this.setErrorText("Invalid username or password");
			return;
		}
		this.setErrorText("");
		this.updateProgressBar(100);
	}
}
