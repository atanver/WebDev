/*CSCI 489 - Milepost #1
Devin Truax
Cynthia Padron
Aasimah Tanveer
*/

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;

public class Lexi {
	
	//Globals
	public static ArrayList<Token> tokens = new ArrayList<Token>();
	public static ArrayList<Token> variables = new ArrayList<Token>();
	public static ArrayList<String> errors = new ArrayList<String>();
	public static String abPath;
	
	public static enum TokenType {
	//Types of Tokens
		CONST("-?[0-9]+"), 
		WHITESPACE("[ \t\f\r]+"),
		NEWLINE("[\n]"),
    
		//Keywords
		READ("read"),
		WRITE("write"),
		IF("if"),
		THEN("then"),
		ELSE("else"),
		FI("fi"),
		TO("to"),
		DO("do"),
		LOOP("loop"),
		ENDLOOP("endloop"),
		DECLARE("declare"),
		ENDDECLARE("enddeclare"),
		INTEGER("integer"),
		
		SEMI(";"), 
		COMMA(","),
		ASGN(":="),
		PLUS("[+]"),
		COMMENT("--.*"),
		MINUS("-"),
		STAR("[*]"),
		DVD("/"),
		EQR("="),
		LER("<="),
		GER(">="),
		GTR(">"),
		LTR("<"),
		NER("#"),
    	LPAR("[(]"),
    	RPAR("[)]"),
		IDR("((\\w+)|(a-zA-Z_0-9))+"),
		APO("'");

		public final String pattern;
    
		//Codes
		public final static int T_IDR = 1;
		public final static int T_CONST = 2;
		
		//Keywords
		public final static int T_READ = 3;
		public final static int T_WRITE = 4;
		public final static int T_IF = 5;
		public final static int T_THEN = 6;
		public final static int T_ELSE = 7;
		public final static int T_FI = 8;
		public final static int T_TO = 9;
		public final static int T_LOOP = 10;
		public final static int T_ENDLOOP = 11;
		public final static int T_DECLARE = 12;
		public final static int T_ENDDECLARE = 13;
		public final static int T_INTEGER = 14;
		
		public final static int T_SEMI = 15;
		public final static int T_COMMA = 16;
		public final static int T_ASGN = 17;
		public final static int T_PLUS = 18;
		public final static int T_MINUS = 19;
		public final static int T_STAR = 20;
		public final static int T_DVD = 21;
		public final static int T_EQR = 22;
		public final static int T_GTR = 23; //>
		public final static int T_LTR = 24; //<
		public final static int T_LER = 25; // <=
		public final static int T_GER = 26; // >=
		public final static int T_NER = 27;
		public final static int T_LPAR = 28;
		public final static int T_RPAR = 29;
		public final static int T_APO = 30;
		public final static int T_COMMENT = 31;
		public final static int T_NEWLINE = 32;
		
		private TokenType(String pattern) {
			this.pattern = pattern;
		}
		public String getPattern(){
			return pattern;
		}
	}//End TokenType

	//Token Class
	public static class Token {
		public int type;
		public String data;

		public Token(int type, String data) {
			this.type = type;
			this.data = data;
		}
		public String getData(){
			return data;
		}
		public int getType(){
			return type;
		}
		@Override
		public String toString() {
			return String.format(" %d %s ", type, data);
		}
	}//End Token Class	
	
