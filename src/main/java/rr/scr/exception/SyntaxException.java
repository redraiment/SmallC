package rr.scr.exception;

public class SyntaxException extends RuntimeException {
	private static final long serialVersionUID = -8897772694568576221L;
	
	public SyntaxException() {
		super();
	}
	
	public SyntaxException(String msg) {
		super(msg);
	}
}