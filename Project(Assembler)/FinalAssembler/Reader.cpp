#include "Reader.h"

#include <string.h>
#include <string>
#include <algorithm>
#include <functional>
#include <numeric>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include <cctype>
#include <cassert>
#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <fstream>
#include <sstream>
#include <iostream>
string currentLine;

int Reader::CHUNK_SIZE = 100;
int Reader::ADDRESS_LENGTH = 4;
int Reader::COMMENT_LENGTH = 31;
int Reader::OPERAND_LENGTH = 18;
int Reader::MNEMONIC_LENGTH = 6;
int Reader::LABEL_LENGTH = 8;
int Reader::ALL_LENGTH = 66;

Reader::Reader(string fileName) {
	// open file to read instructions
	inputFile.open(fileName.c_str());
	chunkReader.open(fileName.c_str());
	lineCnt = 0;

	if (!inputFile.is_open() || !chunkReader.is_open()) {//file not found
		std::cerr << "Error opening assemble file";
		exit(EXIT_FAILURE);
	}
	readChunk();
}

string Reader::getLine() {
	return trim(currLine);
}

string Reader::getLabel() {
	//returns current label
	return trim(currLine.substr(13, LABEL_LENGTH));
}

string Reader::getAddress() {
	// returns current address
	return currLine.substr(8, ADDRESS_LENGTH);
}

string Reader::getOpcode() {
	// returns current opcode
	return trim(currLine.substr(22, MNEMONIC_LENGTH));
}

string Reader::getOperand() {
	// returns current Operand
	return trim(currLine.substr(30, OPERAND_LENGTH));
}

string Reader::getNextAddress() {
	// returns the valid address of the next line
	int i = lineCnt % CHUNK_SIZE;
	for (; i < CHUNK_SIZE; i++)
		if (addList[i].length() != 0)
			return addList[i];

	readChunk();// no addresses were found in the next couple of lines
	return getNextAddress();
}

bool Reader::advance() {
	//advances the current line
	getline(inputFile, currLine);//read next line

	if (currLine.length() == 0)//end of file
		return 0;

	if (lineCnt % CHUNK_SIZE == 0)
		readChunk();//read another chunk of lines

	lineCnt++;
	if (testComment(currLine))
		return advance();//recursively advance lines in case a comment line is encountered

	return 1;
}

bool Reader::testComment(string line) {
	line = trim(line);
	return line[0] == '.';
}

string Reader::trim(string in) {
	if (in[0] == ' ')
		in = in.erase(0, in.find_first_not_of(" "));
	if (in[in.length() - 1] == ' ')
		in = in.erase(in.find_last_not_of(" ") + 1, in.length());
	return in;
}

void Reader::readChunk() {
	string tempStr;
	int tempCnt = lineCnt, counter = 0;
	while (counter < 100 && getline(chunkReader, tempStr)) {
		if (tempStr.length() == 0)
			continue;
		if (testComment(tempStr))
			tempStr = "";
		else {
			tempStr = tempStr.substr(8, ADDRESS_LENGTH);
		}
		addList[(tempCnt++) % CHUNK_SIZE] = tempStr;
		counter++;
	}
}

void Reader::closeFile() {
	// closes the opened file
	inputFile.close();
	chunkReader.close();
}
