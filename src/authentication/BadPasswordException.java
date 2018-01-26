package authentication;

/**
 * This Exception class is thrown when a user is attempting to make an account and enters in an invalid password.
 * It contains an ArrayList with enums describing each case why the password might invalid.
 * It contains a String describing the reason the Exception was thrown.
 * 
 *  @author Nils Johnson
 */
import java.util.ArrayList;

public class BadPasswordException extends NewUserException
{
	public BadPasswordException(ArrayList<PasswordError> errorList)
	{
		// Initialize array to hold errors.
		errors = new Enum<?>[errorList.size()];
		// A String builder to create the message
		StringBuilder temp = new StringBuilder();
		// a flag to stop building message and list if unknown error occurs.
		boolean shouldBreakLoop = false;
		
		for (int i = 0; i < errorList.size(); i++)
		{
			// put current error in array
			errors[i] = errorList.get(i);
			
			// add to message
			switch (errorList.get(i))
			{
			case HAS_ILLEGAL_CHAR:
				temp.append("Has Invalid Character");
				break;
			case NEEDS_LOWER:
				temp.append("Needs Lowercase ");
				break;
			case NEEDS_UPPER:
				temp.append("Needs Uppercase");
				break;
			case NEEDS_NUMBER:
				temp.append("Needs Number");
				break;
			case NEEDS_SPECIAL_CHAR:
				temp.append("Needs Special Character");
				break;
			default:
				message = "Illegal Password";
			}
			
			if (shouldBreakLoop)
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
		if(!shouldBreakLoop)
		{
			message = temp.toString();
		}
	}
}
