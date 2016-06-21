package shelterfinder.adapters;

import java.util.ArrayList;
import java.util.List;

import shelterfinder.objects.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shelterfinder.R;

public class AboutListAdapter extends ArrayAdapter<ListItem> {
	Context context;
	ArrayList<ListItem> listItems;
	public AboutListAdapter(Context context, int resource, ArrayList<ListItem> listItems) {
		super(context, resource, listItems);
		this.context = context;
		this.listItems = listItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_list_about, parent, false);
			TextView contentItem = (TextView) view.findViewById(R.id.tv_content);	
			contentItem.setText(listItems.get(position).getContent());
			ImageView icon = (ImageView) view.findViewById(R.id.iv_icon);
			icon.setImageResource(listItems.get(position).getIconId());
		}
		return view;
	}
	
	
	
}
