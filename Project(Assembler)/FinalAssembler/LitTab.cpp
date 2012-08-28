/*
 * LitTab.cpp
 *
 *  Created on: May 6, 2012
 *      Author: Ahmed_Gad
 */

#include "LitTab.h"
#include <fstream>


LitTab::LitTab() {
	ltorgQ = new queue<vector<string>*> ();
	entry = new vector<string> ();
	v = new vull();
}

LitTab::~LitTab() {
	delete (ltorgQ);
	delete (v);
	delete (entry);
}

void LitTab::print(ofstream& listFile) {
	mymap::iterator it;
	cout << "\n    Literal table      " << endl;
    cout << "HEX VALUE\tlength\taddress" << endl;
	cout << "-----------------------------------------------" << endl;
    cout << hex;

    listFile << "\n    Literal table      " << endl;
    listFile << "HEX VALUE\tlength\taddress" << endl;
    listFile << "-----------------------------------------------" << endl;
    listFile << hex;

	for (it = m.begin(); it != m.end(); it++) {
		cout << it->first << "\t\t" << it->second.first << "\t" << toHexString(
				it->second.second, 4) << endl;
        listFile << it->first << "\t\t" << it->second.first << "\t" << toHexString(
                it->second.second, 4) << endl;
	}
}

//return -1 if error 0 if done
int LitTab::addLit(string lit) {
	if (lit.size() > (14 + 4))
		return -1;
	pair<ull, int> p = getValue(lit);
	if (p.second == -1)
		return -1;
	if (m.find(p.first) == m.end()) {
		stringstream ss;
		if (lit[1] == 'X')
			ss << lit.substr(3, lit.size() - 4);
		else
			ss << hex << p.first;
		entry->push_back(ss.str());
		v->push_back(p.first);
		m[p.first] = ipair(p.second, -1);
	}
	return 0;
}

//get first adderess to set
//returns last address after setting all
int LitTab::setAddresses(int startAddress) {
	int currAddress = startAddress;
	for (ui i = 0; i < v->size(); ++i) {
		ipair p = m[(*v)[i]];
		p.second = currAddress;
		m[(*v)[i]] = p;
		currAddress += p.first;
	}
	ltorgQ->push(entry);
	entry = new vector<string> ();
	v->clear();

	return currAddress;
}

ipair LitTab::getLit(string lit) {
	pair<ull, int> p = getValue(lit);
	if (p.second == -1)
		return ipair(-1, 0);
	if (m.find(p.first) == m.end())
		return ipair(-1, 0);
	return m[p.first];
}

pair<ull, int> LitTab::getValue(string lit) {
	if (lit[0] != '=' || lit[2] != '\'' || lit[lit.length() - 1] != '\'')
		return pair<ull, int> (0, -1);

	ull res = 0;
	int length = 0;
	if (tolower(lit[1]) == 'c') {
		for (ui i = 3; i < lit.size() - 1; ++i) {
			res <<= 8;
			res |= lit[i];
			length++;
		}
	} else if (tolower(lit[1]) == 'x') {
		for (ui i = 3; i < lit.size() - 1; ++i) {
			res <<= 4;
			if ((i & 1) == 1)
				length++;
			if (lit[i] >= '0' && lit[i] <= '9')
				res |= lit[i] - '0';
			else if (tolower(lit[i]) >= 'a' && tolower(lit[i]) <= 'f')
				res |= (10 + lit[i] - 'a');
			else
				return pair<ull, int> (0, -1);
		}
	} else
		return pair<ull, int> (0, -1);

	return pair<ull, int> (res, length);
}

mymap LitTab::getLitTab() {
	return m;
}

string LitTab::toHexString(int i, unsigned int length) {
	stringstream stream;
	stream << hex << i;
	string s = stream.str();
	if (s.length() > length)
		s = s.substr(s.length() - length, s.length());
	else if (s.length() < length)
		for (unsigned int j = s.length(); j < length; j++)
			s = "0" + s;
	transform(s.begin(), s.end(), s.begin(), ::toupper);
	return s;
}
