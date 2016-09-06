/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;

/**
 *
 * @author Zach Wilson
 */
import java.lang.*;
import java.util.*;
import lexicalanalizer.*;
import Parser.*;
import SymbolTable.*;

public class SemanticActions {
	
	private Stack<Object> semanticStack ;
        private Stack<Integer> parmCount;
        private Stack<Stack<TypedEntry>> nextParm;
	private Quadruples quads ;
        private boolean isParm;
	private int globalMemory ;
	private int localMemory ;
        private int globalStore;
        private int localStore;
        private static final boolean INSERT = true;
        private static final boolean GLOBAL = true;
        private static final boolean ARRAY = true;
        private static final boolean SEARCH = false;
        private static final boolean LOCAL = false;
        private static final boolean SIMPLE = false;
        private static final boolean ARITHMETIC = true;
        private static final boolean RELATIONAL = false;
        private static final TokenType REAL = TokenType.REAL;
        private static final TokenType INTEGER = TokenType.INTEGER;
        private boolean insertSearch;
        private boolean globalLocal;
        private boolean arraySimple;
        private SymbolTable globalTable;
        private SymbolTable localTable;
        private SymbolTable constantTable;
        private int tableSize = 37;
        private int tempNum;
        private FunctionEntry currFunction;
	

	public SemanticActions() {
		semanticStack = new Stack<Object>();
                parmCount = new Stack<Integer>();
                nextParm = new Stack<Stack<TypedEntry>>();
		quads = new Quadruples();
		isParm = false; 
		globalMemory = 0 ;
		localMemory = 0;
		globalTable = new SymbolTable(tableSize);
		constantTable = new SymbolTable(tableSize);
		globalTable.installBuiltIns();
                insertSearch = INSERT;
                globalLocal = GLOBAL;
                arraySimple = SIMPLE;
                tempNum = 1;
	}
	
