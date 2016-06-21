package shelterfinder.activities;

import shelterfinder.objects.MotelRoom;
import shelterfinder.tools.GPSTracker;
import shelterfinder.tools.GoogleMapFunctions;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.shelterfinder.R;

public class GoogleMapActivity extends FragmentActivity {
	MotelRoom motelRoom;
	GoogleMap googleMap;
	TextView tvDistance;
	double currentLatitude;
	double currentLongitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_google_map);
		currentLatitude = 0;
		currentLongitude = 0;
		GPSTracker gpsTracker = new GPSTracker(GoogleMapActivity.this);
		currentLatitude = gpsTracker.getLatitude();
		currentLongitude = gpsTracker.getLongitude();
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		tvDistance = (TextView) findViewById(R.id.tv_distance);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent data = getIntent();
		motelRoom = (MotelRoom) data.getSerializableExtra("MotelRoom");
		Log.i(getClass().getName(), motelRoom.toString());
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_google)).getMap();
			if (googleMap == null) return;
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
    		googleMap.getUiSettings().setZoomControlsEnabled(true);
    		GoogleMapFunctions mapFunctions = new GoogleMapFunctions(googleMap, this);
    		double latitude = motelRoom.getLatitude();
    		double longitude = motelRoom.getLongitude();
    		mapFunctions.moveCameraToLatLng(latitude, longitude, motelRoom.getAddress(), "");
    		
		}
		new GetDistanceTask().execute();
	}
	
	class GetDistanceTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
	
			if (currentLatitude == 0 && currentLongitude == 0) {
				return "";
			}
			Log.i("GoogleMap", "Current Location: " + currentLatitude + ", "
					+ currentLongitude);
			String distanceOnRoad = GoogleMapFunctions.getDistanceOnRoad(
					motelRoom.getLatitude(), motelRoom.getLongitude(),
					currentLatitude, currentLongitude);
			if (!distanceOnRoad.trim().equals("-")) {
				return distanceOnRoad + " trên đường";
			} 
			else {
				Location motelLocation = new Location("");
				motelLocation.setLatitude(motelRoom.getLatitude());
				motelLocation.setLongitude(motelRoom.getLongitude());
				Location currentLocation = new Location("");
				currentLocation.setLatitude(currentLatitude);
				currentLocation.setLongitude(currentLongitude);
				float distance = currentLocation.distanceTo(motelLocation);
				return distance + "";
			}

		}
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("")) {
				tvDistance.setText("Không tính được khoảng cách");
			}
			else {
				tvDistance.setText("Khoảng cách với bạn: " + result);
			}		
		}
	}
}
