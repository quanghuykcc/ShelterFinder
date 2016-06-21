package shelterfinder.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import shelterfinder.activities.MotelRoomActivity;
import shelterfinder.objects.MotelRoom;
import shelterfinder.tools.Constants;
import shelterfinder.tools.GoogleMapFunctions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shelterfinder.R;

public class GoogleMapFragment extends Fragment {
	GoogleMap googleMap = null;
	private HashMap<Marker, Integer> markerHashMap;
	ArrayList<MotelRoom> motelRooms;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_google_map, container,
				false);
		markerHashMap = new HashMap<Marker, Integer>();
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_GET_ROOM) {
			motelRooms = StatusFragment.motelRooms;
			if (googleMap == null) {
				googleMap = ((SupportMapFragment) getChildFragmentManager()
						.findFragmentById(R.id.map_google_viewpager)).getMap();
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				GoogleMapFunctions mapFunctions = new GoogleMapFunctions(
						googleMap, getActivity());
				for (int i = 0; i < motelRooms.size(); i++) {
					double latitude = motelRooms.get(i)
							.getLatitude();
					double longitude = motelRooms.get(i)
							.getLongitude();
					MarkerOptions markerOptions = GoogleMapFunctions.createMarkerOptions(latitude, longitude, motelRooms.get(i).getAddress(), 							"Giá phòng: "
							+ StatusFragment.motelRooms.get(i)
							.getPrice() + " đồng / tháng");
					Marker marker = googleMap.addMarker(markerOptions);
					markerHashMap.put(marker, i);
				}

				
				mapFunctions.moveCameraToCurrentPosition(getActivity());
				googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker marker) {
						marker.showInfoWindow();
						return false;
					}
				});
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick(Marker marker) {
						int pos = markerHashMap.get(marker);
						Log.i("GoogleMap", "Position of the marker clicked: " + pos);
						Intent motelRoomIntent = new Intent(getActivity(), MotelRoomActivity.class);
						motelRoomIntent.putExtra("MotelRoom", motelRooms.get(pos));
						startActivity(motelRoomIntent);
					}
				});

			}
		}

	}

}
