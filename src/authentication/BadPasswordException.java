package authentication;

/**
 * This Exception class is thrown when a user is attempting to make an account and enters in an illegal password.
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
		StringBuilder temp = new StringBuilder("Password ");
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
				temp.append("has invalid character");
				break;
			case NEEDS_LOWER:
				temp.append("needs lowercase");
				break;
			case NEEDS_UPPER:
				temp.append("needs uppercase");
				break;
			case NEEDS_NUMBER:
				temp.append("needs number");
				break;
			case NEEDS_SPECIAL_CHAR:
				temp.append("needs special character");
				break;
			default:
				message = "Illegal Password";
			}
			
			if (shouldBreakLoop)
			{
				break;
			}
			
			appendPunctuation(temp, i, errorList.size());
		}
		if(!shouldBreakLoop)
		{
			message = temp.toString();
		}
	}
}
