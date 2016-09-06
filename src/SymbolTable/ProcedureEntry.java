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
public class ProcedureEntry extends CallableEntry{
    
    public ProcedureEntry(String n){
        super(n);
    }
    
    public boolean isProcedure(){
        return true;
    }
    
    public String toString(){
        String s = "ProcedureEntry," + name + "," + numberOfParameters + "," + parmInfo;
        return s;
    }
}
