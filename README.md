# message-board-api

A sample Restful API for a message board application where users can create, update and delete their own messages 
and list all the messages of the board.

### Dependencies
It's a Maven project and the pom.xml file has the dependencies mentioned below:

Spring Boot Web for rest controller, <br/>
Spring Boot JPA for database operations, <br/>
Spring Boot Test for unit testing, <br/>
Spring Boot Security and Spring Boot Oauth2 Resource Server to secure the application, <br/>
Spring Boot Validation to validate rest api inputs, <br/>
H2Database for the in memory database usage.

### Resources
There are 2 property resources for the project:

/src/main/resources/application.yml is the main property resource. 

/src/test/resources/application.yml is used by unit test methods with 'test' profile 
they use.

Dockerfile has necessary configuration to create a Docker image of an AWS compatible Java 11 application.

appspec.yml, buildspec.yml and taskdef.json are CI/CD files to be used with CodePipeline service of AWS

### Building the project

A jar file can be created with "mvn package" command to test the project on a local computer.

With CodePipeline service of AWS, the application is now running on Elastic Container Service with a 
serverless methodology. Github Webhooks is used to trigger the CodePipeline. CodeBuild service of AWS uses this 
repository as a source and creates a Docker image to push to ECR (Elastic Container Registry) service of AWS.
CodeDeploy service of AWS starts a blue/green deployment to run a new application on Elastic Container Service with 
the docker image which is stored in ECR service.

Here is a screenshot of existing pipeline;

 ![image info](./images/codepipeline.jpg)

### Testing the project

There are three ways to test the application. 

####1 - Using the demo front-end application: 

There is a demo front-end application which can be used to test the back-end 
api. 

Cognito User Pools service of AWS is the IAM service of the demo environment. The user pool has a client configuration 
to use Oauth2 Authorization Code flow. To use demo applications, user should sign-in and get a temporary token which will
later be used to get access token. 

https://msgboard.serdarburakguneri.com  is the url of the demo front-end application.

The source code can be found at : https://github.com/serdarburakguneri/message-board-ui

 ![image info](./images/frontend1.jpg)
 
####2- Using the demo back-end application. 

It's better to have a back-end application in a private subnet but a public URL was created for demonstration purposes.

Since the application is secure, a valid jwt is necessary to get successful responses. There is a section in the front-end 
application where jwt and user id info can be found. 

 ![image info](./images/frontend2.jpg)
 
 ````
a) Creating a message
 
Method : POST
URL:  https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/user/{USER_ID}/message
(Please find your user id from the demo front-end application)
HEADERS: 'Authorization: BEARER {JWT}' , 'Content-Type: application/json'
(Please find your jwt from the demo front-end application)
BODY:  { "text" : "messageToCreate" }
 
Request
 
curl --location --request POST 'https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/user/26c21f47-5823-4252-9731-8c77cbf4c2a1/message' \
--header 'Authorization: Bearer eyJraWQiOiJoUWlmM2pTZFB0OGFZRVYreHVyOEt5MDY0SE1JSUg5Y0hLSWNUSDEwWlwvbz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyNmMyMWY0Ny01ODIzLTQyNTItOTczMS04Yzc3Y2JmNGMyYTEiLCJldmVudF9pZCI6ImM2OGQ0MTJjLTI5M2MtNDRkNi05MTVjLWMyNTNlMzI5MWZmOCIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoib3BlbmlkIiwiYXV0aF90aW1lIjoxNjI2MzUwOTIzLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0yLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMl9NWFQ5amVXSDUiLCJleHAiOjE2MjY0MzczMjMsImlhdCI6MTYyNjM1MDkyMywidmVyc2lvbiI6MiwianRpIjoiY2EyYjViMTMtZTkzNS00OTBiLThhZTMtODI4NGM2OTMwYTQ5IiwiY2xpZW50X2lkIjoiMWhybW85azBnbGZoOXVkZ25pZ2kzMGxydGQiLCJ1c2VybmFtZSI6InNiZ3VuZXJpIn0.v5DE3PmyEVu7s0ZCwvSrIqSYQ-IpBaZsAmy7TZi1tc-XUBr_JNTj76ATEXci4wI71_2CRFD7BsJ2FA8D10Z1vMkYAkSrW4UwzYorotKPCIQAqYEy8REMdBouOJ9fR4drR3esz8Y5PiDFD5LMkU7aEipMCYq2LKWxcTRC7q5tASMIDY0cUhyxp5vI_WIcOV4qrla55aKUEcig2rVUrDVOi82UQ_okkfaxvy2V9M9tBhMnQ_R_Qu3tZEUJ8AgkzwNKlr3tCp1kY8E908ejZxuZd96p-si4ROLAbuTH01czXeZT5N8erLdiMYCs6prQDt82SCWYCcyhX7211CPyseg1UA' \
--header 'Content-Type: application/json' \
--data-raw '{
    "text" : "hi"
}'
 
