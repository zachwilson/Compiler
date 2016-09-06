/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalanalizer;

public class TokenizerDriver
{

	public TokenizerDriver()
	{
		super();
	}

	protected void run() throws LexicalError   // change this function to call GetNextToken until all tokens are read
	{
		Tokenizer tokenizer = 
			new Tokenizer("SemanticTest2.dat");  

		Token token = new Token();

		token =	tokenizer.getNextToken();

		while (!(token.getType() == TokenType.ENDOFFILE))
		{
			System.out.print("Recognized Token:  " + token.getType());
			if ((token.getType() == TokenType.IDENTIFIER) || (token.getType() == TokenType.REALCONSTANT) 
					|| (token.getType() == TokenType.INTCONSTANT) )
				System.out.print(" Value : " + token.getValue());
			else if ((token.getType() == TokenType.RELOP)
					|| (token.getType() == TokenType.ADDOP) || (token.getType() == TokenType.MULOP))
				System.out.print(" OpType : " + token.getValue());
			System.out.println();

			token = tokenizer.getNextToken();
		}
	}


	public static void main(String[] args) throws LexicalError
	{
		TokenizerDriver test = new TokenizerDriver();
		test.run();
	}
}
