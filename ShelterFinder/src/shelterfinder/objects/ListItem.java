package shelterfinder.objects;

public class ListItem {
	int iconId;
	String content;
	
	public int getIconId() {
		return iconId;
	}
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ListItem(int iconId, String content) {
		super();
		this.iconId = iconId;
		this.content = content;
	}
	
	
}
