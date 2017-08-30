package accounttools;

import java.util.ArrayList;

public class BadUsernameException extends NewUserException
{
	private String message = null;
	private ArrayList<UsernameError> errorList = new ArrayList<>();

	public BadUsernameException(ArrayList<UsernameError> errorList)
	{
		this.errorList = errorList;
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
				message = "Illegal Username";
				shouldBreak = true;
				break;
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