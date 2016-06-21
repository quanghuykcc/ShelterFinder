package shelterfinder.fragments;

import java.util.ArrayList;

import shelterfinder.activities.LoginActivity;
import shelterfinder.activities.MainActivity;
import shelterfinder.adapters.ExpandUserAdapter;
import shelterfinder.objects.MotelRoom;
import shelterfinder.tools.Constants;
import shelterfinder.tools.CustomDialog;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.LoadImageTask;
import shelterfinder.tools.MotelRoomFunctions;
import shelterfinder.tools.UserFunctions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.shelterfinder.R;

public class UserFragment extends Fragment {
	public TextView tvFullName;
	public ImageView ivAvatar;
	private ExpandableListView expandList;
	private ArrayList<MotelRoom> motelRooms;
	private ExpandUserAdapter adapter;
	private ImageView ivLogOut;
		
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user, container, false);
		tvFullName = (TextView) view.findViewById(R.id.tv_fullname);
		ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar_user);
		ivLogOut = (ImageView) view.findViewById(R.id.iv_log_out);
		if (MainActivity.userLogin == null) {
			ivLogOut.setVisibility(View.GONE);
		}
		ivAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.getUserLogin() != null) {

				} else {
					Intent loginIntent = new Intent(UserFragment.this
							.getActivity(), LoginActivity.class);
					startActivityForResult(loginIntent, Constants.LOGIN_CODE);
				}
			}
		});
		
		ivLogOut.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				CustomDialog.showLogOutDialog(getActivity(), UserFragment.this);
			}
		});
		
		expandList = (ExpandableListView) view.findViewById(R.id.expandable_info_user);
		adapter = new ExpandUserAdapter(getActivity(), MainActivity.userLogin, motelRooms);
		expandList.setAdapter(adapter);
		expandList.setOnChildClickListener(new OnChildClickListener() {		
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (groupPosition == 1) {
					ShelterDialogFragment fragmentDialog = new ShelterDialogFragment(motelRooms.get(childPosition), getActivity(), motelRooms, adapter);
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					ShelterDialogFragment tPrev =  (ShelterDialogFragment) fragmentManager.findFragmentByTag("time_dialog");
	                if(tPrev!=null) fragmentTransaction.remove(tPrev);
	                fragmentDialog.show(fragmentTransaction, "time_dialog");
				}
				else {
					if (childPosition == 1 && !MainActivity.userLogin.getPassword().equals("")) {
						CustomDialog.showChangePasswordDialog(getActivity(), UserFragment.this);
					}
					else if (childPosition == 2) {
						CustomDialog.showChangeFullNameDialog(getActivity(), UserFragment.this);
					}
				}
				return false;
			}
		});
		
		return view;

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.LOGIN_CODE) {
			if (resultCode == Constants.GET_USER_LOGIN_CODE) {
				tvFullName.setText(data.getStringExtra("FullName"));
				new LoadImageTask(getActivity(), ivAvatar,
						R.drawable.no_avatar)
						.execute(Constants.USER_AVATARS_URL
								+ data.getStringExtra("Avatar"));
				
				new GetRoomPostedByUserTask().execute(MainActivity.userLogin.getUserId() + "");
				ivLogOut.setVisibility(View.VISIBLE);
				
			}
		}
	}
	
	class GetRoomPostedByUserTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			MotelRoomFunctions motelFunctions = new MotelRoomFunctions();
			motelRooms = motelFunctions.searchMotelRoomByUser(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			adapter = new ExpandUserAdapter(getActivity(), MainActivity.userLogin, motelRooms);
			expandList.setAdapter(adapter);
		}
		 
	 }
	
	public void changePassword(String username, String value) {
		new ChangePasswordTask().execute(username, value);
	}
	
	public void logOut() {
		MainActivity.userLogin = null;
		tvFullName.setText("Chạm vào hình trên để đăng nhập");
		ivAvatar.setImageResource(R.drawable.ic_no_avatar_blue);
		ivLogOut.setVisibility(View.GONE);
		motelRooms = null;
		
		adapter = new ExpandUserAdapter(getActivity(), MainActivity.userLogin, motelRooms);
		expandList.setAdapter(adapter);
		
		
	}
	
	class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			boolean result = userFunctions.submitField(params[0], "Password", params[1]);
			if (result) MainActivity.userLogin.setPassword(params[1]);
			return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				CustomToast.showToast("Thay đổi mật khẩu thành công", getActivity());
			}
			else {
				CustomToast.showToast("Thay đổi mật khẩu thất bại", getActivity());
			}
			
		}
	}
	
	public void changeFullName(String username, String value) {
		new ChangeFullNameTask().execute(username, value);
	}
	
	class ChangeFullNameTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			boolean result = userFunctions.submitField(params[0], "FullName", params[1]);
			if (result) MainActivity.userLogin.setFullName(params[1]);
			return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				CustomToast.showToast("Thay đổi tên thành công", getActivity());
				tvFullName.setText(MainActivity.userLogin.getFullName());
				adapter.refreshUserFullName();
				
			}
			else {
				CustomToast.showToast("Thay đổi tên thất bại", getActivity());
			}
			
		}
	}
	
	
	
	
	
	

}
