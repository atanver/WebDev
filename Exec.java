import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

/*CSCI 489 - Milepost #4
Cynthia Padron
Aasimah Tanveer
*/

public class Exec{
   
	//PHASE 4 VARIABLES
	public static Scanner file, Cfile, STFile, SCFile;
	public static PrintWriter writerP4;
	public static int rowCount, numCount, row, num;
	public static String symTable[][];
	public static String postfix[];
	
	//ICG
	public static void main (String[] args) throws IOException {
		// File input is the ICG's output, symbol table is also used
		file = new Scanner(new File("C:/Users/cpadr_000/Desktop/ICGOutput.txt"));
		Cfile = new Scanner(new File("C:/Users/cpadr_000/Desktop/ICGOutput.txt"));
		STFile = new Scanner (new File("C:/Users/cpadr_000/Desktop/symTable.txt"));
		SCFile = new Scanner (new File("C:/Users/cpadr_000/Desktop/symTable.txt"));
		
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
		while (STFile.hasNext()){
			symTable[row][0] = STFile.next(); //Table position
			symTable[row][1] = STFile.next(); //Variable name
			symTable[row][2] = "0"; //Value that'll be changed later
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
		postfix = new String[numCount+1];
		num = 1; //Not going to start with 0 because of branching positions
		while (file.hasNext()){
			postfix[num] = file.next();
			num++;
		}
		
		//Prints out symbol table
		/*for (int row = 1; row < symTable.length; row++) {
			 for (int col = 0; col < 3; col++) {
				 System.out.print(symTable[row][col]+" ");
			 }
			 System.out.println();
		 }*/
		 
		 //Prints out postfix
		 /*for (int num = 1; num < numCount; num++) {
			 System.out.println(postfix[num]);
		 }*/
		
		//Creates stack
		Stack st = new Stack();
		
		//Walk through array
		int fixedLen = postfix.length;
		int position = 1;
		for (int i = 1; i < postfix.length +1; i++) {
			if (postfix[i].equalsIgnoreCase("ASGN")) {
				int x = st.pop();
				int y = st.pop();
			}
			else if (postfix[i].equalsIgnoreCase("WRITE")) {
				
			}
			else if (postfix[i].equalsIgnoreCase("READ")) {
				
			}
			else if (postfix[i].equalsIgnoreCase("NEWLINE")) {
				writerP4.println();
			}
			else if (postfix[i].equalsIgnoreCase("ASGN")) {
				
			}
			else if (postfix[i].equalsIgnoreCase("INC")) {
				
			}
			else if (postfix[i].equalsIgnoreCase("ASGN")) {
				
			}
			else if (postfix[i].equalsIgnoreCase("BR")) {
				
			}
			else if (postfix[i].equalsIgnoreCase("BZ")) {
				
			}
			else {
				st.push
			}
		}
		
		//WriterP4 is the txt file that will have the code's output
		writerP4 = new PrintWriter("/Users/cpadr_000/Desktop/execOutput.txt", "UTF-8");
		
		SCFile.close();
		STFile.close();
		writerP4.close();
  		//file.close();
	} //End main()
}
	