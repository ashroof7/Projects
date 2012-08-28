/*
 * OpTab.cpp
 * Created on: May 7, 2012
 * Author: Ashraf
 */

#include "OpTab.h"
#include <string.h>
#include <iostream>
#include <map>
#include <utility>
#include "Opcode.h"

using namespace std;

OpTab::OpTab() {

	table = new map<string, Opcode*> ;
	// fill the map with the opcodes
	table->insert(make_pair("ADD", new Opcode("ADD", 0x18, 3)));
	table->insert(make_pair("ADDR", new Opcode("ADDR", 0x90, 2)));
	table->insert(make_pair("AND", new Opcode("AND", 0x40, 3)));
	table->insert(make_pair("CLEAR", new Opcode("CLEAR", 0xB4, 2)));
	table->insert(make_pair("COMP", new Opcode("COMP", 0x28, 3)));
	table->insert(make_pair("COMPR", new Opcode("COMPR", 0xA0, 2)));
	table->insert(make_pair("DIV", new Opcode("DIV", 0x24, 3)));
	table->insert(make_pair("DIVR", new Opcode("DIVR", 0x9C, 2)));
	table->insert(make_pair("HIO", new Opcode("HIO", 0xF4, 1)));
	table->insert(make_pair("J", new Opcode("J", 0x3C, 3)));
	table->insert(make_pair("JEQ", new Opcode("JEQ", 0x30, 3)));
	table->insert(make_pair("JGT", new Opcode("JGT", 0x34, 3)));
	table->insert(make_pair("JLT", new Opcode("JLT", 0x38, 3)));
	table->insert(make_pair("JSUB", new Opcode("JSUB", 0x48, 3)));
	table->insert(make_pair("LDA", new Opcode("LDA", 0x0, 3)));
	table->insert(make_pair("LDB", new Opcode("LDB", 0x68, 3)));
	table->insert(make_pair("LDCH", new Opcode("LDCH", 0x50, 3)));
	table->insert(make_pair("LDL", new Opcode("LDL", 0x8, 3)));
	table->insert(make_pair("LDS", new Opcode("LDS", 0x6C, 3)));
	table->insert(make_pair("LDT", new Opcode("LDT", 0x74, 3)));
	table->insert(make_pair("LDX", new Opcode("LDX", 0x4, 3)));
	table->insert(make_pair("LPS", new Opcode("LPS", 0xD0, 3)));
	table->insert(make_pair("MUL", new Opcode("MUL", 0x20, 3)));
	table->insert(make_pair("MULR", new Opcode("MULR", 0x98, 2)));
	table->insert(make_pair("OR", new Opcode("OR", 0x44, 3)));
	table->insert(make_pair("RD", new Opcode("RD", 0xD8, 3)));
	table->insert(make_pair("RMO", new Opcode("RMO", 0xAC, 2)));
	table->insert(make_pair("RSUB", new Opcode("RSUB", 0x4C, 3)));
	table->insert(make_pair("SHIFTL", new Opcode("SHIFTL", 0xA4, 2)));
	table->insert(make_pair("SHIFTR", new Opcode("SHIFTR", 0xA8, 2)));
	table->insert(make_pair("SIO", new Opcode("SIO", 0xF0, 1)));
	table->insert(make_pair("SSK", new Opcode("SSK", 0xEC, 3)));
	table->insert(make_pair("STA", new Opcode("STA", 0x0C, 3)));
	table->insert(make_pair("STB", new Opcode("STB", 0x78, 3)));
	table->insert(make_pair("STCH", new Opcode("STCH", 0x54, 3)));
	table->insert(make_pair("STI", new Opcode("STI", 0xD4, 3)));
	table->insert(make_pair("STL", new Opcode("STL", 0x14, 3)));
	table->insert(make_pair("STS", new Opcode("STS", 0x7C, 3)));
	table->insert(make_pair("STSW", new Opcode("STSW", 0xE8, 3)));
	table->insert(make_pair("STT", new Opcode("STT", 0x84, 3)));
	table->insert(make_pair("STX", new Opcode("STX", 0x10, 3)));
	table->insert(make_pair("SUB", new Opcode("SUB", 0x1C, 3)));
	table->insert(make_pair("SUBR", new Opcode("SUBR", 0x94, 2)));
	table->insert(make_pair("SVC", new Opcode("SVC", 0xB0, 2)));
	table->insert(make_pair("TD", new Opcode("TD", 0xE0, 3)));
	table->insert(make_pair("TIO", new Opcode("TIO", 0xF8, 1)));
	table->insert(make_pair("TIX", new Opcode("TIX", 0x2C, 3)));
	table->insert(make_pair("TIXR", new Opcode("TIXR", 0xB8, 2)));
	table->insert(make_pair("WD", new Opcode("WD", 0xDC, 3)));

	//floating point instructions (maybe not supported)
	table->insert(make_pair("ADDF", new Opcode("ADDF", 0x58, 3)));
	table->insert(make_pair("COMPF", new Opcode("COMPF", 0x88, 3)));
	table->insert(make_pair("DIVF", new Opcode("DIVF", 0x64, 3)));
	table->insert(make_pair("FIX", new Opcode("FIX", 0xC4, 1)));
	table->insert(make_pair("FLOAT", new Opcode("FLOAT", 0xC0, 1)));
	table->insert(make_pair("LDF", new Opcode("LDF", 0x70, 3)));
	table->insert(make_pair("MULF", new Opcode("MULF", 0x60, 3)));
	table->insert(make_pair("NORM", new Opcode("NORM", 0xC8, 1)));
	table->insert(make_pair("STF", new Opcode("STF", 0x80, 3)));
	table->insert(make_pair("SUBF", new Opcode("SUBF", 0x5C, 3)));

}

bool OpTab::contains(string key) {
	if ((*table).find(key) != (*table).end()
			|| (key[0] == '+' && (*table).find(key.substr(1, key.length() - 1))
					!= (*table).end()))
		return true;
	else
		return false;
}

Opcode* OpTab::get(string key) {
	if (this->contains(key)) {
		return (*table)[key];
	} else {
		std::cerr << "entry not found " << endl;
		return NULL;
	}
}

OpTab::~OpTab() {
	delete table;
}

