import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

/*CSCI 489 - Milepost #4
Cynthia Padron
Aasimah Tanveer
*/

public class Exec{
   
	//PHASE 4 VARIABLES
	public static Scanner file, Cfile, STFile, SCFile;
	public static PrintWriter writerP4;
	public static int rowCount, numCount, row, num, INC = 0;
	public static String symTable[][];
	public static String postfix[];
	public static int[][] INCOP = new int[100][1];
	
	//ICG
	public static void main (String[] args) throws IOException {
		// File input is the ICG's output, symbol table is also used
		file = new Scanner(new File("C:/Users/cpadr_000/Desktop/test2ICGOut.txt"));
		Cfile = new Scanner(new File("C:/Users/cpadr_000/Desktop/test2ICGOut.txt"));
		STFile = new Scanner (new File("C:/Users/cpadr_000/Desktop/symT2.txt"));
		SCFile = new Scanner (new File("C:/Users/cpadr_000/Desktop/symT2.txt"));
		
		//Counts the number of rows that'll be needed based on the number of rows in the 
		//symTable.txt file
		rowCount = 0;
		if(SCFile != null){
			while (SCFile.hasNextLine()){
				rowCount++;
				SCFile.nextLine();
			}
		}
		
		//Creates the symbol table and fills it with symTable.txt contents
		symTable = new String[rowCount+1][3];
		row = 1; //Not going to start with 0 because integer codes start with 1
		while (row < rowCount+1){
			symTable[row][0] = STFile.next(); //Table position
			symTable[row][1] = STFile.next(); //Variable name
			symTable[row][2] = STFile.next(); //Value of variable
			row++;
		}
		
		//Counts number of strings in file in order to know size for array
		numCount = 0;
		if(Cfile != null){
			while (Cfile.hasNext()){
				if (Cfile.next() != null) {
					numCount++;
				}
			}
		}
		
		//Creates the postfix array and fills it with ICGOutput.txt contents
		postfix = new String[numCount+2]; //Up to the last before the end
		num = 1; //Not going to start with 0 because of branching positions
		while (file.hasNext()){
			postfix[num] = file.next();
			num++;
		}
		
		/*Prints out symbol table
		for (int row = 1; row < symTable.length; row++) {
			 for (int col = 0; col < 3; col++) {
				 System.out.print(symTable[row][col]+" ");
			 }
			 System.out.println();
		 }*/
		 
		 /*Prints out postfix
		 for (int num = 1; num < postfix.length; num++) {
			 System.out.println(postfix[num]);
		 }*/
		
		//WriterP4 is the txt file that will have the code's output
		writerP4 = new PrintWriter("/Users/cpadr_000/Desktop/testExOut.txt", "UTF-8");
		
		INCOP[99][0] = 0;

		//Creates stack
		Stack<Integer> st = new Stack<>();
		
		//Walk through array
		int fixedLen = postfix.length;
		int pc = 1; //Counter
		while (pc < fixedLen) {
			if (postfix[pc] == null){
				break;
			}
			else if (postfix[pc].equals("ASGN")) {
				// j := i 
				int i = st.pop(); //Constant
				int j = st.pop(); //Symbol Table position
				System.out.println(j+"er "+i);

				if (j == 99) { //INC := 1
					INCOP[99][0] = i;
					System.out.println(INCOP[99][0]);
				}
				else {
					symTable[j][2] = Integer.toString(i); //Places constant
				}
				pc++;
			}
			else if (postfix[pc].equals("WRITE")) {
				int last = st.pop();
				writerP4.println(last);
				pc++;
			}
			else if (postfix[pc].equals("READ")) {
				int last = st.pop(); //Just pops it out and does nothing with it
				pc++;
			}
			else if (postfix[pc].equals("NEWLINE")) {
				writerP4.println();
				pc++;
			}
			else if (postfix[pc].equals("INC")) {
				st.push(99); //Global variable INC 
				pc++;
			}
			else if (postfix[pc].equals("+")) {
				int i = st.pop();
				int j = 0;
				if (st.peek()==99){ // If it's the INC op
					int place = st.pop();
					j = INCOP[99][0];
				}
				else {
					j = st.pop();
				}
				int k = i + j;
				st.push(k);
				pc++;
			}
			else if (postfix[pc].equals("-")) {
				// infix x - y
				// postfix x y -
				// stack bottom: x y
				int k = 0;
				int i = st.pop(); // the "y" position
				int j = 0;
				if (st.peek()==99){ // If its the INC op, value in table
					int place = st.pop();
					j = INCOP[99][0];
					k = j- Integer.parseInt(symTable[i][2]);
				}
				else {
					j = st.pop(); //the "x" position
					k = Integer.parseInt(symTable[j][2]) - Integer.parseInt(symTable[i][0]); //Want to do "x" - "y"
				}
				System.out.println("j" + j+ " i" +i);
				st.push(k);
				pc++;
			}
			else if (postfix[pc].equals("*")) {
				int i = st.pop();
				int j = st.pop();
				int k = i * j;
				st.push(k);
				pc++;
				
			}
			else if (postfix[pc].equals("/")) {
				// infix x / y
				// postfix x y /
				// stack bottom: x y
				int i = st.pop(); // the "y"
				int j = st.pop(); // the "x"
				int k = j / i; //Want to do "x" / "y"
				st.push(k);
				pc++;
			}
			else if (postfix[pc].equals("BR")) {
				int branch = st.pop(); //Points to position it'll branch to, value before BR
				pc = branch;
			}
			else if (postfix[pc].equals("BMZ")) {
				int branch = st.pop();
				int check = st.pop();
				if (check == 0){ //If it is zero
					pc = branch;
				}
				else { //It isn't so it'll keep going
					pc++;
				}
			}
			else { //Otherwise it's an integer rather than string
				int i = Integer.parseInt(postfix[pc]);
				if (i == 1) { //It's an identifier
					pc++; //Now looking at position of idr in symbol table
					if (postfix[pc].equals("+")) { //It's incrementing INC
						st.push(1);
					}
					else {// It's not incrementing so regular identifer
						int place = Integer.parseInt(postfix[pc]);
						st.push(place); 
						pc++; //Looking at next thing in array
					}
				}
				else if (i == 2) { //Its a constant
					pc++; //Now looking at the value
					int k = Integer.parseInt(postfix[pc]);
					st.push(k);
					pc++;
				}
				else if (i == 99) { //Let's us know it's an INC op
					st.push(i);
					pc++;
				}
				else { //It's a branch position
					int branch = Integer.parseInt(postfix[pc]);
					st.push(branch);
					pc++;
				}
			}
		}
		
		file.close();
		Cfile.close();
		SCFile.close();
		STFile.close();
		writerP4.close();
  		//file.close();
	} //End main()
}
	