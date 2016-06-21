package shelterfinder.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import shelterfinder.adapters.MotelRoomCommentAdapter;
import shelterfinder.objects.MotelRoom;
import shelterfinder.objects.MotelRoomComment;
import shelterfinder.objects.User;
import shelterfinder.tools.BitmapUri;
import shelterfinder.tools.Constants;
import shelterfinder.tools.CustomDialog;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.GoogleMapFunctions;
import shelterfinder.tools.LoadImageTask;
import shelterfinder.tools.MotelRoomFunctions;
import shelterfinder.tools.TimeFunctions;
import shelterfinder.tools.UserFunctions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.shelterfinder.R;

public class MotelRoomActivity extends FragmentActivity {
	MotelRoom motelRoom;
	ImageView imageAvatarPosted;
	User userPosted;
	ListView lvComment;
	EditText etComment;
	Button btSendComment;
	TextView userNameTextView;
	Bitmap bitmapGoogle;
	private ImageView ivStaticMap;
	private ImageView ivMap;
	private ImageView ivBack;
	private ImageView ivShare;
	ImageView imageRoom;
	ArrayList<MotelRoomComment> comments;
	ArrayList<User> userPostedComments = new ArrayList<User>();
	ProgressBar progressBar;
	MotelRoomCommentAdapter commentAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_motel_room);
		Intent data = getIntent();
		motelRoom = (MotelRoom) data.getSerializableExtra("MotelRoom");
		
		
		imageRoom = (ImageView) findViewById(R.id.image_room);
		imageAvatarPosted = (ImageView) findViewById(R.id.img_avatar);
		userNameTextView = (TextView) findViewById(R.id.txt_user_name);
		TextView dateUpTextView = (TextView) findViewById(R.id.txt_date_up);
		TextView addressTextView = (TextView) findViewById(R.id.txt_address);
		TextView areaTextView = (TextView) findViewById(R.id.txt_area);
		TextView priceTextView = (TextView) findViewById(R.id.txt_price);
		TextView descriptionTextView = (TextView) findViewById(R.id.txt_description);
		TextView phoneTextView = (TextView) findViewById(R.id.txt_phone_contact);
		progressBar = (ProgressBar) findViewById(R.id.progressBarComment);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		TimeFunctions timeFunctions = new TimeFunctions();
		Date dateUp = timeFunctions.formatDate(motelRoom.getTimePosted());
		dateUpTextView.setText(timeFunctions.calculteDateDif(dateUp));
		addressTextView.setText("Địa chỉ: " + motelRoom.getAddress());
		areaTextView.setText("Diện tích: " + motelRoom.getArea() + " mét vuông");
		priceTextView.setText("Giá: " + motelRoom.getPrice() + " đồng/tháng");
		descriptionTextView.setText("Mô tả: " + motelRoom.getDescription());
		phoneTextView.setText(motelRoom.getPhone());
		phoneTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + motelRoom.getPhone()));
				startActivity(phoneIntent);
				
			}
		});
		new LoadImageTask(getApplicationContext(), imageRoom,
				R.drawable.ic_no_image)
				.execute(Constants.MOTELROOM_IMAGES_URL + motelRoom.getImages());
		new GetUserTask().execute();
		
		
		
		lvComment = (ListView) findViewById(R.id.listview_comment);
		
		new GetCommentTask().execute();
		
		
		
		
		etComment = (EditText) findViewById(R.id.edt_comment);
		btSendComment = (Button) findViewById(R.id.bt_send_comment);
		btSendComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendComment();
			}
		});
			
		ivStaticMap = (ImageView) findViewById(R.id.iv_static_map);
		new GetGoogleMapBitmap().execute();
		ivMap = (ImageView) findViewById(R.id.iv_map);
		ivMap.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent mapIntent = new Intent(MotelRoomActivity.this, GoogleMapActivity.class);
				mapIntent.putExtra("MotelRoom", motelRoom);
				startActivity(mapIntent);
				
			}
		});
		
		ivShare = (ImageView) findViewById(R.id.iv_share);
		ivShare.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				share();		
			}
		});
		
	}
	
	private void share() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("*/*");
		Uri bitmapUri = BitmapUri.getLocalBitmapUri(imageRoom);
		
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.putExtra(Intent.EXTRA_TEXT, motelRoom.createShareContent());
		if (bitmapUri != null) {
			shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
		}
		
		startActivity(Intent.createChooser(shareIntent, "Chia sẻ với"));
		
	}
	
	class GetGoogleMapBitmap extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			double latitude = motelRoom.getLatitude();
			double longitude = motelRoom.getLongitude();
			bitmapGoogle = GoogleMapFunctions.getGoogleMapThumbnail(
					latitude, longitude);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			ivStaticMap.setImageBitmap(bitmapGoogle);
		}
	}
	
	class GetUserTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			UserFunctions userFunctions = new UserFunctions();
			userPosted = userFunctions.getUserByID(motelRoom.getUserIDPosted());		
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			userNameTextView.setText(userPosted.getFullName());
			new LoadImageTask(getApplicationContext(), imageAvatarPosted,
					R.drawable.no_avatar)
					.execute(Constants.USER_AVATARS_URL + userPosted.getAvatar());
		}
	}
	
	class GetCommentTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			MotelRoomFunctions motelFunctions = new MotelRoomFunctions();
			comments = motelFunctions.getCommentByMotelRoom(motelRoom.getMotelRoomID() + "");
			if (comments.size() == 0) return null;
			UserFunctions userFunctions = new UserFunctions();
			for (int i = 0; i < comments.size(); i++) {
				userPostedComments.add(userFunctions.getUserByID(comments.get(i).getUserIDPosted()));
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			commentAdapter = new MotelRoomCommentAdapter(
					MotelRoomActivity.this, R.layout.comment_motel_room, comments, userPostedComments);
			Collections.reverse(comments);
			Collections.reverse(userPostedComments);
			lvComment.setAdapter(commentAdapter);
		}
	}
	
	class SendCommentTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			String comment = etComment.getText().toString().trim();
			MotelRoomFunctions motelFunctions = new MotelRoomFunctions();
			
			final MotelRoomComment commentMotel = motelFunctions.submitCommentMotel(comment,
					MainActivity.getUserLogin().getUserId() + "", motelRoom.getMotelRoomID() + "");
			if (commentMotel != null) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						comments.add(0, commentMotel);	
						userPostedComments.add(0, MainActivity.userLogin);
						
					}
				});
				
				
				return true;
			}
			else {
				
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				CustomToast.showToast("Đăng bình luận thành công", getApplicationContext());
				if (commentAdapter != null) {
					commentAdapter.notifyDataSetChanged();
					etComment.setText("");
				}
			}
			else {
				CustomToast.showToast("Đăng bình luận thất bại", getApplicationContext());
			}
		}
	}
	
	private void sendComment() {
		
		if (MainActivity.getUserLogin() == null) {
			CustomDialog.showRequestLoginDialog(this);
			return;
		}
		new SendCommentTask().execute();
		
	}
	
	

	
}
