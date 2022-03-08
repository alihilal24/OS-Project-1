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
import java.util.Random;
import java.util.Scanner;

import javax.management.loading.PrivateClassLoader;


public class CPU {
    final static int SYSTEM_STACK_TOP = 2000, USER_STACK_TOP = 1000;
    static int PC = 0, SP = 1000, IR, AC, X, Y, timer = 0, instructionCounter = 0;
    static boolean userMode = true, procInterupt = false;;
    static String filename = "";
    public static void main(String args[]){
    //     if(args.length != 2){
    //         System.err.println("Invalid length of arguments, termiating program....");
    //         System.exit(0);
    //     }
        try {
           filename = args[0];
            // filename = "s1p0.txt"; //debugging purposes
            timer = Integer.parseInt(args[1]);
            // timer = 30; //debugging purposes
        } catch (Exception e) {
            System.err.println("Invalid timer or file name value, terminating program....");
            System.exit(0);
        }
            System.out.println("Timer is: " + timer);

            try {

                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec("java Memory.java");
                InputStream instr = pr.getInputStream();
                OutputStream oustr = pr.getOutputStream();
                PrintWriter pw = new PrintWriter(oustr);
                Scanner memIn = new Scanner(instr); 

                pw.printf(filename + "\n");
                pw.flush();

                do{
                    if(instructionCounter != 0 && (instructionCounter % timer) == 0 && procInterupt == false){
                        procInterupt = true;
                        interupt(pw, memIn, instr, oustr);
                    }
                    IR = readFromMem(pw, instr, oustr, memIn, PC);
                    if(IR == -1){
                        break;
                    }
                    executeInstruction(pw, memIn, instr, oustr);
                } while(true);
                pr.waitFor();
                System.out.println("Process ended with value " + pr.exitValue());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

    }

    private static int readFromMem(PrintWriter pw, InputStream instr, OutputStream oustr, Scanner memIn, int PC){
        if(userMode && PC > 1000){
            System.err.println("User program attempted to access system stack, Process terminating...");
            System.exit(0);
        }
        pw.printf("read," + PC + "\n");
        pw.flush();
        if(memIn.hasNext()){
            if(!(memIn.next().isEmpty())){
                return Integer.parseInt(memIn.next());
            }
        }
        return -1;
    }

    private static void writeToMem(PrintWriter pw, InputStream instr, OutputStream oustr, int address, int data){
        pw.printf("write," + address + "," + data + "\n");
        pw.flush();
    }

    private static void interupt(PrintWriter pw, Scanner memIn, InputStream instr, OutputStream oustr){
        int operand = SP;
        userMode = false;
        SP = SYSTEM_STACK_TOP;
        pushToStack(pw, instr, oustr, operand);
        operand = PC;
        PC = 1000;
        pushToStack(pw, instr, oustr, operand);
    }

    private static void pushToStack(PrintWriter pw, InputStream instr, OutputStream oustr, int data){
        SP--;
        writeToMem(pw, instr, oustr, SP, data);
    }
    private static int popFromStack(PrintWriter pw, InputStream instr, OutputStream oustr, Scanner memIn){
        int temp =  readFromMem(pw, instr, oustr, memIn, SP);
        writeToMem(pw, instr, oustr, SP, 0);
        SP++;
        return temp;
    }
    private static void processInteruptCheck(){
        if(procInterupt == false)
            instructionCounter++;
    }
    private static void executeInstruction(PrintWriter pw, Scanner memIn, InputStream instr, OutputStream oustr){
        // System.out.println(instruciton + "\n"); //debugging
        int operand;
        switch (IR) {
            case 1: //Load the value into the AC
                PC++;
                AC = readFromMem(pw, instr, oustr, memIn, PC);
                processInteruptCheck();
                PC++;
                break;
            case 2: //Load the value at the address into the AC
                PC++;
                operand = readFromMem(pw, instr, oustr, memIn, PC);
                AC = readFromMem(pw, instr, oustr, memIn, operand);
                processInteruptCheck();
                PC++;
                break;
            case 3: //Load the value from the address found in the given address into the AC  (for example, if LoadInd 500, and 500 contains 100, then load from 100).
                PC++;
                operand = readFromMem(pw, instr, oustr, memIn, PC);
                operand = readFromMem(pw, instr, oustr, memIn, operand);
                AC = readFromMem(pw, instr, oustr, memIn, operand);
                processInteruptCheck();
                PC++;
                break;
            case 4: //Load the value at (address+X) into the AC (for example, if LoadIdxX 500, and X contains 10, then load from 510).
                PC++;
                operand = readFromMem(pw, instr, oustr, memIn, PC) + X;
                AC = readFromMem(pw, instr, oustr, memIn, operand);
                processInteruptCheck();
                PC++;
                break;
            case 5: //Load the value at (address+Y) into the AC
                PC++;
                operand = readFromMem(pw, instr, oustr, memIn, PC) + Y;
                AC = readFromMem(pw, instr, oustr, memIn, operand);
                processInteruptCheck();
                PC++;
                break;
            case 6: //Load from (Sp+X) into the AC (if SP is 990, and X is 1, load from 991).
                PC++;
                AC = readFromMem(pw, instr, oustr, memIn, SP + X);
                processInteruptCheck();
                PC++;
                break;
            case 7: //Store the value in the AC into the address
                PC++;
                operand = readFromMem(pw, instr, oustr, memIn, PC);
                writeToMem(pw, instr, oustr, operand, AC);
                processInteruptCheck();
                PC++;
                break;
            case 8: //Gets a random int from 1 to 100 into the AC
                PC++;
                Random rand = new Random();
                AC = rand.nextInt(100) + 1;
                processInteruptCheck();
                PC++;
                break;
            case 9: //If port=1, writes AC as an int to the screen If port=2, writes AC as a char to the screen
                PC++;
                operand = readFromMem(pw, instr, oustr, memIn, PC);
                if(operand == 1){
                    System.out.println(AC);
                }
                else if(operand == 2){
                    System.out.println((char)AC);
                }
                else{
                    System.err.println("Port number " + operand + " is unrecognised by the program\nTerminating program...");
                    System.exit(0);
                }
                processInteruptCheck();
                PC++;
                break;
            case 10: //Add the value in X to the AC
                AC += X;
                processInteruptCheck();
                PC++;
                break;
            case 11: //Add the value in Y to the AC
                AC += Y;
                processInteruptCheck();
                PC++;
                break;
            case 12: //Subtract the value in X from the AC
                AC -= X;
                processInteruptCheck();
                PC++;
                break;
            case 13: //Subtract the value in Y from the AC
                AC -= Y;
                processInteruptCheck();
                PC++;
                break;
            case 14: //Copy the value in the AC to X
                X = AC;
                processInteruptCheck();
                PC++;
                break;
            case 15: //Copy the value in X to the AC
                AC = X;
                processInteruptCheck();
                PC++;
                break;
            case 16: //Copy the value in the AC to Y
                Y = AC;
                processInteruptCheck();
                PC++;
                break;
            case 17: //Copy the value in Y to the AC
                AC = Y;
                processInteruptCheck();
                PC++;
                break;
            case 18: //Copy the value in AC to the SP
                SP = AC;
                processInteruptCheck();
                PC++;
                break;
            case 19: //Copy the value in SP to the AC 
                PC++;
                AC = SP;
                processInteruptCheck();
                PC++;
                break;
            case 20: //Jump to the address
                PC++;
                PC = readFromMem(pw, instr, oustr, memIn, PC);
                processInteruptCheck();
                PC++;
                break;
            case 21: //Jump to the address only if the value in the AC is zero
                PC++;
                if(AC == 0){
                    PC = readFromMem(pw, instr, oustr, memIn, PC);
                    processInteruptCheck();
                    break;
                }
                processInteruptCheck();
                PC++;
                break;
            case 22: //Jump to the address only if the value in the AC is not zero
                PC++;
                if(AC != 0){
                    PC = readFromMem(pw, instr, oustr, memIn, PC);
                    processInteruptCheck();
                    break;
                }
                processInteruptCheck();
                break;
            case 23: //Push return address onto stack, jump to the address
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
            case 24: //Pop return address from the stack, jump to the address
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
            case 25: //Increment the value in X
                X++;                
                processInteruptCheck();
                PC++;
                break;
            case 26: //Decrement the value in X
                X--;
                processInteruptCheck();
                PC++;
                break;
            case 27: //Push AC onto stack
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
            case 28: //Pop from stack into AC
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
            case 29: //Perform system call
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
            case 30: //Return from system call
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
            case 50: //End execution
                PC++;
                
                processInteruptCheck();
                PC++;
                break;
        
            default:
                System.err.println("Internal Error: Unknown Instruction");
                System.exit(0);
        }
    }

}
