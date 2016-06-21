package shelterfinder.tools;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

public class CheckNetwork
{
	public static boolean isOnline(Context paramContext) {
	    ConnectivityManager cm =
	        (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	
	public static boolean checkEnableGPS(Activity activity) {
		boolean isGPSEnable = false;
		String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider.contains("gps")) {
			Log.i("GPS", "GPS enabled: " + provider);
			isGPSEnable = true;
		}
		else {
			Log.i("GPS", "Not enabled GPS yet: " + provider);
			isGPSEnable = false;
		}
		return isGPSEnable;
	}
	
}