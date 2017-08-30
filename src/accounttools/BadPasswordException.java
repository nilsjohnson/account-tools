package accounttools;

import java.util.ArrayList;

public class BadPasswordException extends NewUserException
{
	private ArrayList<PasswordError> errorList;
	private String message = null;

	public BadPasswordException(ArrayList<PasswordError> errorList)
	{
		StringBuilder temp = new StringBuilder();
		boolean shouldBreak = false;
		this.errorList = errorList;
		
		for (int i = 0; i < errorList.size(); i++)
		{
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
			case NEEDS_SPECIAL:
				temp.append("Needs Special Character");
				break;
			default:
				message = "Illegal Password";
				shouldBreak = true;
			}
			
			if (shouldBreak)
			{
				break;
			}
			
			if (i >= 1 && i < errorList.size())
			{
				temp.append(", ");
			}
		}
		if(!shouldBreak)
		{
			message = temp.toString();
		}
	}


}
