package utils;

@SuppressWarnings("serial")
public class InconceivableException extends RuntimeException {
    
	public InconceivableException(String message) {
        super(message);
    }

    public InconceivableException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
