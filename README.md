# Project Roadmap

## Introduction
This project provides a digital solution for handling issues such as booking flights and hotel reservations.

## User Management Service (user-ms)
### Authentication, Authorization, and Registration Service
- **Endpoint**: `user-ms/authentication - POST`
  - **Permission**: Permit all
  - **Description**: Allows users to log in to the program.
  - **Request body**:
    ```json
    { "username": "", "password": "" }
    ```
  - **Response model**:
    ```json
    { "access-token": "", "refresh-token": "" }
    ```

- **Endpoint**: `user-ms/renew-password/{username} - POST`
  - **Permission**: Permit all
  - **Description**: Sends a request to the user's email when they want to reset their password.
  - **Response model**:
    ```json
    { "message": "" }
    ```
  - **Request parameter**:
    ```json
    {"token": ""}
    ```

- **Endpoint**: `user-ms/resets-password - POST`
  - **Permission**: Permit all
  - **Description**: Sets a new password.
  - **Request body**:
    ```json
    { "newPassword": "", "repeatPassword": "" }
    ```
  - **Response body**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `user-ms/refresh-token - GET`
  - **Permission**: Authenticated
  - **Description**: Used to refresh the user's token.
  - **Request header**:
    ```json
    { "Authentication": "", "User-id": "" }
    ```
  - **Response body**:
    ```json
    { "access-token": "", "refresh-token": "" }
    ```

- **Endpoint**: `user-ms/registration - POST`
  - **Permission**: Permit all
  - **Description**: Allows users to create an account on our platform, and authentication is possible only after email verification.
  - **Request body**:
    ```json
    { "username": "", "password": "", "phoneNumber": "", "email": "", "birthdate": "", "paspFin": "", "paspSeria": "" }
    ```
  - **Response body**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `user-ms/confirmation - GET`
  - **Permission**: Permit all
  - **Description**: Endpoint sent to the email after registration, used to activate the account.
  - **Request parameter**:
    ```json
    {"token": ""}
    ```
  - **Response body**:
    ```json
    { "isConfirmed": "" }
    ```

- **Endpoint**: `user-ms/admin/registration - POST`
  - **Permission**: Admin
  - **Description**: Enables adding a new admin. A link is sent to the email, and after clicking the link, the admin must set a password, and the account should be activated.
  - **Request body**:
    ```json
    { "username": "", "password": "", "phoneNumber": "", "email": "", "birthdate": "", "paspFin": "", "paspSeria": "" }
    ```
  - **Request header**:
    ```json
    { "Authentication": "", "User-id": "" }
    ```

## Booking Service (booking-ms)
### Booking Service for Tickets
- **Endpoint**: `booking-ms/booking/tickets - GET`
  - **Permission**: Authenticated
  - **Description**: Displays all tickets purchased by the user.
  - **Response body**:
    ```json
    [{ "ticketId": "", "firstName": "", "lastName": "", "from": "", "to": "", "departureDateTime": "", "arrivalDateTime": "", "price": "", "flightId": "" }]
    ```

- **Endpoint**: `booking-ms/booking/tickets/{ticketId} - GET`
  - **Permission**: Authenticated
  - **Description**: Allows the user to view the details of the purchased ticket.
  - **Response body**:
    ```json
    { "ticketId": "", "firstName": "", "lastName": "", "from": "", "to": "", "departureDateTime": "", "arrivalDateTime": "", "price": "", "flightId": "" }
    ```

- **Endpoint**: `booking-ms/booking/tickets/{flightId} - POST`
  - **Permission**: Authenticated
  - **Description**: Allows the user to purchase a ticket for the selected flight. Information should be sent to Kafka along with the formatted ticketId. The Notification Service should consume this information and send the PDF to the email.
  - **Response body**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `booking-ms/booking/tickets/{ticketId}/pdf - GET`
  - **Permission**: Authenticated
  - **Description**: Allows the user to download the purchased ticket in PDF format. The response should contain the PDF. Libraries like FOP or iText can be used.
  - **Response body**:
    ```
    { PDF content }
    ```

## Flight Service (flight-ms)
### Flight Management Service
- **Endpoint**: `flight-ms/flights - POST`
  - **Permission**: Admin
  - **Description**: Determines a new flight.
  - **Request model**:
    - **Request header**:
      ```json
      { "Authentication": "", "User-id": "" }
      ```
    - **Request body**:
      ```json
      { "fromAirlineId": "", "toAirlineId": "", "departureDateTime": "", "arrivalDateTime": "", "initialPrice": "", "airplaneId": "" }
      ```
  - **Response model**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `flight-ms/flights - GET`
  - **Permission**: Permit all
  - **Description**: Displays all upcoming and available flights after the current time. Provides filtering options.
  - **Response model**:
    ```json
    [{ "From": "", "To": "", "DepartureDateTime": "", "ArrivalDateTime": "", "price": "" }]
    ```

