package tv.joyplus.backend.report.exception;

public class ReportBaseException extends RuntimeException {

	/**
	 * Generated SerialVersion UID
	 */
	private static final long serialVersionUID = -1444208094241940204L;
	
	private String exceptionId;
	private String errorMessage;
	private Exception exception;
	
	public ReportBaseException(String exceptionId, String errorMessage, Exception exception){
		super();
		this.setExceptionId(exceptionId);
		this.setErrorMessage(errorMessage);
		this.setException(exception);
	}

	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
