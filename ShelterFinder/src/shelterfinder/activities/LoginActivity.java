package shelterfinder.activities;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import shelterfinder.tools.BitmapUri;
import shelterfinder.tools.Constants;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.ImageLoader;
import shelterfinder.tools.LoadImageTask;
import shelterfinder.tools.UploadImage;
import shelterfinder.tools.UserFunctions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.shelterfinder.R;

public class LoginActivity extends FragmentActivity implements OnClickListener{
	Button loginButton;
	Button registerButton;
	EditText usernameEditText;
	EditText passwordEditText;
	TextView tvError;
	ImageView ivBack;
	ImageView ivFacebookLogin;
	String picture = null;
	ImageView ivAvatarFacebook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		registerButton = (Button) findViewById(R.id.registerButton);
		registerButton.setOnClickListener(this);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		tvError = (TextView) findViewById(R.id.tv_error);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
		ivAvatarFacebook = (ImageView) findViewById(R.id.iv_avatar_facebook);
		
		ivFacebookLogin = (ImageView) findViewById(R.id.iv_facebook);
		ivFacebookLogin.setOnClickListener(this);
		
		LoginManager.getInstance().registerCallback(MainActivity.callbackManager, new FacebookCallback<LoginResult>() {

		    String id;
		    String name;
		    String email;

		    @Override
		    public void onSuccess(LoginResult loginResult) {

		        System.out.println("onSuccess");
		        GraphRequest request = GraphRequest.newMeRequest
		                (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
		                    @Override
		                    public void onCompleted(JSONObject object, GraphResponse response) {
		                        // Application code
		                        Log.v("LoginActivity", response.toString());
		                        //System.out.println("Check: " + response.toString());
		                        try {
		                            id = object.getString("id");
		                            
		                            name = object.getString("name");
		                            email = object.getString("email");
		                            picture= object.getJSONObject("picture").getJSONObject("data").getString("url");
		                
		                            
		                            
		                            Log.i("id", id);
		                            Log.i("name", name);
		                            Log.i("picture", picture);
		                            Log.i("email", email);
		                            

		                   
		                        } catch (JSONException e) {
		                            e.printStackTrace();
		                            email = "";
		                            
		                        }
		                        registerFacebook(id, "", email, name);
		                    }
		                    
		                 
		                });
		                Bundle parameters = new Bundle();
		                parameters.putString("fields", "id,name,email,gender, picture");
				        request.setParameters(parameters);
				        request.executeAsync();
		    }
		    @Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				
			}

		        

				
	});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginButton:
			login();
			break;
		case R.id.registerButton:
			register();
			break;
		case R.id.iv_back:
			finish();
			break;	
		case R.id.iv_facebook:
			logInFacebook();
			break;
		
		}
	}
	
	
	private void logInFacebook() {
		LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_about_me"));

	}
	
	private void registerFacebook(String username, String password, String email, String fullName) {
		new RegisterFacebookTask().execute(username, password, email, fullName);
	}
	
	
	class RegisterFacebookTask extends AsyncTask<String, Void, ArrayList<String>> {
		
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			int registerUserID = userFunctions.registerUser(params[0], params[1], params[2], params[3]);
			if (registerUserID != -1 && picture != null) {
				ImageLoader imageLoader = new ImageLoader(getApplicationContext());
				imageLoader.DisplayImage(picture, R.drawable.no_avatar, ivAvatarFacebook);
				Log.i("Upload", "Start upload facebook avatar");
				Uri uriFacebook = BitmapUri.getLocalBitmapUri(ivAvatarFacebook);
				String tempPath = uriFacebook.getPath();
				UploadImage uploadImage = new UploadImage();
				uploadImage.uploadFile(tempPath, registerUserID + "", true);
				
			}
			ArrayList<String> userFacebook = new ArrayList<String>();
			userFacebook.add(params[0]);
			userFacebook.add(params[1]);
			return userFacebook;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			new LoginTask().execute(result.get(0), result.get(1));
		}
		
	}

	class LoginTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			MainActivity.userLogin = userFunctions.loginUser(params[0], params[1]);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if (MainActivity.userLogin != null) {
				Log.i(getClass().getName(), MainActivity.userLogin.toString());
				CustomToast.showToast("Bạn đã đăng nhập thành công", getApplicationContext());
				tvError.setText("");
				Intent data = new Intent();
				data.putExtra("FullName", MainActivity.userLogin.getFullName());
				data.putExtra("Avatar", MainActivity.userLogin.getAvatar());
				MainActivity.userFragment.onActivityResult(Constants.LOGIN_CODE, Constants.GET_USER_LOGIN_CODE, data);
				finish();
			}
			
			else {
				tvError.setText("Tài khoản hoặc mật khẩu không chính xác");
			}
		}
	}
	
	private void login() {
		String username = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		new LoginTask().execute(username, password);
	}
	
	private void register() {
		Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(registerIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		MainActivity.callbackManager.onActivityResult(requestCode, resultCode, data);
	}

}
