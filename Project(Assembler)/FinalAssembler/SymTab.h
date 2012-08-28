
#ifndef SYMTAB_H_
#define SYMTAB_H_
#include "Symbol.h"
#include <map>
#include <string.h>
#include <string>
#include <iostream>

#include <sstream>
#include <iostream>

using namespace std;
class SymTab {
public:
	SymTab();
	~SymTab();
	void insert(string label, int address,char type);
	Symbol* get(string label);
	bool contains(string label);
    void show(ofstream &listFile);
	map<string, Symbol*> hash;
private:
    string toHexString(int i,unsigned int length);
};

#endif /* SYMTAB_H_ */
