package authentication;

/**
 * Objects that are to associated with a user must implement this interface in
 * order to be serialized and allow for user deletion.
 * 
 * @author Nils
 *
 */
public interface IsetUser
{
	void setUsername(String username);
	String getUsername();
}
