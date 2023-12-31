Created 2 test suites- one is for web automation and other one is API automation. Please find both the test suites under src/test/java.

1. WebPortalAutomationTests : Automated 3 scenarios

Test Case 1: User Registration

Objective: To check whether the user registration process functions correctly.

Test Steps:

1. Navigate to the Registration page.
2. Enter required details and click the continue button.
3. Verify that the user is successfully registered or not.

Expected: It's a negative scenario. The user won't be registered. Hence, the test must fail.

Test Case 2: Sorting Functionality

Objective: To ensure that the sorting functionality on a web page works correctly in descending order.

Test Steps:

1. Navigate to the Desktops page.
2. Set the dropdown selection to the maximum allowable value for 'Show'.
3. Select the 'Price(High > Low)' option from the 'Sort By' dropdown to sort the data in descending order.
4. Capture the sorted data.
5. Verify that the captured data is in descending order.

Expected: The data on the page is successfully sorted in descending order.

Test Case 3: Contact Form Submission

Objective: To ensure that the application handles form submission gracefully when no details are provided and 
then, provide the details and verify that the contact form can be filled out and submitted successfully.

Test Steps:

1. Navigate to the contact form page.
2. Keep the form blank and click on the 'Submit' button.
3. Capture and verify the displayed error message and visual indication(Red border on required fields).
4. Now, fill in the required fields of the contact form (e.g., name, email, message).
5. Click on the 'Submit' button.
6. Capture and Verify that the confirmation message indicates a successful submission.

Expected: After step 3, The application displays an error message and visual indication when the form is submitted with no details.
After step 6, The contact form is filled out without errors, and a confirmation message is displayed, indicating a successful 
submission.

=============================================================

2. APIPortalTests : Automated 5 scenarios

Test Case 1: Authenticate User

Objective: Send a POST request to the authentication endpoint and validate that the response status code is 200 OK and get an 
authentication token. 

Test Case 2: Get Booking Ids

Objective: Send a GET request to get all the building ids and validate that the response status code is 200 OK.

Test Case 3: Get Booking Information

Objective: Send a GET request to retrieve booking information and validate that the response status code is 200 OK and check that 
the response contains relevant booking details.

Test Case 4: Create Booking

Objective: Send a POST request to create a new booking and verify that the response status code is 200 Ok and confirm that 
the new booking is created successfully.

Test Case 5: Delete Booking

Objective: Send a DELETE request to delete the above created booking and and verify that the response status code is 201 Created.

