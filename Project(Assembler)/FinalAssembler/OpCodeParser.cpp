#include "OpCodeParser.h"
#include "OpTab.h"
using namespace std;

void OpCodeParser::setBaseFlag(bool flag, int baseV) {
	baseFlag = flag;
	baseValue = baseV;
}

OpCodeParser::OpCodeParser(SymTab *symbolTable, regMap rigesters, LitTab* lit) {
	opCodeTable = new OpTab();
	symTab = symbolTable;
	rigesterTable = rigesters;
	litTable = lit;
	baseFlag = false;
	isFour = false;

}

void OpCodeParser::setBit(int & i, int shift, int value) {
	if (value == 1)
		i |= (1 << shift);
	else
		i &= ~(1 << shift);
}

char OpCodeParser::defineFormat(string Operator) {
	if (Operator[0] == '+') {
		if (opCodeTable->contains(Operator)) {
			isFour = true;
			return 4;
		} else
			return 'r';
	} else if (opCodeTable->contains(Operator)) {
		return opCodeTable->get(Operator)->format;
	} else
		return 'r';
}
int OpCodeParser::setFlagbits(int & flags, string operand, bool isBase) {
	//nixpbe
	//n (indirect) and i (immediate)
	if (operand[0] == '@') {
		setBit(flags, 5, 1);
		setBit(flags, 4, 0);
	} else if (operand[0] == '#')
		setBit(flags, 4, 1);
	else {
		setBit(flags, 5, 1);
		setBit(flags, 4, 1);
	}

	//x (indexing)
	if (operand[operand.size() - 1] == 'X' && operand[operand.size() - 2]
			== ',') {
		if (operand[0] == '#' || operand[0] == '@')
			return -1;
		setBit(flags, 3, 1);
	} else
		setBit(flags, 3, 0);

	//b (base relative) p (pc relative)
	if (operand[0] == '#' && operand.size() == 1)
		return -1;
	if ((operand[0] == '#' && operand[1] >= '0' && operand[1] <= '9') || isFour) {

	} else if (isBase) {
		setBit(flags, 2, 1);
		setBit(flags, 1, 0);
	} else
		setBit(flags, 1, 1);

	//e (format4)

	if (isFour)
		setBit(flags, 0, 1);
	else
		setBit(flags, 0, 0);
	return 0;
}

