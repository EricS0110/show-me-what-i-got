# Show Me What I Got

----

## A MongoDB Android Interface

An android app that connects to a pre-existing MongoDB Atlas instance and retrieves user documents for display

----
## Pre-requisites
 * A MongoDB Atlas account (user & password authentication)
 * Minimum one cluster, one database, and one collection
 * (not required, but recommended) at least one document within the collection

## App Design Plan
This app is intended to be a generalized version of a personal hobby project.  My hobby app was designed to act as a
read-only interface to a pre-created and pre-populated MongoDB Atlas instance.  All connections were hard-coded and 
the queryable fields were predetermined and hard-coded.  By allowing the user to manually enter their own connection
string, the MongoDB instance details and user-interface details can be generalized.

Generalizations and updated app flow are...
1. User "logs into" the app, caching the username and password for MongoDB Atlas account locally
2. User enters the remaining connection string details, cached behind the scenes
3. Application attempts to use the credentials to create a connection
   * If there is a login/connection issue, prompt the user to verify the connection details from a new screen
   * If login is successful, proceed to step 4
4. Application queries Mongo for the available collections, populates into a dropdown
5. User clicks a go button and the application queries available query fields for the selected collection
6. Available fields populated into a dropdown selector, empty text field for query value
7. User enters value, hits go, and document collection gets searched
8. Results displayed within card view

----

## Long-term support plan
 * Add support for single document upserts