package it.unibo.myalma.business.exceptions;

public class PermissionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PermissionException(String msg)
	{
		super(msg);
	}

}
