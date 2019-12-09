import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*CSCI 489 - Milepost #3
Cynthia Padron
Aasimah Tanveer
Devin Truax 
*/

public class ICG3{
    
	//Codes
	public final static int IDR = 1;
	public final static int CONST = 2;
	public final static int READ = 3;
	public final static int WRITE = 4;
	public final static int IF = 5;
	public final static int THEN = 6;
	public final static int ELSE = 7;
	public final static int FI = 8;
	public final static int TO = 9;
	public final static int LOOP = 10;
	public final static int ENDLOOP = 11;
	public final static int DECLARE = 12;
	public final static int ENDDECLARE = 13;
	public final static int INTEGER = 14;
	
	public final static int SEMI = 15;
	public final static int COMMA = 16;
	public final static int ASGN = 17;
	public final static int PLUS = 18;
	public final static int MINUS = 19;
	public final static int STAR = 20;
	public final static int DVD = 21;
	public final static int EQR = 22;
	public final static int GTR = 23;
	public final static int LTR = 24;
	public final static int LER = 25;
	public final static int GER = 26;
	public final static int NER = 27;
	public final static int LPAR = 28;
	public final static int RPAR = 29;
	public final static int APO = 30;
	public final static int COMMENT = 31;
	public final static int NEWLINE = 32;
	
	public static int current;
	public static Scanner file;
	public static PrintWriter writerP3;
	public static boolean leftCheck, rightCheck, declFlag;
	public static int leftCount, rightCount;
	
	//PHASE 3 GLOBALS
	public static String postfix[] = new String[60];
	public static int pfCount = 0, save1, save2, save3, lineCount;
	public static String code;
	
	//ICG
	public static void main (String[] args) throws IOException {
		// File input is the scanner's output, assuming we've checked it with the parser
		// and it is grammatically correct
		file = new Scanner(new File("/Users/cpadr_000/Desktop/scanOutput.txt"));
		
		//WriterP3 is the txt file that will output the postfix codes
		PrintWriter writerP3;
		writerP3 = new PrintWriter("/Users/cpadr_000/Desktop/ICGOutput.txt", "UTF-8");
		
		//Scans the first token of the input file and calls prog()
		scan();
		prog();
		
		//Writes the postfix codes to ICGOutput.txt
		for (int i = 0; i < postfix.length; i++){
			if (postfix[i] == null) {
				continue; //Skips null
			}
			writerP3.print(postfix[i] + " ");
			System.out.print(postfix[i] + " ");
		}
		writerP3.close();
  		file.close();
	} //End main()
	
	public static int scan() { //Will scan for the next integer and return it
		if (file.hasNextInt()){
			current = file.nextInt();
		}
		return current;
	} //End scan()

	public static void prog(){ //Beginning of program
		declFlag = false;
		if (current == DECLARE){ //Checks for the optional decl part
			declFlag = true;
			current = scan();
			declpart();
			declFlag = false;
		}
		stgroup();
		if (current == NER) { //Checks for the ##
			current = scan();
			if (current == NER) {
				current = scan();
			}
		}
	} //End prog()
	
	public static void declpart(){ //Checks for the declare part
		decllist();
		if (current == ENDDECLARE){ //Checks for KW enddeclare
			current = scan();
		}
	} //End declpart()
	
	public static void decllist(){ //Checks for the declare list
		decl();
		while (current == SEMI){ //Will continuously check for more declarations
			current = scan();
			decl();
		} 
	} //End decllist()
	
	public static void decl(){ //Checks for the declare
		if (current == INTEGER) {
			current = scan();
			idrlist();
		}
	} //End decl()
	
	public static void stgroup(){ //Checks the statement group
		int oper;
		st();
		while (current == SEMI){ //Will check for more statements
			current = scan();
			st();
		}
	} //End stgroup()
	
	public static void st(){ //Checks for a statement
		if (current == READ){ //Checks read
			current = scan();
			read();
			
			//PHASE 3
			postfix[pfCount++] = "READ"; //Adds READ to the end of each statement
			//
		}
		else if (current == WRITE){ //Checks write
			current = scan();
			write();
			
			//PHASE 3
			postfix[pfCount++] = "WRITE"; //Adds WRITE 
			postfix[pfCount++] = "NEWLINE"; //Adds NEWLINE because it's the end
			//
		}
		else if (current == IF){ //Checks if
			current = scan();
			cond();
		}
		else if (current == TO){ //Checks loop
			save2 = pfCount; //Saves starting position of to loop
			current = scan();
			loop();
		}
		else{ //None of the above KW, so it might be an assignment statement
			asgn();
		}
	} //End st()
	
	public static void read(){ //Checks read
		idrlist();
		postfix[pfCount++] = "READ"; //Adds READ to the very end
	} //End read()
	
	public static void write() { //Checks write
		outputlist();
	} //End write()
	
	public static void idrlist(){ //Checks identifier list
		idr();
		while (current == COMMA) {
			current = scan();
			idr();
		}
	} //End idrlist()
	
	public static void outputlist(){ //Checks the output list, only for KW write
		if(current == LPAR) { //Checks for left parentheses for quote
			current = scan();
			quote();
		}
		else {
			expr();
		}
		
		//PHASE 3
		postfix[pfCount++] = "WRITE"; //Adds WRITE to the end of the statement
		//
		
		while (current == COMMA) { //Continuously checks for more expr/quotes
			current = scan();
			if (current == APO){
				quote();
			}
			else {
				expr();
			}
		} //End while that checks for more
	} //End outputlist()
	
	public static void quote(){ //Checks for a quote
		idr();
		if (current ==APO){
			current = scan();
		}
	} //End quote
	