	public void Execute (SemanticAction action, Token token)  throws SemanticError {
		int actionNumber = action.getIndex();
		System.out.println("calling action : " + actionNumber + " with " + token.toString());
                switch (actionNumber){
                    case 1 : {
                        insertSearch = INSERT;
                        break;
                    }
                    case 2 : {
                        insertSearch = SEARCH;
                        break;
                    }
                    case 3 : {
                        TokenType type = ((Token)semanticStack.pop()).getType();
                        if (arraySimple == ARRAY){
                            int ub = Integer.parseInt(((Token)semanticStack.pop()).getValue());
                            int lb = Integer.parseInt(((Token)semanticStack.pop()).getValue());
                            int mSize = (ub - lb) + 1;
                            while((!(semanticStack.empty()) && ((Token)semanticStack.peek()).getType() == TokenType.IDENTIFIER)){
                                String id = ((Token)semanticStack.pop()).getValue();
                                if (globalLocal == GLOBAL){
                                    ArrayEntry entry = new ArrayEntry(id);
                                    entry.lowerBound = lb;
                                    entry.upperBound = ub;
                                    entry.type = type;
                                    entry.address = "_" + globalMemory;
                                    globalTable.insert(entry);
                                    globalMemory += mSize;
                                    
                                }
                                else{
                                    ArrayEntry entry = new ArrayEntry(id);
                                    entry.lowerBound = lb;
                                    entry.upperBound = ub;
                                    entry.type = type;
                                    entry.address = "_" + localMemory;
                                    localTable.insert(entry);
                                    localMemory += mSize;                                    
                                }
                            }
                        }
                        else{
                            while(!semanticStack.empty() && (semanticStack.peek() instanceof Token) && ((Token)semanticStack.peek()).getType() == TokenType.IDENTIFIER){
                                String id = ((Token)semanticStack.pop()).getValue();
                                if (globalLocal == GLOBAL){
                                    this.varInsert(id,type);
                                }
                                else{
                                    this.varInsert(id,type);
                                }
                            }
                        }
                        arraySimple = SIMPLE;
                        break;
                    }
                    case 4 : {
                        semanticStack.push(token);
                        break;
                    }
                    
                    case 5 : {
                        insertSearch = SEARCH;
                        CallableEntry id = (CallableEntry)semanticStack.pop();
                        quads.generate("PROCBEGIN",id.getName());
                        localStore = quads.getNextQuad();
                        quads.generate("alloc");
                        break;
                    }
                    
                    case 6 : {
                        arraySimple = ARRAY;
                        break;
                    }
                    case 7 : {
                        semanticStack.push(token);
                        break;
                    }
                    
                    case 9 : {
                        while((!(semanticStack.empty()) && ((Token)semanticStack.peek()).getType() == TokenType.IDENTIFIER)){
                            Token t = (Token)semanticStack.pop();
                            VariableEntry v = this.varInsert(t);
                            v.reserved = true;
                        }
                        insertSearch = SEARCH;
                        quads.generate("CODE");
                        quads.generate("call","main","0");
                        quads.generate("exit");
                        break;
                    }
                    
                    case 11 : {
                        globalLocal = GLOBAL;
                        localTable = new SymbolTable(tableSize);
                        currFunction = null;
                        quads.backPatch(localStore,Integer.toString(localMemory));
                        quads.generate("free",Integer.toString(localMemory));
                        quads.generate("PROCEND");
                        break;
                    }
                    
                    case 13 : {
                        semanticStack.push(token);
                        break;
                    }
                    
                    case 15 : {
                        FunctionEntry f = new FunctionEntry(token.getValue());
                        globalTable.insert(f);
                        semanticStack.push(f);
                        VariableEntry result = create(TokenType.INTEGER);
                        f.result = result;
                        globalLocal = LOCAL;
                        localTable = new SymbolTable(tableSize);
                        localMemory = 0;
                        break;
                    }
                    
                    case 16 : {
                        Token t = (Token)semanticStack.pop();
                        TokenType type = t.getType();
                        FunctionEntry f = (FunctionEntry)getCallable();
                        f.result.type = type;
                        currFunction = f;
                        break;
                    }
                    
                    case 17 : {
                        ProcedureEntry id = new ProcedureEntry(token.getValue());
                        globalTable.insert(id);
                        semanticStack.push(id);
                        globalLocal = LOCAL;
                        localTable = new SymbolTable(tableSize);
                        localMemory = 0;
                        break;
                    }
                    
                    case 19 : {
                        parmCount.push(0);
                        break;
                    }
                    
                    case 20 : {
                        CallableEntry p = getCallable();
                        p.numberOfParameters = parmCount.pop();
                        break;
                    }
                    
                    case 21 : {
                        // I changed this slightly so it would work with thing like (x,y,z : array [1..5] of real;)
                        // also takes into account that if arraySimple == array it will not change untill after the loop is over
                        TokenType type = ((Token)semanticStack.pop()).getType();
                        if(arraySimple == ARRAY){
                            int ub = Integer.parseInt(((Token)semanticStack.pop()).getValue());
                            int lb = Integer.parseInt(((Token)semanticStack.pop()).getValue());
                            while((!semanticStack.empty())&&semanticStack.peek() instanceof Token){
                                String name = ((Token)semanticStack.pop()).getValue();
                                ArrayEntry array = new ArrayEntry(name);
                                array.type = type;
                                array.lowerBound = lb;
                                array.upperBound = ub;
                                array.isParam = true;
                                localTable.insert(array);
                                array.address = "^%" + localMemory;
                                localMemory++;
                                CallableEntry p = getCallable();
                                p.parmInfo.add(array);
                                int i = parmCount.pop();
                                i++;
                                parmCount.push(i);
                            }
                            
                        }
                        else{
                            while((!semanticStack.empty())&&semanticStack.peek() instanceof Token){
                                Token t = (Token)semanticStack.pop();
                                VariableEntry v = new VariableEntry(t.getValue());
                                v.isParam = true;
                                v.type = type;
                                localTable.insert(v);
                                v.address = "^%" + localMemory;
                                localMemory++;
                                CallableEntry p = getCallable();
                                p.parmInfo.add(v);
                                int i = parmCount.pop();
                                i++;
                                parmCount.push(i);
                            }
                        }
                        arraySimple = SIMPLE;
                        break;
                    }
                    
                    case 22 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if(etype == ARITHMETIC){
                            throw SemanticError.needRelationalError();
                        }
                        EList etrue = getETrue();
                        quads.backPatch(etrue,Integer.toString(quads.getNextQuad()));
                        break;
                    }
                    
                    case 24 : {
                        Integer beginloop = quads.getNextQuad();
                        semanticStack.push(beginloop);
                        break;
                    }
                    
                    case 25 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if(etype == ARITHMETIC){
                            throw SemanticError.needRelationalError();
                        }
                        EList eTrue = getETrue();
                        quads.backPatch(eTrue,Integer.toString(quads.getNextQuad()));
                        break;
                    }
                    
                    case 26 : {
                        EList eFalse = (EList)semanticStack.pop();
                        EList eTrue = (EList)semanticStack.pop();
                        Integer beginloop = (Integer)semanticStack.pop();
                        quads.generate("goto",beginloop.toString());
                        quads.backPatch(eFalse,Integer.toString(quads.getNextQuad()));
                        break;
                    }
                    
