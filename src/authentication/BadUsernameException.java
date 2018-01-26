package authentication;

import java.util.ArrayList;

/**
 * This Exception is thrown when a user attempts to make a new account with an
 * invalid user name. It contains an ArrayList with enums describing each case
 * why the user name might invalid. It contains a String describing the reason
 * the Exception was thrown.
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
		StringBuilder temp = new StringBuilder();
		
		boolean shouldBreak = false;

		for (int i = 0; i < errorList.size(); i++)
		{
			switch (errorList.get(i))
			{
			case HAS_ILLEGAL_CHAR:
				temp.append("Has Invalid Character");
				break;
			case TOO_LONG:
				temp.append("Needs Lowercase ");
				break;
			case TOO_SHORT:
				temp.append("Needs Uppercase");
				break;
			case UNAVAILABLE:
				temp.append("Needs Number");
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

			if (i >= 1 && i < errorList.size() && i != errorList.size() - 1)
			{
				temp.append(", ");
			}
			else if (i == errorList.size() - 1)
			{
				temp.append("and ");
			}
		}
		if (!shouldBreak)
		{
			message = temp.toString();
		}
	}
}