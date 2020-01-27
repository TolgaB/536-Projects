import java.util.HashMap;

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

/*
 * Class for testing the implementation of Sym and SymTable
 */
public class P1 {
  
  // method to check the functionality of Sym and SymTable 
  // including their exception handling
  public static void main(String[] args) {
    boolean failedTests = false;
    
    /* 
     * Sym class tests
     */
    Sym testSym = new Sym("int");
    //test to see if getType returns the correct val
    if (!testSym.getType().equals("int")) {
      failedTests = true;
      System.out.println("The Sym class failed to return the correct given typ"
          + "e when the getType method was called");
    }
    //test to see if toString returns the correct val
    if (!testSym.toString().equals("int")) {
      failedTests = true;
      System.out.println("The Sym class failed to return the correct given type"
          + " when the toString method was called");
    }
    
    /*
     * SymTable class tests
     */  
    //test the addDecl method and the exceptions it throws
    SymTable testTable = new SymTable();
    try {
      //any thrown exceptions mean incorrect implementation
      testTable.addDecl("name", testSym);
    } catch (DuplicateSymException ex) {
      failedTests = true;
      System.out.println("DuplicateSymException was incorrectly thrown during a"
          + "ddDecl");
    } catch (EmptySymTableException ex) {
      failedTests = true;
      System.out.println("EmptySymTableException was incorrectly thrown during "
          + "addDecl");
    } catch (IllegalArgumentException ex) {
      failedTests = true;
      System.out.println("IllegalArgumentException was incorrectly thrown durin"
          + "g addDecl");
    }
    //check if exceptions are thrown when given invalid input
    testTable = new SymTable();
    try {
      //check the EmptySymTableException
      testTable.removeScope();
      try {
        testTable.addDecl("name", testSym);
        System.out.println("addDecl failed to throw an EmptySymTableException"
            + " when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
      //check the IllegalArgumentException
      testTable = new SymTable();
      try {
        testTable.addDecl(null, null);
        System.out.println("addDecl failed to throw an IllegalArgumentExcept"
            + "ion when one occured");
        failedTests = true;
      } catch (IllegalArgumentException ex) {
      }
      //check the DuplicateSymException
      testTable = new SymTable();
      try {
        testTable.addDecl("name", testSym);
        testTable.addDecl("name", testSym);
        System.out.println("addDecl failed to throw a DuplicateSymException w"
            + "hen one occured");
        failedTests = true;
      } catch (DuplicateSymException ex) {
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("When testing addDecl exceptions removeScope falsly "
          + "threw an exception, more tests for removescope below");
    }

    //Tests for addScope method
    testTable = new SymTable();
    try {
      testTable.removeScope();
      testTable.addScope();
      testTable.removeScope();
      try {
        //calls removescope when empty to trigger exception
        testTable.removeScope();
        System.out.println("addScope failed to throw an EmptySymTableE"
            + "xception when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
    } catch (EmptySymTableException ex) {
      failedTests = true;
      System.out.println("When testing addScope functionality removeS"
          + "cope incorrectly threw an exception");
    }
    
    //Tests for the lookupLocal method (correct exception throwing tests below)
    testTable = new SymTable();
    try {
      testTable.addDecl("name", testSym);
      try {
        //call lookuplocal on the added value
        Sym retVal = testTable.lookupLocal("name");
        if (retVal == null) {
          failedTests = false;
          System.out.println("lookupLocal returned null instead of"
              + " the correct symbol");
        }
        if (retVal != testSym) {
          failedTests = true;
          System.out.println("lookupLocal returned a value that was no"
              + "t the correct Sym");
        }
      } catch (EmptySymTableException ex) {
        failedTests = true;
        System.out.println("lookupLocal incorrectly threw an EmptySymTa"
            + "bleException");
      }
    } catch (EmptySymTableException|DuplicateSymException ex) {
      failedTests = true;
      System.out.println("When testing lookupLocal functionality addDecl in"
          + "correctly threw an exception");
    }
    //Tests to see if lookupLocal correctly throws exceptions
    testTable = new SymTable();
    try {
      testTable.removeScope();
      try {
        testTable.lookupLocal("name");
        System.out.println("lookupLocal failed to throw an EmptySymTableExcep"
            + "tion when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("removeScope incorrectly threw an exception while t"
          + "esting functionality of lookupLocal");
    }
       
    //Tests for the lookupGlobal method (correct exception throwing tests below)
    testTable = new SymTable();
    try {
      testTable.addScope();
      try {
        //add multiple sym with the same name key and put an empty hashmap as buffer
        //test to see if the program returns the first and not any 
        //given sym with the same name key
        testTable.addDecl("name", testSym);
        testTable.addScope();
        Sym otherSym = new Sym("string");
        testTable.addDecl("name", otherSym);
        try {
          Sym retVal = testTable.lookupGlobal("name");
          if (retVal == null) {
            failedTests = true;
            System.out.println("lookupGlobal returned null instead of t"
                + "he correct symbol");
          }
          if (retVal == testSym) {
            failedTests = true;
            System.out.println("lookupGlobal returned a sym with the given n"
                + "ame as key but it was not the first in the list");
          } if (retVal != otherSym) {
            failedTests = true;
            System.out.println("lookupGlobal returned a sym that was not the"
                + " correct inserted value");
          }
        } catch (EmptySymTableException ex) {
          failedTests = true;
          System.out.println("lookupGlobal incorrectly threw an EmptySymTabl"
              + "eException");
        }
      } catch (Exception ex) {
        failedTests = true;
        System.out.println("addDecl incorrectly threw an exception while testi"
            + "ng functionality of lookupGlobal");
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("addScope incorrectly threw an exception while testin"
          + "g functionality of lookupGlobal");
    }
    //Tests to see if lookupGlobal correctly throws exceptions 
    //maybe automate this since the code is similar to what is above
    testTable = new SymTable();
    try {
      testTable.removeScope();
      try {
        testTable.lookupGlobal("name");
        System.out.println("lookupGlobal failed to throw an EmptySymTableExcep"
            + "tion when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("removeScope incorrectly threw an exception while t"
          + "esting functionality of lookupGlobal");
    }
    
    //Tests for the removeScope method 
    testTable = new SymTable();
    try {
      testTable.removeScope();
      try {
        testTable.removeScope();
        failedTests = true;
        System.out.println("removeScope failed to throw an EmptySymTableEx"
            + "ception when one occured");
      } catch (EmptySymTableException ex) {
      }
    } catch (EmptySymTableException ex) {
      failedTests = true;
      System.out.println("removeScope incorrectly threw a EmptySymTabl"
          + "eException");
    }
    
    //Tests for the print method

    testTable = new SymTable();
    HashMap<String, Sym> emptyMap = new HashMap<>();
    HashMap<String, Sym> nonEmptyMap = new HashMap<>();
    nonEmptyMap.put("name", testSym);
    
    System.out.print("|| EXPECTED OUTPUT FOR PRINT TEST1 ||");
    System.out.print("\nSym Table\n");
    System.out.print(emptyMap.toString() +"\n\n");
    System.out.print("|| OUTPUT FOR PRINT TEST1 ||");
    testTable.print();
    
    System.out.print("|| EXPECTED OUTPUT FOR PRINT TEST2 ||");
    System.out.print("\nSym Table\n");
    System.out.print(nonEmptyMap.toString() +"\n");
    System.out.print(emptyMap.toString() + "\n\n");
    System.out.print("|| OUTPUT FOR PRINT TEST2 ||");
    try {
      testTable.addDecl("name", testSym);
      testTable.print();
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("addDecl falsly threw an exception when t"
          + "esting the toPrint method");
    }
    
    //Tell the tester if all the tests passed without fail
    if (failedTests == false) {
      System.out.println("ALL TESTS PASSED");
    }
    
  }
}
