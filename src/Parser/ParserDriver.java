/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserDriver {
	
	Parser parser;
        public static String file = "phase2-1.pas";
	
	public ParserDriver(String filename)
	{
		parser = new Parser(filename);
	}

	protected void run() throws FileNotFoundException, UnsupportedEncodingException    
	{
		try
		{
			parser.parse();
		}
		
		catch (CompilerError ex)
		{
			System.out.println(ex + " at " + parser.getLine());
		}
		
                parser.dataDump();
		System.out.println("Compilation successful.");
                PrintWriter writer = new PrintWriter("out.tvi","utf-8");
                writer.write(parser.toString());
                writer.close();
	}


	public static void main(String[] args)
	{
		ParserDriver test = new ParserDriver(file);
                
                
            try {
                test.run();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ParserDriver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ParserDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
