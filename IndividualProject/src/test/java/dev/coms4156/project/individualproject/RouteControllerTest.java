package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test class for RouteController.
 */
public class RouteControllerTest {

    private RouteController routeController;
    private MyFileDatabase mockDatabase;
    private HashMap<String, Department> mockDepartmentMapping;

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
    public void testRetrieveDepartment_ValidDepartment() {
        // Create a mock department and add it to the mock mapping
        Department mockDept = new Department("COMS", new HashMap<>(), "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        // Test the retrieval
        ResponseEntity<?> response = routeController.retrieveDepartment("COMS");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        assertTrue(response.getBody().toString().contains("COMS"), "Response should contain department details.");
    }

    @Test
    public void testRetrieveDepartment_InvalidDepartment() {
        // Test retrieval of a non-existent department
        ResponseEntity<?> response = routeController.retrieveDepartment("INVALID");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
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
        Department mockDept = new Department("COMS", courseMap, "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        // Test dropping a student
        ResponseEntity<?> response = routeController.dropStudent("COMS", 101);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        verify(mockCourse, times(1)).dropStudent();
    }

    @Test
    public void testDropStudentFromCourse_InvalidCourse() {
        // Test dropping a student from a non-existent course
        Department mockDept = new Department("COMS", new HashMap<>(), "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        ResponseEntity<?> response = routeController.dropStudent("COMS", 999);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
    }

    // Additional edge case tests
    @Test
    public void testAddMajorToDept_NullDepartment() {
        // Test adding a major with null department
        ResponseEntity<?> response = routeController.addMajorToDept(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    }

    @Test
    public void testDropStudentFromCourse_EmptyCourseMap() {
        // Test dropping a student when course map is empty
        Department mockDept = new Department("COMS", new HashMap<>(), "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        ResponseEntity<?> response = routeController.dropStudent("COMS", 101);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
    }

    @Test
    public void testGetMajorCtFromDept_ValidDepartment() {
        // Create a mock department and add it to the mapping
        Department mockDept = new Department("COMS", new HashMap<>(), "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        // Test the retrieval of major count
        ResponseEntity<?> response = routeController.getMajorCtFromDept("COMS");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        assertTrue(response.getBody().toString().contains("3000"), "Response should contain the correct major count.");
    }

    @Test
    public void testGetMajorCtFromDept_InvalidDepartment() {
        // Test for a non-existent department
        ResponseEntity<?> response = routeController.getMajorCtFromDept("INVALID");
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Status should be FORBIDDEN.");
    }

    @Test
    public void testIdentifyDeptChair_ValidDepartment() {
        // Create a mock department and add it to the mapping
        Department mockDept = new Department("COMS", new HashMap<>(), "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        // Test the retrieval of department chair
        ResponseEntity<?> response = routeController.identifyDeptChair("COMS");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        assertTrue(response.getBody().toString().contains("Chair Name"), "Response should contain the correct chair name.");
    }

    @Test
    public void testChangeCourseLocation_ValidCourse() {
        // Create a mock course and department
        Course mockCourse = mock(Course.class);
        HashMap<String, Course> courseMap = new HashMap<>();
        courseMap.put("101", mockCourse);
        Department mockDept = new Department("COMS", courseMap, "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        // Test changing the course location
        ResponseEntity<?> response = routeController.changeCourseLocation("COMS", 101, "New Location");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        verify(mockCourse, times(1)).reassignLocation("New Location");
    }

    @Test
    public void testHandleException() {
        // Simulate an exception
        ResponseEntity<?> response = routeController.handleException(new Exception("Test Exception"));
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        assertTrue(response.getBody().toString().contains("An Error has occurred"), "Response should contain the error message.");
    }

    @Test
    public void testRetrieveDepartment_EmptyDepartmentCode() {
        // Test retrieval with an empty department code
        ResponseEntity<?> response = routeController.retrieveDepartment("");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    }

    @Test
    public void testGetMajorCtFromDept_NullDepartmentCode() {
        // Test for a null department code
        ResponseEntity<?> response = routeController.getMajorCtFromDept(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    }

    @Test
    public void testIdentifyDeptChair_EmptyDepartmentCode() {
        // Test for empty department code
        ResponseEntity<?> response = routeController.identifyDeptChair("");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    }

    @Test
    public void testChangeCourseLocation_NullDepartmentCode() {
        // Test for null department code
        ResponseEntity<?> response = routeController.changeCourseLocation(null, 101, "New Location");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD REQUEST.");
    }

    @Test
    public void testChangeCourseLocation_InvalidCourse() {
        // Create a mock department without the course
        Department mockDept = new Department("COMS", new HashMap<>(), "Chair Name", 3000);
        mockDepartmentMapping.put("COMS", mockDept);

        // Test changing location for a non-existent course
        ResponseEntity<?> response = routeController.changeCourseLocation("COMS", 999, "New Location");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT FOUND.");
    }

    @Test
    public void testChangeCourseLocation_ExceptionHandling() {
        // Simulate an exception during location change
        when(routeController.changeCourseLocation("COMS", 101, "New Location"))
                .thenThrow(new RuntimeException("Simulated Exception"));

        ResponseEntity<?> response = routeController.handleException(new RuntimeException("Simulated Exception"));
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK.");
        assertTrue(response.getBody().toString().contains("An Error has occurred"), "Response should contain error message.");
    }

}