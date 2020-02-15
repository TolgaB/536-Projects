///////////////////////////////////////////////////////////////////////////////
//
// Title:            P2
// Files:            P2.java, sym.Java, wumbo.jlex
// Semester:         536 Spring 2020
//
// Author:           Tolga Beser
// Email:            tbeser@wisc.edu
// CS Login:         tolga
// Lecturer's Name:  Loris
//
/////////////////////////////////////////////////////////////////////////////  
import java.util.*;
import java.io.*;
import java_cup.runtime.*;  // defines Symbol

/**
 * This program is to be used to test the Wumbo scanner.
 * This version is set up to test all tokens, but you are required to test 
 * other aspects of the scanner (e.g., input that causes errors, character 
 * numbers, values associated with tokens)
 */
public class P2 {
    public static void main(String[] args) throws IOException {
                                           // exception may be thrown by yylex
        // test all tokens
        testAllTokens();
        CharNum.num = 1;
        reservedWordTest();
        CharNum.num = 1;
        integerOverflowTest();
        CharNum.num = 1;
        symbolTest();
        CharNum.num = 1;
        commentTest();
        CharNum.num = 1;
        basicStringLitTest();
        CharNum.num = 1;
        identifierTest();
        CharNum.num = 1;
        incorrectStringTest();
        CharNum.num = 1;
        eofTests();
        CharNum.num = 1;
        testLineCharNum();
        CharNum.num = 1;
        
    }
    
    private static void eofTests() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("eof.txt");
          outFile = new PrintWriter(new FileWriter("eof.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File eof.txt not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("eof.out cannot be opened.");
          System.exit(-1);
      }
      System.out.println("\nEXPECTED(eof test):SHOULD GIVE AN UNTERMINATED STRING LITERAL WARNING");
      System.out.println("---------------------------------------------------------------");
      parseSym(inFile, outFile);
      System.out.println("---------------------------------------------------------------\n");
      
      try {
        inFile = new FileReader("eof2.txt");
        outFile = new PrintWriter(new FileWriter("eof2.out"));
      } catch (FileNotFoundException ex) {
        System.err.println("File eof2.txt not found.");
        System.exit(-1);
      } catch (IOException ex) {
        System.err.println("eof2.out cannot be opened.");
        System.exit(-1);
      }
      System.out.println("EXPECTED(eof test): SHOULD GIVE AN unterminated string literal with bad escaped character ignored");
      System.out.println("---------------------------------------------------------------");
      parseSym(inFile, outFile);
      System.out.println("---------------------------------------------------------------\n");
      
      
      outFile.close();
      
    }
    
    private static void basicStringLitTest() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("basicStringLitTest.in");
          outFile = new PrintWriter(new FileWriter("basicStringLitTest.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File basicStringLitTest.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("basicStringLitTest.out cannot be opened.");
          System.exit(-1);
      }
      parseSym(inFile, outFile);
      outFile.close();
    }
    
    private static void incorrectStringTest() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("incorrectStringTest.in");
          outFile = new PrintWriter(new FileWriter("incorrectStringTest.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File incorrectStringTest.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("incorrectStringTest.out cannot be opened.");
          System.exit(-1);
      }

      parseSym(inFile, outFile);
      outFile.close();
      
    }
    
    private static void identifierTest() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("identifierTest.in");
          outFile = new PrintWriter(new FileWriter("identifierTest.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File identifierTest.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("identifierTest.out cannot be opened.");
          System.exit(-1);
      }
      parseSym(inFile, outFile);
      outFile.close();
    }
    
    private static void commentTest() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("commentTest.in");
          outFile = new PrintWriter(new FileWriter("commentTest.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File commentTest.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("commentTest.out cannot be opened.");
          System.exit(-1);
      }

      parseSym(inFile, outFile);
      outFile.close();
      
    }
    
    private static void symbolTest() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("symbolTest.in");
          outFile = new PrintWriter(new FileWriter("symbolTest.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File symbolTest.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("symbolTest.out cannot be opened.");
          System.exit(-1);
      }

      System.out.println("EXPECTED(symbolTest): unterminated string literal ignored");
      System.out.println("EXPECTED(symbolTest): unterminated string literal ignored");
      System.out.println("EXPECTED(symbolTest): string literal with bad escaped character ignored");
      System.out.println("EXPECTED(symbolTest): unterminated string literal with bad escaped character ignored");
      System.out.println("EXPECTED(symbolTest): unterminated string literal with bad escaped character ignored");
      System.out.println("---------------------------------------------------------------");
      parseSym(inFile, outFile);
      outFile.close();
      
  }
    
