Quiz Micro-service:

Introduction:-
This is the micro-service for Surveys. The micro-service is running on port = 8080. There are few APIs as follows:-

1. Authentication endpoint (/token):-
- It is a public endpoint.
- It is used to generate a JWT which will be passed to validate the user when they want to access the survey APIs.
- It requires a json to generate token which are username and password which can be anything but not null or empty. e.g :-
- It will return a json response containing the token value.

2. Survey REST API endpoints:-
The following endpoints are protected and we require the above JWT obtained in the “Authentication” endpoint to attach to each request. If the JWT is missing or invalid, these endpoints will be rejected.
    a. Create a Survey (/create-survey):-
    - It is a protected endpoint and require JWT in the header to access it.
    - It is a POST request and require json to be passed in the request body which contains details about the survey like surveyId, and questions inorder to create the survey.   
    - It will create an entry in the Surveys table using SQL which has a single column “surveyId” and “Questions” table which will be having all the questions of the survey. “Questions” table contains surveyId, question column.
    b. Take a Survey (/take-survey):-
    - It is also a protected endpoint. It is a POST request which requires a json which contains the surveyId you want to take and a list of answers.
    - It will return a json containing message which will tell you about status of your response to the survey.
    - This message tells that your response has been recorded.
    c. Results of a Survey (/results/{surveyId}):-
    - It is also a protected endpoint. We can see the result of each survey by passing the surveyId into the url.
    - It is a GET request which will return a json containing the result of the survey.
     d. See all the Surveys (/survey):-
    - It is also a protectes endpoint and, we can access it.
    - It is a GET request which will return a json containing the list of all the surveys created till now.
    
3. Image Thumbnail generation endpoint:-
- It is also a protected endpoint. It is a POST request and require json which will be containing the public url of the image.
- It will return a json response containing the url of the thumbnail.
    
