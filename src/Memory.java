package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*

----- MEMORY -----

Contains one program that will be excecuted by the CPU
consists of 2000 int entries, 0-999 for user program, 1000 - 1999 for system code
two operations: 
    -read(address) - returns value  at address
    -write(address, data) - writes data at address
    MEMORY READS INPUT FILE INTO ARRAY
    file input must be completed before cpu excecution
    *** Memory is just storage, only reads and writes ***
*/

public class Memory {
    Double[] mainMemory = new Double[2000];

    public static void main(String args[]) throws FileNotFoundException{
        File INPUT_FILE = new File (inputFileName);
        Scanner infile = new Scanner(INPUT_FILE);
        int counter = 0;
        while(counter != 999 && infile.hasNextLine()){
            String line = infile.nextLine();
            // if(line == ""){    //this line is to check for extra gaps, does work thou
            //     counter++;
            //     continue;
            // }
            String arrline[] = line.split(" ", 2);
            mainMemory[counter] = Double.parseDouble(arrline[0]);
            System.out.println(mainMemory[counter]);
            counter++;
        }
        infile.close();
    }

    void read(int address){

    }

    void write(int address, char data){

    }
    
}