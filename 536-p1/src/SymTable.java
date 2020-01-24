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
import java.util.*;

/**
 * @author Tolga
 *
 */
public class SymTable {
  //still not sure if should implement with linkedlist or arraylist
  private LinkedList<HashMap<String,Sym>> list;
  
  SymTable() {
    //not sure if this adds an empty hashmap need to double check
    list = new LinkedList<HashMap<String, Sym>>();
    list.add(new HashMap<String, Sym>());
  }
  
  public void addDecl(String name, Sym sym) throws DuplicateSymException, EmptySymTableException  {
    //empty? because we add 1 empty one at the start so need to double check
    if (list.isEmpty()) {
      throw new EmptySymTableException();
    }
    if (name == null || sym == null) {
      throw new IllegalArgumentException();
    }
    if (list.get(0).containsKey(name)) {
      throw new DuplicateSymException();
    } else {
      HashMap<String,Sym> newHashM = new HashMap<String,Sym>();
      newHashM.put(name, sym);
     // list.set(0, newHashM);
      //TODO: ASK IF THIS SHOULD BE SET OR ADD
      list.add(0, newHashM);
    }
  }
  
  //add a new empty hashmap to the front of the list
  public void addScope() {
    list.add(0, new HashMap<String, Sym>());
  }
  
  //Check if the first hashmap has the given name as a key and return if so, null otherwise
  public Sym lookupLocal(String name) throws EmptySymTableException {
    if (list.isEmpty()) {
      throw new EmptySymTableException();
    } 
    if (list.get(0).containsKey(name)) {
      return list.get(0).get(name);
    }
    return null;
  }
  
  //check if any of the hashmaps contain the given name as a key and return if so, null otherwise
  public Sym lookupGlobal(String name) throws EmptySymTableException {
    if (list.isEmpty()) {
      throw new EmptySymTableException();
    }
    for (HashMap<String, Sym> search : list) {
      if (search.containsKey(name)) {
        return search.get(name);
      }
    }
    return null;
  }
  
  //Remove hashmap from front of list, throw emptysymtableexception if empty
  public void removeScope() throws EmptySymTableException {
    if (list.isEmpty()) {
      throw new EmptySymTableException();
    }
    list.remove();
  }
  
  //Print the given string then use the hashmaps .toString() method to print every hashmap
  public void print() {
    System.out.print("\nSym Table\n");
    for (HashMap<String, Sym> search : list) {
      System.out.print(search.toString() +"\n");
    }
    System.out.print("\n");
  }
}
