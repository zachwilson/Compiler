/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.io.*;
import java.util.*;

/**
 *
 * @author Zach Wilson
 */

//This entire thing was a failed attempt create the parse table directly from the file.
public class ParseTableReader {
    
    private BufferedReader reader;
    
    
    public boolean start()
   {
      try
      {
         reader = new BufferedReader(new FileReader("parsetable-2const.dat"));
      }
      catch (Exception ex)
      {
         System.out.println(ex);
         ex.printStackTrace(System.out);
         reader = null;
      }
      return reader != null;
   }
    
    
    private Integer[] lineNums(String line){
        Integer[] hold = new Integer[38];
        int i = 0;
        int j = 0;
        char currChar;
        String temp = "";
        while(i < line.length()){
            currChar = line.charAt(i);
            if(currChar == ' '){
                if(temp != ""){
                    hold[j] = Integer.parseInt(temp);
                    temp = "";
                    j++;
                    i++;
                }
                else{
                    i++;
                }
            }
            else{
                temp = temp + currChar;
                i++;
            }
        }
        return hold;
        
    }
    
    public Integer[][] arrayInit(){
        start();
        Integer[][] hold = new Integer[35][];
        String temp = "";
        int i = 0;
        while(i < 35){
            try
            {
             reader.readLine();
             temp = reader.readLine();
            }
            catch(Exception ex){
                System.out.println(ex);
            }
            hold[i] = lineNums(temp);
            i++;
        }
        return hold;
    }
    
}
