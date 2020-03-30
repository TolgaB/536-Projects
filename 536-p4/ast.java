import java.io.*;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Wumbo program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Children
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PostIncStmtNode     ExpNode
//       PostDecStmtNode     ExpNode
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       RepeatStmtNode      ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of children, or
// internal nodes with a fixed number of children:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of children:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PostIncStmtNode, PostDecStmtNode, ReadStmtNode,   WriteStmtNode
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  RepeatStmtNode,
//        CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode {
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    // this method can be used by the unparse methods to do indenting
    protected void addIndentation(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++) p.print(" ");
    }

    public SymTable nameAnalysis(SymTable workingSymTable) throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        return null;
    }


}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {

    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        System.out.println("ProgramNode name analysis called");
        return myDeclList.nameAnalysis(workingSymTable);
    }

    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {

    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }

    public SymTable nameAnalysis(SymTable workingCopy)
            throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        
        System.out.println("DeclListNode name analysis called");
        //create a symboltable going through the list
        for (DeclNode temp : myDecls) {
            //go through each DeclNode and add to the symbol table
            workingCopy = temp.nameAnalysis(workingCopy);
        }
        return workingCopy;
    }


    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        for (FormalDeclNode tempNode : myFormals) {
            workingSymTable = tempNode.nameAnalysis(workingSymTable);
        }
        return workingSymTable;
    }

    public ArrayList<Sym> getFormalListSym() {
        ArrayList<Sym> symList = new ArrayList<Sym>();
        for (FormalDeclNode tempNode : myFormals) {
            symList.add(tempNode.getSymForList());
        }
        return symList;
    }

    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        //first add the DeclListNode
        workingSymTable = myDeclList.nameAnalysis(workingSymTable);
        //now check the stmts for errors etc
        myStmtList.nameAnalysis(workingSymTable);
        return workingSymTable;
    }

    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        //check the declarations
        //TODO: error checking w/ statements and stuff
        for (StmtNode tempStmtNode: myStmts) {
            tempStmtNode.nameAnalysisNoReturn(workingSymTable);
        }
        return null;
    }

    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable)
            throws EmptySymTableException{
        for (ExpNode tempExpNode : myExps) {
            tempExpNode.nameAnalysisNoReturn(workingSymTable);
        }
    }


    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {
    abstract public SymTable nameAnalysis(SymTable workingSymTable) throws EmptySymTableException, DuplicateSymException, IllegalArgumentException;
}

class VarDeclNode extends DeclNode {

    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.println(";");
    }

    public SymTable nameAnalysis(SymTable workingSymTable) throws EmptySymTableException, DuplicateSymException, IllegalArgumentException {
       //TODO: fix the null returns
        //first check if it is a struct
        if (mySize == NOT_STRUCT) {
            //check to see if it is a double declaration
            if (workingSymTable.lookupLocal(myId.getStrVal()) == null) {
                //then its not in the symboltable
                workingSymTable.addDecl(myId.getStrVal(), new Sym(myType.strVal()));
            } else {
                //not sure about returning null here prob have to throw error
                (new ErrMsg()).fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
                return workingSymTable;
            }
        } else {
            boolean valid = true;
            //check to see if double declaration
            if (workingSymTable.lookupLocal(myId.getStrVal()) != null) {
                valid = false;
                (new ErrMsg()).fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
            }
            if (workingSymTable.lookupGlobal(myType.strVal()) == null) {
                System.out.println("myType toString is :" + myType.strVal());
                valid = false;
                (new ErrMsg()).fatal(myId.getLineNum(), myId.getCharNum(), "Invalid name of a struct type");
            } 
            if (valid) {
                workingSymTable.addDecl(myId.getStrVal(), new StructSym(myType.strVal()));
            }
        }
        return workingSymTable;
    } 

    private TypeNode myType;
    private IdNode myId;
    private int mySize;  // use value NOT_STRUCT if this is not a struct type

    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type,
                      IdNode id,
                      FormalsListNode formalList,
                      FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent+4);
        p.println("}\n");
    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, DuplicateSymException, EmptySymTableException {
        //generate arraylist for params
        //still need to add to the data structure
        //TODO: MULTIPLE DECLARATION CHECKING FOR FUNCS
        workingSymTable.addDecl(myId.getStrVal(), new FnSym(myType.strVal(), myFormalsList.getFormalListSym()));
        workingSymTable.addScope();
        workingSymTable = myFormalsList.nameAnalysis(workingSymTable);
        workingSymTable = myBody.nameAnalysis(workingSymTable);
        workingSymTable.removeScope();
        return workingSymTable;
    }

    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, DuplicateSymException, EmptySymTableException {
        //Check to make sure that it isnt already defined
        if (workingSymTable.lookupLocal(myId.getStrVal()) != null) {
            (new ErrMsg()).fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
            return workingSymTable;
        }
        workingSymTable.addDecl(myId.getStrVal(), new Sym(myType.strVal()));
        return workingSymTable;
    }

    public Sym getSymForList() {
        return new Sym(myType.strVal());
    }

    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("struct ");
        myId.unparse(p, 0);
        p.println("{");
        myDeclList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("};\n");

    }

    public SymTable nameAnalysis(SymTable workingSymTable)
            throws IllegalArgumentException, EmptySymTableException, DuplicateSymException {
        
        System.out.println("name analysis for StructDeclNode called");

        StructDecSym structDec = new StructDecSym("struct", myDeclList.nameAnalysis(new SymTable()));
        if (workingSymTable.lookupLocal(myId.getStrVal()) == null) {
            System.out.println("Struct added to symTable");
            workingSymTable.addDecl(myId.getStrVal(), structDec);
        } else {
            //IDK WHAT TO DO ABOUT THIS
            (new ErrMsg()).fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
            return workingSymTable;
        }
        return workingSymTable;
    }

    private IdNode myId;
    private DeclListNode myDeclList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
    abstract public String strVal();
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }

    public String strVal() {
        return "int";
    }
}

