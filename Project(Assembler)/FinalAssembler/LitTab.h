/*
 * LitTab.h
 *
 *  Created on: May 6, 2012
 *      Author: Ahmed_Gad
 */

#ifndef LITTAB_H_
#define LITTAB_H_

#include "LitTab.h"

#include <map>
#include <set>
#include <list>
#include <cmath>
#include <ctime>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <cstdio>
#include <vector>
#include <cstdlib>
#include <cstring>
#include <iomanip>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <functional>
#include <fstream>
using namespace std;

#define max(a,b)a>b?a:b;
#define min(a,b)a>b?b:a;
#define sqr(x) ((x)*(x))
#define two(X) (1<<(X))
#define twoL(X) (((ll)(1))<<(X))
#define SIZE(A) ((int)A.size())
#define MP(A,B) make_pair(A,B)
#define PB(X) push_back(X)
#define ME(a) memset((a), 0, sizeof((a)))
#define MM(a, b) memcpy((a), (b), sizeof((a)))
#define FOR(i,n) for (int (i) = 0; (i) < (n); ++(i))
#define REP(i,a,b) for (int (i) = (a); (i) < (b); ++(i))

typedef long long ll;
typedef unsigned long long ull;
typedef unsigned int ui;
typedef vector<int> vi;
typedef vector<ull> vull;
typedef pair<int, int> ipair;
typedef map<ull, ipair> mymap;

class LitTab {
private:
	mymap m;
	vull * v;
	pair<ull, int> getValue(string lit);

public:
	LitTab();
	virtual ~LitTab();
	string toHexString(int i, unsigned int length);

	vector<string>* entry;
	queue<vector<string>*> * ltorgQ;

	int addLit(string lit);
	int setAddresses(int startAddress);
	ipair getLit(string lit);
	mymap getLitTab();
    void print(ofstream &listFile);
};

#endif /* LITTAB_H_ */
