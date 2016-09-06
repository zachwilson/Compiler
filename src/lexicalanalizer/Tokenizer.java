/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalanalizer;

/**
 *
 * @author Zach Wilson
 */

import java.util.*;
import java.io.*;

public class Tokenizer {
    
    private Queue<Character> queue = new LinkedList<Character>();
    
    private CharStream stream = null;
    
    private Token prevToken = null;
    
    public Tokenizer(String file){
        super();
        stream = new CharStream(file);
    }
    
    // gets the next token
    public Token getNextToken() throws LexicalError{
        Token tok = assembler();
        if(tok.getType().equals(TokenType.IDENTIFIER)){
            identCheck(tok);
        }
        prevToken = tok;
        return tok;
    }
    
    
    // assembles the token, all keywords are expressed as identifiers
    public Token assembler() throws LexicalError{
        // I use Character.toLowerCase so upper and lower case letters will be used the same.
        Character curr = Character.toLowerCase(stream.currentChar());
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String nums = "1234567890";
        
        if (curr.equals(' ')){
            curr = Character.toLowerCase(stream.currentChar());
        }
        
        if (curr.equals('=')){
            Token tok = new Token(TokenType.RELOP);
            tok.setValue("1");
            return tok;
        }
        if (curr.equals('*')){
            Token tok = new Token(TokenType.MULOP);
            tok.setValue("1");
            return tok;
        }
        if (curr.equals('/')){
            Token tok = new Token(TokenType.MULOP);
            tok.setValue("2");
            return tok;
        }
        if (curr.equals(',')){
            Token tok = new Token(TokenType.COMMA);
            return tok;
        }
        if (curr.equals(';')){
            Token tok = new Token(TokenType.SEMICOLON);
            return tok;
        }
        if (curr.equals('(')){
            Token tok = new Token(TokenType.LEFTPAREN);
            return tok;
        }
        if (curr.equals(')')){
            Token tok = new Token(TokenType.RIGHTPAREN);
            return tok;
        }
        if (curr.equals('[')){
            Token tok = new Token(TokenType.LEFTBRACKET);
            return tok;
        }
        if (curr.equals(']')){
            Token tok = new Token(TokenType.RIGHTBRACKET);
            return tok;
        }
        if (curr.equals('[')){
            Token tok = new Token(TokenType.LEFTBRACKET);
            return tok;
        }
        if (curr.equals(CharStream.EOF)){
            Token tok = new Token(TokenType.ENDOFFILE);
            return tok;
        }
        if (curr.equals('<')){
            curr = Character.toLowerCase(stream.currentChar());
            if(curr.equals('=')){
                Token tok = new Token(TokenType.RELOP);
                tok.setValue("5");
                return tok;
                
            }
            if(curr.equals('>')){
                Token tok = new Token(TokenType.RELOP);
                tok.setValue("2");
                return tok;
                
            }
            stream.pushBack(curr);
            Token tok = new Token(TokenType.RELOP);
            tok.setValue("3");
            return tok;
            
            
        }
        if (curr.equals('>')){
            curr = Character.toLowerCase(stream.currentChar());
            if(curr.equals('=')){
                Token tok = new Token(TokenType.RELOP);
                tok.setValue("6");
                return tok;  
            }
            stream.pushBack(curr);
            Token tok = new Token(TokenType.RELOP);
            tok.setValue("4");
            return tok;
        }
        
        if(curr.equals(':')){
            curr = Character.toLowerCase(stream.currentChar());
            if(curr.equals('=')){
                Token tok = new Token(TokenType.ASSIGNOP);
                return tok;
            }
            stream.pushBack(curr);
            return new Token(TokenType.COLON);
        }
        
        if(curr.equals('.')){
            curr = Character.toLowerCase(stream.currentChar());
            if(curr.equals('.')){
                Token tok = new Token(TokenType.DOUBLEDOT);
                return tok;
            }
            stream.pushBack(curr);
            return new Token(TokenType.ENDMARKER);
        }
        
        if(curr.equals('+')){
            if(!(prevToken.getType().equals(TokenType.RIGHTBRACKET) ||
                 prevToken.getType().equals(TokenType.RIGHTPAREN)   ||
                 prevToken.getType().equals(TokenType.IDENTIFIER)   || 
                 prevToken.getType().equals(TokenType.REALCONSTANT) || 
                 prevToken.getType().equals(TokenType.INTCONSTANT))){
                    return new Token(TokenType.UNARYPLUS);
            }
            Token tok = new Token(TokenType.ADDOP);
            tok.setValue("1");
            return tok;
        }
        if(curr.equals('-')){
            if(!(prevToken.getType().equals(TokenType.RIGHTBRACKET) ||
                 prevToken.getType().equals(TokenType.RIGHTPAREN)   ||
                 prevToken.getType().equals(TokenType.IDENTIFIER)   || 
                 prevToken.getType().equals(TokenType.REALCONSTANT) || 
                 prevToken.getType().equals(TokenType.INTCONSTANT))){
                    return new Token(TokenType.UNARYMINUS);
            }
            Token tok = new Token(TokenType.ADDOP);
            tok.setValue("2");
            return tok;
        }
        if(nums.indexOf(curr) != -1){
            queue.add(curr);
            return numAssembler();
        }
        if(alpha.indexOf(curr) != -1){
            queue.add(curr);
            return identAssembler();
        }
        
        if(curr.equals('}')){
            throw LexicalError.BadRCurly(stream.lineNumber);
        }
        
        Token tok = new Token(TokenType.IDENTIFIER);
        String str = "What about this char" + curr;
        tok.setValue(str);
        return tok;
 
    }
    
