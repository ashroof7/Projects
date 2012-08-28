#ifndef WRITER_H
#define WRITER_H
#include <string.h>
#include <string>
#include <algorithm>
#include <functional>
#include <numeric>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include <cctype>
#include <cassert>
#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <fstream>
#include <sstream>
#include <iostream>
using namespace std;

class Writer {
public:
	Writer(string fileName);
    void start(string name, string startAdr, string len);
    void writeTextRec(string address, string curLctr, bool close);
    void writeModRec(string address, int len);
	void closeObjFile();
    void dumpTexToFile();
    void dumpModToFile();
    void end(string address);
    void closeRecord();
    int  freeSpace();

private:
    int recLen;//open record length
	ofstream outFile;// produce object file
    string curRec;//current open record
    string* outBuffer;
    string* modBuffer;
    static const int bufferLength = 100;
    int index ;
    int modIndex;
    int maxIndex ;
    int totalBytes ;
    bool startNewRecord;

};

#endif // WRITER_H
