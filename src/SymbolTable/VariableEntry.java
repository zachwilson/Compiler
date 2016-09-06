/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SymbolTable;

import lexicalanalizer.*;

public class VariableEntry extends TypedEntry {

        public boolean reserved;

/***********************************************************
  These variables are used later 
  
	boolean parm = false, functionResult = false, reserved = false;
**************************************************************/	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public VariableEntry(String Name) {
		super(Name);
                reserved = false;
	}
        
        public VariableEntry(String Name,TokenType Type){
            super(Name);
            type = Type;
            reserved = false;
        }

	public boolean isVariable() { 
		return true; 
	}
        
        public TokenType getType(){
            return type;
        }
	
	public void print () {
		
		System.out.println("Variable Entry:");
		System.out.println("   Name    : " + this.getName());
		System.out.println("   Type    : " + this.getType());
		System.out.println("   Address : " + this.getAddress());
		System.out.println();
	}
        
        public String toString(){
            String s;
            s = "VariableEntry," + this.name + "," + this.type + "," + this.address;
            return s;
        }

/***********************************************************

The methods below are not used until later

// A function result will be stored as a variable entry
	public boolean isFunctionResult() {
		return functionResult;
	}

	public void setFunctionResult() {
		this.functionResult = true;
	}
	
// this flag indicates if the variable is a parameter to a procedure or function
	public boolean isParameter() {
		return parm;
	}

// read, write, and main are reserved	
	public boolean isReserved() {
		return reserved;
	}

	public void setParameter (boolean parm) {
		this.parm = parm;
	}
	
	public void setParm() {
		this.parm = true;
	} 
	public void makeReserved() {
		this.reserved = true;
	} 
**************************************************************/

	
}