string OpCodeParser::parse(int pc, string Operator, string Operand,
		bool& needModRec) {

	needModRec = false;
	char form = defineFormat(Operator);
	if (form == 1) {
		int code = getInt(opCodeTable->get(Operator)->code);
		string res = toHexString(code, 3);
		for (unsigned int i = res.length(); i < 3; i++)
			res = "0" + res;
		return res;
	} else if (form == 2) {
		int firstPart = getInt(opCodeTable->get(Operator)->code);
		int x1 = getInt((*rigesterTable)[Operand[0]]);
		int x2 = 0;
		if (Operand.size() != 1)
			x2 = getInt((*rigesterTable)[Operand[2]]);
		string secondPart;

		firstPart <<= 8;
		firstPart |= (x1 << 4);
		firstPart |= x2;
		secondPart = toHexString(firstPart, 4);

		return secondPart;
	} else if (form == 3) {

		if (Operand.size() == 0) {// eg. rsub
			return toHexString(opCodeTable->get(Operator)->code + 3, 2)
					+ "0000";
		}

		int opcode_flags = getInt(opCodeTable->get(Operator)->code);//now == opcode

		//then I should call a method to set n,i bits and add it to first part
		bool baseIsUsed = false;
		int disp = getDisplacement(pc, Operand, baseIsUsed);

		if (disp == -10000000)
			return "error Displacement out of range";

		//then I should call a method to set x,b,p,e the flags and add this to disp
		int nixbpe = 0;
		int error = setFlagbits(nixbpe, Operand, baseIsUsed);
		if (error == -1)
			return "error Wrong operand" + Operand;
		string displacement = toHexString(disp, 3);

		opcode_flags <<= 4;
		opcode_flags |= nixbpe;

		string res = toHexString(opcode_flags, 3);
		for (unsigned int i = res.length(); i < 3; i++)
			res = "0" + res;
		res += displacement;
		return res;

	} else if (form == 4) {
		int opcode_flags = getInt(opCodeTable->get(Operator.substr(1,
				Operator.size() - 1))->code);
		isFour = true;
		int TA = 0;
		needModRec = true;

		if (Operand[0] == '#') {

			string tmp = Operand.substr(1, Operand.size() - 1);
			if (symTab->contains(tmp)) {
				TA = symTab->get(tmp)->getAddress();

			} else {
				for (ui i = 0; i < tmp.size(); ++i)
					if (tmp[i] < '0' || tmp[i] > '9')
						return "error Wrong operand" + Operand;
				TA = atoi(tmp.c_str());
				needModRec = false;
			}

		} else if (Operand[0] == '@') {
			string tmp = Operand.substr(1, Operand.size());
			if (!symTab->contains(tmp))
				return "error  Wrong operand";
			TA = symTab->get(tmp)->getAddress();
		} else if (Operand[Operand.size() - 1] == 'X' && Operand[Operand.size()
				- 2] == ',') {
			string tmp = Operand.substr(0, Operand.size() - 2);
			if (!symTab->contains(tmp))
				return "error Wrong operand" + Operand;
			TA = symTab->get(tmp)->getAddress();
		} else if (Operand[0] == '=') {
			pair<int, int> lit = litTable->getLit(Operand);
			if (lit.first != -1)
				TA = lit.second;
		} else {
			if (!symTab->contains(Operand))
				return "error";
			TA = symTab->get(Operand)->getAddress();
		}

		int nixbpe = 0;
		int error = setFlagbits(nixbpe, Operand, false);

		isFour = false;
		if (error != -1) {
			string displacement = toHexString(TA, 5);

			opcode_flags <<= 4;
			opcode_flags |= nixbpe;
			string res = toHexString(opcode_flags, 3);
			for (unsigned int i = res.length(); i < 3; i++)
				res = "0" + res;
			res += displacement;

			return res;
		} else
			return "error";
	}
	return "error";
}

//-10^7 means error
int OpCodeParser::getDisplacement(int pc, string operand, bool& isBase) {
	int error = -10000000, res = error, ta = error;
	bool mayBeSym = false;
	string tmp = operand;

	if (operand[0] == '#' && operand.size() > 1) {
		tmp = operand.substr(1, operand.size() - 1);
		for (unsigned int i = 1; i < operand.size(); i++)
			if (operand[i] < '0' || operand[i] > '9') {
				mayBeSym = true;
				break;
			}
		if (!mayBeSym) {
			stringstream ss;
			ss << tmp;
			ss >> res;
			return res;
		}
	} else if (operand[0] == '@')
		tmp = operand.substr(1, operand.size() - 1);

	if (tmp[tmp.size() - 1] == 'X')
		tmp = tmp.substr(0, tmp.size() - 2);

	if (symTab->contains(tmp))
		ta = symTab->get(tmp)->getAddress();
	else if (operand[0] == '=') {
		pair<int, int> lit = litTable->getLit(tmp);
		if (lit.first != -1)
			ta = lit.second;
		else
			return error;
	} else
		return error;

	if (isFour)
		return ta;

	res = ta - pc;
	if (res < -2048 || res > 2047) {
		if (baseFlag) {
			isBase = true;
			res = ta - baseValue;
			if (res < 0)
				return error;
		} else
			return error;

	}
	return res;
}

int OpCodeParser::getInt(char x) {
	int i = x;
	if (i < 0)
		i += 256;
	return i;
}

OpCodeParser::~OpCodeParser() {
	delete (symTab);
	delete (opCodeTable);
	delete (rigesterTable);
}

string OpCodeParser::toHexString(int i, unsigned int length) {
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