                    case 27 : {
                        EList skipElse = new EList("SkipElse",quads.getNextQuad());
                        semanticStack.push(skipElse);
                        quads.generate("goto");
                        EList efalse = getEFalse();
                        quads.backPatch(efalse,Integer.toString(quads.getNextQuad()));
                        break;
                    }
                    
                    case 28 : {
                        EList skipElse = (EList)semanticStack.pop();
                        quads.backPatch(skipElse,Integer.toString(quads.getNextQuad()));
                        semanticStack.pop();
                        semanticStack.pop();
                        break;
                    }
                    
                    case 29 : {
                        EList eFalse = (EList)semanticStack.pop();
                        semanticStack.pop();
                        quads.backPatch(eFalse,Integer.toString(quads.getNextQuad()));
                        break;
                    }
                    
                    case 30 : {
                        String id = token.getValue();
                        SymbolTableEntry s = this.lookup(id);
                        if(s == null){
                            throw SemanticError.idNotFoundError(id);
                        }
                        else{
                            semanticStack.push(s);
                            semanticStack.push(ARITHMETIC);
                        }
                        break;
                    }
                    
                    case 31 : {
                        boolean eType = (boolean) semanticStack.pop();
                        TypedEntry id2;
                        if(semanticStack.peek() instanceof FunctionEntry){
                            id2 = ((FunctionEntry)semanticStack.pop()).result;
                        }
                        else{
                            id2 = (TypedEntry)semanticStack.pop();
                        }
                        VariableEntry offset = (VariableEntry)semanticStack.pop();
                        TypedEntry id1;
                        if(semanticStack.peek() instanceof FunctionEntry){
                            id1 = ((FunctionEntry)semanticStack.pop()).result;
                        }
                        else{
                            id1 = (TypedEntry)semanticStack.pop();
                        }
                        if(eType == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        if(this.typeCheck(id1, id2) == 3){
                            throw SemanticError.realToIntError();
                        }
                        if(this.typeCheck(id1, id2) == 2){
                            VariableEntry temp = this.create(REAL);
                            quads.generate("ltof",id2.address,temp.address);
                            if(offset == null){
                                quads.generate("move",temp.address,id1.address);
                            }
                            else{
                                quads.generate("store",temp.address,offset.address,id1.address);
                            }
                        }
                        else if(offset == null){
                            quads.generate("move",id2.address,id1.address);
                        }
                        else{
                            quads.generate("stor",id2.address,offset.address,id1.address);
                        }
                        break;
                    }
                    
                    case 32 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if(etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        SymbolTableEntry id = (SymbolTableEntry)semanticStack.peek();
                        if(!id.isArray()){
                            throw SemanticError.needArrayError(id);
                        }
                        break;
                    }
                    
                    case 33 : {
                        boolean etype = (boolean) semanticStack.pop();
                        if(etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        VariableEntry id = (VariableEntry)semanticStack.pop();
                        if(id.type != TokenType.INTEGER){
                            throw SemanticError.needIntError(id);
                        }
                        VariableEntry temp = create(TokenType.INTEGER);
                        int lbound = getArray().lowerBound;
                        quads.generate("sub",id.address,Integer.toString(lbound),temp.address);
                        semanticStack.push(temp);
                        break;
                    }
                    
                    case 34 : {
                        boolean eType = (boolean) semanticStack.pop();
                        if(semanticStack.peek() instanceof SymbolTableEntry){
                            if(((SymbolTableEntry)semanticStack.peek()).isFunction()){
                                
                            }
                            else{
                                semanticStack.push(null);
                            }
                        }
                        else{
                            semanticStack.push(null);
                        }
                        break;
                        
                    }
                    
                    case 35 : {
                        parmCount.push(0);
                        if(!(token.getValue().equals("write") || token.getValue().equals("read"))){
                            ProcedureEntry id = (ProcedureEntry)lookup(token.getValue());
                            nextParm.push((Stack<TypedEntry>)id.parmInfo.clone());
                        }
                        break;
                    }
                    
                    case 36 : {
                        semanticStack.pop();
                        CallableEntry id = (CallableEntry)semanticStack.pop();
                        if(id.numberOfParameters != 0){
                            throw SemanticError.needParamError(id);
                        }
                        quads.generate("call",id.getName(),"0");
                        break;
                    }
                    
                    case 37 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if(etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        SymbolTableEntry id = (SymbolTableEntry)semanticStack.peek();
                        if(! id.isTyped()){
                            throw SemanticError.badParamError(id);
                        }
                        Integer value = parmCount.pop();
                        parmCount.push(value + 1);
                        CallableEntry c = getCallable();
                        if(!(c.getName().equals("read") || c.getName().equals("write"))){
                            int i = parmCount.peek();
                            if(i > c.numberOfParameters){
                                throw SemanticError.manyParamError(c);
                            }
                            if(((TypedEntry)id).type != nextParm.peek().peek().type){
                                throw SemanticError.wrongParamError(id);
                            }
                            if(nextParm.peek().peek().isArray()){
                                ArrayEntry a1 = (ArrayEntry)nextParm.peek().peek();
                                ArrayEntry a2 = (ArrayEntry)id;
                                if(a1.upperBound != a2.upperBound || a1.lowerBound != a2.lowerBound){
                                    throw SemanticError.arraySizeParamError(a2);
                                }
                            }
                            nextParm.peek().pop();
                        }
                        break;
                    }
                    
                    case 38 : {
                        boolean etype = (boolean) semanticStack.pop();
                        if(etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        semanticStack.push(token);
                        break;
                    }
                    
                    case 39 : {
                        boolean etype = (boolean) semanticStack.pop();
                        if (etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        VariableEntry id2 = (VariableEntry)semanticStack.pop();
                        Token operator = (Token)semanticStack.pop();
                        VariableEntry id1 = (VariableEntry)semanticStack.pop();
                        int check = typeCheck(id1,id2);
                        if(check == 2){
                            VariableEntry temp = create(TokenType.REAL);
                            quads.generate("ltof",id2,temp);
                            String s = help39(operator);
                            quads.generate(s,id1,temp);
                        }
                        else if(check == 3){
                            VariableEntry temp = create(TokenType.REAL);
                            quads.generate("ltof",id1,temp);
                            String s = help39(operator);
                            quads.generate(s,temp,id2);
                        }
                        else{
                            String s = help39(operator);
                            quads.generate(s,id1,id2);
                        }
                        quads.generate("goto");
                        EList etrue = new EList("ETrue",quads.getNextQuad()-2);
                        EList efalse = new EList("EFalse",quads.getNextQuad()-1);
                        semanticStack.push(etrue);
                        semanticStack.push(efalse);                        
                        semanticStack.push(RELATIONAL);
                        break;
                    }
                    
                    case 40 : {
                        semanticStack.push(token.getType());
                        break;
                    }
                    
                    case 41 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if(etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        TypedEntry id = (TypedEntry)semanticStack.pop();
                        TokenType type = (TokenType)semanticStack.pop();
                        if(type == TokenType.UNARYMINUS){
                            VariableEntry temp = create(id.type);
                            quads.generate("uminus",id.address,temp.address);
                            semanticStack.push(temp);
                        }
                        else{
                            semanticStack.push(id);
                        }
                        semanticStack.push(ARITHMETIC);
                        break;
                    }
                    
                    case 42 : {
                        boolean eType = (boolean)semanticStack.pop();
                        if(token.getValue().equals("3")){
                            if(eType == ARITHMETIC){
                                throw SemanticError.needRelationalError();
                            }
                            
                        }
                        semanticStack.push(token);
                        break;
                    }
                    
                    case 43 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if (etype == RELATIONAL){
                            EList e1False = (EList)semanticStack.pop();
                            EList e1True = (EList)semanticStack.pop();
                            semanticStack.pop();
                            EList e2False = (EList)semanticStack.pop();
                            EList e2True = (EList)semanticStack.pop();
                            e1True.list.addAll(e2True.list);
                            LinkedList trueList = (LinkedList)e1True.list.clone();
                            EList eTrue = new EList("ETrue",trueList);
                            e1False.list.addAll(e2False.list);
                            LinkedList falseList = (LinkedList)e1False.list.clone();
                            EList eFalse = new EList("ETrue",falseList);
                            semanticStack.push(eTrue);
                            semanticStack.push(eFalse);
                            semanticStack.push(RELATIONAL);
                        }
                        else {
                            VariableEntry id2 = (VariableEntry)semanticStack.pop();
                            Token operator = (Token)semanticStack.pop();
                            VariableEntry id1 = (VariableEntry)semanticStack.pop();
                            String type = operator.getValue();
                            VariableEntry result;
                            int check = typeCheck(id1,id2);
                            if(check == 0){
                                VariableEntry temp = create(TokenType.INTEGER);
                                result = temp;
                                if(type.equals("1")){
                                    quads.generate("add",id1,id2,temp);
                                }
                                else{
                                    quads.generate("sub",id1,id2,temp);
                                }
                            }
                            else if(check == 1){
                                VariableEntry temp = create(TokenType.REAL);
                                result = temp;
                                if(type.equals("1")){
                                    quads.generate("fadd",id1,id2,temp);
                                }
                                else{
                                    quads.generate("fsub",id1,id2,temp);
                                }
                            }
                            else if(check == 2){
                                VariableEntry temp1 = create(TokenType.REAL);
                                quads.generate("ltof",id2,temp1);
                                VariableEntry temp2 = create(TokenType.REAL);
                                result = temp2;
                                if(type.equals("1")){
                                    quads.generate("fadd",id1,temp1,temp2);
                                }
                                else{
                                    quads.generate("fsub",id1,temp1,temp2);
                                }
                            }
                            else{
                                VariableEntry temp1 = create(TokenType.REAL);
                                quads.generate("ltof",id1,temp1);
                                VariableEntry temp2 = create(TokenType.REAL);
                                result = temp2;
                                if(type.equals("1")){
                                    quads.generate("fadd",temp1,id2,temp2);
                                }
                                else{
                                    quads.generate("fsub",temp1,id2,temp2);
                                }
                            }
                            semanticStack.push(result);
                            semanticStack.push(ARITHMETIC);
                        }
                        break;
                    }
                    
                    case 44 : {
                        boolean eType = (boolean)semanticStack.pop();
                        if(eType == RELATIONAL){
                            if(token.getValue().equals("5")){
                                quads.backPatch(getETrue(),Integer.toString(quads.getNextQuad()));
                            }
                        }
                        semanticStack.push(token);
                        break;
                    }
                    
                    case 45 : {
                        boolean eType = (boolean)semanticStack.pop();
                        // as I cannot access the operator itself I have to use the fact that only if the operator is <and> will the top of the stack be a list
                        if(semanticStack.peek() instanceof EList){ 
                            if(eType == ARITHMETIC){
                                throw SemanticError.needRelationalError();
                            }
                            EList e2False = (EList)semanticStack.pop();
                            EList e2True = (EList)semanticStack.pop();
                            semanticStack.pop();
                            EList e1False = (EList)semanticStack.pop();
                            EList e1True = (EList)semanticStack.pop();
                            EList eTrue = new EList("ETrue",e2True.list);
                            e1False.list.addAll(e2False.list);
                            EList eFalse = new EList ("EFalse",e1False.list);
                            semanticStack.push(eTrue);
                            semanticStack.push(eFalse);
                            semanticStack.push(RELATIONAL);
                        }
                        else{
                            VariableEntry result;
                            VariableEntry id2;
                            if(((SymbolTableEntry)semanticStack.peek()).isFunction()){
                                id2 = ((FunctionEntry)semanticStack.pop()).result;
                            }
                            else{
                                id2 = (VariableEntry)semanticStack.pop();
                            }
                            Token operator = (Token)semanticStack.pop();
                            VariableEntry id1;
                            if(((SymbolTableEntry)semanticStack.peek()).isFunction()){
                                id1 = ((FunctionEntry)semanticStack.pop()).result;
                            }
                            else{
                                id1 = (VariableEntry)semanticStack.pop();
                            }
                            String value = operator.getValue();
                            if(eType != ARITHMETIC){
                                // etype error
                            }
                            int typecheck = this.typeCheck(id1, id2);
                            if(typecheck != 0 && value.equals("4")){
                                // error, mod requires integers
                            }
                            if(typecheck == 0){
                                if(value.equals("4")){
                                    VariableEntry temp1 = create(INTEGER);
                                    quads.generate("move",id1.address,temp1.address);
                                    VariableEntry temp2 = create(INTEGER);
                                    quads.generate("move",temp1.address,temp2.address);
                                    quads.generate("sub",temp2.address,id2.address,temp1.address);
                                    quads.generate("bge",temp1.address,id2.address,Integer.toString(quads.getNextQuad()-2));
                                    result = temp1;
                                }
                                else if(value.equals("2")){
                                    VariableEntry temp1 = create(REAL);
                                    quads.generate("ltof",id1,temp1);
                                    VariableEntry temp2 = create(REAL);
                                    quads.generate("ltof",id2,temp2);
                                    VariableEntry temp3 = create(REAL);
                                    quads.generate("fdiv",temp1,temp2,temp3);
                                    result = temp3;
                                }
                                else {
                                    VariableEntry temp1 = create(INTEGER);
                                    if(value.equals("1")){
                                        quads.generate("mul",id1,id2,temp1);
                                    }
                                    else{
                                        quads.generate("div",id1,id2,temp1);
                                    }
                                    result = temp1;
                                }
                            }
                            else if(typecheck == 1){
                                if(value.equals("3")){
                                    VariableEntry temp1 = create(INTEGER);
                                    quads.generate("ftol",id1,temp1);
                                    VariableEntry temp2 = create(INTEGER);
                                    quads.generate("ftol",id2,temp2);
                                    VariableEntry temp3 = create(INTEGER);
                                    quads.generate("div",temp1,temp2,temp3);
                                    result = temp3;
                                }
                                else{
                                    VariableEntry temp1 = create(REAL);
                                    if(value.equals("1")){
                                        quads.generate("fmul",id1,id2,temp1);
                                    }
                                    else{
                                        quads.generate("fdiv",id1,id2,temp1);
                                    }
                                    result = temp1;
                                }
                            }
                            else if(typecheck == 2){
                                if(value.equals("3")){
                                    VariableEntry temp1 = create(INTEGER);
                                    quads.generate("ftol",id1,temp1);
                                    VariableEntry temp2 = create(INTEGER);
                                    quads.generate("div",temp1,id2,temp2);
                                    result = temp2;
                                }
                                else{
                                    VariableEntry temp1 = create(REAL);
                                    quads.generate("ltof",id2,temp1);
                                    VariableEntry temp2 = create(REAL);
                                    if(value.equals("1")){
                                        quads.generate("fmul",id1,temp1,temp2);
                                    }
                                    else{
                                        quads.generate("fdiv",id1,temp1,temp2);
                                    }
                                    result = temp2;
                                }
                            }
                            else{
                                if(value.equals("3")){
                                    VariableEntry temp1 = create(INTEGER);
                                    quads.generate("ftol",id2,temp1);
                                    VariableEntry temp2 = create(INTEGER);
                                    quads.generate("div",id1,temp1,temp2);
                                    result = temp2;
                                }
                                else{
                                    VariableEntry temp1 = create(REAL);
                                    quads.generate("lotf",id1,temp1);
                                    VariableEntry temp2 = create(REAL);
                                    if(value.equals("1")){
                                        quads.generate("fmul",temp1,id2,temp2);
                                    }
                                    else{
                                        quads.generate("fdiv",temp1,id2,temp2);
                                    }
                                    result = temp2;
                                }
                            }
                            semanticStack.push(result);
                            semanticStack.push(ARITHMETIC);
                        }
                        break;
                    }
                    
                    case 46 : {
                        if(token.getType() == TokenType.IDENTIFIER){
                            SymbolTableEntry s = this.lookup(token.getValue());
                            if(s == null){
                                throw SemanticError.undecVarError(token.getValue());
                            }
                            semanticStack.push(s);
                        }
                        else if(token.getType() == TokenType.INTCONSTANT || token.getType() == TokenType.REALCONSTANT){
                            VariableEntry v = (VariableEntry)constantTable.lookup(token.getValue());
                            if(v == null){
                                v = this.constInsert(token);
                            }
                            semanticStack.push(v);
                        }
                        semanticStack.push(ARITHMETIC);
                        break;
                    }
                    
                    case 47 : {
                        boolean etype = (boolean)semanticStack.pop();
                        if(etype == ARITHMETIC){
                            throw SemanticError.needRelationalError();
                        }
                        EList eFalse = (EList)semanticStack.pop();
                        EList eTrue = (EList)semanticStack.pop();
                        LinkedList temp = eTrue.list;
                        eTrue.list = eFalse.list;
                        eFalse.list = temp;
                        semanticStack.push(eTrue);
                        semanticStack.push(eFalse);
                        semanticStack.push(RELATIONAL);
                        break;
                    }
                    
                    case 48 : {
                        if(semanticStack.peek() != null){
                            VariableEntry offset = (VariableEntry) semanticStack.pop();
                            ArrayEntry id = (ArrayEntry) semanticStack.pop();
                            VariableEntry temp = this.create(id.type);
                            quads.generate("load",id.address,offset.address,temp.address);
                            semanticStack.push(temp);
                        }
                        else{
                            semanticStack.pop();
                        }
                        semanticStack.push(ARITHMETIC);
                        break;
                    }
                    
                    case 49 : {
                        boolean etype = (boolean)semanticStack.peek();
                        if(etype == RELATIONAL){
                            throw SemanticError.needArithmeticError();
                        }
                        CallableEntry c = getCallable();
                        if(!c.isFunction()){
                            throw SemanticError.notFuncError(c);
                        }
                        parmCount.push(0);
                        nextParm.push((Stack<TypedEntry>)c.parmInfo.clone());
                        break;
                    }
                    
                    case 50 : {
                        Stack<TypedEntry> funcStack = new Stack();
                        while(!semanticStack.empty() && (semanticStack.peek() instanceof TypedEntry)){
                            TypedEntry t = (TypedEntry)semanticStack.pop();
                            funcStack.push(t);
                        }
                        while(!funcStack.empty()){
                            TypedEntry t = funcStack.pop();
                            quads.generate("param","@" + t.address);
                            localMemory++;
                        }
                        int count = parmCount.pop();
                        boolean etype = (boolean)semanticStack.pop();
                        FunctionEntry id = (FunctionEntry)semanticStack.pop();
                        if(count > id.numberOfParameters){
                            throw SemanticError.manyParamError(id);
                        }
                        quads.generate("call",id.getName(),Integer.toString(count));
                        VariableEntry temp = create(id.result.type);
                        quads.generate("move",id.result,temp);
                        nextParm.pop();
                        semanticStack.push(temp);
                        semanticStack.push(ARITHMETIC);
                        break;
                    }
                    
                    case 51 : {
                        // allows me to pass the parameters to read and write and puts them into the correct order.
                        Stack<TypedEntry> paramStack = new Stack();
                        while(!semanticStack.empty() && (semanticStack.peek() instanceof TypedEntry)){
                            TypedEntry t = (TypedEntry)semanticStack.pop();
                            paramStack.push(t);
                        }
                        boolean etype = (boolean)semanticStack.pop();
                        ProcedureEntry id = (ProcedureEntry)semanticStack.pop();
                        if(id.getName().equals("read")){
                            read(paramStack);
                        }
                        else if(id.getName().equals("write")){
                            write(paramStack);
                        }
                        else {
                            Integer num = parmCount.pop();
                            if(num != id.numberOfParameters){
                                throw SemanticError.wrongNumParamError(id);
                            }
                            while(!paramStack.empty()){
                                TypedEntry t = paramStack.pop();
                                quads.generate("param","@" + t.address);
                                localMemory += 1;
                            }
                            nextParm.pop();
                            quads.generate("call",id.getName(),Integer.toString(num));
                        }
                        break;
                    }
                    
                    case 52 : {
                        dumpStack();
                        FunctionEntry id = (FunctionEntry)semanticStack.pop();
                        if (id.numberOfParameters > 0){
                            throw SemanticError.manyParamError(id);
                        }
                        quads.generate(id.getName(),"0");
                        VariableEntry temp = create(id.result.type);
                        quads.generate("move",id.result,temp);
                        semanticStack.push(temp);
                        semanticStack.push(ARITHMETIC);
                        dumpQuads();
                        break;
                    }
                    
                    case 53 : {
                        SymbolTableEntry s = lookup(token.getValue());
                        if(s.isFunction()){
                            if(s != currFunction){
                                throw SemanticError.currFuncError(s);
                            }
                            FunctionEntry id1 = (FunctionEntry) s;
                            semanticStack.pop();
                            semanticStack.pop();
                            semanticStack.push(id1.result);
                            semanticStack.push(ARITHMETIC);
                        }
                        break;
                    }
                    
                    case 54 : {
                        SymbolTableEntry id = lookup(token.getValue());
                        if((id == null) || !id.isProcedure()){
                            throw SemanticError.notProcError(id);
                        }
                        break;
                    }
                    
                    case 55 : {
                        quads.backPatch(globalStore,Integer.toString(globalMemory));
                        quads.generate("free",Integer.toString(globalMemory));
                        quads.generate("PROCEND");
                        break;
                    }
                    
                    case 56 : {
                        quads.generate("PROCBEGIN","main");
                        globalStore = quads.getNextQuad();
                        quads.generate("alloc");
                        break;
                    }
                    
                    default : {
                        System.out.println("I have not made that action yet");
                        break;
                    }
                }
        }
        
        public void dumpTables(){
            globalTable.dumpTable();
            constantTable.dumpTable();
        }
        
        public void dumpStack(){
            for(Object o: semanticStack){
                if(o == null){
                    System.out.println("null");
                }
                else{
                    System.out.println(o.toString());
                }
            }
        }
        
        public void dumpQuads(){
            quads.print();
        }
        
        public VariableEntry varInsert(Token t){
            VariableEntry v = new VariableEntry(t.getValue(),t.getType());
            if(globalLocal == GLOBAL){
                v.address = "_" + globalMemory;
                globalTable.insert(v);
                globalMemory++;
            }
            if(globalLocal == LOCAL){
                v.address = "%" + localMemory;
                localTable.insert(v);
                localMemory++;
            }
            return v;
        }
        
        public VariableEntry varInsert(String t,TokenType ty){
            VariableEntry v = new VariableEntry(t,ty);
            if(globalLocal == GLOBAL){
                v.address = "_" + globalMemory;
                globalTable.insert(v);
                globalMemory++;
            }
            if(globalLocal == LOCAL){
                v.address = "%" + localMemory;
                localTable.insert(v);
                localMemory++;
            }
            return v;
        }
        
        public SymbolTableEntry lookup(String id){
            SymbolTableEntry s;
            try {
                s = localTable.lookup(id);
            }
            catch(NullPointerException e){
                s = globalTable.lookup(id);
            }
            return s;
        }
        
        public VariableEntry create(TokenType type){
            String s = "$$Temp" + tempNum;
            tempNum++;
            VariableEntry v = this.varInsert(s,type);
            return v;
        }
        
        public int typeCheck(TypedEntry id1,TypedEntry id2){
            if(id1.type == REAL){
                if(id2.type == REAL){
                    return 1;
                }
                else{
                    return 2;
                }
            }
            else{
                if(id2.type == REAL){
                    return 3;
                }
                else{
                    return 0;
                }
            }
        }
        
        public VariableEntry constInsert(Token token){
            VariableEntry v;
            if(token.getType() == TokenType.REALCONSTANT){
                v = new VariableEntry(token.getValue(),REAL);
                v.address = "_" + globalMemory;
                globalMemory++;
                constantTable.insert(v);
            }
            else{
                v = new VariableEntry(token.getValue(),INTEGER);
                v.address = "_" + globalMemory;
                globalMemory++;
                constantTable.insert(v);
            }
            quads.generate("move",v.getName(),v.address);
            return v;
        }
        
        private void read(Stack<TypedEntry> s){
            while(!s.empty()){
                TypedEntry e = s.pop();
                if(e.type == TokenType.REAL){
                    quads.generate("finp",e.address);
                }
                else{
                    quads.generate("inp",e.address);
                }
            }
            parmCount.pop();
        }
        
        private void write(Stack<TypedEntry> s){
            while(!s.empty()){
                TypedEntry e = s.pop();
                if(e.type == TokenType.REAL){
                    quads.generate("foutp",e.address);
                }
                else{
                    quads.generate("outp",e.address);
                }
            }
            parmCount.pop();
        }
        
        private ArrayEntry getArray(){
            Stack<ArrayEntry> list = new Stack();
            for(Object o:semanticStack){
                if(o instanceof ArrayEntry){
                    list.push((ArrayEntry)o);
                }
            }
            if(list.empty()){
                System.out.println("array not found error");
                return new ArrayEntry("bad");
            }
            else{
                return list.pop();
            }
        }
        
        private CallableEntry getCallable(){
            Stack<CallableEntry> list = new Stack();
            for(Object o:semanticStack){
                if(o instanceof CallableEntry){
                    list.push((CallableEntry)o);
                }
            }
            if(list.empty()){
                System.out.println("Callable not found error");
                return new CallableEntry("bad");
            }
            else{
                return list.pop();
            }
        }
        
        private EList getEFalse(){
            Stack<EList> list = new Stack();
            for(Object o:semanticStack){
                if(o instanceof EList){
                    if(((EList)o).name.equals("EFalse")){
                        list.push((EList)o);
                    }
                }
            }
            if(list.empty()){
                System.out.println("EFalse not found error");
                return new EList("bad",0);
            }
            else{
                return list.pop();
            }
        }
        
        private EList getETrue(){
            Stack<EList> list = new Stack();
            for(Object o:semanticStack){
                if(o instanceof EList){
                    if(((EList)o).name.equals("ETrue")){
                        list.push((EList)o);
                    }
                }
            }
            if(list.empty()){
                System.out.println("ETrue not found error");
                return new EList("bad",0);
            }
            else{
                return list.pop();
            }
        }
        
        private String help39(Token t){
            String s = t.getValue();
            if(s.equals("1")){
                return "beq";
            }
            else if(s.equals("2")){
                return "bne";
            }
            else if(s.equals("3")){
                return "blt";
            }
            else if(s.equals("4")){
                return "bgt";
            }
            else if(s.equals("5")){
                return "ble";
            }
            else{
                return"bge";
            }
        }
        
        public String toString(){
            return quads.toString();
        }
}