	//Scanner
	public static ArrayList<Token> lex(String input) throws IOException {

		// Buffer
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for (TokenType tokenType : TokenType.values())
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

		// Matching
		Matcher matcher = tokenPatterns.matcher(input);
		
		//Variables used
		int counter = 1;
		int lineCounter = 1;
		int errorCounter = 0;
		String T_counter = null;
		
		//Searches for match
		while (matcher.find()) {
			if (matcher.group(TokenType.COMMENT.name()) != null) { 
				tokens.add(new Token(TokenType.T_COMMENT, ""));
				continue;
			} 
			else if (matcher.group(TokenType.CONST.name()) != null){
				tokens.add(new Token(TokenType.T_CONST, matcher.group(TokenType.CONST.name())));
				continue;
			}
			else if (matcher.group(TokenType.READ.name()) != null) {
				tokens.add(new Token(TokenType.T_READ, ""));
				continue;
			}
			else if (matcher.group(TokenType.WRITE.name()) != null) {
				tokens.add(new Token(TokenType.T_WRITE, ""));
				continue;
			}
			else if (matcher.group(TokenType.IF.name()) != null) {
				tokens.add(new Token(TokenType.T_IF, ""));
				continue;
			}
			else if (matcher.group(TokenType.THEN.name()) != null) {
				tokens.add(new Token(TokenType.T_THEN, ""));
				continue;
			}
			else if (matcher.group(TokenType.ELSE.name()) != null) {
				tokens.add(new Token(TokenType.T_ELSE, ""));
				continue;
			}
			else if (matcher.group(TokenType.FI.name()) != null) {
				tokens.add(new Token(TokenType.T_FI, ""));
				continue;
			}
			else if (matcher.group(TokenType.TO.name()) != null) {
				tokens.add(new Token(TokenType.T_TO, ""));
				continue;
			}
			else if (matcher.group(TokenType.LOOP.name()) != null) {
				tokens.add(new Token(TokenType.T_LOOP, ""));
				continue;
			}
			else if (matcher.group(TokenType.ENDLOOP.name()) != null) {
				tokens.add(new Token(TokenType.T_ENDLOOP, ""));
				continue;
			}
			else if (matcher.group(TokenType.DECLARE.name()) != null){
				tokens.add(new Token(TokenType.T_DECLARE, ""));
				continue;
			}
			else if (matcher.group(TokenType.ENDDECLARE.name()) != null){
				tokens.add(new Token(TokenType.T_ENDDECLARE, ""));
				continue;
			}
			else if (matcher.group(TokenType.INTEGER.name()) != null) {
				tokens.add(new Token(TokenType.T_INTEGER, ""));
				continue;
			}
			else if (matcher.group(TokenType.IDR.name()) != null) { //Identifier
		        String varName = matcher.group(TokenType.IDR.name());
		        boolean found = false;
		        if (variables.size() == 0){ //First Variable
					tokens.add(new Token(TokenType.T_IDR, T_counter = Integer.toString(counter)));
					variables.add(new Token(counter, varName));
					counter ++;
		        }
		       else {
					for (int j = 0; j < variables.size(); ++j){
						if (varName.equals(variables.get(j).getData())){ //Duplicate Variable
							found = true;
							tokens.add(new Token(TokenType.T_IDR, Integer.toString(variables.get(j).getType())));
							errors.add(new String ("Line " +lineCounter+ ": Duplicate variable."));
							errorCounter++;
						}
					}
					if(found == false){
						tokens.add(new Token(TokenType.T_IDR, T_counter = Integer.toString(counter)));
						variables.add(new Token(counter, varName));
						counter++;
					}
				}	
		        continue;
			}//End else if for IDR
			else if (matcher.group(TokenType.SEMI.name()) != null) {
				tokens.add(new Token(TokenType.T_SEMI, ""));
				continue;
			}
			else if (matcher.group(TokenType.COMMA.name()) != null) {
				tokens.add(new Token(TokenType.T_COMMA, ""));
				continue;
			}
			else if (matcher.group(TokenType.ASGN.name()) != null) {
				tokens.add(new Token(TokenType.T_ASGN, ""));
				continue;
			}
			else if (matcher.group(TokenType.PLUS.name()) != null) {
				tokens.add(new Token(TokenType.T_PLUS, ""));
				continue;
			} 
			else if (matcher.group(TokenType.MINUS.name()) != null) {
				tokens.add(new Token(TokenType.T_MINUS, ""));
				continue;
			}
			else if (matcher.group(TokenType.STAR.name()) != null) {
				tokens.add(new Token(TokenType.T_STAR, ""));
				continue;
			}
			else if (matcher.group(TokenType.DVD.name()) != null) {
				tokens.add(new Token(TokenType.T_DVD, ""));
				continue;
			}
			else if (matcher.group(TokenType.EQR.name()) != null) {
				tokens.add(new Token(TokenType.T_EQR, ""));
				continue;
			}
			else if (matcher.group(TokenType.GTR.name()) != null) {
				tokens.add(new Token(TokenType.T_GTR, ""));
				continue;
			}
			else if (matcher.group(TokenType.LTR.name()) != null) {
				tokens.add(new Token(TokenType.T_LTR, ""));
				continue;
			}
			else if (matcher.group(TokenType.LER.name()) != null) {
				tokens.add(new Token(TokenType.T_LER, ""));
				continue;
			}
			else if (matcher.group(TokenType.GER.name()) != null) {
				tokens.add(new Token(TokenType.T_GER, ""));
				continue;
			}
			else if (matcher.group(TokenType.NER.name()) != null) {
				tokens.add(new Token(TokenType.T_NER, ""));
				continue;
			}
			else if (matcher.group(TokenType.LPAR.name()) != null) {
				tokens.add(new Token(TokenType.T_LPAR, ""));
				continue;
			}
			else if (matcher.group(TokenType.RPAR.name()) != null) {
				tokens.add(new Token(TokenType.T_RPAR, ""));
				continue;
			}	 
			else if (matcher.group(TokenType.APO.name()) != null){
				tokens.add(new Token(TokenType.T_APO, ""));
				continue;
			}
			else if (matcher.group(TokenType.WHITESPACE.name()) != null)
				continue;
			else if (matcher.group(TokenType.NEWLINE.name()) != null){
				if (errorCounter == 0){ //If there are no errors in that line
					errors.add(new String ("Line " +lineCounter+ ": Okay."));
				}
				tokens.add(new Token(TokenType.T_NEWLINE, "\n"));
				lineCounter++; // Increments line counter
				errorCounter = 0; //Sets error counter back to zero for next line
				continue;
			}
			else{
				errors.add(new String ("Line " +lineCounter+ ": Illegal Token."));
				errorCounter++;
				continue;
			}
		}//End of while loop
		
		//Writes symbol table to separate file
		PrintWriter writerST = new PrintWriter("C:/Users/cpadr_000/Desktop/symTable.txt", "UTF-8");
  		System.out.println("Symbol Table:");
  		for (Token token : variables){
  			writerST.println(token.getType() + " " + token.getData());
  			System.out.println(token.getType() + " " + token.getData());
  		}
  		writerST.close();
		return tokens;
	}//End of for loop
  
