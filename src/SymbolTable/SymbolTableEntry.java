/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

/**
 *
 * @author Zach Wilson
 */
public class SymbolTableEntry {
    String name;
    
    public SymbolTableEntry(String n){
        name = n;
    }
    
    public String getName(){
        return this.name;
    }
    
    public boolean isArray(){
        return false;
    }
    
    public boolean isConstant(){
        return false;
    }
    
    public boolean isFunction(){
        return false;
    }
    
    public boolean isProcedure(){
        return false;
    }
    
    public boolean isVariable(){
        return false;
    }
    
    public boolean isTyped(){
        return false;
    }
    
    
}
