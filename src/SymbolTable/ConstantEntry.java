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
import lexicalanalizer.*;

public class ConstantEntry extends SymbolTableEntry{
    TokenType type;
    
    public ConstantEntry(String n,TokenType t){
        super(n);
        type = t;
    }
    
}
