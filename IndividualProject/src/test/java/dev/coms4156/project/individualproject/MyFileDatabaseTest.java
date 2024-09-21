package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for MyFileDatabase.
 */
public class MyFileDatabaseTest {

  private MyFileDatabase fileDatabase;
  private String testFilePath = "./test-data.txt";

  /**
   * Setup test database.
   */
  @BeforeEach
  public void setUp() {
    fileDatabase = new MyFileDatabase(1, testFilePath);
  }

  @Test
  public void testSetAndGetMapping() {
    Course testCourse = new Course("Test Instructor",
            "Test Location", "10:00-11:00", 100);
    HashMap<String, Course> courses = new HashMap<>();
    courses.put("101", testCourse);
    Department testDept = new Department("TEST",
            courses, "Test Chair", 50);
    HashMap<String, Department> deptMap = new HashMap<>();
    deptMap.put("TEST", testDept);
    fileDatabase.setMapping(deptMap);
    assertEquals(deptMap, fileDatabase.getDepartmentMapping(),
            "The department mapping should match the input mapping.");
  }

  @Test
  public void testSaveContentsToFile() {
    Course testCourse = new Course("Test Instructor",
            "Test Location", "10:00-11:00", 100);
    HashMap<String, Course> courses = new HashMap<>();
    courses.put("101", testCourse);
    Department testDept = new Department("TEST",
            courses, "Test Chair", 50);
    HashMap<String, Department> deptMap = new HashMap<>();
    deptMap.put("TEST", testDept);
    fileDatabase.setMapping(deptMap);
    fileDatabase.saveContentsToFile();
    MyFileDatabase loadedDatabase = new MyFileDatabase(0, testFilePath);
    HashMap<String, Department> loadedMap = loadedDatabase.getDepartmentMapping();
    assertNotNull(loadedMap, "Loaded map should not be null.");
    assertEquals(deptMap.size(), loadedMap.size(),
            "Loaded map size should match the original map.");
    assertEquals(testDept.getDepartmentChair(), loadedMap.get("TEST").getDepartmentChair(),
            "Department chair should match.");
    assertEquals(testCourse.getInstructorName(),
            loadedMap.get("TEST").getCourseSelection().get("101").getInstructorName(),
            "Instructor name should match.");
  }

  @Test
  public void testToString() {
    Course testCourse = new Course("Test Instructor",
            "Test Location", "10:00-11:00", 100);
    HashMap<String, Course> courses = new HashMap<>();
    courses.put("101", testCourse);
    Department testDept = new Department("TEST", courses,
            "Test Chair", 50);
    HashMap<String, Department> deptMap = new HashMap<>();
    deptMap.put("TEST", testDept);
    fileDatabase.setMapping(deptMap);
    String expected = "For the TEST department: \n"
            + "Department Code: TEST, Chair: Test Chair, Number of Majors: 50\n"
            + "Courses:\n"
            + "TEST 101: \nInstructor: Test Instructor;"
            + " Location: Test Location; Time: 10:00-11:00\n";

    assertEquals(expected, fileDatabase.toString(),
            "The string representation should match.");
  }
}