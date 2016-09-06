/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import java.util.Stack;

/**
 *
 * @author Zach Wilson
 */
public class CallableEntry extends SymbolTableEntry{
    public int numberOfParameters;
    public Stack<TypedEntry> parmInfo;
    
    public CallableEntry(String s){
        super(s);
        parmInfo = new Stack<TypedEntry>();
        numberOfParameters = 0;
    }
    
    
}
