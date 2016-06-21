package shelterfinder.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shelterfinder.R;

public class SpinnerCustomAdapter extends ArrayAdapter {
	Context context;
	String[] listItems;
	public SpinnerCustomAdapter(Context context, int resource, String[] items) {
		super(context, resource, items);
		this.context = context;
		listItems = items;
	}
	
	
	public View getCustomView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false);
			holder.contentItem = (TextView) convertView.findViewById(R.id.tv_spinner);	
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.contentItem.setText(listItems[position]);
		return convertView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}
	
	static class ViewHolder {
		TextView contentItem;
	}
	
}
