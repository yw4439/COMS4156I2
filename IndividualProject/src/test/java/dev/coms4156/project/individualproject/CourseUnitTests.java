package dev.coms4156.project.individualproject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Course} class.
 */
public class CourseUnitTests {

  /** The test course instance used for testing. */
  private static Course testCourse;

  @BeforeAll
  public static void setupCourseForTesting() {
    testCourse = new Course("Griffin Newbold",
            "417 IAB", "11:40-12:55", 250);
  }

  @Test
  public void enrollStudentTrueTest() {
    testCourse.setEnrolledStudentCount(0);
    assertTrue(testCourse.enrollStudent(), "Enrollment should succeed when course have space.");
  }

  @Test
  public void enrollStudentFalseTest() {
    testCourse.setEnrolledStudentCount(250);
    assertFalse(testCourse.enrollStudent(), "Enrollment should fail when course is full.");
  }

  @Test
  public void dropStudentTrueTest() {
    testCourse.setEnrolledStudentCount(250);
    assertTrue(testCourse.dropStudent(), "Dropping should succeed when students are enrolled.");
  }

  @Test
  public void dropStudentFalseTest() {
    testCourse.setEnrolledStudentCount(0);
    assertFalse(testCourse.dropStudent(), "Dropping should fail when no students are enrolled.");
  }

  @Test
  public void getCourseLocationTest() {
    assertEquals("417 IAB", testCourse.getCourseLocation(), "Course location should match.");
  }

  @Test
  public void getInstructorNameTest() {
    assertEquals("Griffin Newbold", testCourse.getInstructorName(),
            "Instructor name should match.");
  }

  @Test
  public void getCourseTimeSlotTest() {
    assertEquals("11:40-12:55", testCourse.getCourseTimeSlot(), "Course time slot should match.");
  }

  @Test
  public void toStringTest() {
    String expectedResult = "\nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString());
  }

  @Test
  public void reassignInstructorTest() {
    testCourse.reassignInstructor("Daniel Lee");
    assertEquals("Daniel Lee", testCourse.getInstructorName(),
            "Instructor name should be reassigned correctly.");
    assertNotEquals("Griffin Newbold", testCourse.getInstructorName(),
            "Instructor name should be reset to new one.");
    testCourse.reassignInstructor("Griffin Newbold");
  }

  @Test
  public void reassignLocationTest() {
    testCourse.reassignLocation("223 IAB");
    assertEquals("223 IAB", testCourse.getCourseLocation(),
            "Course location should be reassigned correctly.");
    assertNotEquals("417 IAB", testCourse.getCourseLocation(),
            "Course location should be reset to new one.");
    testCourse.reassignLocation("417 IAB");
  }

  @Test
  public void reassignTimeTest() {
    testCourse.reassignTime("16:10-17:25");
    assertEquals("16:10-17:25", testCourse.getCourseTimeSlot(),
            "Course time slot should be reassigned correctly.");
    assertNotEquals("11:40-12:55", testCourse.getCourseTimeSlot(),
            "Course time slot should be reset to new one.");
    testCourse.reassignTime("11:40-12:55");
  }

  @Test
  public void setEnrolledStudentCountTest() {
    testCourse.setEnrolledStudentCount(249);
    assertFalse(testCourse.isCourseFull(), "Course should not be considered full.");
    testCourse.enrollStudent();
    assertTrue(testCourse.isCourseFull(), "Course should be full.");
  }

  @Test
  public void isCourseFullTest() {
    testCourse.setEnrolledStudentCount(250);
    assertTrue(testCourse.isCourseFull(), "Course should be considered full.");
    testCourse.setEnrolledStudentCount(249);
    assertFalse(testCourse.isCourseFull(), "Course should not be considered full.");
  }

  @Test
  public void enrollStudentBoundaryTest() {
    testCourse.setEnrolledStudentCount(249);
    assertTrue(testCourse.enrollStudent(), "Should enroll student when capacity allows.");
    assertFalse(testCourse.enrollStudent(), "Should not enroll when course is full.");
  }

  @Test
  public void dropStudentBoundaryTest() {
    testCourse.setEnrolledStudentCount(1);
    assertTrue(testCourse.dropStudent(), "Should drop one enrolled student.");
    assertFalse(testCourse.dropStudent(), "Should not drop a student when none are enrolled.");
  }

}
