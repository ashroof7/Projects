/*
 * OpTab.h
 *
 *  Created on: May 6, 2012
 *      Author: Ashraf
 */

#ifndef OPTAB_H_
#define OPTAB_H_

#include <string>
#include <string.h>
#include <iostream>
#include <map>
#include <utility>

#include "Opcode.h"

using namespace std;

class OpTab {
public:
	OpTab();
	virtual ~OpTab();
	bool contains(string key);
	Opcode* get(string key);
	map<string, Opcode*>* table;
};

#endif /* OPTAB_H_ */
