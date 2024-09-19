package dev.coms4156.project.individualproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * This class represents a file-based database containing department mappings.
 */
public class MyFileDatabase {

  private int mode;
  private boolean initialized = false;
  private boolean saved = false;
  /**
   * Constructs a MyFileDatabase object and loads up the data structure with
   * the contents of the file.
   *
   * @param flag     used to distinguish mode of database
   * @param filePath the path to the file containing the entries of the database
   */
  public MyFileDatabase(int flag, String filePath) {
    this.filePath = filePath;
    if (flag == 0) {
      this.departmentMapping = deSerializeObjectFromFile();
    }
    initialized = true;
  }

  /**
   * Sets the department mapping of the database.
   *
   * @param mapping the mapping of department names to Department objects
   */
  public void setMapping(HashMap<String, Department> mapping) {
    this.departmentMapping = mapping;
  }

  /**
   * Deserializes the object from the file and returns the department mapping.
   *
   * @return the deserialized department mapping
   */
  public HashMap<String, Department> deSerializeObjectFromFile() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
      Object obj = in.readObject();
      if (obj instanceof HashMap) {
        return (HashMap<String, Department>) obj;
      } else {
        throw new IllegalArgumentException("Invalid object type in file.");
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Saves the contents of the internal data structure to the file. Contents of the file are
   * overwritten with this operation.
   */
  public void saveContentsToFile() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
      out.writeObject(departmentMapping);
      saved = true;
      System.out.println("Object serialized successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the department mapping of the database.
   *
   * @return the department mapping
   */
  public HashMap<String, Department> getDepartmentMapping() {
    return this.departmentMapping;
  }

  /**
   * Retrieves a department by its code.
   *
   * @param deptCode the department code to retrieve
   * @return the department with the given code, or null if not found
   */
  public Department getDepartment(String deptCode) {
    return this.departmentMapping.get(deptCode);
  }

  /**
   * Returns whether the database is initialized.
   *
   * @return true if initialized, false otherwise
   */
  public boolean isInitialized() {
    return initialized;
  }

  /**
   * Returns whether the database has been saved.
   *
   * @return true if saved, false otherwise
   */
  public boolean isSaved() {
    return saved;
  }

  /**
   * Returns the mode of the database (setup or normal).
   *
   * @return the mode of the database
   */
  public int getMode() {
    return mode;
  }

  /**
   * Returns a string representation of the database.
   *
   * @return a string representation of the database
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Department> entry : departmentMapping.entrySet()) {
      String key = entry.getKey();
      Department value = entry.getValue();
      result.append("For the ").append(key).append(" department: \n").append(value.toString());
    }
    return result.toString();
  }

  /** The path to the file containing the database entries. */
  private String filePath;

  /** The mapping of department names to Department objects. */
  private HashMap<String, Department> departmentMapping;
}
