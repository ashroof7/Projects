/*
 * Symbol.h
 *
 *  Created on: May 7, 2012
 *      Author: ALEX-2010
 */

#ifndef SYMBOL_H_
#define SYMBOL_H_
#include <string.h>
#include <string>
using namespace std;
class Symbol{
public:
	Symbol(string s, int add, char type);
	~Symbol();
	void setLabel(string s);
	void setAddress(int add);
	char getType();
	int getAddress();
	string getLabel();
private:
	int address;
	string label;
	char type;

};

#endif /* SYMBOL_H_ */
