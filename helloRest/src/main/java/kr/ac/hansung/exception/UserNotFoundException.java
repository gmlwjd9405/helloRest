package kr.ac.hansung.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 31911388852133284L;
	private long userId;

	public UserNotFoundException(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}
}
