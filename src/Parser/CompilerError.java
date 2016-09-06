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
public class CompilerError extends Exception {

    /**
     * Creates a new instance of <code>CompilerError</code> without detail
     * message.
     */
    public CompilerError() {
    }
    
    public CompilerError(String msg) {
        super(msg);
    }
}
