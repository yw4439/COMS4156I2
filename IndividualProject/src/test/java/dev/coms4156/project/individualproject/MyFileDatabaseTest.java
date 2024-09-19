package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for MyFileDatabase.
 */
public class MyFileDatabaseTest {

    private MyFileDatabase fileDatabase;
    private String testFilePath = "./test-data.txt";

    @BeforeEach
    public void setUp() {
        // Initialize with a clean slate
        fileDatabase = new MyFileDatabase(1, testFilePath);
    }

    @Test
    public void testSetAndGetMapping() {
        // Create a mock department and add it to the map
        Course testCourse = new Course("Test Instructor", "Test Location", "10:00-11:00", 100);
        HashMap<String, Course> courses = new HashMap<>();
        courses.put("101", testCourse);

        Department testDept = new Department("TEST", courses, "Test Chair", 50);
        HashMap<String, Department> deptMap = new HashMap<>();
        deptMap.put("TEST", testDept);

        // Set the mapping
        fileDatabase.setMapping(deptMap);

        // Verify the mapping
        assertEquals(deptMap, fileDatabase.getDepartmentMapping(), "The department mapping should match the input mapping.");
    }

    @Test
    public void testSaveContentsToFile() {
        // Create a mock department and add it to the map
        Course testCourse = new Course("Test Instructor", "Test Location", "10:00-11:00", 100);
        HashMap<String, Course> courses = new HashMap<>();
        courses.put("101", testCourse);

        Department testDept = new Department("TEST", courses, "Test Chair", 50);
        HashMap<String, Department> deptMap = new HashMap<>();
        deptMap.put("TEST", testDept);

        // Set the mapping and save to file
        fileDatabase.setMapping(deptMap);
        fileDatabase.saveContentsToFile();

        // Create a new database object and load the file
        MyFileDatabase loadedDatabase = new MyFileDatabase(0, testFilePath);
        HashMap<String, Department> loadedMap = loadedDatabase.getDepartmentMapping();

        // Verify the loaded map matches the saved map
        assertNotNull(loadedMap, "Loaded map should not be null.");
        assertEquals(deptMap.size(), loadedMap.size(), "Loaded map size should match the original map.");
        assertEquals(testDept.getDepartmentChair(), loadedMap.get("TEST").getDepartmentChair(), "Department chair should match.");
        assertEquals(testCourse.getInstructorName(), loadedMap.get("TEST").getCourseSelection().get("101").getInstructorName(), "Instructor name should match.");
    }

    @Test
    public void testDeSerializeObjectFromFile_FileNotFound() {
        // Initialize the database with a non-existing file
        MyFileDatabase nonExistingDatabase = new MyFileDatabase(0, "./non-existing-file.txt");

        // Ensure that the deserialized object is null due to file not found
        assertNull(nonExistingDatabase.getDepartmentMapping(), "Mapping should be null when file not found.");
    }

    @Test
    public void testToString() {
        // Create a mock course and add it to the course map
        Course testCourse = new Course("Test Instructor", "Test Location", "10:00-11:00", 100);
        HashMap<String, Course> courses = new HashMap<>();
        courses.put("101", testCourse);

        // Create a mock department with the course map and add it to the department map
        Department testDept = new Department("TEST", courses, "Test Chair", 50);
        HashMap<String, Department> deptMap = new HashMap<>();
        deptMap.put("TEST", testDept);

        // Set the mapping in the file database
        fileDatabase.setMapping(deptMap);

        // Corrected expected string with department details (removed extra indentation before "TEST 101:")
        String expected = "For the TEST department: \n" +
                "Department Code: TEST, Chair: Test Chair, Number of Majors: 50\n" +
                "Courses:\n" +
                "TEST 101: \nInstructor: Test Instructor; Location: Test Location; Time: 10:00-11:00\n";

        // Verify the string representation
        assertEquals(expected, fileDatabase.toString(), "The string representation should match.");
    }

    @Test
    public void testInvalidObjectInFile() throws IOException {
        // Write an invalid object to the file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(testFilePath))) {
            out.writeObject(new String("Invalid Object"));
        }

        // Try to load it and catch the exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new MyFileDatabase(0, testFilePath);
        });

        String expectedMessage = "Invalid object type in file.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Exception message should contain 'Invalid object type in file.'");
    }
}