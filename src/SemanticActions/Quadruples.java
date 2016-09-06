/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;

import java.util.*;
import SymbolTable.*;
/**
 *
 * @author Zach Wilson
 */
public class Quadruples {
    private Vector<String[]> quads;
    private int nextQuad;
    
    
    public Quadruples(){
        nextQuad = 0;
        quads = new Vector();
    }
    
    public String getField(int quadIndex, int field){
        return quads.get(quadIndex)[field];
    }
    
    public void setField(int quadIndex,int field,String insert){
        quads.get(quadIndex)[field] = insert;
    }
    
    public int getNextQuad(){
        return nextQuad;
    }
    
    public void incrementNextQuad(){
        nextQuad ++;
    }
    
    public String[] getQuad(int index){
        return quads.get(index);
    }
    
    public void addQuad(String[] quad){
        quads.add(quad);
        this.incrementNextQuad();
    }
    
    public void print(){
        for(int i = 0; i < quads.size(); i++){
            String string = "";
            if(i != 0){
                string = string + i +": ";
            }
            for(int x = 0;x < quads.get(i).length;x++){
                String s = quads.get(i)[x];
                if (x > 1){
                    if(s != null){
                        string = string + ", " +  s;
                    }
                }
                else{
                    if(s != null){
                        string = string + s;
                        if(x == 0){
                            string += " ";
                        }
                    }
                }
            }
            string = string + "";
            System.out.println(string);
        }
    }
    
    public void generate(String s){
        String[] quad = {s,null,null,null};
        this.addQuad(quad);
    }
    
    public void generate(String s1,String s2){
        String[] quad = {s1,s2,null,null};
        this.addQuad(quad);
    }
    
    public void generate(String s1,String s2,String s3){
        String[] quad = {s1,s2,s3,null};
        this.addQuad(quad);
    }
    
    public void generate(String s1,String s2,String s3,String s4){
        String[] quad = {s1,s2,s3,s4};
        this.addQuad(quad);
    }
    
    public void generate(String s,VariableEntry v1){
        String[] quad = {s,v1.address,null,null};
        this.addQuad(quad);
    }
    
    public void generate(String s,VariableEntry v1,VariableEntry v2){
        String[] quad = {s,v1.address,v2.address,null};
        this.addQuad(quad);
    }
    
    public void generate(String s,VariableEntry v1,VariableEntry v2,VariableEntry v3){
        String[] quad = {s,v1.address,v2.address,v3.address};
        this.addQuad(quad);
    }
    
    public void generate(String s,VariableEntry v1,Integer i,VariableEntry v3){
        String[] quad = {s,v1.address,Integer.toString(i),v3.address};
        this.addQuad(quad);
    }
    
    public void backPatch(EList l,String p){
        List<Integer> i = l.list;
        for(int x:i){
            String[] q = quads.get(x);
            int y = 0;
            while(y < 4){
                if (q[y] == null){
                    q[y] = p;
                    break;
                }
                y++;
            }
        }
    }
    
    public void backPatch(int i,String p){
        String[] q = quads.get(i);
        int x = 0;
        while(x < 4){
            if (q[x] == null){
                q[x] = p;
                break;
            }
            x++;
        }
    }
    
    public String toString(){
        String total = "";
        for(int i = 0; i < quads.size(); i++){
            String string = "";
            if(i != 0){
                string = string + i +": ";
            }
            for(int x = 0;x < quads.get(i).length;x++){
                String s = quads.get(i)[x];
                if (x > 1){
                    if(s != null){
                        string = string + ", " +  s;
                    }
                }
                else{
                    if(s != null){
                        string = string + s;
                        if(x == 0){
                            string += " ";
                        }
                    }
                }
            }
            string = string + "\n";
            total += string;
        }
        return total;
    }
    
}
