//										CMPE 212 Assignment 2      Xiaofeng Lin 10138176 13xl40
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JOptionPane;
//Methods Explanations
//readFile(): Use bufferreader class to read the data from the log. Since data in .csv files are separated by ", " we use it as token break each lines to 
//			  obtain data we want.
//store(): Store data read to the array. Since .read returns an string array, we need to convert each string data to double to store in the array. Since the 
//		   first data is always time, while the column index of our array can indicates the time, it is not needed. thus we only store the data after the
//		   first data.
//printarray(): Print out some data to check if the array stores the expected value.You can change values of i, j and t in the method to check current of 
//              Motor(i+1) between time j and t. Note that outputs are printed in console, not in the report file.
//printstream(): Setup printstream, call the method summary() and print the console output from summary() to a file.
//summary(): Analysis the data and output sentences as the assignment requested. Since the process of analyzing each motor is identical, for loops are used.
public class Assignment2_13xl40 {

	public static void main(String[] args){
		Double[][] data = new Double[8][1000];				//initialize array as object to store data read from the log
		int i , j;											//i is the column index(indicates motor number) and j is the row index (indicates time)
		for (i = 0; i < 8; i++){
			for (j = 0; j< 1000; j++)
				data[i][j] = 0.0;
		}
		readFile(data);										
		printarray(data);									
		printstream(data);									
		JOptionPane.showMessageDialog(null,"Analysis complete! The report file is generated under the project folder.");
	}
	
	//read data and store them in data[][]
	public static void readFile(Double[][] data){
		int i = 0;											//same as the definition in main
		int j = 0;
		try {
			BufferedReader file = new BufferedReader(new FileReader("Logger.csv")) ; 		//read the source file
			String line = null;																//initialize line read from source file
			while ((line = file.readLine()) != null) {										//read each line in source file (splited by ", " ) and put them into the data array
				String[] string = line.split(", ");											//line.split() returns a size 8 matrix, storing the time, motor1, motor2.....,motor8
				store(string, data, i, j);													//store only the motor value to the array
				j++;
			}
			file.close();																	//close the file to prevent data leak
		}
		catch (FileNotFoundException e) {													
			e.printStackTrace();
		}
		catch (IOException io){
			io.printStackTrace();
		}
	}
	
	//Store the motor value to the array data[][]
	//Only the motor value are stored.time is indicated by j and motor number is indicated by (i+1)
	public static void store(String[] string, Double[][] data,  int i, int j){				
		data[i][j] = Double.parseDouble(string[1]);
		data[i+1][j] = Double.parseDouble(string[2]);
		data[i+2][j] = Double.parseDouble(string[3]);
		data[i+3][j] = Double.parseDouble(string[4]);
		data[i+4][j] = Double.parseDouble(string[5]);
		data[i+5][j] = Double.parseDouble(string[6]);
		data[i+6][j] = Double.parseDouble(string[7]);
	}
	
	//print out sample data from the array
	public static void printarray(Double[][] data){
		int i = 0;										//index of motor
		int j = 0;										//starting time
		int t = 50;										//ending time
		System.out.print("The following are sample data read from the log:\n");
		while (j <= t){
		System.out.print("The current of Motor" + (i + 1) + " at " + j + " second is " + data[i][j] + "amps.\n");	
		j++;
		}
	}
	
	//analysis data and print the summary to a file created
	public static void printstream(Double[][] data){
		try{
			PrintStream printstream = new PrintStream(new FileOutputStream("report.txt"));		//use printStream to write all console output to report.txt
			System.setOut(printstream);
			summary(data);	
			printstream.close();																//close printstream to prevent data leaking
		}
		catch (FileNotFoundException e) {													
			e.printStackTrace();
		}
	}
	
	//analysis data in the array and generate reports in the console
	 public static void summary(Double[][] data){
		int i;											//index indicates which motor is measured	
		int j;											//index indicates the time (or which value is measured)
		boolean status = false;							//a switcher indicating the current status of the motor
		int activePeriod = 0;							//an accumulative value indicates the active period of motor
		int inactivePeriod = 0;							//an accumulative value indicates the inactive period of motor
		int sum = 0;									//an accumulative value indicates the sum of the current during in the period
		double current = 0;								//a variable holds the current current of the motor
		double avg;										//the average current of motor in the period
		for (i = 0; i < 8; i++){
			System.out.print("\nMotor" + (i + 1) + ":\n"); 
			for (j = 0; j < 1000; j++){					//each motor has the same analysis process, thus a for loop is used
				current = data[i][j];					//read current value from the array
				if (current > 1){						//if motor is active at current time
					activePeriod ++;					//activePeriod increment 
					sum += current;						//accumulate the current
					status = true;						//indicates motor is activated
					if (current > 8)					//current exceeded
						System.out.print("Alert! Motor" + (i + 1) + " current exceeded at " + j + " second.\n");
				}
				else{									//motor inactive at current time
					if (!status)						//if motor is inactive from the start of the time
						inactivePeriod ++;				//inactivePeriod is increased for later use
					else{								//motor is switched from active to inactive. Thus we need to compute the average current to generate result
						status = false;					//switch the status	
						avg = sum/activePeriod;			//calculate average current
						System.out.print("The average current from " + (j-activePeriod) + "s to " + (j-1) + "s is " + avg + "amps.\n");
						sum = 0;						//reset current sum for next active period/motor
						activePeriod = 0;				//reset active time	for next active period/motor
					}
				}
			}
			if (inactivePeriod == 1000)					//indicates that motor is inactive for the whole time
				System.out.print("The motor is inactive for the whole time.\n");
			inactivePeriod = 0;							//reset inactive time for next motor
		 }
	}

}