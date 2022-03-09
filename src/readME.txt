4)	A “readme” file listing your files, a description of each file, and how to compile and run your project.  

Ali Hilal
March 8th 2022
Operating Systems

Project One 

Files being submitted:
    - CPU.java
    - Memory.java
    - sample5.txt
    - readME.txt 

CPU.java:
This file hold the CPU process. It creates the child process of Memory.java and send it the file name. It tracks and implements
interrupts and excecutes instructions read from memory. 

Memory.java:
This file holds the Memory process. It reads the file name from the CPU process and opens the file and reads in and cleans the data read into and 
array of 2000 int. Then it listens for read or write calls from the CPU and either sends the CPU process the data needed for the read 
call or writes the data sent to it at the address it is given.

sample5.txt:
When executed correctly, this program should print a heart:

__xxxxxxxxxxx______xxxxxxxxxx
_xxxxxxxxxxxxxx___xxxxxxxxxxxxx
xxxxxxxxxxxxxxxx_xxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
__xxxxxxxxxxxxxxxxxxxxxxxxxxx
____xxxxxxxxxxxxxxxxxxxxxx
_______xxxxxxxxxxxxxxxxx
________xxxxxxxxxxxx
__________xxxxxxxxx
____________xxxxx
_____________xxx
_____________xx
_____________*

*** To compile and run the code, the CPU.java file, Memory.java file, and the .txt file that is being read should  be in the same working
directory. The just write in terminal java CPU.java "name of .txt file" "interrupt timer value"