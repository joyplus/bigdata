package tv.joyplus.backend.report.exception;

public class ReportBaseException extends RuntimeException {

	/**
	 * Generated SerialVersion UID
	 */
	private static final long serialVersionUID = -1444208094241940204L;
	
	private int exceptionId;
	private String errorMessage;
	private String exceptionMessage;
	
	public ReportBaseException(int exceptionId, String errorMessage, String exceptionMessage){
		super();
		this.setExceptionId(exceptionId);
		this.setErrorMessage(errorMessage);
		this.setExceptionMessage(exceptionMessage);
	}

	public int getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(int exceptionId) {
		this.exceptionId = exceptionId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