- **Endpoint**: `flight-ms/flights/{id}? - GET`
  - **Permission**: Permit all
  - **Description**: Displays details of the selected flight.
  - **Response model**:
    ```json
    { "From": "", "To": "", "DepartureDateTime": "", "ArrivalDateTime": "", "price": "" }
    ```

- **Endpoint**: `flight-ms/flights/{id} - DELETE`
  - **Permission**: Admin
  - **Description**: Deactivates the selected flight.
  - **Response model**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `flight-ms/flights/{id} - PUT`
  - **Permission**: Admin
  - **Description**: Allows the admin to modify the details of the selected flight.
  - **Request model**:
    ```json
    { "fromAirlineId": "", "toAirlineId": "", "departureDateTime": "", "arrivalDateTime": "", "initialPrice": "", "airplaneId": "" }
    ```
  - **Response model**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `flight-ms/flights/{id} - PATCH`
  - **Permission**: Admin
  - **Description**: Allows changing the 'available', 'isFly', and 'field' of the selected flight.
  - **Request model**:
    ```json
    { "isFly": "" }
    ```
  - **Response model**:
    ```json
    { "message": "" }
    ```

## Airplane Service (airplane-ms)
### Airplane Management Service
- **Endpoint**: `/airplane-ms/airplanes - GET`
  - **Permission**: Admin
  - **Description**: Displays all airplanes. A parameter is sent to either view all airplanes or only those that are not busy.
  - **Request parameter**:
    ```json
    { "busy": "" }
    ```
  - **Response body**:
    ```json
    [{ "Id": "", "Name": "", "Max seats": "", "Max speed": "" }]
    ```

- **Endpoint**: `/airplane-ms/airplanes/{id} - GET`
  - **Permission**: Admin
  - **Description**: Provides details about the selected airplane.
  - **Response body**:
    ```json
    { "Name": "", "Max seats": "", "Max speed": "" }
    ```

- **Endpoint**: `/airplane-ms/airplanes - POST`
  - **Permission**: Admin
  - **Description**: Used to add a new airplane.
  - **Request body**:
    ```json
    { "Name": "", "Max seats": "", "Max speed": "" }
    ```
  - **Response body**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `/airplane-ms/airplanes - PUT`
  - **Permission**: Admin
  - **Description**: Allows the admin to update the selected airplane.
  - **Request model**:
    ```json
    { "Id": "", "Name": "", "Max seats": "", "Max speed": "" }
    ```
  - **Response model**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `/airplane-ms/airplanes/{id} - DELETE`
  - **Permission**: Admin
  - **Description**: Used to delete the selected airplane from the database.
  - **Response model**:
    ```json
    { "message": "" }
    ```

- **Endpoint**: `/airplane-ms/airplanes/{id} - PATCH`
  - **Permission**: Admin
  - **Description**: Used to change the 'isBusy' field of the airplane.
  - **Request parameter**:
    ```json
    { "IsBusy": "" }
    ```
  - **Response body**:
    ```json
    { "message": "" }
    ```

## Notification Service (notification-ms)
- This service does not have any endpoints. Its primary function is to consume information produced by Kafka and send emails.

## Common Service (common-ms)
### Common Endpoints
- `/common-ms/airlines - GET` - Admin
  - **Permission**: Admin
  - **Description**: When creating a flight, the airlineId is specified. This endpoint is responsible for displaying all airports in the admin dropdown using these airlineIds. It should retrieve a list of airlines.
  - **Response body**:
    ```json
    [{ "id": "", "country": "Azerbaijan", "name": "Baku airline" }, { "id": "", "country": "Turkey", "name": "Sabiha Gokcen airline" }]
    ```
  - **Request parameter**:
    ```json
    { "country": "", "airline": "" } // Both are optional
    ```

- `/common-ms/airlines/{id} - GET` - Admin
  - **Permission**: Admin
  - **Description**: Provides details about the selected airline.
  - **Response body**:
    ```json
    { "id": "", "country": "Azerbaijan", "name": "Baku airline" }
    ```

### Libraries (Libs)
- `:common-exception`
  - Holds all errors and enums common to all services.
- `:common-email`
  - Contains services and objects for sending emails.
- `:common-notification`
  - Contains objects used for producing information to Kafka, consumed by the Notification Service.
- `:common-security`
  - Implements common security logic, requiring Authentication headers and User-id in all authenticated requests, possibly using AOP.
- `:common-file-generator`
  - Encapsulates PDF generation logic within a library. Classes and objects from here can be easily used in services where PDF generation is required.
- `:common`
  - Used to add any additional objects or logic that may be required.

## Common Guidelines
- Apply validation on request models.
- Do not forget to include Authentication and User-id in the headers of authenticated endpoints.
- Pay attention to response codes. Do not return 200 in case of an error; customize each error. Consider race conditions and potential inconsistencies in resources when multiple customers access the same resource simultaneously. Parallel processing logic may be necessary in such cases.
- Keep documentation updated with any additional requirements or changes.
