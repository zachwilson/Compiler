/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SemanticActions;

import java.util.*;
/**
 *
 * @author Zach Wilson
 */
public class EList {
    public String name;
    public LinkedList<Integer> list;
    
    public EList(String s,int i){
        name = s;
        LinkedList<Integer> l = new LinkedList();
        l.add(i);
        list = l;
    }
    
    public EList (String s,LinkedList l){
        name = s;
        list = l;
    }
    
    public String toString(){
        String s = name + list.toString();
        return s;
    }
    
}
