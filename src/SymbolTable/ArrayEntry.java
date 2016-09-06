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

public class ArrayEntry extends TypedEntry{
    
    public int upperBound;
    public int lowerBound;
    
    
    public ArrayEntry(String s){
        super(s);
    }
    
    public boolean isArray(){
        return true;
    }
    
    public String toString(){
        String s = "ArrayEntry," + name + "," + lowerBound + "," + upperBound + "," + type + "," + address;
        return s;
    }
    
}
