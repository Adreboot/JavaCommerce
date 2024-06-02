# JavaCommerce - PetStore

## Introduction
An online Shop web application using Spring Boot 2, Thymeleaf and Keycloak.

## Prerequisites

- Ensure you have Gradle installed.
- Docker should be installed and running on your system.

## Setup Instructions

### Step 0: Execute Gradle

Ensure Gradle is properly configured by executing the following command:
```sh
gradle
````

### Step 1: Download Backup and Place it in the Project Root
Download the backup.tar file (in raw format) and place it in the root directory of the project. This file contains all the parameters required for Keycloak.

### Step 2: Generate Docker Elements
#### For Windows Users (without Linux sub-shell)
Execute the following batch script:
```sh
run_docker_commands.bat
````

#### For Linux Shell Users
Execute the following shell script:
```sh
./run_docker_commands.sh
````

> 
> Note: In some configurations, the keycloak-petstore container might not function correctly. If this happens, remove the container keycloak-petstore and re-run the script run_docker_commands :
> ```sh
> docker rm keycloak-petstore
> ./run_docker_commands.sh
>````

### Step 3 : Generate the Database for the Application
You can create the database using one of the following methods:

#### With Gradle
```sh
gradle create_db
````

#### With Shell Script
```sh
./createDB.sh petstoreDB root root
````

#### Manually
Manually create a database named petstoreDB.

### Step 4 : Launch the Application
Once the setup is complete, you can launch the application. Ensure all necessary services are running and configurations are properly set.

## User Levels for the Application

1. **Non-logged in User**:
    - This user has not logged into the application. They have limited access and can only view public information.

2. **Logged in USER**:
    - **Username**: `job5`
    - **Password**: `cnam`
    - This user level allows access to standard features and functionalities available to regular users.

3. **Logged in FRANCHISEE**:
    - **Username**: `jeff01`
    - **Password**: `cnam`
    - This user level grants access to additional features and functionalities specifically designed for franchisee users, providing more control and options relevant to managing franchise operations.

4. **Logged in ADMIN**:
    - **Username**: `stb01`
    - **Password**: `cnam`
    - This is the highest level of access, granting administrative privileges. Admin users can manage other users, modify settings, and access all areas of the application.

Each level of user has progressively more access and control within the application.
