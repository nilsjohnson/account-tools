This repository contains code that can be used in an application in which you wish to associate a user with object that can be retrieved with that user's login credentials.

Example useage: A resource game in which the user has an object containing their resources, which you would like to be able to save and reload for them.

Each user's username and password are stored in an SQLlite db. The username is stored as plain-text, and the password is stored as ciphertext with the key being the password itself. The purpose of this encryption is so that someone can't open the database and just see login credentials. This is not actually a secure system of storage. 

To use: See AuthenticatorExample.java and ExampleUserObject.java. 