class BoolNode extends TypeNode {
    public BoolNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }

    public String strVal() {
        return "bool";
    }
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }

    public String strVal() {
        return "void";
    }
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        myId.unparse(p, 0);
    }

    public String strVal() {
        return myId.getStrVal();
    }

    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
    abstract public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException;
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException {
        myAssign.nameAnalysis(workingSymTable);
    }
    

    private AssignNode myAssign;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException { 
        myExp.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
}

class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
    }
    // 1 child (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
    DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
        myDeclList.nameAnalysis(workingSymTable);
        myStmtList.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
                          StmtListNode slist1, DeclListNode dlist2,
                          StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent+4);
        myThenStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
        addIndentation(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent+4);
        myElseStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
            DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
        myThenDeclList.nameAnalysis(workingSymTable);
        myThenStmtList.nameAnalysis(workingSymTable);
        myElseStmtList.nameAnalysis(workingSymTable);
        myElseDeclList.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
            DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
        myDeclList.nameAnalysis(workingSymTable);
        myStmtList.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
	addIndentation(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
            DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
        myDeclList.nameAnalysis(workingSymTable);
        myStmtList.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
            DuplicateSymException {
        myCall.nameAnalysis(workingSymTable);
    }
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException, IllegalArgumentException,
            DuplicateSymException {
        myExp.nameAnalysis(workingSymTable);
    }

    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
    }
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }

    
    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }


    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }


    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }


    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    //gonna have to have a field of type sym 

    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
        
    }

    public void unparse(PrintWriter p, int indent) {
        if (idSym == null) {
            p.print(myStrVal);
        }else {
            p.print(myStrVal + "(" + idSym.getType() + ")");
        }
    }

    //need to do name analysis
    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
        idSym = workingSymTable.lookupGlobal(myStrVal);
        System.out.println("looking at :" + myStrVal);
        if (idSym == null) {
            System.out.println("not found in symbol table: " + myStrVal);
            //found
            (new ErrMsg()).fatal(myLineNum, myCharNum, "Undeclared Identifier");
        } 
    }

    public Sym getSym() {
        return idSym;
    }

    public int getLineNum() {
        return myLineNum;
    }

    public int getCharNum() {
        return myCharNum;
    }

    public String getStrVal() {
        return myStrVal;
    }

    private Sym idSym;
    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myLoc.unparse(p, 0);
        p.print(").");
        myId.unparse(p, 0);
    }

    //need to do name analysis
    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
        //check to see if the id exists in the symbol table
        /*
        myId.nameAnalysis(workingSymTable);
        //check to see if it is a struct
        Sym idSym = myId.getSym();
        if (idSym != null) {
            //if not a valid struct type
            if (!idSym.structType()) {
                (new ErrMsg()).fatal(myId.getLineNum(), myId.getCharNum(), "Dot-access of non-struct type");
            } else {

            }
        }
        */
        //need to rewrite for myLoc and myId fuck this
    }

    private ExpNode myLoc;
    private IdNode myId;
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1)  p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1)  p.print(")");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
        //check for both the lhs and rhs
        myLhs.nameAnalysisNoReturn(workingSymTable);
        myExp.nameAnalysisNoReturn(workingSymTable);
    }

    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
        //check if the function exists
        myId.nameAnalysisNoReturn(workingSymTable);
        //check the functions expressions
        myExpList.nameAnalysisNoReturn(workingSymTable);
    }

    private IdNode myId;
    private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
        myExp.nameAnalysisNoReturn(workingSymTable);
    } 
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }

    public void nameAnalysisNoReturn(SymTable workingSymTable) throws EmptySymTableException {
        myExp1.nameAnalysisNoReturn(workingSymTable);
        myExp2.nameAnalysisNoReturn(workingSymTable);
    }

    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}
