#include "Opcode.h"

string name;
unsigned char code;
char format;

Opcode::Opcode() {
}
Opcode::Opcode(string _name, char _code, int _format) :
	name(_name), code(_code), format(_format) {
}

