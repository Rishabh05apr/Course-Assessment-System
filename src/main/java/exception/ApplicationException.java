package exception;

/**
 * Created by pinak on 10/24/2017.
 */
public class ApplicationException extends Exception {
    private String errorMessage;
    private Exception exceptionObj;

    public ApplicationException(String errorMessage, Exception e) {
        this.errorMessage = errorMessage;
        this.exceptionObj = e;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Exception getExceptionObj() {
        return exceptionObj;
    }

    public void setExceptionObj(Exception exceptionObj) {
        this.exceptionObj = exceptionObj;
    }
}
