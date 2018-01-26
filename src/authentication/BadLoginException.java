package authentication;

/**

 * This Exception class is thrown when a user cannot login.
 * It describes why providing by an enum value corresponding to the problem,
 * or a String based on that value.
 *  
 * @author Nils
 *
 */

public class BadLoginException extends Exception
{
	private LoginError loginError;
	private String message = null;
	
	public BadLoginException(LoginError e)
	{
		switch (e)
		{
		case INVALID_PASSWORD:
			message = "Invalid Password";
			break;
		case NO_USERS:
			message = "No Accounts Exist.";
			break;
		case USER_NOT_FOUND:
			message = "User Not Found";
			break;
		case INVALID_ATTEMPT:
			message = "Incomplete Login Credentials";
			break;
		default:
			message = "Login Error";
		}
		loginError = e;
	}
	
	public BadLoginException(LoginError e, String message)
	{
		this.message = message;
		this.loginError = e;
	}

	public LoginError getError()
	{
		return this.loginError;
	}

	@Override
	public String getMessage()
	{
		return this.message;
	}
}