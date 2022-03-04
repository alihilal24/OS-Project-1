/*
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
    static final int USER_PROGRAM_LOCATION = 0, SYSTEM_CODE_LOCATION = 1;
    Integer[][] mainMemory = new Integer[2][1000];
    
}
