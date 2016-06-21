package shelterfinder.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shelterfinder.R;

public class CustomToast {
	public static void showToast(String message, Context context) {
        LayoutInflater li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );  
        View layout = li.inflate(R.layout.custom_toast,  
          null); 
        TextView tvMessage = (TextView) layout.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        Toast toast = new Toast(context);  
        toast.setDuration(Toast.LENGTH_SHORT);  
        toast.setGravity(Gravity.BOTTOM, 0, 30); 
        toast.setView(layout);
        toast.show();  
	}
}
