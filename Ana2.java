import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*CSCI 489 - Milepost #2
Aasimah Tanveer
Cynthia Padron

Extended BNF Grammar
<program> ::= {<decl part>} <st group> ##
<decl part> ::= declare <decl>  enddeclare
<decl list> ::= <decl> {; <decl>}
<decl> ::= integer <identifier list>
<st group> ::= <st> {; <st>}
<st> ::= <asgn>|<read>|<write>|<cond>|<loop>
<read> ::= read <identifier list>
<write> ::= write <output list>
<identifier list> ::= <identifier> {, <identifier>}
<output list> ::= <expr>|<quote>|{,<expr>}|{,<quote>}
<quote> ::= '<word>'
<word> ::= <letter>|<digit>|{<letter>}|{<digit>}
Word isn't needed as a method because the scanner identifies the quotes and the inside as an identifier.
<identifier> ::= <letter>({<letter>|<digit>})
<letter> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z
<digit> ::= 0|1|2|3|4|5|6|7|8|9
Letter/Digit aren't needed because the scanner recognizes them all together and places them in the table.
<const> ::= (+|-) <digit> {<digit>}
Constant method isn't needed because it has its' own integer code.
<asgn> ::= <identifier> := <expr>
<expr> ::= <term> {(+|-)<term>}
<term> ::= <factor> {(*|/)<factor>}
<factor> ::= [-] (<identifier>|<constant>|(<expr>))
<cond> ::= if <expr> (=|>|<|<=|>=|#) <expr> then <st group> [else <st group>] fi
<loop> ::= to <expr> loop <st group> endloop

*/

public class Ana2{
    
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
	
	//Globals
	public static int current;
	public static Scanner file;
	public static PrintWriter writer;
	public static boolean leftCheck, rightCheck;
	public static int leftCount, rightCount;
	
	//PARSER
	public static void main (String[] args) throws IOException {
		//File is the input that will be parsed, named after the file that the scanner produces
		file = new Scanner(new File("/Users/cpadr_000/Desktop/scanOutput.txt"));
		
		//Writer is the txt file that will output any parsing errors
		writer = new PrintWriter("/Users/cpadr_000/Desktop/parserOutput.txt", "UTF-8");
		
		//Scans the first token of the input file
		scan();
		prog();
		writer.println("PARSING COMPLETE");
  		file.close();
  		writer.close();
	} //End main()
	
	public static int scan() { //Will scan for the next integer and return it
		if (file.hasNextInt()){
			current = file.nextInt();
		}
		return current;
	} //End scan()

	public static void prog(){ //Beginning of program
		if (current == DECLARE){ //Checks for the optional decl part
			current = scan();
			declpart();
		}
		stgroup();
		if (current == NER) { //Checks for the ##
			current = scan();
			if (current == NER) {
				current = scan();
			}
			else{
				writer.println("Missing #");
			}
		}
		else{
			writer.println("Missing first #");
		}
	} //End prog()
	
	public static void declpart(){ //Checks for the declare part
		decllist();
		if (current == ENDDECLARE){ //Checks for KW enddeclare
			current = scan();
		}
		else{
			writer.println("Missing KW enddeclare");
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
		else {
			writer.println("Missing KW Integer");
		}
	} //End decl()
	
	public static void stgroup(){ //Checks the statement group
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
		}
		else if (current == WRITE){ //Checks write
			current = scan();
			write();
		}
		else if (current == IF){ //Checks if
			current = scan();
			cond();
		}
		else if (current == TO){ //Checks loop
			current = scan();
			loop();
		}
		else{ //None of the above KW, so it might be an assignment statement
			asgn();
		}
	} //End st()
	
	public static void read(){ //Checks read
		idrlist();
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
	
	public static void outputlist(){ //Checks the output list
		if(current == LPAR) { //Checks for left parentheses for quote
			current = scan();
			quote();
		}
		else {
			expr();
		}
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
		if (current !=APO){
			writer.println("Missing last quote");
		}
		else {
			current = scan();
		}
	} //End quote
	
	public static void idr(){ //Checks an identifier
		if(current != IDR){
			writer.println("Missing identifier");
		}
		else {
			current = scan(); //Skips the identifier table position
			current = scan();
		}
	} //End idr()
	
	public static void asgn() { //Checks for assignment
		idr();
		if(current == ASGN){
			current = scan();
			expr();
		}
		else {
			writer.println("Missing an expression");
		}
	} //End asgn()
	
	public static void expr(){ //Checks expressions
		if (current == MINUS){ //Checks if it is negative 
			current = scan();
			term();
		}
		else {
			term();
		}
		while (current == PLUS || current == MINUS){ //Checks for more
			current = scan();
			term();
		}
		if (current == SEMI){
			if (!rightCheck) { //There is no right
				if (leftCheck && leftCount > 0){ //But there is a left
					writer.println("Missing right parentheses");
					leftCount--;
			
				}
			}
		}
	} //End expr()
	
	public static void term(){ //Checks terms
		factor();
		while (current == STAR || current == DVD){ //Checks for more
			current = scan();
			factor();
		}
	} //End term()
	
	public static void factor(){ //Checks factors
		if (current == CONST) { //Checks for a constant
			current = scan(); //Skips the table position for the constant
			current = scan();
		}
		else if (current == IDR) { //Checks for an identifier
			current = scan(); //Skips the table position for the identifier
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
				writer.println("Missing left parentheses");
				rightCheck = false; //Sets rightCheck back to false
				rightCount--;
			}
		}
	} //End factor()
	
	public static void cond(){ //Checks conditional
		expr();
		if (current != GTR && current != GER && current != LTR && current != LER & current != NER && current != EQR){
			writer.println("Invalid operator in expression");
		}
		current = scan();
		expr();
		if (current != THEN){ //Checks for required KW then
			writer.println("Missing KW then");
		}
		current = scan();
		stgroup();
		if (current == ELSE) { //Checks for optional KW else
			current = scan();
			stgroup();
		}
		if(current != FI){ // Checks for required KW fi 
			writer.println("Missing KW else");
		}
		current = scan();
	} //End cond()
	
	public static void loop(){
		expr();
		if (current != LOOP){ //Checks for required KW loop
			writer.println("Missing KW loop");
		}
		else {
			current = scan(); //Will scan next if the KW loop is there
		}
		stgroup();
		if (current != ENDLOOP){ //Checks for required KW endloop
			writer.println("Missing KW endloop");
		}
		else {
			current = scan();
		}
	} //End loop()
}
	