(Please note message id for update and delete operations)

Response 201 CREATED

{
    "id": "ae8dea29-3653-4167-8fcf-37ad332c3864",
    "text": "hi",
    "senderId": "26c21f47-5823-4252-9731-8c77cbf4c2a1",
    "createdAt": "2021-07-16T00:16:32.031476Z",
    "modifiedAt": "2021-07-16T00:16:32.031484Z",
    "version": 0
}

````

````
b) Updating a message
 
Method : PUT
URL:  https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/user/{USER_ID}/message/{MESSAGE_ID}
(Please find your user id from the demo front-end application and use the message id from create message response)
HEADERS: 'Authorization: BEARER {JWT}' , 'Content-Type: application/json'
(Please find your jwt from front end)
BODY:  { "text" : "messageToUpdate" }
  
Request
   
curl --location --request PUT 'https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/user/26c21f47-5823-4252-9731-8c77cbf4c2a1/message/ebd622ff-9fbe-49c7-8894-0ef190d9f75f' \
--header 'Authorization: Bearer eyJraWQiOiJoUWlmM2pTZFB0OGFZRVYreHVyOEt5MDY0SE1JSUg5Y0hLSWNUSDEwWlwvbz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyNmMyMWY0Ny01ODIzLTQyNTItOTczMS04Yzc3Y2JmNGMyYTEiLCJldmVudF9pZCI6ImM2OGQ0MTJjLTI5M2MtNDRkNi05MTVjLWMyNTNlMzI5MWZmOCIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoib3BlbmlkIiwiYXV0aF90aW1lIjoxNjI2MzUwOTIzLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0yLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMl9NWFQ5amVXSDUiLCJleHAiOjE2MjY0MzczMjMsImlhdCI6MTYyNjM1MDkyMywidmVyc2lvbiI6MiwianRpIjoiY2EyYjViMTMtZTkzNS00OTBiLThhZTMtODI4NGM2OTMwYTQ5IiwiY2xpZW50X2lkIjoiMWhybW85azBnbGZoOXVkZ25pZ2kzMGxydGQiLCJ1c2VybmFtZSI6InNiZ3VuZXJpIn0.v5DE3PmyEVu7s0ZCwvSrIqSYQ-IpBaZsAmy7TZi1tc-XUBr_JNTj76ATEXci4wI71_2CRFD7BsJ2FA8D10Z1vMkYAkSrW4UwzYorotKPCIQAqYEy8REMdBouOJ9fR4drR3esz8Y5PiDFD5LMkU7aEipMCYq2LKWxcTRC7q5tASMIDY0cUhyxp5vI_WIcOV4qrla55aKUEcig2rVUrDVOi82UQ_okkfaxvy2V9M9tBhMnQ_R_Qu3tZEUJ8AgkzwNKlr3tCp1kY8E908ejZxuZd96p-si4ROLAbuTH01czXeZT5N8erLdiMYCs6prQDt82SCWYCcyhX7211CPyseg1UA' \
--header 'Content-Type: application/json' \
--data-raw '{ 
    "text" : "At 2 pm"    
}'

Response 200 OK

{
    "id": "ebd622ff-9fbe-49c7-8894-0ef190d9f75f",
    "text": "At 2 pm",
    "senderId": "26c21f47-5823-4252-9731-8c77cbf4c2a1",
    "createdAt": "2021-07-15T23:31:34.913482Z",
    "modifiedAt": "2021-07-16T01:22:43.716720Z",
    "version": 1
}

 ````

````
c) Deleting a message
  
Method : DELETE
URL:  https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/user/{USER_ID}/message/{MESSAGE_ID}
(Please find your user id from the demo front-end application and use the message id from create message response)
HEADERS: 'Authorization: BEARER {JWT}'
(Please find your jwt from front end)
   
Request
   
