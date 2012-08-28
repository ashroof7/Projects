#include "SymTab.h"
#include "Symbol.h"
#include <map>
#include <string.h>
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>
#include <algorithm>
using namespace std;
SymTab::SymTab() {
}

SymTab::~SymTab() {
	hash.clear();

}

Symbol* SymTab::get(string sym) {
	return hash[sym];
}

void SymTab::insert(string label, int address, char type) {
	Symbol* symb = new Symbol(label, address, type);
	hash[label] = symb;
}

bool SymTab::contains(string label) {
	if (hash.find(label) == hash.end()) {
		return false;
	} else
		return true;
}

void SymTab::show(ofstream &listFile) {
	map<string, Symbol*>::iterator it;
	cout << "    Symbol table      " << endl;
	cout << "LABEL        HEX VALUE" << endl;
	cout << "----------------------" << endl;

    listFile << "    Symbol table      " << endl;
    listFile << "LABEL        HEX VALUE" << endl;
    listFile << "----------------------" << endl;

	for (it = hash.begin(); it != hash.end(); it++) {
		cout << it->first;
        cout << "\t\t" << toHexString(it->second ->getAddress(),4) << endl;
        listFile<<it->first<<"\t\t" << toHexString(it->second ->getAddress(),4) << endl;
	}
}

string SymTab::toHexString(int i,unsigned int length) {
    stringstream stream;
    stream << hex << i;
    string s = stream.str();
    if (s.length()>length)
        s = s.substr(s.length()-length,s.length());
    else if(s.length()<length)
        for (unsigned int j = s.length();j < length ;j++)
             s="0"+s;

    transform(s.begin(), s.end(), s.begin(), ::toupper);
    return s;
}
