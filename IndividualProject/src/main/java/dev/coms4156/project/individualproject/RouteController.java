package dev.coms4156.project.individualproject;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains all the API routes for the system.
 */
@RestController
public class RouteController {

  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "Welcome, in order to make an API call direct your browser or Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n http:127.0.0"
        + ".1:8080/endpoint?arg=value";
  }

  /**
   * Returns the details of the specified department.
   *
   * @param deptCode A {@code String} representing the department the user wishes
   *                 to retrieve.
   *
   * @return A {@code ResponseEntity} object containing either the details of the Department and
   *         an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @GetMapping(value = "/retrieveDept", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDepartment(@RequestParam(value = "deptCode") String deptCode) {
    try {
      // Check if deptCode is null or empty
      if (deptCode == null || deptCode.trim().isEmpty()) {
        return new ResponseEntity<>("Department code cannot be null or empty",
                HttpStatus.BAD_REQUEST);
      }

      HashMap<String, Department> departmentMapping =
              IndividualProjectApplication.myFileDatabase.getDepartmentMapping();

      // Normalize deptCode to uppercase for consistent lookup
      String normalizedDeptCode = deptCode.toUpperCase();

      // Check if the department exists in the mapping
      if (!departmentMapping.containsKey(normalizedDeptCode)) {
        return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
      } else {
        // Assuming the Department class has a meaningful toString() method
        Department department = departmentMapping.get(normalizedDeptCode);
        return new ResponseEntity<>(department.toString(), HttpStatus.OK);
      }

    } catch (Exception e) {
      // Return a more specific error message and status
      return new ResponseEntity<>("An error occurred: " + e.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Displays the details of the requested course to the user or displays the proper error
   * message in response to the request.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   *
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to retrieve.
   *
   * @return           A {@code ResponseEntity} object containing either the details of the
   *                   course and an HTTP 200 response or, an appropriate message indicating the
   *                   proper response.
   */
  @GetMapping(value = "/retrieveCourse", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveCourse(@RequestParam(value = "deptCode") String deptCode,
                                          @RequestParam(value = "courseCode") int courseCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if dept exist
      boolean doesDepartmentExists = retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        // Check if course exist in dept
        if (coursesMapping.containsKey(Integer.toString(courseCode))) {
          return new ResponseEntity<>(coursesMapping.get(Integer.toString(courseCode)).toString(),
                  HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
        }
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Displays whether the course has at minimum reached its enrollmentCapacity.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   *
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to retrieve.
   *
   * @return           A {@code ResponseEntity} object containing either the requested information
   *                   and an HTTP 200 response or, an appropriate message indicating the proper
   *                   response.
   */
  @GetMapping(value = "/isCourseFull", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> isCourseFull(@RequestParam(value = "deptCode") String deptCode,
                                        @RequestParam(value = "courseCode") int courseCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode.toUpperCase()).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>(requestedCourse.isCourseFull(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Displays the number of majors in the specified department.
   *
   * @param deptCode     A {@code String} representing the department the user wishes
   *                     to find number of majors for.
   *
   * @return             A {@code ResponseEntity} object containing either number of majors for the
   *                     specified department and an HTTP 200 response or, an appropriate message
   *                     indicating the proper response.
   */
  @GetMapping(value = "/getMajorCountFromDept", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getMajorCtFromDept(@RequestParam(value = "deptCode") String deptCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if dept exist
      boolean doesDepartmentExists = retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        HashMap<String, Department> departmentMapping =
                IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        return new ResponseEntity<>("There are: "
                + departmentMapping.get(deptCode).getNumberOfMajors()
                + " majors in the department", HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found",
              HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Displays the department chair for the specified department.
   *
   * @param deptCode  A {@code String} representing the department the user wishes
   *                  to find the department chair of.
   *
   * @return          A {@code ResponseEntity} object containing either department chair of the
   *                  specified department and an HTTP 200 response or, an appropriate message
   *                  indicating the proper response.
   */
  @GetMapping(value = "/idDeptChair", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> identifyDeptChair(@RequestParam(value = "deptCode") String deptCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if dept exist
      boolean doesDepartmentExists = retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        HashMap<String, Department> departmentMapping =
                IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        return new ResponseEntity<>(departmentMapping.get(deptCode).getDepartmentChair()
                + " is the department chair.", HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Displays the location for the specified course.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   *
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to find information about.
   *
   * @return           A {@code ResponseEntity} object containing either the location of the
   *                   course and an HTTP 200 response or, an appropriate message indicating the
   *                   proper response.
   */
  @GetMapping(value = "/findCourseLocation", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findCourseLocation(@RequestParam(value = "deptCode") String deptCode,
                                              @RequestParam(value = "courseCode") int courseCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>(requestedCourse.getCourseLocation() + " is where the course "
            + "is located.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Displays the instructor for the specified course.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   *
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to find information about.
   *
   * @return           A {@code ResponseEntity} object containing either the course instructor and
   *                   an HTTP 200 response or, an appropriate message indicating the proper
   *                   response.
   */
  @GetMapping(value = "/findCourseInstructor", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findCourseInstructor(@RequestParam(value = "deptCode") String deptCode,
                                                @RequestParam(value = "courseCode")
                                                int courseCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>(requestedCourse.getInstructorName() + " is the instructor for"
            + " the course.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Displays the time the course meets at for the specified course.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   *
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to find information about.
   *
   * @return           A {@code ResponseEntity} object containing either the details of the
   *                   course timeslot and an HTTP 200 response or, an appropriate message
   *                   indicating the proper response.
   */
  @GetMapping(value = "/findCourseTime", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findCourseTime(@RequestParam(value = "deptCode") String deptCode,
                                          @RequestParam(value = "courseCode")
                                          int courseCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>("The course meets at: " + requestedCourse.getCourseTimeSlot(),
                HttpStatus.OK);

      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempts to add a student to the specified department.
   *
   * @param deptCode       A {@code String} representing the department.
   *
   * @return               A {@code ResponseEntity} object containing an HTTP 200
   *                       response with an appropriate message or the proper status
   *                       code in tune with what has happened.
   */
  @PatchMapping(value = "/addMajorToDept", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addMajorToDept(@RequestParam(value = "deptCode") String deptCode) {
    try {
      // Check if deptCode is null or empty
      if (deptCode == null || deptCode.trim().isEmpty()) {
        return new ResponseEntity<>("Department code cannot be null or empty",
                HttpStatus.BAD_REQUEST);
      }

      // Check if dept exist
      boolean doesDepartmentExists = retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        HashMap<String, Department> departmentMapping =
                IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Department specifiedDept = departmentMapping.get(deptCode);
        specifiedDept.addPersonToMajor();
        return new ResponseEntity<>("Attribute was updated successfully", HttpStatus.OK);
      }

      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempts to remove a student from the specified department.
   *
   * @param deptCode       A {@code String} representing the department.
   *
   * @return               A {@code ResponseEntity} object containing an HTTP 200
   *                       response with an appropriate message or the proper status
   *                       code in tune with what has happened.
   */
  @PatchMapping(value = "/removeMajorFromDept", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> removeMajorFromDept(@RequestParam(value = "deptCode") String deptCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if dept exist
      boolean doesDepartmentExists = retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Department specifiedDept = departmentMapping.get(deptCode);
        specifiedDept.dropPersonFromMajor();
        return new ResponseEntity<>("Attribute was updated or is at minimum", HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempts to drop a student from the specified course.
   *
   * @param deptCode       A {@code String} representing the department.
   *
   * @param courseCode     A {@code int} representing the course within the department.
   *
   * @return               A {@code ResponseEntity} object containing an HTTP 200
   *                       response with an appropriate message or the proper status
   *                       code in tune with what has happened.
   */
  @PatchMapping(value = "/dropStudentFromCourse", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> dropStudent(@RequestParam(value = "deptCode") String deptCode,
                                       @RequestParam(value = "courseCode") int courseCode) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        boolean isStudentDropped = requestedCourse.dropStudent();

        if (isStudentDropped) {
          // Check if drop succeed
          return new ResponseEntity<>("Student has been dropped.", HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Student has not been dropped.", HttpStatus.BAD_REQUEST);
        }
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to set the enrollment count for a specific course in the department.

   * @param deptCode   A {@code String} representing the department.
   *
   * @param courseCode A {@code int} representing the course within the department.
   *
   * @param count      A {@code int} representing the new enrollment count
   *
   * @return           A {@code ResponseEntity} a success message if the operation is
   *                   successful, or an error message if the course is not found.
   */
  @PatchMapping(value = "/setEnrollmentCount", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> setEnrollmentCount(@RequestParam(value = "deptCode") String deptCode,
                                              @RequestParam(value = "courseCode") int courseCode,
                                              @RequestParam(value = "count") int count) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.setEnrolledStudentCount(count);
        return new ResponseEntity<>("Attributed was updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Endpoint for changing the time of a course.
   * This method handles PATCH requests to change the time of a course identified by
   * department code and course code.If the course exists, its time is updated to the provided time.
   *
   * @param deptCode                    the code of the department containing the course
   * @param courseCode                  the code of the course to change the time for
   * @param time                        the new time for the course
   *
   * @return                            a ResponseEntity with a success message if the operation is
   *                                    successful, or an error message if the course is not found
   */
  @PatchMapping(value = "/changeCourseTime", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changeCourseTime(@RequestParam(value = "deptCode") String deptCode,
                                            @RequestParam(value = "courseCode") int courseCode,
                                            @RequestParam(value = "time") String time) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }
    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.reassignTime(time);
        return new ResponseEntity<>("Attributed was updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Endpoint for changing the instructor of a course.
   * This method handles PATCH requests to change the instructor of a course identified by
   * department code and course code. If the course exists, its instructor is updated to the
   * provided instructor.
   *
   * @param deptCode                  the code of the department containing the course
   * @param courseCode                the code of the course to change the instructor for
   * @param teacher                   the new instructor for the course
   *
   * @return                          a ResponseEntity with a success message if the operation is
   *                                  successful, or an error message if the course is not found
   */
  @PatchMapping(value = "/changeCourseTeacher", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changeCourseTeacher(@RequestParam(value = "deptCode") String deptCode,
                                               @RequestParam(value = "courseCode") int courseCode,
                                               @RequestParam(value = "teacher") String teacher) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.reassignInstructor(teacher);
        return new ResponseEntity<>("Attributed was updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to changes the location of a specific course in a department.
   * This method change the location of a course specified by its department code and course code.
   * If the course exists, its location will be set to the provided value.
   *
   * @param deptCode      The code of the department containing the course.
   * @param courseCode    The code of the course to change the location for.
   * @param location      The new location for the course.
   *
   * @return              A {@code ResponseEntity} with a success message if the operation is
   *                      successful, or an error message if the course is not found.
   */
  @PatchMapping(value = "/changeCourseLocation", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changeCourseLocation(@RequestParam(value = "deptCode") String deptCode,
                                                @RequestParam(value = "courseCode") int courseCode,
                                                @RequestParam(value = "location") String location) {

    // Check if deptCode is null or empty
    if (deptCode == null || deptCode.trim().isEmpty()) {
      return new ResponseEntity<>("Department code cannot be null or empty",
              HttpStatus.BAD_REQUEST);
    }

    try {
      // Check if course exist
      boolean doesCourseExists;
      doesCourseExists = retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;

      if (doesCourseExists) {
        HashMap<String, Department> departmentMapping;
        departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        HashMap<String, Course> coursesMapping;
        coursesMapping = departmentMapping.get(deptCode).getCourseSelection();

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.reassignLocation(location);
        return new ResponseEntity<>("Attributed was updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  public ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An Error has occurred", HttpStatus.OK);
  }

  /*New stuff for I2*/
  /**
   * Attempt to get courses information
   * This method search all the course in each department to track all the course
   * that has same course code as given.
   *
   * @param courseCode    The code of the course we want to find.
   *
   * @return              A {@code ResponseEntity} with a success message if the course is found,
   *                        or an error message if the course is not found.
   */
  @GetMapping(value = "/retrieveCourses", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveCourses(@RequestParam(value = "courseCode")
                                             String courseCode) {

    StringBuilder result = new StringBuilder();

    try {
      // Get the department mapping from the database
      HashMap<String, Department> departmentMapping =
              IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
      // If no department found we do not need courseFound
      boolean courseFound = false;

      // Loop through all the department
      for (Department dept : departmentMapping.values()) {
        if (dept.getCourseSelection().containsKey(courseCode)) {
          Course course = dept.getCourseSelection().get(courseCode);
          result.append(dept.getDeptCode()).append(" ").append(courseCode).append(": ")
                  .append(course.toString()).append("\n");
          courseFound = true;
        }
      }

      if (!courseFound) {
        return new ResponseEntity<>("No course found with code: "
                + courseCode, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(result.toString(), HttpStatus.OK);

    } catch (Exception e) {
      return handleException(e);
    }
  }


  /**
   * Attempt to add a student to specific course.
   * This method use existed methods to try to add a student to a specific course.
   *
   * @param deptCode      The code of the department containing the course.
   * @param courseCode    The code of the course want to add student.
   *
   * @return              A {@code ResponseEntity} with a success message if the course is found,
   *                        or an error message if the course is not found.
   */
  @PatchMapping(value = "/enrollStudentInCourse", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> enrollStudentInCourse(@RequestParam(value = "deptCode") String deptCode,
                                                 @RequestParam(value = "courseCode")
                                                 String courseCode) {

    try {
      // Use existing retrieveCourse method to get the course
      ResponseEntity<?> courseResponse = retrieveCourse(deptCode, Integer.parseInt(courseCode));
      // Check if course exist in the department
      if (courseResponse.getStatusCode() != HttpStatus.OK) {
        return courseResponse;
      }
      Course course = (Course) courseResponse.getBody();

      // Use existing isCourseFull method to check if course is full
      if (course.isCourseFull()) {
        return new ResponseEntity<>("Course is full", HttpStatus.FORBIDDEN);
      }

      // Everything good, we enroll the student
      course.setEnrolledStudentCount(course.getEnrolledStudentCount() + 1);
      return new ResponseEntity<>("Student enrolled successfully", HttpStatus.OK);

    } catch (Exception e) {
      return handleException(e);
    }
  }


}