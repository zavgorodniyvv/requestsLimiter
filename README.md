Application RequstLimiter created as Web API usage by preventing abuse caused by excessive requests from multiple users (quota).
How to run application:
1. If you have already installed MySQL on your machine:
  1.1. you need to edit related properties in application.properties file:
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>?useSSL=false&serverTimezone=UTC
spring.datasource.username=<user_name>
spring.datasource.password=<password>
  1.2. connect to MySQL and run sql scripts from resources/sql/init.sql file.
  1.3. Run application with main() method from LimiterApplication class.

2. If you have alredy intalled Docker on your machine, you could run application with main() method from src/test/java/com/example/limiter/DevLimiterApplication.java
It will run TestContainers framework which will up Docker container with latest MySQL DB, automatecally will configure it, create database and table User.

3. After you run application, you could use CRUD endpoints for Users:
3.1. Add user.
   POST localhost:8080/api/v1/users
   body:
   {
    "firstName": "Mick",
    "lastName": "Jagger"
  }
responce:
{
    "id": "d92de0bb-d912-4908-904c-863303a20d18",
    "firstName": "Mick",
    "lastName": "Jagger",
    "lastLoginTimeUtc": "2024-02-26T07:07:02.692732451",
    "quota": 0,
    "fullName": "Mick Jagger",
    "blocked": false
}
3.2. Get user:
   GET localhost:8080/api/v1/users/d92de0bb-d912-4908-904c-863303a20d18
   responce:
{
    "id": "d92de0bb-d912-4908-904c-863303a20d18",
    "firstName": "Mick",
    "lastName": "Jagger",
    "lastLoginTimeUtc": "2024-02-26T07:07:03",
    "quota": 0,
    "fullName": "Mick Jagger",
    "blocked": false
}
3.3. Update User
   PUT localhost:8080/api/v1/users/d92de0bb-d912-4908-904c-863303a20d18
   body:
   {
    "firstName": "Freddy",
    "lastName": "Mercury"
    }
responce:
{
    "id": "d92de0bb-d912-4908-904c-863303a20d18",
    "firstName": "Freddy",
    "lastName": "Mercury",
    "lastLoginTimeUtc": "2024-02-26T07:10:21.859721684",
    "quota": 0,
    "fullName": "Freddy Mercury",
    "blocked": false
}
3.4 Delete user:
   DELETE localhost:8080/api/v1/users/d92de0bb-d912-4908-904c-863303a20d18
respoce:
{
    "id": "d92de0bb-d912-4908-904c-863303a20d18",
    "firstName": "Freddy",
    "lastName": "Mercury",
    "lastLoginTimeUtc": "2024-02-26T07:10:22",
    "quota": 0,
    "fullName": "Freddy Mercury",
    "blocked": false
}
4. Dummy API - emulation of API call with quota controll. Call will be anable, if quota is not reached. If quota reached - API will responce with error and user will be blocked 
   4.1 GET localhost:8080/api/v1/dummy?userId=ffca1b74-5818-4fd1-9016-947c2134ac6f
   responce quota was not reached: 
     Dummy request handled successfully
   responce if quota was reached:
     Quota was reached. User is blocked
5. Quota API
   5.1  Concumer quota:
   GET localhost:8080/api/v1/quota/ffca1b74-5818-4fd1-9016-947c2134ac6f
   responce:
     6
   5.2 Get users quota:
   GET localhost:8080/api/v1/quota
   responce:
   {
    "Mick Jagger": 2,
    "Freddy Mercury": 3
    }
   
