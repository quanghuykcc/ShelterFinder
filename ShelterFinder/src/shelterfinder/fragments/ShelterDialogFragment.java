package shelterfinder.fragments;

import java.util.ArrayList;

import shelterfinder.activities.MotelRoomActivity;
import shelterfinder.activities.PostRoomActivity;
import shelterfinder.adapters.ExpandUserAdapter;
import shelterfinder.objects.MotelRoom;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.MotelRoomFunctions;

import com.shelterfinder.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShelterDialogFragment extends DialogFragment {
	private MotelRoom motelRoom;
	private Context context;
	private ArrayList<MotelRoom> motelRooms;
	private ExpandUserAdapter adapter;
	public ShelterDialogFragment(MotelRoom motelRoom, Context context, ArrayList<MotelRoom> motelRooms, ExpandUserAdapter adapter) {
		this.motelRoom = motelRoom;
		this.context = context;
		this.motelRooms = motelRooms;
		this.adapter = adapter;
	}
	
	public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.room_dialog, null);
		Button btDelete = (Button) v.findViewById(R.id.bt_delete);
		Button btShowInfo = (Button) v.findViewById(R.id.bt_show_info);
		btDelete.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				new DeleteRoomTask().execute(motelRoom.getMotelRoomID() + "");
				getDialog().dismiss();
			}
		});
		btShowInfo.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MotelRoomActivity.class);
				intent.putExtra("MotelRoom", motelRoom);
				startActivity(intent);
				getDialog().dismiss();
			}
		});

        return v;
		
	};
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		return dialog;
	}
	
	class DeleteRoomTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			MotelRoomFunctions motelFunctions = new MotelRoomFunctions();
			boolean result = motelFunctions.deleteRoom(params[0]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				adapter.removeData(motelRoom);
				CustomToast.showToast("Xóa phòng trọ đã đăng thành công", context);
			}
			else {
				CustomToast.showToast("Xóa phòng trọ thất bại", context);
			}
		}
		
	}

}