    // Takes all of the characters in the queue and turns them into a string.
    private String queueDump(){
        String ret = "";
        while (!queue.isEmpty()){
            ret = ret + queue.poll();
            
        }
        return ret;
    }
    
    
    // produces an identifier from a set of letters and numbers
    private Token identAssembler() throws LexicalError{
      String identChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

      Character curr = Character.toLowerCase(stream.currentChar());
      while(true){
          if (identChars.indexOf(curr) == -1){
              Token tok =  new Token(TokenType.IDENTIFIER);
              tok.setValue(this.queueDump());
              stream.pushBack(curr);
              return tok;
              
          }
          else if(Character.isWhitespace(curr)){
              Token tok =  new Token(TokenType.IDENTIFIER);
              tok.setValue(this.queueDump());
              return tok;
          }
          
          queue.add(curr);
          curr = Character.toLowerCase(stream.currentChar());
      }


    }
    
    
    // Finds out if the constant is an int or a real and creates the value
    private Token numAssembler() throws LexicalError{
        Character curr = Character.toLowerCase(stream.currentChar());
        String nums = "1234567890";
        while(true){
            if(curr.equals('e')){
                stream.pushBack(curr);
                return realAssembler();
            }
            if(curr.equals('.')){
                curr = Character.toLowerCase(stream.currentChar());
                if(curr.equals('.')){
                    stream.pushBack('.');
                    stream.pushBack(curr);
                    Token tok = new Token(TokenType.INTCONSTANT);
                    tok.setValue(this.queueDump());
                    return tok;
                }
                if(nums.indexOf(curr) == -1){
                    queue.add('.');
                    queue.add(curr);
                    throw LexicalError.PoorConstant(this.queueDump(),stream.lineNumber);
                }
                stream.pushBack(curr);
                queue.add('.');
                return realAssembler();
            }
            if(nums.indexOf(curr) == -1){
                stream.pushBack(curr);
                Token tok = new Token(TokenType.INTCONSTANT);
                tok.setValue(this.queueDump());
                return tok;
            }
            queue.add(curr);
            curr = Character.toLowerCase(stream.currentChar());
        
        }
    }
    
    
    // Creates a REALCONSTANT Token, Assumes value in queue has been proven to be real.
    private Token realAssembler() throws LexicalError{
        Character curr = Character.toLowerCase(stream.currentChar());
        String nums = "1234567890";
        Boolean hasE = false;
        while(true){
            // If the current char is e and the program has already put an e into the queue the REALCONSTANT is finished
            if(curr.equals('e') && (hasE)){
                stream.pushBack(curr);
                Token tok = new Token(TokenType.REALCONSTANT);
                tok.setValue(this.queueDump());
                return tok;
            }
            // If the current char is not e or a number the REALCONSTANT is finished
            if((nums.indexOf(curr) == -1) && (!curr.equals('e'))){
                stream.pushBack(curr);
                Token tok = new Token(TokenType.REALCONSTANT);
                tok.setValue(this.queueDump());
                return tok;
            }
            
            // if the function has not seen an e yet set that it has seen an e, The e will be put into the queue later.
            if(curr.equals('e')){
                hasE = true;
                curr = Character.toLowerCase(stream.currentChar());
                if(nums.indexOf(curr) == -1){
                    queue.add('e');
                    queue.add(curr);
                    throw LexicalError.PoorConstant(this.queueDump(),stream.lineNumber);
                }
                queue.add('e');
                queue.add(curr);
                curr = Character.toLowerCase(stream.currentChar());
            }
            else{
            
                // now curr has to either be a number or e so we put it into the queue and load the next char
                queue.add(curr);
                curr = Character.toLowerCase(stream.currentChar());
            }
        }
        
    }
    
