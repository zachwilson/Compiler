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
import java.util.*;
import java.math.*;
import SymbolTable.*;
import SemanticActions.*;


public class Parser {
    private Tokenizer tokenizer;
    private Stack<GrammarSymbol> stack = new Stack<GrammarSymbol>();
    private static final int ERROR = 999;
    private ParseTableReader table = new ParseTableReader();
    private Integer[][] parseTable = table.arrayInit();
    private RHSTable rhsTable = new RHSTable();
    private SemanticActions semanticActions = new SemanticActions();
    private Token holdToken;
    
    
    public Parser(String file){
        super();
        tokenizer = new Tokenizer(file);
    }
    
    public void parse() throws CompilerError{
        stack.push(TokenType.ENDOFFILE);
        stack.push(NonTerminal.Goal);
        GrammarSymbol predicted;
        Token currToken = tokenizer.getNextToken();
        while(!(stack.empty())){
            predicted = stack.pop();
            if(predicted.isToken()){
                if(predicted.getIndex() == currToken.getType().getIndex()){
                    holdToken = currToken;
                    currToken = tokenizer.getNextToken();
                }
                else{
                    TokenType t = (TokenType) predicted;
                    throw ParserError.tokenError(t, currToken);
                }
            }
            else if(predicted.isAction()){
                semanticActions.Execute((SemanticAction)predicted,holdToken);
                
            }
            else{
                int tableNum = Math.abs(parseTable[currToken.getType().getIndex()][predicted.getIndex()]);
                if(tableNum == ERROR){
                    throw ParserError.tableError(currToken);
                }
                else{
                    pushAll(rhsTable.getRule(tableNum));
                    
                }
            }
        }
    }
    
    public void pushAll(GrammarSymbol[] array){
        int i = (array.length - 1);
        while(i > -1){
            stack.push(array[i]);
            i--;
        }
    }
    
    public void dataDump(){
        semanticActions.dumpQuads();
    }
    
    public int getLine(){
        return tokenizer.getLine();
    }
    
    public String toString(){
        return semanticActions.toString();
    }
    
    
}
