package shelterfinder.adapters;

import java.util.ArrayList;
import java.util.Date;

import com.shelterfinder.R;

import shelterfinder.objects.MotelRoomComment;
import shelterfinder.objects.User;
import shelterfinder.tools.Constants;
import shelterfinder.tools.LoadImageTask;
import shelterfinder.tools.TimeFunctions;
import shelterfinder.tools.UserFunctions;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MotelRoomCommentAdapter extends ArrayAdapter<MotelRoomComment> {
	Activity context;
	int layoutId;
	ArrayList<MotelRoomComment> commentList = null;
	ArrayList<User> userPostedComments;
	TimeFunctions timeFunctions;
	LayoutInflater mInflater;
	
	public MotelRoomCommentAdapter(Activity context, int resource,
			ArrayList<MotelRoomComment> list, ArrayList<User> userPostedComments) {
		super(context, resource, list);
		this.context = context;
		layoutId = resource;
		commentList = list;
		mInflater = context.getLayoutInflater();
		this.userPostedComments = userPostedComments;
		timeFunctions = new TimeFunctions();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(layoutId, null);
			holder = new ViewHolder();
			holder.txtTimePosted = (TextView) convertView.findViewById(R.id.txt_time_posted);
			holder.txtFullName = (TextView) convertView.findViewById(R.id.txt_full_name);
			holder.txtComment = (TextView) convertView.findViewById(R.id.txt_comment);
			holder.ivAvatar = (ImageView) convertView.findViewById(R.id.image_avatar);
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MotelRoomComment comment = commentList.get(position);
		String avatarStr = userPostedComments.get(position).getAvatar();
		new LoadImageTask(context, holder.ivAvatar, R.drawable.no_avatar).execute(Constants.USER_AVATARS_URL + avatarStr);
		Date datePosted = timeFunctions.formatDate(comment.getTimePosted());
		holder.txtTimePosted.setText(timeFunctions.calculteDateDif(datePosted));
		holder.txtComment.setText(comment.getComment());
		holder.txtFullName.setText(userPostedComments.get(position).getFullName());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView txtTimePosted;
		TextView txtFullName;
		TextView txtComment;
		ImageView ivAvatar;
	}
	
	
	
}
