package examples;

import java.util.ArrayList;

import authentication.Authenticator;
import authentication.BadLoginException;
import authentication.LoginError;
import authentication.NewUserException;
import authentication.PasswordError;
import authentication.UsernameError;

public class AuthenticatorExample
{
	/**
	 * This program demonstrates proper usage of the Authenticator Object
	 * 
	 * @author Nils Johnson
	 */
	public static void main(String[] args)
	{
		// make authenticator object that works with your object. -> See
		// ExampleUserObject
		Authenticator<ExampleUserObject> authenticator = new Authenticator<>();

		// sample login credentials. Change these to experiment.
		final String USERNAME = "Dave";
		final String PASSWORD = "!Pass123";

		// if desired, you may validate legality of username and password using
		// Authenticator's static methods
		ArrayList<PasswordError> pwErrorList = Authenticator.checkPasswordLegality(PASSWORD);
		ArrayList<UsernameError> usernameErrorList = Authenticator.checkUsernameLegality(USERNAME);

		// both lists remain null, there are no errors.
		if (pwErrorList == null && usernameErrorList == null)
		{
			System.out.println("This username/password combination is legal");
			// this is where you should call authenticator.createUser to prevent a frivolous
			// database call.
		}
		if (pwErrorList != null)
		{
			System.out.println("Password Issues:");
			for (PasswordError error : pwErrorList)
			{
				System.out.println(error.toString());
			}
		}
		if (usernameErrorList != null)
		{
			System.out.println("Username Issues:");
			for (UsernameError error : usernameErrorList)
			{
				System.out.println(error.toString());
			}
		}

		System.out.println("===========================");
		
		//////////////////////////////////////
		// --------Making a new user -------//
		//////////////////////////////////////
		try
		{
			// create an Object to associate with user.
			ExampleUserObject example = new ExampleUserObject(USERNAME, "Blue", 12.9);

			// create this user.
			authenticator.createUser(USERNAME, PASSWORD, PASSWORD, example);
			System.out.println("Making of user entry and object successful.");
		}
		catch (NewUserException e)
		{
			// get an array of Enums representing the problems and a message.
			Enum<?>[] errors = e.getErrors();
			String message = e.getMessage();
			
			System.out.println(message);
			
			for (Enum<?> error : errors)
			{
				if (error instanceof PasswordError)
				{
					System.out.println("Password Error: " + error.toString());

					switch ((PasswordError) error)
					{
					case HAS_ILLEGAL_CHAR:
						// do whatever you want here
						break;
					case MIS_MATCH:
						break;
					case NEEDS_LOWER:
						break;
					case NEEDS_NUMBER:
						break;
					case NEEDS_SPECIAL_CHAR:
						break;
					case NEEDS_UPPER:
						break;
					case TOO_LONG:
						break;
					case TOO_SHORT:
						break;
					default:
						break;
					}
				}
				else if (error instanceof UsernameError)
				{
					System.out.println("Username Error: " + error.toString());

					switch ((UsernameError) error)
					{
					case HAS_ILLEGAL_CHAR:
						// do whatever you want here
						break;
					case TOO_LONG:
						break;
					case TOO_SHORT:
						break;
					case UNAVAILABLE:
						break;
					default:
						break;
					}
				}
			}

		}
		
		System.out.println("===========================");
		
		/////////////////////////////////////
		//------Getting user's Object------//
		/////////////////////////////////////
		
		// user's object
		ExampleUserObject object = null;

		try
		{
			// if valid credentials are entered, retrieves object.
			object = (ExampleUserObject) authenticator.getUser(USERNAME, PASSWORD);
			System.out.println("Object retrival successful");
		}
		catch (BadLoginException ex)
		{	
			// exception handing examples
			LoginError error = ex.getError();
			String errorMessage = ex.getMessage();
			
			System.out.println("Login Error: " + error.toString());
			System.out.println(errorMessage);
			
			switch(error)
			{
			case INVALID_ATTEMPT:
				break;
			case INVALID_PASSWORD:
				break;
			case NO_USERS:
				break;
			case USER_NOT_FOUND:
				break;
			default:
				break;
			}
		}
		
		System.out.println("===========================");

		/////////////////////////////////////////
		//------Saving the user's object ------//
		/////////////////////////////////////////
		
		// if you've succesfully fetched your user's object, do whatever with it.
		object.favoriteColor = "Orange";
		object.favoriteNumber = 832.567;

		// when it's time, simply save it. 
		authenticator.saveUserObject(object);
		
		// and if you need to, you can delete this user and their object
		authenticator.deleteUser(object);
	}
}
