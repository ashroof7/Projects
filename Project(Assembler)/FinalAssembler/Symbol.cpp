/*
 * Symbol.cpp
 *
 *  Created on: May 7, 2012
 *      Author: ALEX-2010
 */

#include "Symbol.h"
#include <string.h>
#include <string>
using namespace std;

void Symbol::setAddress(int  add)
{
	address = add;
}



int Symbol::getAddress()
{
	return address;
}



void Symbol::setLabel(string s)
{
	label = s;
}


Symbol::Symbol(string s, int  add, char r)
{
	label=s;
	address = add;
	type = r;
}

char Symbol::getType(){
	return type;
}


string Symbol::getLabel()
{
	return label;
}



Symbol::~Symbol()
{
	delete(&label);
	delete(&address);
	delete(&type);
}



