/*
 * OpCodeParser.h
 *
 *  Created on: May 16, 2012
 *      Author: ALEX-2010
 */

#ifndef OPCODEPARSER_H_
#define OPCODEPARSER_H_

#include "SymTab.h"
#include "OpTab.h"
#include "LitTab.h"

#include <string>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <cstring>
#include <sstream>

typedef map<char, char>* regMap;

using namespace std;

class OpCodeParser {
public:
	OpCodeParser(SymTab* symbolTable, regMap rigesters, LitTab* lit);
	~OpCodeParser();
	string parse(int pc, string Operator, string Operand, bool&needModRec);
	void setBaseFlag(bool flag, int baseValue);
private:
	int getDisplacement(int pc, string operand, bool& isBase);
	int setFlagbits(int& flags, string operand, bool isBase);
	void setBit(int& i, int shift, int value);
	char defineFormat(string Operator);
    string toHexString(int i, unsigned int length);
	int getInt(char x);
	LitTab* litTable;
	int baseValue;
	bool baseFlag;
	bool isFour;
	SymTab* symTab;
	regMap rigesterTable;
	OpTab* opCodeTable;

};

#endif /* OPCODEPARSER_H_ */
