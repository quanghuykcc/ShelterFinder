package shelterfinder.adapters;

import java.util.ArrayList;
import java.util.Date;

import shelterfinder.activities.PostRoomActivity;
import shelterfinder.objects.StatusPost;
import shelterfinder.tools.Constants;
import shelterfinder.tools.LoadImageTask;
import shelterfinder.tools.TimeFunctions;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shelterfinder.R;

public class StatusFragmentAdapter extends ArrayAdapter<StatusPost>{
	Activity context;
	int layoutId;
	ArrayList<StatusPost> list= null;
	TimeFunctions timeFunctions;
	LayoutInflater mInflater;
	public StatusFragmentAdapter(Activity context, int resource,
			ArrayList<StatusPost> list) {
		super(context, resource, list);
		timeFunctions = new TimeFunctions();
		this.context=context;
		this.layoutId=resource;
		this.list=list;
		mInflater = context.getLayoutInflater();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(layoutId, null);
			holder.img_avatar_item = (ImageView) convertView.findViewById(R.id.img_avatar);
			holder.txt_user_name_item = (TextView) convertView.findViewById(R.id.txt_user_name);
			holder.txt_address_item = (TextView) convertView.findViewById(R.id.txt_address);
			holder.txt_date_up_item = (TextView) convertView.findViewById(R.id.txt_date_up);
			holder.txt_area_item = (TextView) convertView.findViewById(R.id.txt_area);
			holder.txt_price_item = (TextView) convertView.findViewById(R.id.txt_price);
			holder.img_description_item = (ImageView) convertView.findViewById(R.id.img_description);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
	
		StatusPost status = list.get(position);
		holder.txt_user_name_item.setText(status.getUserName());
		holder.txt_address_item.setText("Địa chỉ: " + status.getAddress());
		Date datePosted = timeFunctions.formatDate(status.getDateUp());
		holder.txt_date_up_item.setText(timeFunctions.calculteDateDif(datePosted));
		holder.txt_area_item.setText("Diện tích: " + status.getArea() + " mét vuông");
		holder.txt_price_item.setText("Giá: " + status.getPrice() + " đồng/tháng");
		
		new LoadImageTask(getContext(), holder.img_avatar_item, R.drawable.no_avatar)
				.execute(Constants.USER_AVATARS_URL + status.getImagesAvatar());
		
		new LoadImageTask(getContext(), holder.img_description_item,
				R.drawable.ic_no_image)
				.execute(Constants.MOTELROOM_IMAGES_URL
						+ status.getImagesDescription());
		

		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView img_avatar_item;
		TextView txt_user_name_item;
		TextView txt_address_item;
		TextView txt_date_up_item;
		TextView txt_area_item;
		TextView txt_price_item;
		ImageView img_description_item;
		
	}
	
}
