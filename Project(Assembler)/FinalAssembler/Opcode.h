#ifndef OPCODE_H
#define OPCODE_H
#include <string>
#include <string.h>
#include <iostream>


using namespace std;

class Opcode
{
public:
   string name;
   unsigned char code;
   char format;
   Opcode();
   Opcode(string _name,char _code,int _format);

};

#endif // OPCODE_H
