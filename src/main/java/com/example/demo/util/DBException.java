package com.example.demo.util;

/**
 * Exception occurs from database connecting activity
 * @author Manisa
 * @since Dec 19, 2557 BE
 */
public class DBException extends Exception {

	private static final long serialVersionUID = 7514700163141683661L;
	/**
	 * Create an instance of {@link DBException}
	 * @param errorMessage
	 * @return an instance of DBException
	 */
	public static DBException newInstance(String errorMessage) {
		return new DBException(errorMessage);
	}
	public static DBException newInstance(String errorMessage, Exception sourceException) {
		errorMessage = errorMessage + "[" + sourceException.getMessage() + "]";
		return new DBException(errorMessage);
	}
	private DBException(String errorMessage) {
		super(errorMessage);
	}
}
