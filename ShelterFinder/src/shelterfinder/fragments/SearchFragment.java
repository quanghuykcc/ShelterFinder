package shelterfinder.fragments;

import java.util.ArrayList;
import java.util.Collections;

import shelterfinder.activities.MotelRoomActivity;
import shelterfinder.adapters.SpinnerCustomAdapter;
import shelterfinder.adapters.StatusFragmentAdapter;
import shelterfinder.objects.MotelRoom;
import shelterfinder.objects.StatusPost;
import shelterfinder.objects.User;
import shelterfinder.tools.Constants;
import shelterfinder.tools.CustomDialog;
import shelterfinder.tools.MotelRoomFunctions;
import shelterfinder.tools.UserFunctions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.shelterfinder.R;

public class SearchFragment extends Fragment implements OnItemClickListener {
	Button btSearch;
	ListView lvRoom;
	ArrayList<MotelRoom> roomsSearched = new ArrayList<MotelRoom>();
	ArrayList<StatusPost> listStatus = new ArrayList<StatusPost>();
	ArrayList<User> userPostedList = new ArrayList<User>();
	StatusFragmentAdapter adapterStatus = null;
	ProgressBar progressBar;
	TextView tvMessage;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		btSearch = (Button) view.findViewById(R.id.bt_search);
	
		
		btSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomDialog.showFilterSearchDialog(getActivity(), SearchFragment.this);
			}
		});
		
		lvRoom = (ListView) view.findViewById(R.id.lv_room);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		tvMessage = (TextView) view.findViewById(R.id.tv_message);
		adapterStatus = new StatusFragmentAdapter(getActivity(),
				R.layout.fragment_status_item, listStatus);
		lvRoom.setAdapter(adapterStatus);
		lvRoom.setOnItemClickListener(this);
		return view;
		
	}
	
	public void search(String city, String price, String area) {
		new SearchTask().execute(city, price, area);
	}
	
	class SearchTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			lvRoom.setFocusable(false);
			lvRoom.setFocusableInTouchMode(false);
			listStatus.clear();
			roomsSearched.clear();
			userPostedList.clear();
			progressBar.setVisibility(View.VISIBLE);
			tvMessage.setText("Đang tải...");
			tvMessage.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(String... params) {
			MotelRoomFunctions roomFunctions = new MotelRoomFunctions();
			roomsSearched = roomFunctions.searchMotelRoom(params[0], params[1], params[2]);
			UserFunctions userFunctions = new UserFunctions();
			if (roomsSearched != null) {
				for (int i = 0; i < roomsSearched.size(); i++) {
					userPostedList.add(userFunctions.getUserByID(roomsSearched.get(i).getUserIDPosted()));
				}
	    
			}
			
			return null;
			
		}
		@Override
		protected void onPostExecute(Void result) {	
			progressBar.setVisibility(View.GONE);	
			
			if (roomsSearched.size() == 0) {
				tvMessage.setText("Không có phòng trọ được tìm thấy");				
			}
			else {
				tvMessage.setVisibility(View.GONE);
			}
			for (int i = 0; i < roomsSearched.size(); i++) {
				MotelRoom room = roomsSearched.get(i);
				User userPosted = userPostedList.get(i);
				listStatus
						.add(new StatusPost(userPosted.getAvatar(),
								userPosted.getFullName(), room.getTimePosted(), room
										.getAddress(), room.getArea() + "", room
										.getPrice() + "", room.getImages()));
			}
			Collections.reverse(listStatus);
			Collections.reverse(roomsSearched);
			adapterStatus.notifyDataSetChanged();
			lvRoom.setFocusable(true);
			lvRoom.setFocusableInTouchMode(true);
			Log.i(getClass().getName(), "Tải xong dữ liệu phòng trọ");
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent motelRoomIntent = new Intent(getActivity(), MotelRoomActivity.class);
		motelRoomIntent.putExtra("MotelRoom", roomsSearched.get(position));
		startActivity(motelRoomIntent);
		
	}


}
