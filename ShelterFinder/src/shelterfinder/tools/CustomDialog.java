package shelterfinder.tools;

import shelterfinder.activities.LoginActivity;
import shelterfinder.activities.MainActivity;
import shelterfinder.adapters.SpinnerCustomAdapter;
import shelterfinder.fragments.AboutFragment;
import shelterfinder.fragments.SearchFragment;
import shelterfinder.fragments.UserFragment;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.drive.query.SearchableField;
import com.shelterfinder.R;

public class CustomDialog {
	public static void showRequestLoginDialog(final Activity activity) {
		final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.request_login);

        Button btLogin = (Button) dialog.findViewById(R.id.bt_login);
		Button btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
		btLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(activity, LoginActivity.class);
				activity.startActivity(loginIntent);
				dialog.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

        dialog.show();
	}
	
	public static void showFilterSearchDialog(final Activity activity, final SearchFragment fragment) {
		final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.filter_search);
        Button btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				dialog.dismiss();	
			}
		});
        final TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_price);
        final TextView tvArea = (TextView) dialog.findViewById(R.id.tv_area);
        final SeekBar sbPrice = (SeekBar) dialog.findViewById(R.id.seek_price);
        final SeekBar sbArea = (SeekBar) dialog.findViewById(R.id.seek_area);
        final Spinner spCity = (Spinner) dialog.findViewById(R.id.sp_city);
        SpinnerCustomAdapter adapter = new SpinnerCustomAdapter(activity, R.layout.custom_spinner, Constants.CITY_LIST);
        spCity.setAdapter(adapter);
        sbPrice.setMax(Constants.COST_LIST.length - 1);
        sbArea.setMax(Constants.AREA_LIST.length - 1);
        tvPrice.setText(Constants.COST_LIST[0]);
        tvArea.setText(Constants.AREA_LIST[0]);
        Button btSearch = (Button) dialog.findViewById(R.id.bt_search);
        btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cost =  Constants.REAL_COST_LIST[sbPrice.getProgress()];
				String area = Constants.REAL_AREA_LIST[sbArea.getProgress()];
				String city = (String) spCity.getSelectedItem();
				fragment.search(city, cost, area);
				dialog.dismiss();
				
			}
		});
        
        OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (seekBar.equals(sbPrice)) {
					tvPrice.setText(Constants.COST_LIST[progress]);
				}
				else {
					tvArea.setText(Constants.AREA_LIST[progress]);
				}
				
			}
		};
		sbPrice.setOnSeekBarChangeListener(listener);
		sbArea.setOnSeekBarChangeListener(listener);
        
   

        dialog.show();
	}
	
	public static void showChangePasswordDialog(final Activity activity, final UserFragment userFragment) {
		
		final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.change_password_dialog);

        Button btChange = (Button) dialog.findViewById(R.id.bt_change);
		Button btBack = (Button) dialog.findViewById(R.id.bt_back);
		final EditText etPasswordOld = (EditText) dialog.findViewById(R.id.et_pass_old);
		final EditText etPasswordNew = (EditText) dialog.findViewById(R.id.et_pass_new);
		final EditText etPasswordRetype = (EditText) dialog.findViewById(R.id.et_pass_retype);
		btChange.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String passwordOld = etPasswordOld.getText().toString().trim();
				if (!passwordOld.equals(MainActivity.userLogin.getPassword())) {
					CustomToast.showToast("Mật khẩu hiện tại không đúng", activity);
					etPasswordOld.setText("");
					return;
				}
				String passwordNew = etPasswordNew.getText().toString().trim();
				String passwordRetype = etPasswordRetype.getText().toString().trim();
				if (!passwordNew.equals(passwordRetype)) {
					CustomToast.showToast("Mật khẩu mới và mật khẩu xác nhận không trùng", activity);
					etPasswordNew.setText("");
					etPasswordRetype.setText("");
					return;
				}
				userFragment.changePassword(MainActivity.userLogin.getUsername(), passwordNew);
				dialog.dismiss();
			}
		});
		btBack.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

        dialog.show();
	}
	
	
	public static void showChangeFullNameDialog(final Activity activity,
			final UserFragment userFragment) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.change_fullname);

		Button btChange = (Button) dialog.findViewById(R.id.bt_change);
		Button btBack = (Button) dialog.findViewById(R.id.bt_back);
		final EditText etNewFullName = (EditText) dialog
				.findViewById(R.id.et_full_name);
		btChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String newFullName = etNewFullName.getText().toString().trim();
				if (newFullName.equals("")) {
					CustomToast.showToast("Vui lòng nhập đủ thông tin",
							activity);
					return;
				}
				userFragment.changeFullName(
						MainActivity.userLogin.getUsername(), newFullName);
				dialog.dismiss();
			}
		});
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public static void showSubmitFeedbackDialog(final Activity activity, final AboutFragment aboutFragment) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.submit_feedback_dialog);

		Button btSubmit = (Button) dialog.findViewById(R.id.bt_submit_feedback);
		Button btBack = (Button) dialog.findViewById(R.id.bt_back);
		final EditText etEmail = (EditText) dialog
				.findViewById(R.id.et_email);
		final EditText etContent = (EditText) dialog.findViewById(R.id.et_feedback);
		btSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString().trim();
				String content = etContent.getText().toString().trim();
				if (email.equals("") || content.equals("")) {
					CustomToast.showToast("Vui lòng nhập đủ thông tin",
							activity);
					return;
				}
				aboutFragment.submitFeedback(email, content);
				dialog.dismiss();
			}
		});
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public static void showRequestEnableGPSDialog(final Activity activity) {
		final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.request_enable_gps);

        Button btSetting = (Button) dialog.findViewById(R.id.bt_setting);
		Button btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
		btSetting.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent settingIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
				activity.startActivity(settingIntent);
				dialog.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

        dialog.show();
	}
	
	
	public static void showLogOutDialog(final Activity activity,
			final UserFragment userFragment) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.log_out_dialog);

		Button btLogOut = (Button) dialog.findViewById(R.id.bt_ok);
		Button btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
		btLogOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userFragment.logOut();
				dialog.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public static void showRatingDialog(final Activity activity) {
		
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.rating_dialog);
		final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
		
		final TextView tvRating = (TextView) dialog.findViewById(R.id.txt_rating);
		new GetCurrentRatingTask(tvRating).execute();
		Button btRating = (Button) dialog.findViewById(R.id.bt_rating);
		Button btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
		btRating.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String rating = String.valueOf(ratingBar.getRating());
				new SubmitRatingTask(activity).execute(rating);
				dialog.dismiss();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		
		

		
		dialog.show();
	}
	
	
	
}
