/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

/**
 *
 * @author Zach Wilson
 */

import lexicalanalizer.*;
public class ParserError extends CompilerError {

    /**
     * Creates a new instance of <code>ParserError</code> without detail
     * message.
     */
    public ParserError() {
    }

    /**
     * Constructs an instance of <code>ParserError</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ParserError(String msg) {
        super(msg);
    }
    
    
    public static ParserError tokenError(TokenType x, Token y){
        String str = "Expecting " + x + "," + y.getType();
        return new ParserError(str);
    }
    
    public static ParserError tableError(Token x){
        String str = "Unexpected :" + x + " at ";
        return new ParserError(str);
    }
    
}