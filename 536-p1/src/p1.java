/**
 * 
 */

/**
 * @author Tolga
 *
 */
public class P1 {
  /**
   * @param args
   */
  
  //check the functionality of Sym and SymTable including their exception handling
  public static void main(String[] args) {
    boolean failedTests = false;
    
    /* 
     * Sym class tests
     */
    Sym testSym = new Sym("int");
    if (!testSym.getType().equals("int")) {
      failedTests = true;
      System.out.println("The Sym class failed to return the correct given type when the getType method was called");
    }
    if (!testSym.toString().equals("int")) {
      failedTests = true;
      System.out.println("The Sym class failed to return the correct given type when the toString method was called");
    }
    
    /*
     * SymTable class tests
     */  
    //addDecl tests
    //see if any exception are improperly thrown
    SymTable testTable = new SymTable();
    try {
      testTable.addDecl("name", testSym);
    } catch (DuplicateSymException ex) {
      failedTests = true;
      System.out.println("DuplicateSymException was incorrectly thrown during addDecl");
    } catch (EmptySymTableException ex) {
      failedTests = true;
      System.out.println("EmptySymTableException was incorrectly thrown during addDecl");
    } catch (IllegalArgumentException ex) {
      failedTests = true;
      System.out.println("IllegalArgumentException was incorrectly thrown during addDecl");
    }
    //check if the correct exception are thrown when given incorrect input
    testTable = new SymTable();
    try {
      //check the EmptySymTableException
      testTable.removeScope();
      try {
        testTable.addDecl("name", testSym);
        System.out.println("addDecl failed to throw an EmptySymTableException when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
      //check the IllegalArgumentException
      testTable = new SymTable();
      try {
        testTable.addDecl(null, null);
        System.out.println("addDecl failed to throw an IllegalArgumentException when one occured");
        failedTests = true;
      } catch (IllegalArgumentException ex) {
      }
      //check the DuplicateSymException
      testTable = new SymTable();
      try {
        testTable.addDecl("name", testSym);
        testTable.addDecl("name", testSym);
        System.out.println("addDecl failed to throw a DuplicateSymException when one occured");
        failedTests = true;
      } catch (DuplicateSymException ex) {
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("When testing addDecl exceptions removeScope falsly threw an exception, more tests for removescope below");
    }

    //Tests for addScope method
    testTable = new SymTable();
    try {
      testTable.removeScope();
      testTable.addScope();
      testTable.removeScope();
      try {
        testTable.removeScope();
        System.out.println("addScope failed to throw an EmptySymTableException when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
    } catch (EmptySymTableException ex) {
      failedTests = true;
      System.out.println("When testing addScope functionality removeScope incorrectly threw an exception");
    }
    
    //Tests for the lookupLocal method (correct exception throwing tests below)
    testTable = new SymTable();
    try {
      testTable.addDecl("name", testSym);
      try {
        Sym retVal = testTable.lookupLocal("name");
        if (retVal == null) {
          failedTests = false;
          System.out.println("lookupLocal returned null instead of the correct symbol");
        }
        if (retVal != testSym) {
          failedTests = true;
          System.out.println("lookupLocal returned a value that was not the correct Sym");
        }
      } catch (EmptySymTableException ex) {
        failedTests = true;
        System.out.println("lookupLocal incorrectly threw an EmptySymTableException");
      }
    } catch (EmptySymTableException|DuplicateSymException ex) {
      failedTests = true;
      System.out.println("When testing lookupLocal functionality addDecl incorrectly threw an exception");
    }
    //Tests to see if lookupLocal correctly throws exceptions
    testTable = new SymTable();
    try {
      testTable.removeScope();
      try {
        testTable.lookupLocal("name");
        System.out.println("lookupLocal failed to throw an EmptySymTableException when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("removeScope incorrectly threw an exception while testing functionality of lookupLocal");
    }
       
    //Tests for the lookupGlobal method (correct exception throwing tests below)
    testTable = new SymTable();
    try {
      testTable.addScope();
      try {
        //add multiple sym with the same name key and put an empty hashmap as buffer
        //test to see if the program returns the first and not any given sym with the same name key
        testTable.addDecl("name", testSym);
        testTable.addScope();
        Sym otherSym = new Sym("string");
        testTable.addDecl("name", otherSym);
        try {
          Sym retVal = testTable.lookupGlobal("name");
          if (retVal == null) {
            failedTests = true;
            System.out.println("lookupGlobal returned null instead of the correct symbol");
          }
          if (retVal == testSym) {
            failedTests = true;
            System.out.println("lookupGlobal returned a sym with the given name as key but it was not the first in the list");
          } if (retVal != otherSym) {
            failedTests = true;
            System.out.println("lookupGlobal returned a sym that was not the correct inserted value");
          }
        } catch (EmptySymTableException ex) {
          failedTests = true;
          System.out.println("lookupGlobal incorrectly threw an EmptySymTableException");
        }
      } catch (Exception ex) {
        failedTests = true;
        System.out.println("addDecl incorrectly threw an exception while testing functionality of lookupGlobal");
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("addScope incorrectly threw an exception while testing functionality of lookupGlobal");
    }
    //Tests to see if lookupGlobal correctly throws exceptions 
    //maybe automate this since the code is similar to what is above
    testTable = new SymTable();
    try {
      testTable.removeScope();
      try {
        testTable.lookupGlobal("name");
        System.out.println("lookupGlobal failed to throw an EmptySymTableException when one occured");
        failedTests = true;
      } catch (EmptySymTableException ex) {
      }
    } catch (Exception ex) {
      failedTests = true;
      System.out.println("removeScope incorrectly threw an exception while testing functionality of lookupGlobal");
    }
    
    //Tests for the removeScope method 
    testTable = new SymTable();
    try {
      testTable.removeScope();
      try {
        testTable.removeScope();
        failedTests = true;
        System.out.println("removeScope failed to throw an EmptySymTableException when one occured");
      } catch (EmptySymTableException ex) {
      }
    } catch (EmptySymTableException ex) {
      failedTests = true;
      System.out.println("removeScope incorrectly threw a EmptySymTableException");
    }
    
    //Tests for the print method
    /*
     * 
     * REWRITE THIS
     * 
     */
    /*
    testTable = new SymTable();
    System.out.println("EXPECTED OUTPUT FOR PRINT");
    System.out.print("\nSym Table\n");
    System.out.println("OUTPUT FOR PRINT");
    testTable.print();
    System.out.println("EXPECTED OUTPUT FOR PRINT");
    //write tests for the print with the hashmap
     * 
     */
    
    //Tell the tester if all the tests passed without fail
    if (failedTests == false) {
      System.out.println("ALL TESTS PASSED");
    }
    
  }
}
