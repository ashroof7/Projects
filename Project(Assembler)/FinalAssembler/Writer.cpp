#include "Writer.h"
#include <string.h>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <iostream>


Writer::Writer(string fileName) {
	// open file to write object code in
	outFile.open(fileName.c_str());
    modBuffer = new string[bufferLength];
    outBuffer = new string[bufferLength];
    totalBytes = 0;
    startNewRecord = 1;
    maxIndex = bufferLength;
    recLen = 0;
    index = 0;
    modIndex = 0;

}

/*
 * @param name : program name
 * @param startAdr : program starting address
 * @param len : program program length in bytes
 */
void Writer::start(string name, string startAdr, string len){
    if (name.length() > 6){
        cerr << "Error: Program name must be <= 6 characters"<<endl;
        return ;
   }

    curRec = "H"+name;
    for(unsigned int i =name.length() ; i<6 ; i++)
        curRec +=" ";

    for(unsigned int i =startAdr.length() ; i<6 ; i++)
        curRec +="0";
    curRec+=startAdr;

    for(unsigned int i =len.length() ; i<6 ; i++)
        curRec +="0";
    curRec+=len;

    outBuffer[index++] = curRec;
    curRec = "" ;
    if (index >= maxIndex)
        dumpTexToFile();
}

void Writer::dumpTexToFile(){
    // writes the outBuffer to the file
    for(unsigned int i = 0 ; i < outBuffer->length() ;i++)

        if (outBuffer[i]!=""){
            outFile<<outBuffer[i]<<endl;
        }
    index = 0 ;
    outBuffer->clear();

}

void Writer::dumpModToFile(){
    // writes the modBuffer to the file
    for(unsigned int i = 0 ; i < modBuffer->length() ;i++)
        if (modBuffer[i]!=""){
            outFile<<modBuffer[i]<<endl;
        }
    modIndex = 0 ;
    modBuffer->clear();

}

void Writer::closeRecord(){
    if (curRec=="")return;
    stringstream stream;
    stream << hex << recLen;
    string len = stream.str();
    transform(len.begin(),len.end(), len.begin(), ::toupper);

    len = len.length()==2?len:"0"+len;
    curRec[7] = len[0];
    curRec[8] = len[1];
    outBuffer[index++] = curRec;
    if (index >= bufferLength)
        dumpTexToFile();
    recLen = 0;
    curRec =  ""   ;
    startNewRecord = 1;
}

void Writer::writeTextRec(string address, string curLctr, bool close) {
	// append the address to the open text record
	// if the boolean close == true then close the current text record and opens a new one
    // if the remaining space in this record is less than the space of the new address, Open a new record
    // or was explicitly asked to open a new record
    //also set the length of the closed record
    if (curRec.length() + address.length() >=69 || close)
        closeRecord();

    if (startNewRecord){
        startNewRecord = 0;
        curRec = "T";
        for(unsigned int i =curLctr.length() ; i<6 ; i++)
            curRec +="0";
        curRec +=curLctr ;
        //reserved space for record length
        curRec +="##";


    }

        recLen+=address.length()/2;

        curRec += address;
}

void Writer::writeModRec(string address,int len) {
	// writes a modification record
	// len is the length in half bytes

    int x = 0;
    stringstream ss;
    ss<<address;
    ss>>hex>>x;
    x = (len&1)?x+1:x;

    string s = "M";

    stringstream stream;
    stream << hex << x;

    string adr = stream.str();
    transform(adr.begin(),adr.end(), adr.begin(), ::toupper);
    for(unsigned int i = adr.length() ; i<6 ; i++)
        s +="0";
    s+=adr;
    stream.str("");//==.clear
    stream << hex << len;
    adr = stream.str();
    transform(adr.begin(),adr.end(), adr.begin(), ::toupper);
    adr = adr.length()==2?adr:"0"+adr;
    s+=adr;
    modBuffer[modIndex++] = s ;

    if (modIndex >= bufferLength)
        dumpModToFile();
}

void Writer::end(string address){
    //writes the end record
    //@param address is the address of the first executable instruction in object program

    if(curRec!="")
        closeRecord();

    curRec = "E";
    for( int i =address.length() ; i<6 ; i++)
        curRec +="0";
    curRec+=address;
    modBuffer[modIndex++] = curRec;
    dumpTexToFile();
    dumpModToFile();
    closeObjFile();
}

int Writer::freeSpace(){
    return 69-curRec.length();
}

void Writer::closeObjFile() {
	outFile.close();//closing object file
}

