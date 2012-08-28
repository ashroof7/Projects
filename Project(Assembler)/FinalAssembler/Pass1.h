/*
 * Pass1.h
 *
 *  Created on: May 6, 2012
 *      Author:Authors : Ahmed Gad, Ashraf Saleh, Hossam Mahmoud, Islam Hamdy (names are sored alphabetically)
 */

#ifndef PASS1_H_
#define PASS1_H_
#include <string.h>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <iostream>
#include "SymTab.h"
#include "OpTab.h"
#include "LitTab.h"
#include "Symbol.h"

using namespace std;

struct TERM {
	int type;// 1-relative 0-absolute
	string str;
};

class Pass1 {
public:
	SymTab* symTable;// symbol table stores labels information
	OpTab* opTable;// opTable stores operations opcodes
	LitTab* litTable;// literal table stores literals and their values
	map<char, char>* registerTab;
	map<string, Opcode*>* directives;
	string progName;// name of the program
	int lengthOfProgram;// end address - start address in bytes
	int startLocation;//start address
	bool errorDetected;// checks if error occured
	static unsigned int LABEL_LENGTH;
	static unsigned int MNEMONIC_LENGTH;
	static unsigned int OPERAND_LENGTH;
	static unsigned int COMMENT_LENGTH;
	static unsigned int ALL_LENGTH;
	static string operators;
	static int ABSOLUTE;
	static int RELATIVE;
	static int NEUTRAL;//operator or open/close paren
    ofstream listFile;

	Pass1(string file);
	virtual ~Pass1();
	void readInstructions();
	int hexaParser(string s);// parses hexa value to integer, return -1 if invalid

private:
	string fileName;// file name
	ifstream inputFile;// instructions file
	ofstream outFile;// produced intermediate file


    int ExprCounter;
	int LOCCTR;// location counter
	bool startFlag, endFlag;// tracks START and END of the program
	vector<string> list;//stores tokens space separated
	int lineCounter;//counter of lines
	int twoTokensCase;
	map<string, int>* operatorMap;

	void initFiles();// open file to read instructions
	void closeFile();// close file
	string getParser(string s);// parse an input line
	unsigned int countTokens(string s);//counts tokens in a string space separated
	void tokenize(string input);// splits a string into tokens space separated
	string trim(string in);//trims leading and trailing spaces in a string
	string getHexaValue(int num, int width);//converts integer to Hexadecimal value
	string fitString(string in, int length, int width);//fits entries into their length
	string getBeforeComment(string in);//removes comments from input line
	string
    checkOperand(string label, string opcode, string &operand, int format);
    string insertSymTab(string token, char type);//inserts label in symTab if valid
	Symbol* validateExpressions(string expression);// evaluates EQU operand expressions
	void MakeDirectives();
	void MakeRegisters();
	int intParser(string s);
	string checkDirectives(string label, string directive, string operand);
	string checkStartEnd(string label, string opcode, string operand);
	string getOutFormat(string inputLine);
	string getlineStr(int lineCnt);
	string getComment(string in);
	int getType(string s);
	vector<TERM> splitExpression(string exp);
	void replaceLocationAsterisk(vector<TERM> &tokensVector);
	string infixToPostfix(vector<TERM> &postfix, vector<TERM> vc);
	Symbol* getExpressionValue(vector<TERM> postfix);
	string getTermsValue(TERM first, TERM second, string op, TERM &comb);
	int regNum(string opcode);
};

#endif /* PASS1_H_ */
