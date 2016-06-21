package shelterfinder.tools;

import android.os.AsyncTask;
import android.widget.TextView;

public class GetCurrentRatingTask extends AsyncTask<Void, Void, String> {
	private TextView tvCurrentRating;
	public GetCurrentRatingTask(TextView tvCurrentRating) {
		this.tvCurrentRating = tvCurrentRating;
	}
	@Override
	protected String doInBackground(Void... params) {
		UserFunctions userFunctions = new UserFunctions();
		return userFunctions.getCurrentRating();		
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (result.equals("")) {
			tvCurrentRating.setText("Chưa có đánh giá");
		}
		else {
			String rating = String.format("%.2f", result);
			tvCurrentRating.setText(rating);
		}
	}
	
}
