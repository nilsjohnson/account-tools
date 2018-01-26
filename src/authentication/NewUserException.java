package authentication;

import java.util.ArrayList;


/**
 * This class is base class for different types of exceptions that can be thrown
 * when making a new account.
 * 
 * @author Nils Johnson
 */

public abstract class NewUserException extends Exception
{
	protected String message;
	protected Enum<?>[] errors;
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
	
	public Enum<?>[] getErrors()
	{
		return this.errors;
	}
}


