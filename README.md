## Identity Authorization Service
A REST API for User and Role management, token generation and validation

## Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Data Seed](#data-seed)
- [Deployment](#deployment)
- [Tests](#tests)


## Features
This features are available in this service

- **User Registration:** Register user and send email for confirmation
- **Password reset: Send email with a reset link**
- **Authentication with JWT:** Generate JWT Access token and a random string for Refresh token the login
- **Logger:** Log info/error message into a log file
- **Role based authorization:** Allow or denied access to a resource according to user's role
- **Swagger:** API Documented with Swagger 2
- **HTML Email:** Use Thymeleaf to build HTML templates for email

## Prerequisites
- JDK 8
- Maven
- MongoDB
- Redis

## Installation
- Clone the repository
```bash
$ git clone https://github.com/tericcabrel/identity-authorization.git [project_name]
```

- Install dependencies
```bash
$ cd [project_name]
$ mvn install
```
- Create the configuration file and update with your local config
```bash
$ cd src/main/resources
$ cp application-example.properties application.properties
$ nano application.properties
```
- Start Application
```bash
$ mvn spring-boot:run
```
Note: An IDE like **IntelliJ** can perform these tasks for you automatically

## Data Seed
Inside the package **com.tericcabrel.authorization.bootstrap**, the file named _DataSeeder.java_ 
is responsible for loading data on application startup.
It's there the creation of default roles **(ROLE_ADMIN, ROLE_USER)**, 
and the Super Admin (the first user with the role: ROLE_ADMIN) are performed

## Deployment
At the root of the folder, there is a file named **deploy.sh** who is bash file who content
who contains scripts to deploy our app. The main steps are:

- Backup the local configuration file and create one for production 
- Build **.jar** the package
- Delete configuration for production and restore local configuration
- Build docker image with his dependencies **(see docker-compose.yml)** and run the container

## Tests
**Unit tests and Integration tests will come soon**