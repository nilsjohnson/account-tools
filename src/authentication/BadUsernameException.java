package authentication;

import java.util.ArrayList;

/**
 * This Exception is thrown when a user attempts to make a new account with an
 * invalid user name. 
 * 
 * @author Nils
 *
 */
public class BadUsernameException extends NewUserException
{
	public BadUsernameException(ArrayList<UsernameError> errorList)
	{
		// initialize array to hold the errors
		errors = new Enum<?>[errorList.size()];
		
		// String builder for message
		StringBuilder temp = new StringBuilder("Desired username ");
		
		boolean shouldBreak = false;

		for (int i = 0; i < errorList.size(); i++)
		{
			switch (errorList.get(i))
			{
			case HAS_ILLEGAL_CHAR:
				temp.append("has illegal character ");
				break;
			case TOO_LONG:
				temp.append("is too long ");
				break;
			case TOO_SHORT:
				temp.append("is too short ");
				break;
			case UNAVAILABLE:
				temp.append("is unavailable ");
				break;
			default:
				this.message = "Illegal Username";
				shouldBreak = true;
				break;
			}

			if (shouldBreak)
			{
				break;
			}

			appendPunctuation(temp, i, errorList.size());
		}
		if (!shouldBreak)
		{
			message = temp.toString();
		}
	}
}