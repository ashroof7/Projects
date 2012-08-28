#ifndef PASS2_H
#define PASS2_H
#include "SymTab.h"
#include "Symbol.h"
#include "OpTab.h"
#include "Opcode.h"
#include "Reader.h"
#include "Writer.h"
#include "LitTab.h"
#include "OpCodeParser.h"
#include "Pass1.h"
#include <string.h>
#include <string>
#include <fstream>
using namespace std;

typedef map<char, char>* regMap;

class Pass2 {
public:
    Pass2(string assembFile, Pass1* pass1 );
	void readInstructions();
private:
	SymTab* symTable;
	OpTab* opTable;
	LitTab* litTable;
	Reader* reader;
	Writer* writer;
    OpCodeParser* opParser;
    regMap regTab;
    map<string, Opcode*>* directives;
    Pass1* pass_1;
    ofstream listFile;

	void handleDirectives();
    string getByteValue(string in);
    void setLiterals(string currAddress);
    int calculateDisp(string arg, int format);
	string generateObjCode(string opCode, int flagMask, int disp);
};

#endif // PASS2_H