    // Used for testing
    private void dumpTest(){
        queue.add('$');
        Character curr = queue.poll();
        while(! curr.equals('$')){
            System.out.print(curr);
            queue.add(curr);
            curr = queue.poll();
        }
    }
    
    
    // Changes identifiers into keywords if possible
    private void identCheck(Token tok){
        String val = (String) tok.getValue();
        if (val.equals("program")){
            tok.setType(TokenType.PROGRAM);
        }
        else if (val.equals("procedure")){
            tok.setType(TokenType.PROCEDURE);
        }
        else if (val.equals("begin")){
            tok.setType(TokenType.BEGIN);
        }
        else if (val.equals("end")){
            tok.setType(TokenType.END);
        }
        else if (val.equals("else")){
            tok.setType(TokenType.ELSE);
        }
        else if (val.equals("var")){
            tok.setType(TokenType.VAR);
        }
        else if (val.equals("function")){
            tok.setType(TokenType.FUNCTION);
        }
        else if (val.equals("result")){
            tok.setType(TokenType.RESULT);
        }
        else if (val.equals("real")){
            tok.setType(TokenType.REAL);
        }
        else if (val.equals("if")){
            tok.setType(TokenType.IF);
        }
        else if (val.equals("integer")){
            tok.setType(TokenType.INTEGER);
        }
        else if (val.equals("and")){
            tok.setType(TokenType.MULOP);
            tok.setValue("5");
        }
        else if (val.equals("array")){
            tok.setType(TokenType.ARRAY);
        }
        else if (val.equals("of")){
            tok.setType(TokenType.OF);
        }
        else if (val.equals("or")){
            tok.setType(TokenType.ADDOP);
            tok.setValue("3");
        }
        else if (val.equals("then")){
            tok.setType(TokenType.THEN);
        }
        else if (val.equals("while")){
            tok.setType(TokenType.WHILE);
        }
        else if (val.equals("do")){
            tok.setType(TokenType.DO);
        }
        else if (val.equals("div")){
            tok.setType(TokenType.MULOP);
            tok.setValue("3");
        }
        else if (val.equals("not")){
            tok.setType(TokenType.NOT);
        }
        else if (val.equals("mod")){
            tok.setType(TokenType.MULOP);
            tok.setValue("4");
        }
        
    }
    
    public int getLine(){
        return stream.lineNumber;
    }
    
}
