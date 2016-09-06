/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import java.util.*;
/**
 *
 * @author Zach Wilson
 */
public class FunctionEntry extends CallableEntry{
    public VariableEntry result;
    
    public FunctionEntry(String n){
        super(n);
    }
    
    public boolean isFunction(){
        return true;
    }
    
    public String toString(){
        String s = "FunctionEntry," + name + "," + numberOfParameters + "," + parmInfo + ",[" + result + "]";
        return s;
    }
    
}
