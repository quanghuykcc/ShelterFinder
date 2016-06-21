package shelterfinder.activities;

import shelterfinder.activities.RegisterActivity.UploadAvatarTask;
import shelterfinder.adapters.SpinnerCustomAdapter;
import shelterfinder.objects.MotelRoom;
import shelterfinder.tools.BitmapUri;
import shelterfinder.tools.Constants;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.GoogleMapFunctions;
import shelterfinder.tools.MotelRoomFunctions;
import shelterfinder.tools.UploadImage;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.shelterfinder.R;

public class PostRoomActivity extends FragmentActivity {
	GoogleMap googleMap;
	EditText etAddress, etPrice, etArea, etDescription, etPhone;
	Spinner spCity;
	ImageView ivBack;
	ImageView ivRoomPost;
	Button btSubmit;
	double latitude;
	double longitude;
	String imagePath = null;
	String tempPath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_post_room);
		etAddress = (EditText) findViewById(R.id.et_address);
		etPrice = (EditText) findViewById(R.id.et_price);
		etArea = (EditText) findViewById(R.id.et_area);
		etDescription = (EditText) findViewById(R.id.et_description);
		etPhone = (EditText) findViewById(R.id.et_phone_contact);
		latitude = 0;
		longitude = 0;
		
		spCity = (Spinner) findViewById(R.id.spinner_city);
		SpinnerCustomAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.custom_spinner, Constants.CITY_LIST);
		spCity.setAdapter(cityAdapter);
		
		
		btSubmit = (Button) findViewById(R.id.bt_submit);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		createImageViewRoomPost();
		btSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				postRoom();
			}
		});

		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.post_map)).getMap();
			if (googleMap == null)
				return;
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			final GoogleMapFunctions mapFunctions = new GoogleMapFunctions(googleMap,
					this);
			googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng point) {
					googleMap.clear();
					latitude = point.latitude;
					longitude = point.longitude;
					mapFunctions.addMarker(latitude, longitude, "", "");		
				}
			});
			mapFunctions.moveCameraToCurrentPosition(PostRoomActivity.this);

		}
	}

	private void createImageViewRoomPost() {
		ivRoomPost = (ImageView) findViewById(R.id.iv_image_room);
		ivRoomPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);

			}
		});
	}

	private void postRoom() {
		String address = etAddress.getText().toString().trim();
		String description = etDescription.getText().toString().trim();
		String phone = etPhone.getText().toString().trim();
		String area = etArea.getText().toString().trim();
		
		String price = etPrice.getText().toString().trim();
		String city = (String) spCity.getSelectedItem();
		if (MainActivity.userLogin == null) {
			showDialog();
			return;
		}
		
		if (address.equals("") || description.equals("") || phone.equals("") || area.equals("") || price.equals("") || city.equals("")) {
			Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (latitude == 0 && longitude == 0) {
			Toast.makeText(getApplicationContext(), "Vui lòng chọn vị trí trên bản đồ!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		
		new PostRoomTask().execute(address, area, price, phone, latitude + "", longitude + "", description, city, MainActivity.userLogin.getUserId() + "");

	}
	
	private void showDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.request_login);
		dialog.setTitle("Thông báo");
		Button btLogin = (Button) dialog.findViewById(R.id.bt_login);
		Button btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
		btLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(PostRoomActivity.this, LoginActivity.class);
				startActivity(loginIntent);
				dialog.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
 	class PostRoomTask extends AsyncTask<String, Void, MotelRoom> {
		
	
		@Override
		protected MotelRoom doInBackground(String... params) {
			MotelRoomFunctions motelFunctions = new MotelRoomFunctions();
			MotelRoom motelRoomPosted = motelFunctions.submitMotelRoom(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
			return motelRoomPosted;
		}

		@Override
		protected void onPostExecute(MotelRoom result) {
			if (result != null) {
				CustomToast.showToast("Thành công! Hãy đợi chúng tôi kiểm duyệt", getApplicationContext());
				if (imagePath != null) {
					Uri bitmapUri = BitmapUri.getLocalBitmapUri(ivRoomPost);
					tempPath =  bitmapUri.getPath();
					Log.i("Upload", tempPath);
					new UploadImageTask().execute(result.getMotelRoomID() + "");
				}

			}
			else {
				CustomToast.showToast("Đăng phòng thất bại. Hãy thử lại", getApplicationContext());
			}
						
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				imagePath = getPath(selectedImageUri);
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
				ivRoomPost.setImageBitmap(bitmap);
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
	
	
	class UploadImageTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			UploadImage uploadImage = new UploadImage();
			uploadImage.uploadFile(tempPath, params[0], false);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.i("UploadImage", "Tải ảnh lên thành công");
		}
		
	}
}
