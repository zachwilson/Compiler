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

import Parser.CompilerError;

public class LexicalError extends CompilerError {

    /**
     * Creates a new instance of <code>LexicalError</code> without detail
     * message.
     */
    public LexicalError() {
    }

    /**
     * Constructs an instance of <code>LexicalError</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LexicalError(String msg) {
        super(msg);
    }
    
    public static LexicalError IllegalCharacter(char ch,int lineNumber){
        String str = "Illegal Character" + ch + "at line" + lineNumber;
        return new LexicalError(str);
    }
    
    public static LexicalError PoorConstant(String s,int line){
        String str = s + " is an ill-formed constant at " + line;
        return new LexicalError(str);
    }
    
    public static LexicalError BadRCurly(int line){
        return new LexicalError("illegal placement of } at " + line);
    }
    
    public static LexicalError BadComment(int lineNumber){
        String str = "Cannot include { in a comment Line:" + lineNumber;
        return new LexicalError(str);
    }
    
    public static LexicalError UnterminatedComment(int lineNumber){
        String str = "Comment does not terminate Line:" + lineNumber;
        return new LexicalError(str);
    }
    
    
}
