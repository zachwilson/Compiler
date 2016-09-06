/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;
import lexicalanalizer.*;
/**
 *
 * @author Zach Wilson
 */
public class TypedEntry extends SymbolTableEntry{
    public TokenType type;
    public String address;
    public boolean isParam;
    
    public TypedEntry(String s){
        super(s);
        isParam = false;
    }
    
    
    public boolean isTyped(){
        return true;
    }
    
}
