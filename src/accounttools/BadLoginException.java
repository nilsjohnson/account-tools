package accounttools;

public class BadLoginException extends Throwable
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
			message = "No Accounts Exist. Make a new one!";
			break;
		case USER_NOT_FOUND:
			message = "User Not Found";
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

	public String getLoginErrorMessage()
	{
		return this.message;
	}
}