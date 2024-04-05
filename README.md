[![forthebadge](https://forthebadge.com/images/badges/made-with-typescript.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.png)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/build-with-spring.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/code-it-test-it-break-it.svg)](https://forthebadge.com)

# Yoga Session App
Yoga App is a Web Application for managing yoga sessions.

## 🔥Features

- Register as new user & secured Login
- Create Yoga sessions, update sessions & browse existing sessions.
- Subscribe (Participate) and unsubscribe (Unparticipate) to existing sessions.

# Requirements

## Frontend
- [NodeJS](https://nodejs.org/en)

## Backend
- [Java jdk](https://www.oracle.com/en/java/technologies/downloads/) >= v.8
- [MySQL Database Server](https://www.mysql.com/en/)

# Technologies

## Frontend

- NodeJS >= 10.13
- Angular 14
- RxJs v.7.5.6
- Jest v.28.1.3
- Cypress v.10.4.0

## Backend

- Java jdk >= v.8
- Maven
- Spring boot v. 2.6.1
- Spring Security
- Spring Data JPA
- JsonWebToken v. 0.9.1
- Lombok
- MapStruct v. 1.5.1
- JUnit 5 v.1.8
- Jacoco Maven plugin v. 0.8.5
- MySQL Database Driver (used for production)
- H2 Database Driver (used for testing)

# Contribute to the project

Yoga App is an open source project. Feel free to fork the source and contribute with your own features.

# Authors

- Coder : [Joffrey Hernandez](https://github.com/JoffreyHernandez)
- Tester : Patrice

# Licensing

# 🧬 Installing and running

## Running locally for development

To install locally, you must first clone the repository.
```shell
git clone https://github.com/Picrate/OpenClassrooms-Projet-5.git
```
## Install MySQL server

If MySQL server is not installed in your computer follow these installation instructions:

- [Download & Install](https://dev.mysql.com/downloads/installer/)
- [MySQL Reference Manual](https://dev.mysql.com/doc/refman/8.0/en/)

### Create Database, MySQL user & configure Grant Access for user
First we connect to MySQL Server as **root**;
- From shell
```shell
mysql -p
Enter password : 
```
- Then create an empty database (choose your own)
```SQL
CREATE DATABASE IF NOT EXISTS '<dbname>';
```
- Create a database user with password (choose you own)
```SQL
CREATE USER '<dbuser>'@'*' IDENTIFIED BY '<should_be_changed>';
```
- Grant all privileges to <dbuser> on database <dbname>
```SQL
GRANT ALL PRIVILEGES ON <dbname>.* TO '<dbuser>'@'%';
```
- Exit MySQL
```SQL
exit;
```
#### Execute schema.sql
You will find in the root of the repository a directory named `SQL` which contain the database schema creation script: `Schema.sql`.
For creating database schema and insert some datas, use the following command in a terminal:

```shell
mysql -u dbuser -p 'dbname' < Schema.sql
```
Your database is ready.

## Backend
Backend application can be found in "back" subdirectory.
### Modify your backend application.properties
- Modify src/main/resources/application.properties to suite your database name and dbUsername & password
```
spring.datasource.url=jdbc:mysql://localhost:3306/<dbname>?allowPublicKeyRetrieval=true
spring.datasource.username=<dbuser>
spring.datasource.password=<dbpassword>
...
```

### Create .jar package
```bash
mvn clean install
```
This will clean the project, download and install required dependencies, run tests and compile project into .jar file.

***If you don't want to include and execute tests in release***
```bash
mvn clean package -Dmaven.test.skip=true
```
### Running application
Compiled application can be found in target directory
To run this application, copy the 'yoga-app-0.0.1-SNAPSHOT.jar' to a clean directory then run:
```bash
java -jar yoga-app-0.0.1-SNAPSHOT.jar
```
This will run springboot executable and the backend API server will be accessible to 'http://localhost:8080'

**N.B.** Be sure to have MySQL server instance up and running before launching application.

## Testing
If you only want to run unit tests and integration tests, simply run:

```bash
mvn clean test
```
This will run all the tests and generate coverage report.

The coverage report is available at: 'back/target/site/jacoco/index.html'

![Couverture_Tests_Backend](https://github.com/Picrate/OpenClassrooms-Projet-5/assets/110771424/d4f9be4a-7d38-481e-ae0b-62477c607f8c)

## Frontend

### To build production files
Run:
```bash
ng build
```
The build artifacts will be stored in the `build/libs` directory.

### externalize variables for production use
You can externalize application.properties file outside the jar.
- Copy application.properties in a directory `config` at the root directory containing the jar.
- Replace variables names used for development with the production values.
- Add JWT_SECRET_KEY property.

Launch jar with:
```shell
java -jar yoga-version.jar
```
## Accessing to OpenApi documentation
Simply go to http://localhost:3001/swagger-ui.html

# 🤝 Contributors

This project would not be possible without our amazing contributors and the community.
