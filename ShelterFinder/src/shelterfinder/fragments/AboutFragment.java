package shelterfinder.fragments;

import java.util.ArrayList;

import shelterfinder.adapters.AboutListAdapter;
import shelterfinder.objects.ListItem;
import shelterfinder.tools.CustomDialog;
import shelterfinder.tools.CustomToast;
import shelterfinder.tools.UserFunctions;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shelterfinder.R;

public class AboutFragment extends Fragment {
	ListView listViewAbout;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		
		ArrayList<ListItem> listItems = new ArrayList<ListItem>();
		listItems.add(new ListItem(R.drawable.ic_email, "Email góp ý: quanghuyenddev@gmail.com"));
		listItems.add(new ListItem(R.drawable.ic_feedback, "Phản hồi"));
		listItems.add(new ListItem(R.drawable.ic_rating, "Bình chọn"));
		AboutListAdapter adapter = new AboutListAdapter(getActivity(), R.layout.item_list_about, listItems);
		listViewAbout = (ListView) view.findViewById(R.id.lv_item);
		listViewAbout.setAdapter(adapter);
		listViewAbout.setOnItemClickListener(new OnItemClickListener()
		{
		    @Override 
		    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
		    { 
		        if (position == 1) {
		        	CustomDialog.showSubmitFeedbackDialog(getActivity(), AboutFragment.this);
		        }
		        else if (position == 0) {
		        	showEmailFeedback();
		        }
		        else if (position == 2) {
		        	CustomDialog.showRatingDialog(getActivity());
		        }
		    }
		});
		return view;
		
	}
	
	private void showEmailFeedback() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto","abc@gmail.com", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Góp ý Shelter Finder");
		startActivity(Intent.createChooser(emailIntent, "Gửi thư phản hồi qua"));
	}
	
	public void submitFeedback(String email, String content) {
		new SubmitFeedbackTask().execute(email, content);
	}
	
	class SubmitFeedbackTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			boolean result = userFunctions.submitFeedback(params[0], params[1]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				CustomToast.showToast("Gửi phản hồi thành công", getActivity());
			}
			else {
				CustomToast.showToast("Gửi phản hồi thất bại", getActivity());
			}
		}
		
	}
}
