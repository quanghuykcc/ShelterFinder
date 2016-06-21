package shelterfinder.adapters;

import java.util.ArrayList;

import shelterfinder.activities.LoginActivity;
import shelterfinder.activities.MainActivity;
import shelterfinder.objects.MotelRoom;
import shelterfinder.objects.User;
import shelterfinder.tools.CustomDialog;
import shelterfinder.tools.MotelRoomFunctions;

import com.shelterfinder.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandUserAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<String> childInfoItems;
	private ArrayList<String> childPostedItems;
	private User userLogin;
	private ArrayList<MotelRoom> motelRooms;
	
	public ExpandUserAdapter(Context context, User userLogin, ArrayList<MotelRoom> motelRooms) {
		childInfoItems = new ArrayList<String>();
		childPostedItems = new ArrayList<String>();
		this.context = context;
		this.userLogin = userLogin;
		this.motelRooms = motelRooms;
		if (userLogin != null) {
			childInfoItems.add("Tên đăng nhập: " + this.userLogin.getUsername());
			childInfoItems.add("Mật khẩu     : **********");
			childInfoItems.add("Tên đầy đủ   : " + this.userLogin.getFullName()); 
			childInfoItems.add("Địa chỉ email: " + this.userLogin.getEmail());
			if (motelRooms != null) {
				for (int i = 0; i < this.motelRooms.size(); i++) {
					childPostedItems.add(this.motelRooms.get(i).getAddress());
				}
			}
			
		}
		
	}
	
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = convertView;
		
		
		if (v == null) {
			v = LayoutInflater.from(context).inflate(R.layout.item_list_info_user, parent, false);
		}
		TextView contentItem = (TextView) v.findViewById(R.id.tv_content);
		ImageView iconImage = (ImageView) v.findViewById(R.id.iv_icon);
		if (groupPosition == 0) {
			if (childPosition == 0 || childPosition == 3 || (childPosition == 1 && MainActivity.userLogin.getPassword().equals(""))) {
				iconImage.setImageResource(R.drawable.ic_item);
			}			
			else {
				iconImage.setImageResource(R.drawable.ic_change);
			}
			contentItem.setText(childInfoItems.get(childPosition));
		}
		else {
			contentItem.setText(childPostedItems.get(childPosition));
			iconImage.setImageResource(R.drawable.ic_room_posted);
			
		}
		return v;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0) {
			return childInfoItems.size();
		}
		else {
			return childPostedItems.size();
		}
		
		
	}

	
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		return 2;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		
		View v = convertView;
		if (v == null) {
			v= LayoutInflater.from(context).inflate(R.layout.group_list_user_info, parent, false);
		}
		TextView groupName = (TextView) v.findViewById(R.id.tv_info);
		if (groupPosition == 0) {
			groupName.setText("Thông tin tài khoản");
		}
		else {
			groupName.setText("Tin đã đăng");
		}
		int ic_arrow = isExpanded ? R.drawable.ic_down : R.drawable.ic_expand;
		ImageView imageArrow = (ImageView) v.findViewById(R.id.iv_expand);
		int bg_color = isExpanded ? Color.GRAY : Color.WHITE;
		v.setBackgroundColor(bg_color);
		imageArrow.setImageResource(ic_arrow);
		return v;
	}
	
	public void removeData(MotelRoom motelRoom) {
		motelRooms.remove(motelRoom);
		if (motelRooms != null) {
			childPostedItems.clear();
			for (int i = 0; i < this.motelRooms.size(); i++) {
				childPostedItems.add(this.motelRooms.get(i).getAddress());
			}
		}
		notifyDataSetChanged();
	}
	
	public void refreshUserFullName() {
		childInfoItems.remove(2);
		childInfoItems.add(2, "Tên đầy đủ   : " + this.userLogin.getFullName());
		notifyDataSetChanged();
		
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
