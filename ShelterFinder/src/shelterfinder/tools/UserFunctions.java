package shelterfinder.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import shelterfinder.objects.User;
import android.util.Log;

public class UserFunctions {
	private JSONParser jsonParser;
	public static final String USER_ID = "UserID";
	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	public static final String EMAIL = "Email";
	public static final String FULL_NAME = "FullName";
	public static final String AVATAR = "Avatar";
	
	public static final String CONTENT = "Content";
	public static final String TIME_POSTED = "TimePosted";
	public static final String RATING_APP = "AVG(Rating)";
	
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	// thực hiện công việc đăng nhập với username và password
	public User loginUser(String username, String password) {	
		// xây dựng các giá trị gửi đi để đăng nhập
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.LOGIN_TAG));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(Constants.HOST_URL, params);
		
		// phân tích đối tượng JSON thành một user
		try {
			User userLogin = new User();
			userLogin.setFullName(json.getString(FULL_NAME));
			userLogin.setAvatar(json.getString(AVATAR));
			userLogin.setUserId(Integer.parseInt(json.getString(USER_ID)));
			userLogin.setUsername(json.getString(USERNAME));
			userLogin.setPassword(json.getString(PASSWORD));
			userLogin.setEmail(json.getString(EMAIL));	
			return userLogin;
		}
		catch (JSONException e) {
			Log.e(getClass().getName(), "Đăng nhập không thành công!");
			return null;
		}
	}
	
	public boolean submitField(String username, String field, String value) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.SUBMIT_FIELD_USER));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("field", field));
		params.add(new BasicNameValuePair("value", value));
		JSONObject json = jsonParser.getJSONFromUrl(Constants.HOST_URL, params);
		
		// phân tích json trả về
		try {
			if (Integer.parseInt(json.getString(Constants.RESPONSE_CODE)) == Constants.OK) {
				return true;
			}
		} catch (Exception e) {
			Log.e(getClass().getName(), "Lỗi thay đổi dữ liệu tài khoản");
		}

		return false;

	}

	// thực hiện công việc đăng ký tài khoản
	public int registerUser(String username, String password,
			String email, String fullName) {
		// xây dựng các giá trị để gửi lên server đăng ký tài khoản
		int userID = -1;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.REGISTER_TAG));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("fullName", fullName));
		JSONObject json = jsonParser.getJSONFromUrl(Constants.HOST_URL, params);
		
		// phân tích json trả về?
		try {
			if (Integer.parseInt(json.getString(Constants.RESPONSE_CODE)) == Constants.OK) {
				userID = Integer.parseInt(json.getString(USER_ID));
			}
		}
		catch (Exception e) {
			Log.e(getClass().getName(), "Lỗi đăng ký tài khoản");
		}
		return userID;

	}
	
	// lấy thông tin user dựa vào id
	public User getUserByID(String userID) {	
		// xây dựng các giá trị gửi đi để đăng nhập
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.GET_USER_BY_ID));
		params.add(new BasicNameValuePair("userID", userID));
		JSONObject json = jsonParser.getJSONFromUrl(Constants.HOST_URL, params);
		
		// phân tích đối tượng JSON thành một user
		try {
			User userLogin = new User();
			userLogin.setUserId(Integer.parseInt(json.getString(USER_ID)));
			userLogin.setFullName(json.getString(FULL_NAME));
			userLogin.setAvatar(json.getString(AVATAR));
			return userLogin;
		}
		catch (JSONException e) {
			Log.e(getClass().getName(), "Lấy thông tin user thất bại!");
			return null;
		}
	}
	
	public boolean submitFeedback(String email, String content) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.SUBMIT_FEEDBACK));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("content", content));
		JSONObject json = jsonParser.getJSONFromUrl(Constants.HOST_URL, params);
		
		// phân tích json trả về
		try {
			if (Integer.parseInt(json.getString(Constants.RESPONSE_CODE)) == Constants.OK) {
				return true;
			}
		} catch (Exception e) {
			Log.e(getClass().getName(), "Lỗi đăng phản hồi");
		}

		return false;

	}
	
	public boolean submitRating(String rating) {
		boolean result = false;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.SUBMIT_RATING));
		params.add(new BasicNameValuePair("rating", rating));
		JSONObject json = jsonParser.getJSONFromUrl(Constants.HOST_URL, params);
		
		// phân tích json trả về
		try {
			if (Integer.parseInt(json.getString(Constants.RESPONSE_CODE)) == Constants.OK) {
				result = true;
			}
		} catch (Exception e) {
			Log.e(getClass().getName(), "Lỗi truy vấn rating");
		}

		return result;
	}
	
	public String getCurrentRating() {
		String currentRating = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constants.GET_CURRENT_RATING));
		ArrayList<JSONObject> jsonRoomArray = jsonParser.getJSONArrayListFromUrl(
				Constants.HOST_URL, params);
		
		// phân tích json trả về
		try {
			JSONObject json = jsonRoomArray.get(0);
			currentRating = json.getString(RATING_APP);
			
		} catch (Exception e) {
			Log.e(getClass().getName(), "Lỗi truy vấn Rating");
		}

		return currentRating;

	}
	

}
