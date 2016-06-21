package shelterfinder.fragments;

import java.util.ArrayList;
import java.util.Collections;

import shelterfinder.activities.MainActivity;
import shelterfinder.activities.MotelRoomActivity;
import shelterfinder.activities.PostRoomActivity;
import shelterfinder.adapters.StatusFragmentAdapter;
import shelterfinder.objects.MotelRoom;
import shelterfinder.objects.StatusPost;
import shelterfinder.objects.User;
import shelterfinder.tools.CheckNetwork;
import shelterfinder.tools.CustomDialog;
import shelterfinder.tools.MotelRoomFunctions;
import shelterfinder.tools.UserFunctions;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shelterfinder.R;

public class StatusFragment extends Fragment implements OnItemClickListener {
	ListView listViewStatus;
	ListView listPost;
	StatusFragmentAdapter adapterStatus = null;
	public static ArrayList<MotelRoom> motelRooms;
	ArrayList<StatusPost> listStatus = new ArrayList<StatusPost>();
	public static ArrayList<User> userPostedList = new ArrayList<User>();
	ProgressBar progressBar;
	TextView textView;
	Button mPostRoom;
	
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		motelRooms = new ArrayList<MotelRoom>();
		View v = inflater.inflate(R.layout.fragment_status, container, false);
		if (!CheckNetwork.checkEnableGPS(getActivity())) {
			CustomDialog.showRequestEnableGPSDialog(getActivity());
		}
		listViewStatus = (ListView) v.findViewById(R.id.listview_status);
		listPost= (ListView) v.findViewById(R.id.list_post);
		mPostRoom = (Button) v.findViewById(R.id.bt_post_room);
		mPostRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (MainActivity.userLogin == null) {
					CustomDialog.showRequestLoginDialog(getActivity());
					return;
				}
				Intent postRoomIntent = new Intent(getActivity(), PostRoomActivity.class);
				startActivity(postRoomIntent);
				
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		textView = (TextView) v.findViewById(R.id.textView);
		new LoadStatusPostTask().execute();
		listViewStatus.setOnItemClickListener(this);
		return v;
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent motelRoomIntent = new Intent(StatusFragment.this.getActivity(), MotelRoomActivity.class);
		motelRoomIntent.putExtra("MotelRoom", motelRooms.get(position));
		startActivity(motelRoomIntent);
		
	}
	
	class LoadStatusPostTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			MotelRoomFunctions motelRoomFunctions = new MotelRoomFunctions();
			UserFunctions userFunctions = new UserFunctions();
			motelRooms = motelRoomFunctions.getAllMotelRoom();
			for (int i = 0; i < motelRooms.size(); i++) {
				userPostedList.add(userFunctions.getUserByID(motelRooms.get(i).getUserIDPosted()));
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
			for (int i = 0; i < motelRooms.size(); i++) {
				MotelRoom room = motelRooms.get(i);
				User userPosted = userPostedList.get(i);
				listStatus
						.add(new StatusPost(userPosted.getAvatar(),
								userPosted.getFullName(), room.getTimePosted(), room
										.getAddress(), room.getArea() + "", room
										.getPrice() + "", room.getImages()));
			}
			Collections.reverse(listStatus);
			Collections.reverse(motelRooms);
			adapterStatus = new StatusFragmentAdapter(getActivity(),
					R.layout.fragment_status_item, listStatus);
			
			listViewStatus.setAdapter(adapterStatus);
			getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
			Log.i(getClass().getName(), "Tải xong dữ liệu phòng trọ");
		}
		
	}
	
}
	
	
