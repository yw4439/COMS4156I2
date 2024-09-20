package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test class for RouteController.
 */
public class RouteControllerTest {

  private RouteController routeController;
  private MyFileDatabase mockDatabase;
  private HashMap<String, Department> mockDepartmentMapping;


  /**
   * Setup for test database and route controller.
   */
  @BeforeEach
  public void setUp() {
    // Set up the mock database and controller
    routeController = new RouteController();
    mockDatabase = mock(MyFileDatabase.class);
    IndividualProjectApplication.overrideDatabase(mockDatabase);

    // Set up the mock department mapping
    mockDepartmentMapping = new HashMap<>();
    when(mockDatabase.getDepartmentMapping()).thenReturn(mockDepartmentMapping);
  }


  @Test
  public void testRetrieveDepartment_Valid() {
    // Create a mock department and add it to the mock mapping
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test retrieve a department
    ResponseEntity<?> response = routeController.retrieveDepartment("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("COMS"),
            "Response should contain department details.");
  }

  @Test
  public void testRetrieveDepartment_Invalid() {
    // Test retrieve a non-existent department
    ResponseEntity<?> response = routeController.retrieveDepartment("INVALID");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
  }

  @Test
  public void testRetrieveCourse_Valid() {
    // Create a mock course and department
    Course mockCourse = mock(Course.class);
    when(mockCourse.dropStudent()).thenReturn(true);
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test retrieve a course
    ResponseEntity<?> response = routeController.retrieveCourse("COMS", 101);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
  }

  @Test
  public void testRetrieveCourse_Invalid() {
    // Test retrieve a course
    ResponseEntity<?> response = routeController.retrieveCourse("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
  }

  @Test
  public void testIsCourseFull() {
    // Create a mock course and set it as full
    Course mockCourse = mock(Course.class);
    when(mockCourse.isCourseFull()).thenReturn(true); // Simulate a full course
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test isCourseFull
    ResponseEntity<?> response = routeController.isCourseFull("COMS", 101);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("true"),
            "Response should indicate the course is full.");
  }

