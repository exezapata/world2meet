# World2meet

## Overview

The challenge World2Meet application for the maintenance of spaceships from series and movies, endpoints :

**Authentication endpoints :**

1. **POST**: `/register` Register a new user in the system.

2. **POST**: `/login` Logs in for an existing user on the system and returns a JWT authentication token..

**Spaceship endpoints :**

1. **GET**: `/spaceships` Retrieves a list of all spaceships with pagination.
2. **GET**: `/{id}` Retrieves a spaceship by its ID.
3. **GET**: `/search` Search for spaceships by name.
4. **POST**: `/spaceship` Create a new spaceship.
5. **PUT**: `/{id}` Updates an existing spaceship.
6. **DELETE**: `/{id}` Delete a spaceship.

To run the application, follow these steps:

1. Ensure Docker is installed and running.
2. Open a terminal and navigate to the `\spaceships\world2meet` directory.
3. Run the following commands:
   ```bash
   mvn clean install
   docker-compose build
   docker-compose up

#Explore the API documentation by entering the url http://localhost:8080/swagger-ui/index.html#/ , once the app has started.
## Running Tests

To execute the tests, run the following Maven command:
  ```bash
   mvn clean test 
   ```
## Instructions for Use

There are 2 types of ROLES created to associate with user creation:

ROLE_ADMIN :
```json
{
   "id": 1,
   "name":"ROLE_ADMIN"
}
```

ROLE_USER :
```json
{
   "id": 2,
   "name":"ROLE_USER"
}
```

### Steps to follow

1 - Create user - http://localhost:8080/api/auth/register

2 - Get token for the user created above - http://localhost:8080/api/auth/login

3 - Before consulting the spaceship endpoints, the token obtained in step number 2, Bearer Token, must be added and then consult the endpoint. - Example : http://localhost:8080/api/spaceship/spaceships?page=0&size=10

### Authentication

Before using ship-related endpoints, follow these steps:

**User Creation**:
Before you begin, create a user using the following JSON format:
```json
{
   "username": "admin",
   "password": "12345",
   "roles": [
      {
         "id": 1,
         "name":"ROLE_ADMIN"
      }
   ]
}
```
Login:
Sign in with the newly created user to get an authentication token. Sends the user's credentials (username and password) to the /login endpoint.

Authorization
Once you have obtained the authentication token, use it to access the ships' endpoints. Include the token in the header of all requests to these endpoints as follows:
Authorization: Bearer <token>

This way, you will be able to access and use the ship-related resources in the application.

## Postman Collection

A Postman collection containing REST calls for both POST and GET methods is provided at the root of the project.
->  challenge-w2m.postman_collection.json

## Test Coverage

To view the test coverage, after executing mvn clean install, navigate to the following path:
spaceships\world2meet\target\site\jacoco and open the index.html file in a web browser.