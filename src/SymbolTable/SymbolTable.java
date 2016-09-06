/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import java.util.*;
import java.io.*;

/**
 *
 * @author Zach Wilson
 */
public class SymbolTable {
    
    Hashtable<String,SymbolTableEntry> table;
    
    public SymbolTable(int size){
        table = new Hashtable<String,SymbolTableEntry>(size);
    }
    
    public SymbolTableEntry lookup(String name){
        return table.get(name);
    }
    
    public void insert(SymbolTableEntry entry){
        table.put(entry.getName(),entry);
    }
    
    public void dumpTable(){
        System.out.println(table.toString());
    }
    
    public void installBuiltIns(){
        // I will improve this later.
        insert(new ProcedureEntry("main"));
        insert(new ProcedureEntry("read"));
        insert(new ProcedureEntry("write"));
    }
    
}