  @Test
  public void testIsCourseFull_Not() {
    // Create a mock course and set it as not full
    Course mockCourse = mock(Course.class);
    when(mockCourse.isCourseFull()).thenReturn(false);
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test isCourseFull
    ResponseEntity<?> response = routeController.isCourseFull("COMS", 101);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("false"),
            "Response should indicate the course is not full.");
  }

  @Test
  public void testIsCourseFull_InvalidCourse() {
    // Create a mock department without the course
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test isCourseFull
    ResponseEntity<?> response = routeController.isCourseFull("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
            "Status should be NOT FOUND.");
  }

  @Test
  public void testAddMajorToDept_ValidDepartment() {
    // Create a mock department
    Department mockDept = mock(Department.class);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test adding a major
    ResponseEntity<?> response = routeController.addMajorToDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    verify(mockDept, times(1)).addPersonToMajor();
  }

  @Test
  public void testAddMajorToDept_InvalidDepartment() {
    // Test adding a major to non-existent department
    ResponseEntity<?> response = routeController.addMajorToDept("INVALID");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
  }

  @Test
  public void testDropStudentFromCourse_ValidCourse() {
    // Create a mock course and department
    Course mockCourse = mock(Course.class);
    when(mockCourse.dropStudent()).thenReturn(true);
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test dropping a student
    ResponseEntity<?> response = routeController.dropStudent("COMS", 101);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    verify(mockCourse, times(1)).dropStudent();
  }

  @Test
  public void testDropStudentFromCourse_InvalidCourse() {
    // Test dropping a student from a non-existent course
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    ResponseEntity<?> response = routeController.dropStudent("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
  }

  @Test
  public void testAddMajorToDept_NullDepartment() {
    // Test adding a major with null department
    ResponseEntity<?> response = routeController.addMajorToDept(null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
  }

  @Test
  public void testDropStudentFromCourse_EmptyCourseMap() {
    // Test dropping a student when course map is empty
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    ResponseEntity<?> response = routeController.dropStudent("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
  }

  @Test
  public void testGetMajorCtFromDept_ValidDepartment() {
    // Create a mock department and add it to the mapping
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test get major from department
    ResponseEntity<?> response = routeController.getMajorCtFromDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("3000"),
            "Response should contain the correct major count.");
  }

  @Test
  public void testGetMajorCtFromDept_InvalidDepartment() {
    // Test get major from non_exist department
    ResponseEntity<?> response = routeController.getMajorCtFromDept("INVALID");
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Status should be FORBIDDEN.");
    assertTrue(response.getBody().toString().contains("Department Not Found"),
            "Response should indicate department not found.");
  }

  @Test
  public void testGetMajorCtFromDept_NullDepartmentCode() {
    // Test Test get major from Null department
    ResponseEntity<?> response = routeController.getMajorCtFromDept(null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }

  @Test
  public void testGetMajorCtFromDept_EmptyDepartmentCode() {
    // Test get major from empty deptcode
    ResponseEntity<?> response = routeController.getMajorCtFromDept("");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }

  @Test
  public void testIdentifyDeptChair_ValidDepartment() {
    // Create a mock department and add it to the mapping
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test get department chair from department
    ResponseEntity<?> response = routeController.identifyDeptChair("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("Adam Cannon"),
            "Response should contain the correct chair name.");
  }

  @Test
  public void testIdentifyDeptChair_InvalidDepartment() {
    // Test get department chair from non_exist department
    ResponseEntity<?> response = routeController.identifyDeptChair("INVALID");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
    assertTrue(response.getBody().toString().contains("Department Not Found"),
            "Response should indicate department not found.");
  }

  @Test
  public void testIdentifyDeptChair_NullDepartmentCode() {
    // Test get department chair from Null department
    ResponseEntity<?> response = routeController.identifyDeptChair(null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }

  @Test
  public void testIdentifyDeptChair_EmptyDepartmentCode() {
    // Test get department chair from empty deptcode
    ResponseEntity<?> response = routeController.identifyDeptChair("");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
            "Status should be BAD REQUEST.");
  }

  @Test
  public void testFindCourseLocation_ValidCourse() {
    // Create a mock course with a location
    Course mockCourse = mock(Course.class);
    when(mockCourse.getCourseLocation()).thenReturn("417 IAB");

    // Create a department and add the course
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test finding the course location for a valid course
    ResponseEntity<?> response = routeController.findCourseLocation("COMS", 101);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("417 IAB is where the course is located."),
            "Response should contain the correct course location.");
  }

  @Test
  public void testFindCourseInstructor_ValidCourse() {
    // Create a mock course with an instructor
    Course mockCourse = mock(Course.class);
    when(mockCourse.getInstructorName()).thenReturn("Adam Cannon");

    // Create a department and add the course
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test finding the course instructor for a valid course
    ResponseEntity<?> response = routeController.findCourseInstructor("COMS", 101);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("Adam Cannon"
                    + " is the instructor for the course."),
            "Response should contain the correct instructor name.");
  }

  @Test
  public void testFindCourseInstructor_InvalidCourse() {
    // Create a mock department without the course
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test finding the course instructor for a non-existent course
    ResponseEntity<?> response = routeController.findCourseInstructor("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
    assertTrue(response.getBody().toString().contains("Course Not Found"),
            "Response should indicate that the course was not found.");
  }

  @Test
  public void testFindCourseInstructor_NullDepartmentCode() {
    // Test finding the course instructor when Null department
    ResponseEntity<?> response = routeController.findCourseInstructor(null, 101);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }

  @Test
  public void testFindCourseInstructor_EmptyDepartmentCode() {
    // Test finding the course instructor when empty deptcode
    ResponseEntity<?> response = routeController.findCourseInstructor("", 101);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }

  @Test
  public void testFindCourseLocation_InvalidCourse() {
    // Create a mock department without the course
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test finding the course location for a non-existent course
    ResponseEntity<?> response = routeController.findCourseLocation("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
    assertTrue(response.getBody().toString().contains("Course Not Found"),
            "Response should indicate that the course was not found.");
  }

  @Test
  public void testFindCourseLocation_NullDepartmentCode() {
    // Test finding the course location when Null department
    ResponseEntity<?> response = routeController.findCourseLocation(null, 101);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }

  @Test
  public void testFindCourseLocation_EmptyDepartmentCode() {
    // Test finding the course location when empty deptcode
    ResponseEntity<?> response = routeController.findCourseLocation("", 101);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    assertTrue(response.getBody().toString().contains("Department code cannot be null or empty"),
            "Response should indicate null or empty department code.");
  }


  @Test
  public void testChangeCourseLocation_ValidCourse() {
    // Create a mock course and department
    Course mockCourse = mock(Course.class);
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("101", mockCourse);
    Department mockDept = new Department("COMS", courseMap,
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test changing the course location
    ResponseEntity<?> response = routeController.changeCourseLocation("COMS",
            101, "New Location");
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    verify(mockCourse, times(1)).reassignLocation("New Location");
  }

  @Test
  public void testHandleException() {
    // Simulate an exception
    ResponseEntity<?> response = routeController.handleException(new Exception("Test Exception"));
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("An Error has occurred"),
            "Response should contain the error message.");
  }

  @Test
  public void testRetrieveDepartment_EmptyDepartmentCode() {
    // Test retrieval with an empty department code
    ResponseEntity<?> response = routeController.retrieveDepartment("");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
            "Status should be BAD REQUEST.");
  }

  @Test
  public void testChangeCourseLocation_NullDepartmentCode() {
    // Test for null department code
    ResponseEntity<?> response = routeController.changeCourseLocation(null,
            101, "New Location");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
            "Status should be BAD REQUEST.");
  }

  @Test
  public void testChangeCourseLocation_InvalidCourse() {
    // Create a mock department without the course
    Department mockDept = new Department("COMS", new HashMap<>(),
            "Adam Cannon", 3000);
    mockDepartmentMapping.put("COMS", mockDept);

    // Test changing location for a non-existent course
    ResponseEntity<?> response = routeController.changeCourseLocation("COMS",
            999, "New Location");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
  }

  @Test
  public void testChangeCourseLocation_ExceptionHandling() {
    // Simulate an exception during location change
    when(routeController.changeCourseLocation("COMS", 101, "New Location"))
            .thenThrow(new RuntimeException("Simulated Exception"));

    ResponseEntity<?> response =
            routeController.handleException(new RuntimeException("Simulated Exception"));
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
    assertTrue(response.getBody().toString().contains("An Error has occurred"),
            "Response should contain error message.");
  }
}