	public static void idr(){ //Checks an identifier
		if(current == IDR){
			if (!declFlag) {
			//PHASE 3
				postfix[pfCount++] = Integer.toString(current); //Places 1 for IDR
				current = scan(); 
				postfix[pfCount++] = Integer.toString(current); //Places the IDR position
				current = scan();
			//
			}
			else {
				current = scan();
				current = scan();
			}
		}
	} //End idr()
	
	public static void asgn() { //Checks for assignment
		int oper; //Local variable for Phase 3
		idr();
		if(current == ASGN){
			
			//PHASE 3
			oper = current;
			//
			
			current = scan();
			expr();
			
			//PHASE 3
			postfix[pfCount++] = "ASGN"; //Adds ASGN to the end of assignment statement
			//
		}
	} //End asgn()
	
	public static void expr(){ //Checks expressions
		int oper; //Local variable for Phase 3
		if (current == MINUS){ //Checks if it is negative
			current = scan();
			term();
			
			//PHASE 3
			postfix[pfCount++] = "@"; //Unary minus sign is added
			//
			
		}
		else {
			term();
		}
		while (current == PLUS || current == MINUS){ //Checks for more

			//PHASE 3
			oper = current;
			//
			
			current = scan();
			term();
			
			//PHASE 3
			if (oper == PLUS){
				postfix[pfCount++] = "+"; //Adds plus sign
			}
			else {
				postfix[pfCount++] = "-"; //Adds subtraction sign
			}
			//
			
		}
		if (current == SEMI){
			if (!rightCheck) { //There is no right
				if (leftCheck && leftCount > 0){ //But there is a left
					leftCount--;
				}
			}
		}
	} //End expr()
	
	public static void term(){ //Checks terms
		int oper; //Local variable for Phase 3
		factor();
		while (current == STAR || current == DVD){ //Checks for more
			
			//PHASE 3
			oper = current;
			//
			
			current = scan();
			factor();

			//PHASE 3
			if (oper == STAR){
				postfix[pfCount++] = "*"; //Adds asterisk for multiplication
			}
			else {
				postfix[pfCount++] = "/"; //Adds slash for division
			}
			//
			
		}
	} //End term()
	
	public static void factor(){ //Checks factors
		if (current == CONST) { //Checks for a constant
			postfix[pfCount++] = Integer.toString(current); //Adds 2 for CONST
			current = scan(); //Skips the table position for the constant
			postfix[pfCount++] = Integer.toString(current); //Adds CONST value
			current = scan();
		}
		else if (current == IDR) { //Checks for an identifier
			postfix[pfCount] = Integer.toString(current); //Adds 1 for IDR

			
			//PHASE 3
			postfix[pfCount++] = Integer.toString(current); //Saves space
			//
					
			current = scan(); //Skips the table position for the identifier
			postfix[pfCount] = Integer.toString(current);
			
			//PHASE 3
			postfix[pfCount++] = Integer.toString(current);
			//
			
			current = scan();

		}
		else { 
			if (current == LPAR){
				leftCheck = true;
				leftCount++;
				current = scan();
			}
			expr();
		}
		if (current == RPAR) { //There is a right par
			rightCheck = true;
			rightCount++;
			current = scan();
		}
		if (rightCheck && rightCount > 0){ //There is a right par
			if (!leftCheck) { //But no left par
				rightCheck = false; //Sets rightCheck back to false
				rightCount--;
			}
		}
	} //End factor()
	
	public static void cond(){ //Checks conditional
		expr();
		
		//PHASE 3
		if (current == GTR) { //Inserts jump condition
			code = "BMZ";
		}
		else if (current == GER) { //Inserts jump condition
			code = "BM";
		}
		//
			
		current = scan();
		expr();
		
		//PHASE 3
		postfix[pfCount++] = "-";
		postfix[pfCount] = Integer.toString(CONST);
		save1 = pfCount; //Saves position 
		pfCount++; //Goes to the next spot
		postfix[pfCount++] = code; //Adds appropriate branching code
		//
		
		current = scan();
		stgroup();
		if (current == ELSE) { //Checks for optional KW else
			
			//PHASE 3
			postfix[pfCount] = Integer.toString(CONST);
			save2 = pfCount; //Saves position
			pfCount++; //Goes to the next spot
			postfix[pfCount++] = "BR"; //Puts appropriate branching code
			postfix[save1] = Integer.toString(pfCount); //Fills save1's position
			//
			
			current = scan();
			stgroup();
			
			//PHASE 3
			postfix[save2] = Integer.toString(pfCount); //Fills save2's position
			//
			
		}
		current = scan();
	} //End cond()
	
	public static void loop(){
		//PHASE 3
		save1 = ++pfCount; //Saves branching position
		String op = "INC";
		postfix[++pfCount] = op;
		pfCount++;
		//
		
		expr();
		
		//PHASE 3
		postfix[pfCount++] = "-";
		postfix[pfCount++] = "BZ"; //Compares to see if its zero
		//
		
		if (current == LOOP){ //Checks for required KW loop
			current = scan(); //Will scan next if the KW loop is there
		}
		stgroup();
		
		//PHASE 3
		// Programmer's job to insert this postfix part to increment the INC operator and 
		// be able to loop until the loop is done
		postfix[pfCount++] = op;
		postfix[pfCount++] = op;
		postfix[pfCount++] = "1";
		postfix[pfCount++] = "+";
		postfix[pfCount++] = "ASGN";
		postfix[pfCount++] = Integer.toString(save1);
		postfix[pfCount++] = "BR";
		postfix[save1] = Integer.toString(pfCount);
		//
		
		if (current == ENDLOOP){ //Checks for required KW endloop
			current = scan();
		}
	} //End loop()
}
	