curl --location --request DELETE 'https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/user/26c21f47-5823-4252-9731-8c77cbf4c2a1/message/ae8dea29-3653-4167-8fcf-37ad332c3864' \
--header 'Authorization: Bearer eyJraWQiOiJoUWlmM2pTZFB0OGFZRVYreHVyOEt5MDY0SE1JSUg5Y0hLSWNUSDEwWlwvbz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyNmMyMWY0Ny01ODIzLTQyNTItOTczMS04Yzc3Y2JmNGMyYTEiLCJldmVudF9pZCI6ImM2OGQ0MTJjLTI5M2MtNDRkNi05MTVjLWMyNTNlMzI5MWZmOCIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoib3BlbmlkIiwiYXV0aF90aW1lIjoxNjI2MzUwOTIzLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0yLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMl9NWFQ5amVXSDUiLCJleHAiOjE2MjY0MzczMjMsImlhdCI6MTYyNjM1MDkyMywidmVyc2lvbiI6MiwianRpIjoiY2EyYjViMTMtZTkzNS00OTBiLThhZTMtODI4NGM2OTMwYTQ5IiwiY2xpZW50X2lkIjoiMWhybW85azBnbGZoOXVkZ25pZ2kzMGxydGQiLCJ1c2VybmFtZSI6InNiZ3VuZXJpIn0.v5DE3PmyEVu7s0ZCwvSrIqSYQ-IpBaZsAmy7TZi1tc-XUBr_JNTj76ATEXci4wI71_2CRFD7BsJ2FA8D10Z1vMkYAkSrW4UwzYorotKPCIQAqYEy8REMdBouOJ9fR4drR3esz8Y5PiDFD5LMkU7aEipMCYq2LKWxcTRC7q5tASMIDY0cUhyxp5vI_WIcOV4qrla55aKUEcig2rVUrDVOi82UQ_okkfaxvy2V9M9tBhMnQ_R_Qu3tZEUJ8AgkzwNKlr3tCp1kY8E908ejZxuZd96p-si4ROLAbuTH01czXeZT5N8erLdiMYCs6prQDt82SCWYCcyhX7211CPyseg1UA'

Response 200 OK

````

````
d) Listing all messages
  
Method : GET
URL:  https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/message  
HEADERS: 'Authorization: BEARER {JWT}'
(Please find your jwt from the demo front-end application)
   
Request
  
curl --location --request GET 'https://msgboardapi.serdarburakguneri.com/msgboard/api/v1/message?page=0&size=10' \
--header 'Authorization: Bearer eyJraWQiOiJoUWlmM2pTZFB0OGFZRVYreHVyOEt5MDY0SE1JSUg5Y0hLSWNUSDEwWlwvbz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyNmMyMWY0Ny01ODIzLTQyNTItOTczMS04Yzc3Y2JmNGMyYTEiLCJldmVudF9pZCI6ImM2OGQ0MTJjLTI5M2MtNDRkNi05MTVjLWMyNTNlMzI5MWZmOCIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoib3BlbmlkIiwiYXV0aF90aW1lIjoxNjI2MzUwOTIzLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0yLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMl9NWFQ5amVXSDUiLCJleHAiOjE2MjY0MzczMjMsImlhdCI6MTYyNjM1MDkyMywidmVyc2lvbiI6MiwianRpIjoiY2EyYjViMTMtZTkzNS00OTBiLThhZTMtODI4NGM2OTMwYTQ5IiwiY2xpZW50X2lkIjoiMWhybW85azBnbGZoOXVkZ25pZ2kzMGxydGQiLCJ1c2VybmFtZSI6InNiZ3VuZXJpIn0.v5DE3PmyEVu7s0ZCwvSrIqSYQ-IpBaZsAmy7TZi1tc-XUBr_JNTj76ATEXci4wI71_2CRFD7BsJ2FA8D10Z1vMkYAkSrW4UwzYorotKPCIQAqYEy8REMdBouOJ9fR4drR3esz8Y5PiDFD5LMkU7aEipMCYq2LKWxcTRC7q5tASMIDY0cUhyxp5vI_WIcOV4qrla55aKUEcig2rVUrDVOi82UQ_okkfaxvy2V9M9tBhMnQ_R_Qu3tZEUJ8AgkzwNKlr3tCp1kY8E908ejZxuZd96p-si4ROLAbuTH01czXeZT5N8erLdiMYCs6prQDt82SCWYCcyhX7211CPyseg1UA'

Response 200 OK

[  
    {
        "id": "ebd622ff-9fbe-49c7-8894-0ef190d9f75f",
        "text": "At 2 pm",
        "senderId": "26c21f47-5823-4252-9731-8c77cbf4c2a1",
        "createdAt": "2021-07-15T23:31:34.913482Z",
        "modifiedAt": "2021-07-16T01:22:43.716720Z",
        "version": 1
    },
    {
        "id": "89825152-480e-4e1e-b606-2d3e6980d254",
        "text": "What time does the meeting start?",
        "senderId": "ac66d86a-5ccd-4376-88b2-d9833b0eaae1",
        "createdAt": "2021-07-15T23:21:13.232130Z",
        "modifiedAt": "2021-07-15T23:29:14.013928Z",
        "version": 1
    },
    {
        "id": "ee690511-a739-447c-ab29-3f204ee1c2a0",
        "text": "Get ready for the retrospective!",
        "senderId": "26c21f47-5823-4252-9731-8c77cbf4c2a1",
        "createdAt": "2021-07-15T23:18:57.700770Z",
        "modifiedAt": "2021-07-15T23:18:57.700777Z",
        "version": 0
    }
]

````

#### 3 - Using localhost

The application uses port 8080 so please change base url to http://localhost:8080 and try the steps of second approach.
Using a test profile or updating application.yml file by disabling security (security.configuration.enabled = false)
is the only way to test the application without authentication.

  
   


 