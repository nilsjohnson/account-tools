package authentication;

import java.util.ArrayList;


/**
 * This class is base class for different types of exceptions that can be thrown
 * when making a new account. 
 * 
 * It can return an array of Enums which represent the problems and a String detailing the problem.
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
	
	/**
	 * Puts a comma or 'and' between each problem.
	 */
	protected void appendPunctuation(StringBuilder sb, int i, int maxIndex)
	{
		if (i < maxIndex - 2)
		{
			sb.append(", ");
		}
		else if (i == maxIndex - 2)
		{
			sb.append(" and ");
		}
		else if(i == maxIndex -1)
		{
			sb.append(".");
		}
	}
}


