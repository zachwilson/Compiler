/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;
import SymbolTable.*;
/**
 *
 * @author Zach Wilson
 */
import Parser.CompilerError;

public class SemanticError extends CompilerError {
    
    public SemanticError(){
    }
    
    public SemanticError(String msg){
        super(msg);
    }
    
    public static SemanticError needRelationalError(){
        String str = "Need Relational Statement";
        return new SemanticError(str);
    }
    
    public static SemanticError idNotFoundError(String s){
        String str = "id :" + s + " not found";
        return new SemanticError(str);
    }
    
    public static SemanticError needArithmeticError(){
        String str = "Need Arithmetic Statement";
        return new SemanticError(str);
    }
    
    public static SemanticError realToIntError(){
        String str = "Cannot store real as integer";
        return new SemanticError(str);
    }
    
    public static SemanticError needArrayError(SymbolTableEntry id){
        String str = "" + id + " is not an array";
        return new SemanticError(str);
    }
    
    public static SemanticError needIntError(SymbolTableEntry id){
        String str = "" + id.getName() + " is not an Int";
        return new SemanticError(str);
    }
    
    public static SemanticError needParamError(SymbolTableEntry id){
        String str = "" + id.getName() + " needs parameters";
        return new SemanticError(str);
    }
    
    public static SemanticError badParamError(SymbolTableEntry id){
        String str = id.getName() + " is not a valid parameter";
        return new SemanticError(str);
    }
    
    public static SemanticError manyParamError(SymbolTableEntry id){
        String str =id.getName() + " is being given too many parameters";
        return new SemanticError(str);
    }
    
    public static SemanticError wrongParamError(SymbolTableEntry id){
        String str =id.getName() + " does not match given parameter type";
        return new SemanticError(str);
    }
    
    public static SemanticError arraySizeParamError(SymbolTableEntry id){
        String str =id.getName() + " does not match given array sizes";
        return new SemanticError(str);
    }
    
    public static SemanticError undecVarError(String id){
        String str =id + " is not declared";
        return new SemanticError(str);
    }
    
    public static SemanticError notFuncError(SymbolTableEntry id){
        String str =id.getName() + " is not a function";
        return new SemanticError(str);
    }
    
    public static SemanticError wrongNumParamError(SymbolTableEntry id){
        String str =id.getName() + " is being given the wrong number of parameters";
        return new SemanticError(str);
    }
    
    public static SemanticError currFuncError(SymbolTableEntry id){
        String str =id.getName() + " is not the current function";
        return new SemanticError(str);
    }
    
    public static SemanticError notProcError(SymbolTableEntry id){
        String str =id.getName() + " is not a Procedure";
        return new SemanticError(str);
    }
    
}
