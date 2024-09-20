package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


/**
 * Unit tests for the {@link IndividualProjectApplication} class.
 */
public class IndividualProjectApplicationTest {

  private IndividualProjectApplication app;
  private MyFileDatabase mockDatabase;

  /**
   * Set up mock database.
   */
  @BeforeEach
  public void setUp() {
    // Initialize the IndividualProjectApplication object and mock the database
    app = new IndividualProjectApplication();
    mockDatabase = mock(MyFileDatabase.class);
    IndividualProjectApplication.overrideDatabase(mockDatabase);
  }

  @Test
  public void testRunWithSetupArgument() {
    String[] args = {"setup"};
    try (var mockDatabaseConstructor = Mockito.mockConstruction(MyFileDatabase.class,
            (mock, context) -> {
        when(mock.getDepartmentMapping()).thenReturn(new HashMap<>());
        doNothing().when(mock).setMapping(any());
      })) {
      app.run(args);
      var instances = mockDatabaseConstructor.constructed();
      assertFalse(instances.isEmpty(), "MyFileDatabase should have been instantiated.");
      verify(instances.get(0), times(1)).setMapping(any());
    }
  }

  @Test
  public void testRunWithoutSetupArgument() {
    String[] args = {};
    try (var mockDatabaseConstructor = Mockito.mockConstruction(MyFileDatabase.class,
            (mock, context) -> {
        when(mock.getDepartmentMapping()).thenReturn(new HashMap<>());
      })) {
      app.run(args);
      var instances = mockDatabaseConstructor.constructed();
      assertFalse(instances.isEmpty(), "MyFileDatabase should have been instantiated.");
    }
  }

  @Test
  public void testOverrideDatabase() {
    IndividualProjectApplication.overrideDatabase(mockDatabase);
    assertSame(mockDatabase, IndividualProjectApplication.myFileDatabase,
            "The myFileDatabase should be overridden by the mockDatabase.");
  }
}