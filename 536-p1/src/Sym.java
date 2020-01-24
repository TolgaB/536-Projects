///////////////////////////////////////////////////////////////////////////////
//
// Title:            P1
// Files:            P1.java, Sym.Java, SymTable.java
// DuplicateSymException.java, EmptySymTableException.java
// Semester:         536 Spring 2020
//
// Author:           Tolga Beser
// Email:            tbeser@wisc.edu
// CS Login:         tolga
// Lecturer's Name:  Loris
//
/////////////////////////////////////////////////////////////////////////////  

/**
 * @author Tolga
 *
 */
public class Sym {
  private String type;
  
  Sym(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }
  
  public String toString() {
    return type;
  }
  
}
