package shelterfinder.activities;


import shelterfinder.tools.BitmapUri;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.UploadImage;
import shelterfinder.tools.UserFunctions;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.shelterfinder.R;

public class RegisterActivity extends Activity implements OnClickListener {
	Button btRegister;
	EditText usernameEditText;
	EditText passwordEditText;
	EditText retypeEditText;
	EditText emailEditText;
	EditText fullNameEditText;
	ImageView imageAvatar;
	TextView tvError;
	ImageView ivBack;
	String imagePath = null;
	String tempPath = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
		
		btRegister = (Button) findViewById(R.id.bt_register);
		btRegister.setOnClickListener(this);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		retypeEditText = (EditText) findViewById(R.id.retypeEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		fullNameEditText = (EditText) findViewById(R.id.fullNameEditText);
		imageAvatar = (ImageView) findViewById(R.id.iv_avatar_user);
		tvError = (TextView) findViewById(R.id.tv_error);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();			
			}
		});
		
		imageAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_register:
			registerUser();
			
			break;
		}
		
	}
	
	private void registerUser() {
		String username = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		String retypePassword = retypeEditText.getText().toString().trim();
		String email = emailEditText.getText().toString().trim();
		String fullName = fullNameEditText.getText().toString().trim();
		
		if (username.equals("") || password.equals("")
				|| retypePassword.equals("") || email.equals("")
				|| fullName.equals("")) {
			tvError.setText("Vui lòng nhập hết dữ liệu yêu cầu");
			return;
		}
		
		if (!password.equals(retypePassword)) {
			tvError.setText("Mật khẩu và mật khẩu nhập lại không khớp");
			passwordEditText.setText("");
			retypeEditText.setText("");
			return;
		}
		
		new RegisterTask().execute(username, password, email, fullName);
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				imagePath = getPath(selectedImageUri);
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
				imageAvatar.setImageBitmap(bitmap);
			}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	
	class RegisterTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			int registerUserID = userFunctions.registerUser(params[0], params[1], params[2], params[3]);
			return registerUserID;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (result == -1) {
				tvError.setText("Tài khoản đã tồn tại");
			}
			else {
				CustomToast.showToast("Bạn đã đăng ký thành công", getApplicationContext());
				if (imagePath != null) {
					Uri bitmapUri = BitmapUri.getLocalBitmapUri(imageAvatar);
					tempPath =  bitmapUri.getPath();
					Log.i("Upload", tempPath);
					new UploadAvatarTask().execute(result + "");
				}
				finish();
			}
		}
		
	}
	
	class UploadAvatarTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			UploadImage uploadImage = new UploadImage();
			uploadImage.uploadFile(tempPath, params[0], true);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.i("UploadImage", "Tải ảnh lên thành công");
		}
		
	}
	
}
