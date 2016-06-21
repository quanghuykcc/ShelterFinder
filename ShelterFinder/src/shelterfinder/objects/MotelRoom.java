package shelterfinder.objects;

import java.io.Serializable;

public class MotelRoom implements Serializable{
	private int motelRoomID;
	private String address;
	private double area;
	private long price;
	private String phone;
	private double latitude;
	private double longitude;
	private String description;
	private String city;
	private String timePosted;
	private String userIDPosted;
	private String images;
	public int getMotelRoomID() {
		return motelRoomID;
	}
	public void setMotelRoomID(int motelRoomID) {
		this.motelRoomID = motelRoomID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTimePosted() {
		return timePosted;
	}
	public void setTimePosted(String timePosted) {
		this.timePosted = timePosted;
	}
	public String getUserIDPosted() {
		return userIDPosted;
	}
	public void setUserIDPosted(String userIDPosted) {
		this.userIDPosted = userIDPosted;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	@Override
	public String toString() {
		return "MotelRoom [motelRoomID=" + motelRoomID + ", address=" + address
				+ ", area=" + area + ", price=" + price + ", phone=" + phone
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", description=" + description + ", city=" + city
				+ ", timePosted=" + timePosted + ", userIDPosted="
				+ userIDPosted + ", images=" + images + "]";
	}
	
	public String createShareContent() {
		return "Chia sẻ phòng trọ qua ứng dụng Shelter Finder\n" +
				"Địa chỉ: " + address + "\n" +
				"Diện tích: " + area + " mét vuông\n" +
				"Giá phòng: " + price + " đồng/tháng\n" +
				"Điện thoại: " + phone + "\n" +
				"Hãy cài đặt Shelter Finder để biết rõ chi tiết phòng trọ";		
	}
	
	
	
	
}
