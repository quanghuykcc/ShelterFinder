package shelterfinder.tools;

import android.app.Activity;
import android.os.AsyncTask;

public class SubmitRatingTask extends AsyncTask<String, Void, Boolean> {
	Activity activity;
	public SubmitRatingTask(Activity activity) {
		this.activity = activity;
	}
	@Override
	protected Boolean doInBackground(String... params) {
		UserFunctions userFunctions = new UserFunctions();
		boolean result = userFunctions.submitRating(params[0]);
		return result;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			CustomToast.showToast("Rating thành công", activity);
		}
		else {
			CustomToast.showToast("Rating thất bại", activity);
		}
	}
	
	
}