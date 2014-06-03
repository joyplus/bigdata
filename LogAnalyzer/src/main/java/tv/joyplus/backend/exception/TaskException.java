package tv.joyplus.backend.exception;

public class TaskException extends RuntimeException {
	private String code;

	public TaskException(String message) {
		super(message);
	}
	public TaskException(String code, String message) {
		super(message);
		this.code = code;
	}
	public String getCode() {
		return code;
	}
}
