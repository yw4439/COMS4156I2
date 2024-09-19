package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Department} class.
 */
public class DepartmentUnitTests {

    private Department department;
    private Course course1;
    private Course course2;
    private HashMap<String, Course> courses;

    String[] times = {"11:40-12:55", "4:10-5:25", "10:10-11:25", "2:40-3:55"};
    String[] locations = {"417 IAB", "309 HAV", "301 URIS"};

    @BeforeEach
    public void setup() {
        // Setup sample courses and department data
        course1 = new Course("Adam Cannon", "417 IAB", "11:40-12:55", 250);
        course2 = new Course("Brian Borowski", "301 URIS", "4:10-5:25", 250);

        courses = new HashMap<>();
        courses.put("1004", course1);
        courses.put("3134", course2);

        department = new Department("COMS", courses, "Luca Carloni", 2700);
    }

    @Test
    public void testGetNumberOfMajors() {
        assertEquals(2700, department.getNumberOfMajors(), "The number of majors is 2700.");
    }

    @Test
    public void testGetDepartmentChair() {
        assertEquals("Luca Carloni", department.getDepartmentChair(), "The department chair is 'Luca Carloni'.");
    }

    @Test
    public void testGetCourseSelection() {
        assertEquals(courses, department.getCourseSelection(), "The courses should match.");
    }

    @Test
    public void testAddPersonToMajor() {
        department.addPersonToMajor();
        assertEquals(2701, department.getNumberOfMajors(), "The number of majors += 1.");
    }

    @Test
    public void testDropPersonFromMajor() {
        department.dropPersonFromMajor();
        assertEquals(2699, department.getNumberOfMajors(), "The number of majors -= 1.");

        // Ensure majors do not go below zero
        Department tempDept = new Department("CHEM", new HashMap<>(), "Laura J. Kaufman", 0);
        tempDept.dropPersonFromMajor();
        assertEquals(0, tempDept.getNumberOfMajors(), "The number of majors can't go below zero.");
    }

    @Test
    public void testAddCourse() {
        Course newCourse = new Course("Jae Lee", locations[0], times[1], 400);
        department.addCourse("3157", newCourse);

        assertEquals(newCourse, department.getCourseSelection().get("3157"), "The new course adds to the department.");
    }

    @Test
    public void testCreateCourse() {
        department.createCourse("3203", "Ansaf Salleb-Aouissi", locations[2], times[2], 250);

        Course createdCourse = department.getCourseSelection().get("3203");
        assertEquals("Ansaf Salleb-Aouissi", createdCourse.getInstructorName(), "Instructor name match.");
        assertEquals(locations[2], createdCourse.getCourseLocation(), "Course location match.");
        assertEquals(times[2], createdCourse.getCourseTimeSlot(), "Course time slot match.");
        assertEquals(250, createdCourse.enrollmentCapacity, "Course capacity match.");
    }

    @Test
    public void testToString() {
        assertEquals("Department Code: COMS, Chair: Luca Carloni, Number of Majors: 2700\nCourses:\n" +
                "COMS 1004: \nInstructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55\n" +
                "COMS 3134: \nInstructor: Brian Borowski; Location: 301 URIS; Time: 4:10-5:25\n", department.toString());
    }
}