    private static void integerOverflowTest() throws IOException {
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("integerOverflow.in");
            outFile = new PrintWriter(new FileWriter("integerOverflow.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File integerOverflow.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("integerOverflow.out cannot be opened.");
            System.exit(-1);
        }
        System.out.println("EXPECTED(integerOverflowTest): SHOULD GIVE AN INTEGER OVERFLOW WARNING");
        System.out.println("---------------------------------------------------------------");
        parseSym(inFile, outFile);
        System.out.println("---------------------------------------------------------------\n");
        outFile.close();
    }

    private static void reservedWordTest() throws IOException {
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("reservedWord.in");
          outFile = new PrintWriter(new FileWriter("reservedWord.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File reservedWord.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("reservedWord.out cannot be opened.");
          System.exit(-1);
      }
      parseSym(inFile, outFile);
      outFile.close();
    }
    
    /**
     * testAllTokens
     *
     * Open and read from file allTokens.txt
     * For each token read, write the corresponding string to allTokens.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testAllTokens() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("allTokens.in");
            outFile = new PrintWriter(new FileWriter("allTokens.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File allTokens.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("allTokens.out cannot be opened.");
            System.exit(-1);
        }

        parseSym(inFile, outFile);
        
        outFile.close();
    }
    
    private static void testLineCharNum() throws IOException {
      // open input and output files
      FileReader inFile = null;
      PrintWriter outFile = null;
      try {
          inFile = new FileReader("testLineCharNum.in");
          outFile = new PrintWriter(new FileWriter("testLineCharNum.out"));
      } catch (FileNotFoundException ex) {
          System.err.println("File testLineCharNum.in not found.");
          System.exit(-1);
      } catch (IOException ex) {
          System.err.println("testLineCharNum.out cannot be opened.");
          System.exit(-1);
      }
      Yylex scanner = new Yylex(inFile);
      Symbol token = scanner.next_token();
      while (token.sym != sym.EOF) {
        outFile.println(((TokenVal)token.value).linenum + ":" + ((TokenVal)token.value).charnum);
        token = scanner.next_token();
      }
      outFile.close();
    }
    
    
    private static void parseSym(FileReader inFile, PrintWriter outFile) throws IOException{
      // create and call the scanner
      Yylex scanner = new Yylex(inFile);
      Symbol token = scanner.next_token();
      while (token.sym != sym.EOF) {
          switch (token.sym) {
          case sym.BOOL:
              outFile.println("bool"); 
              break;
          case sym.INT:
              outFile.println("int");
              break;
          case sym.VOID:
              outFile.println("void");
              break;
          case sym.TRUE:
              outFile.println("true"); 
              break;
          case sym.FALSE:
              outFile.println("false"); 
              break;
          case sym.STRUCT:
              outFile.println("struct"); 
              break;
          case sym.CIN:
              outFile.println("cin"); 
              break;
          case sym.COUT:
              outFile.println("cout");
              break;              
          case sym.IF:
              outFile.println("if");
              break;
          case sym.ELSE:
              outFile.println("else");
              break;
          case sym.WHILE:
              outFile.println("while");
              break;
          case sym.RETURN:
              outFile.println("return");
              break;
          case sym.ID:
              outFile.println(((IdTokenVal)token.value).idVal);
              break;
          case sym.INTLITERAL:  
              outFile.println(((IntLitTokenVal)token.value).intVal);
              break;
          case sym.STRINGLITERAL: 
              outFile.println(((StrLitTokenVal)token.value).strVal);
              break;    
          case sym.LCURLY:
              outFile.println("{");
              break;
          case sym.RCURLY:
              outFile.println("}");
              break;
          case sym.LPAREN:
              outFile.println("(");
              break;
          case sym.RPAREN:
              outFile.println(")");
              break;
          case sym.SEMICOLON:
              outFile.println(";");
              break;
          case sym.COMMA:
              outFile.println(",");
              break;
          case sym.DOT:
              outFile.println(".");
              break;
          case sym.WRITE:
              outFile.println("<<");
              break;
          case sym.READ:
              outFile.println(">>");
              break;              
          case sym.PLUSPLUS:
              outFile.println("++");
              break;
          case sym.MINUSMINUS:
              outFile.println("--");
              break;  
          case sym.PLUS:
              outFile.println("+");
              break;
          case sym.MINUS:
              outFile.println("-");
              break;
          case sym.TIMES:
              outFile.println("*");
              break;
          case sym.DIVIDE:
              outFile.println("/");
              break;
          case sym.NOT:
              outFile.println("!");
              break;
          case sym.AND:
              outFile.println("&&");
              break;
          case sym.OR:
              outFile.println("||");
              break;
          case sym.EQUALS:
              outFile.println("==");
              break;
          case sym.NOTEQUALS:
              outFile.println("!=");
              break;
          case sym.LESS:
              outFile.println("<");
              break;
          case sym.GREATER:
              outFile.println(">");
              break;
          case sym.LESSEQ:
              outFile.println("<=");
              break;
          case sym.GREATEREQ:
              outFile.println(">=");
              break;
          case sym.ASSIGN:
              outFile.println("=");
              break;
          default:
              outFile.println("UNKNOWN TOKEN");
          } // end switch

          token = scanner.next_token();
      } // end while
     
    }
}
