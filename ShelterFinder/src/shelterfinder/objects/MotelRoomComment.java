package shelterfinder.objects;

public class MotelRoomComment {
	private String commentID;
	private String comment;
	private String timePosted;
	private String userIDPosted;
	private String motelRoomIDPosted;
	public String getCommentID() {
		return commentID;
	}
	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public String getMotelRoomIDPosted() {
		return motelRoomIDPosted;
	}
	public void setMotelRoomIDPosted(String motelRoomIDPosted) {
		this.motelRoomIDPosted = motelRoomIDPosted;
	}
		
	public MotelRoomComment(String commentID, String comment,
			String timePosted, String userIDPosted, String motelRoomIDPosted) {
		super();
		this.commentID = commentID;
		this.comment = comment;
		this.timePosted = timePosted;
		this.userIDPosted = userIDPosted;
		this.motelRoomIDPosted = motelRoomIDPosted;
	}
	
	public MotelRoomComment() {
	}
	@Override
	public String toString() {
		return "MotelRoomComment [commentID=" + commentID + ", comment="
				+ comment + ", timePosted=" + timePosted + ", userIDPosted="
				+ userIDPosted + ", motelRoomIDPosted=" + motelRoomIDPosted
				+ "]";
	}
	
	
	
}
