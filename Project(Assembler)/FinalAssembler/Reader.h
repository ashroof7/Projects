#ifndef READER_H
#define READER_H

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

using namespace std;

class Reader {
public:
	static int CHUNK_SIZE;
	static int ADDRESS_LENGTH;
	static int COMMENT_LENGTH;
	static int OPERAND_LENGTH;
	static int MNEMONIC_LENGTH;
	static int LABEL_LENGTH;
	static int ALL_LENGTH;

	Reader(string fileName);
	string getLine();
	string getLabel();
	string getAddress();
	string getOpcode();
	string getOperand();
	string getNextAddress();
	bool advance();
	void closeFile();
private:
	string currLine;//current line to read
	ifstream inputFile;// instructions file
	ifstream chunkReader;// a file scanner to read chunks of data
	int lineCnt;//tracks the index of the line
	string addList[100];// stores a chunk of addresses

	bool testComment(string line);//tests if a line is a comment line
	string trim(string in);
	void readChunk();
};

#endif // READER_H
