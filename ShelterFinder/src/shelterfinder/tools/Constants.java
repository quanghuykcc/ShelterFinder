package shelterfinder.tools;

public class Constants {
	public static final String HOST_URL = "http://enddev.site50.net/ShelterFinder/";
	public static final String MOTELROOM_IMAGES_URL = "http://enddev.site50.net/ShelterFinder/MotelRoomImages/uploaded/";
	public static final String USER_AVATARS_URL = "http://enddev.site50.net/ShelterFinder/UserAvatars/uploaded/";
	
	public static final String LOGIN_TAG = "login";
	public static final String REGISTER_TAG = "register";
	public static final String SUBMIT_FIELD_USER = "submit_field";
	public static final String GET_USER_BY_ID = "get_user_by_id";
	public static final String GET_ALL_MOTEL_ROOM = "get_all_motel_room";
	public static final String SUBMIT_COMMENT_MOTEL = "submit_comment_motel";
	public static final String GET_COMMENT_BY_MOTEL = "get_comment_by_motelroom";
	public static final String SUBMIT_MOTEL_ROOM = "submit_motelroom";
	public static final String SEARCH_MOTEL_ROOM = "search_motel_room";
	public static final String GET_ROOM_BY_USER = "get_room_by_user";
	public static final String DELETE_ROOM = "delete_room";
	public static final String SUBMIT_FEEDBACK = "submit_feedback";
	public static final String GET_CURRENT_RATING = "get_current_rating";
	public static final String SUBMIT_RATING = "submit_rating";
	
	public static final int OK = 1;
	public static final int FAIL = 0;
	public static final String RESPONSE_CODE= "code";
	public static final int LOGIN_CODE = 0;
	public static final int GET_USER_LOGIN_CODE = 1;
	public static final int REQUEST_GET_ROOM = 2;
	
	
	public static final String[] CITY_LIST = {"Đà Nẵng", "Quảng Nam", "Huế", "Hà Nội", "Quảng Ngãi"};
	public static final String[] COST_LIST = {"Tất cả", "Trên 500.000", "Trên 800.000", "Trên 1.000.000", "Trên 1.500.000", "Trên 2.000.000", "Trên 2.500.000", "Trên 3.000.000"};
	public static final String[] AREA_LIST = {"Tất cả", "Trên 30 mét vuông", "Trên 40 mét vuông", "Trên 50 mét vuông", "Trên 70 mét vuông", "Trên 100 mét vuông", "Trên 120 mét vuông"};
	public static final String[] REAL_COST_LIST = {"", "500000", "800000", "1000000", "1500000", "2000000", "2500000", "3000000"};
	public static final String[] REAL_AREA_LIST = {"", "30", "40", "50", "70", "100", "120"};
}
