/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SymbolTable;

import lexicalanalizer.*;


public class SymbolTableDriver {

    public SymbolTableDriver() {
        super();
    }

    protected void run() {
        SymbolTable KeywordTable = new SymbolTable(17);
        SymbolTable GlobalTable = new SymbolTable(37);
        SymbolTable ConstantTable = new SymbolTable(37);

        // TODO: add read, write, main to global table

        // Develop and use a routine to fill the KeywordTable, if appropriate

        Tokenizer tokenizer =
                new Tokenizer("symtabtest.dat");

        Token token;

        try {
            token = tokenizer.getNextToken();

            while (!(token.getType() == TokenType.ENDOFFILE)) {

                if ((token.getType() == TokenType.INTCONSTANT) || (token.getType() == TokenType.REALCONSTANT)) {
                    // If the token is a constant, add it to constantTable
                    ConstantTable.insert(new ConstantEntry(token.getValue(), token.getType()));
                } else if (token.getType() == TokenType.IDENTIFIER) {

                    //  If it is an identifier add it to Global table
                    // as a variable entry
                    GlobalTable.insert(new VariableEntry(token.getValue(), token.getType()));

                }
                token = tokenizer.getNextToken();
            }
        } catch (LexicalError ex) {
            System.err.println(ex);
        }

//		KeywordTable.dumpTable();
        GlobalTable.dumpTable();
        ConstantTable.dumpTable();
    }


    public static void main(String[] args) {
        SymbolTableDriver test = new SymbolTableDriver();
        test.run();
    }


}