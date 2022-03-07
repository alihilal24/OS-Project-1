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
    final static Integer[] mainMemory = new Integer[2000];

    public static void main(String args[]) throws FileNotFoundException{
        Scanner INPUT_CPU = new Scanner(System.in);
        File INPUT_FILE = null;

        if(INPUT_CPU.hasNextLine()){
            try {
                INPUT_FILE = new File(INPUT_CPU.nextLine());
            } catch (Exception e) {
                System.err.println("File can't be found, terminating program...");
                System.exit(0);
            }
        }

        Scanner infile = new Scanner(INPUT_FILE);
        int counter = 0;

        while(infile.hasNextLine()){
            if(infile.hasNextInt()){
                mainMemory[counter++] = infile.nextInt();
            }
            else{
                String line = infile.next();
                String arrline[] = line.split(" ", 2);
                if(arrline[0].charAt(0) == '.'){
                    counter = Integer.parseInt(line.substring(1));
                }
                else if(line == ""){
                    infile.nextLine();
                }
                else{
                    mainMemory[counter++] = Integer.parseInt(arrline[0]);
                }
            }            
            System.out.println(mainMemory[counter]);
        }
        infile.close();
        INPUT_CPU.close();

        do{

            if(INPUT_CPU.hasNext()){
                String line = INPUT_CPU.nextLine();
                if(!line.isEmpty()){
                    String arrline[] = line.split(",");
                    if(arrline[0] == "read"){
                        System.out.println(mainMemory[Integer.parseInt(arrline[1])]);
                    }
                    else if(arrline[0] == "write"){
                        mainMemory[Integer.parseInt(arrline[1])] = Integer.parseInt(arrline[2]);
                    }
                    else{
                        System.err.println("Internal System Error: Command other than read or write passed\nTerminating Program...");
                        System.exit(0);
                    }
                    
                }
                else{
                    break;
                }
            }
            else{
                break;
            }

        } while(true);
    }

    
}

