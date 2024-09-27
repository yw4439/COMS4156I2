# COMS-4156 Individual Project

This is the GitHub repository for the Individual Project associated with COMS 4156 Advanced Software Engineering. The project involves building and deploying a service to Google Cloud with a Continuous Integration (CI) pipeline, unit tests, and cloud deployment.

## Building and Running a Local Instance

To build and run the project locally, you need to install the following:

### Requirements:

1. Maven 3.9.9: Download Maven and follow the installation instructions. Ensure you set the bin directory in your system’s path if you're on Windows or follow the Mac setup instructions.

2. JDK 22: Download JDK 22. This project is developed using Oracle OpenJDK 22.0.2.

3. IntelliJ IDEA (or another IDE): You can download IntelliJ IDEA or use any other IDE you are comfortable with.

### Steps to Build:
Clone the Repository: Open your IDE and use the clone from GitHub option to pull the repository. You can copy the GitHub link by clicking on the green code button in the GitHub repository page.
Build with Maven:

* To package the project, run the following Maven command:
mvn -B package --file pom.xml

* To run the application, execute the IndividualProjectApplication.java class from your IDE.

* To check the code style using Checkstyle, run:
mvn checkstyle:check

* To generate a style report, use:
mvn checkstyle:checkstyle

* To generate test case coverage report, use:
mvn jacoco:report

### Running Tests:
The unit tests for the project are located in the src/test directory. After building the project, you can run these tests from your IDE by right-clicking the test files and selecting Run.

## Running a Cloud-Based Instance

This project has been deployed to Google Cloud Run, and you can access it through the following link:
Base Cloud Run URL: https://individualprojectapplication-765458693898.us-central1.run.app

### Postman Testing:
To test the API endpoints, you can use Postman or your browser. Below are examples of how to call each endpoint using the Cloud Run service URL:

1. Find Course Time:

URL:https://individualprojectapplication-765458693898.us-central1.run.app/findCourseTime?deptCode=PSYC&courseCode=1001
Method: GET

2. Retrieve Course Details:

URL:https://individualprojectapplication-765458693898.us-central1.run.app/retrieveCourse?deptCode=PSYC&courseCode=1001
Method: GET

3. Enroll Student in Course:

URL:https://individualprojectapplication-765458693898.us-central1.run.app/enrollStudentInCourse?deptCode=PSYC&courseCode=1001
Method: PATCH


## CI/CD Pipeline
The project uses GitHub Actions to automate the build and test processes. Every push to the main branch triggers the pipeline, which includes:

Running unit tests.
* Generating code coverage reports using JaCoCo.
* Ensuring code style compliance using Checkstyle.
* To view the latest CI results, go to the Actions tab in the GitHub repository and select the most recent workflow run.

## API Endpoints
The service provides the following endpoints:

1. GET /findCourseTime
* Parameters: deptCode (e.g., "PSYC"), courseCode (e.g., "1001")
* Description: Retrieves the course time for the specified department and course.

2. GET /retrieveCourse
* Parameters: deptCode (e.g., "PSYC"), courseCode (e.g., "1001")
* Description: Retrieves the details of the specified course.

3. PATCH /enrollStudentInCourse
* Parameters: deptCode, courseCode
* Description: Enrolls a student in the specified course.
* Postman Test Documentation
* For detailed examples of the API calls and their results, you can view the Postman Test Documentation here: Postman Documentation.

## Style Checking Report
We used Checkstyle to ensure that the code follows the defined coding standards. You can generate and view the report by running:
mvn checkstyle:checkstyle
This is the report for style check before final changes:

![image](https://github.com/user-attachments/assets/7883f326-c57a-4cef-9549-0e770a6282f2)


## Branch Coverage Reporting
We used JaCoCo to analyze the branch coverage of the codebase. To generate the coverage report, run:
mvn jacoco:report
This is the report for test coverage reported by Jacococ before final changes:

![image](https://github.com/user-attachments/assets/c786d5f8-3a89-4da7-9cb0-314af00da3e7)


## Tools Used 🧰

* Maven: For dependency management and building the project.

* Google Cloud Run: For deploying the service in a fully managed environment.

* Postman: For testing and documenting API calls.

* JaCoCo: For code coverage analysis.

* Checkstyle: For code style analysis and reporting.

* Docker: To containerize the application and deploy it to Google Cloud Run.

* Github: For continuous integration, including running tests and generating reports automatically.
