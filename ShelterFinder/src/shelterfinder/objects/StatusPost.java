package shelterfinder.objects;

public class StatusPost {
	private String imagesAvatar;
	private String userName;
	private String dateUp;
	private String address;
	private String area;
	private String price;
	private String imagesDescription;
	public String getImagesAvatar() {
		return imagesAvatar;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setImagesAvatar(String imagesAvatar) {
		this.imagesAvatar = imagesAvatar;
	}
	public String getDateUp() {
		return dateUp;
	}
	public void setDateUp(String dateUp) {
		this.dateUp = dateUp;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getImagesDescription() {
		return imagesDescription;
	}
	public void setImagesDescription(String imagesDescription) {
		this.imagesDescription = imagesDescription;
	}
	public StatusPost(){
		super();
	}
	public StatusPost(String imagesAvatar,String userName, String dateUp, String address,
			String area, String price, String imagesDescription) {
		super();
		this.imagesAvatar = imagesAvatar;
		this.userName=userName;
		this.dateUp = dateUp;
		this.address = address;
		this.area = area;
		this.price = price;
		this.imagesDescription = imagesDescription;
	}
	
}