	//Reads file
	static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	    	StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } 
	    finally {
	        br.close();
	    }
	}//End readFile
  	
  	public static void main(String[] args) throws IOException {
  		//Scanner reader = new Scanner(System.in);
  		
  		System.out.println("WELCOME! This is Cynthia and Aasimah's interpreter for CSCI 489!"
  				+ "\nThese programs aim is to make your code executable." 
  				+ "\nFirst, write the absolute pathname to your desktop."
  				+ "\nThis is where all of the files produced from these programs will be kept."
  				+ "\n(e.g. C:/Users/cpadr_000/Desktop)");
  		//abPath = reader.next();
  		
  		System.out.println("Next, write the absolute pathname to your code file, including the extension."
  				+ "\nP.S. Make sure it's on your desktop for a short absolute pathname!"
  				+ "\n(e.g. C:/Users/cpadr_000/Desktop/loopProg.txt");
  		//String filePath = reader.next();
  		
  		System.out.println("Lets begin!");
  		
  		//PrintWriter writer = new PrintWriter(abPath+"/scanOutput.txt", "UTF-8");
		
  		PrintWriter writer = new PrintWriter("C:/Users/cpadr_000/Desktop/scanOutput.txt", "UTF-8");
		//Outputs errors
		System.out.print("Any Errors?: ");
		if (errors.isEmpty()) {
			System.out.println("None!");
		}
		else {
			for (String errorMsg: errors){
				System.out.println(errorMsg);
			}
		}
		
  		//Input is the file that is being scanned
  		//String input = readFile(filePath); 
  		
  		String input = readFile("C:/Users/cpadr_000/Desktop/loopProg.txt");
		//Creates tokens and prints them
  		ArrayList<Token> tokens = lex(input);
  		System.out.println("Integer Code Output:");
  		for (Token token : tokens){
  			String data = token.getData();
  			int type = token.getType();
  			if (data.equals("\n")|| type == 31){ //Skips comments and new line code
  				writer.println();
  				System.out.println();
  				continue;
  			}
  			//reader.close();
  			writer.print(token);
  			System.out.print(token);
  		}
  		writer.close();
  	}//End of main
}//End of program
