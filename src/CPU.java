/*
Ali Hilal 
Monday, March 7th 2022
CS 4348 -  Project 1

Must use Runtime exec method to create processes and streams for communication
CPU and Memory will be simulated by seperate processes that communicate
*/



/*
Registers: PC SP IR AC X Y
Runs user program at address 0
Intructions fetched from memory --> IR (Operand can be put in local variable)
Each instruction is exccuted before the next instruction  is called
user stack at the end of user  memory
system stack at the end of  system memory
user program should not  access system memory
*/

package src;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


public class CPU {
    static int PC, SP, IR, AC, X, Y, timer = 0;
    static String filename = "";
    public static void main(String args[]){
        if(args.length != 2){
            System.err.println("Invalid length of arguments, termiating program....");
            System.exit(0);
        }
        try {
            filename = args[0];
            timer = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.err.println("Invalid timer or file name value, terminating program....");
            System.exit(0);
        }
            System.out.println("Timer is: " + timer);

            try {

                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec("java Memory");
                InputStream inst = pr.getInputStream();
                OutputStream oust = pr.getOutputStream();
                PrintWriter pw = new PrintWriter(oust);
                Scanner memIn = new Scanner(inst); 
                
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

    }
}
