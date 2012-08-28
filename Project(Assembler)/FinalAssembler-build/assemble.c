.2345678901234567890
     ****Error: Start address not valid, setting location counter to 0
prog     start   leo
     1  0000      LEO    LDA                EX^0                                 
     2  0003             STA               ALPHA                                 
     3  0006    ALPHA   RESW                   3                                 
     4  000F     BETA   RESW                   1                                 
     5  0012    GAMMA   RESW                   1                                 
.ISLAM   EQU     (ALPHA + 5/3)*2
     6  006F    ISLAM    EQU   *+2*((5*3)*(5-2))                                 
     ****Error: END operand doesn't match program name
         END     prog
