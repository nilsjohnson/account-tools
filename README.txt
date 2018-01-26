This repository contains code that can be used to create software that will allow users to store and retrieve
objects by making usernames/passwords. The class which you should user to do this is Authenticator.java.

Usage is best suited for small desktop and LAN applications, where security is not of actual concern.
Usernames are stored in a SQLlite DB as plain-text. Passwords are stored as cipher-text, using the password itself
as a key. The sole purpose of this preventing someone from seeing the passwords just by looking at the db. The passwords
are not actually securely stored.

The usage of this class are demonstrated in examples/AuthenticatorExample.java.

To use this class, you first must have an object to associate with your users. This Object must implement
IsetUser, which will define methods that are needed by Authenticator to store your object.

There are also custom Exception classes which you can use. NewUserException(extended by BadPasswordException,
and BadUsernameException) and BadLoginException. These classes provide detailed description of problems that
your users might have. How to use these exceptions is demonstrated in the examples.




