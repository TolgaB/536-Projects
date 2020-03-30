import java.util.ArrayList;

public class Sym {
    private String type;
    
    public Sym(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public String toString() {
        return type;
    }
}

class FnSym extends Sym {
    private ArrayList<Sym> fields;
    public FnSym(String type, ArrayList<Sym> fields) {
        super(type);
        this.fields = fields;
    }

    public ArrayList<Sym> getFields() {
        return fields;
    }

}

class StructDecSym extends Sym {
    private SymTable fields;

    public StructDecSym(String type, SymTable fields) {
        super(type);
        this.fields = fields;
    }

    public SymTable getFields() {
        return fields;
    }

}

class StructSym extends Sym {
    public StructSym(String type) {
        super(type);
    }
}