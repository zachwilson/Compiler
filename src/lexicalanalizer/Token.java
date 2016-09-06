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
public class Token {
    private TokenType type;
    private String value;
    
    public Token(){
        super();
        this.type = TokenType.IDENTIFIER;
        this.value = "This is not supposed to be here";
    }
    
    public Token(TokenType type){
        super();
        this.type = type;
    }
    
    public TokenType getType(){
        return this.type;
    }
    
    public String getValue(){
        return this.value;
    }
    
    public void setValue(String ob){
        this.value = ob;
    }
   
    public void setType(TokenType t){
        this.type = t;
    }
    
    public String toString(){
        String s = "Token," + this.type + "," +this.value;
        return s;
    }
    
}
