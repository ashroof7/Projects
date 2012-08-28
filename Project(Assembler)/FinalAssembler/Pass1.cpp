/*
 * Pass1.cpp
 *
 *  Created on: May 6, 2012
 *  Authors : Ahmed Gad, Ashraf Saleh, Hossam Mahmoud, Islam Hamdy (names are sorted alphabetically)
 */

#include "Pass1.h"
#include "Pass2.h"
#include "SymTab.h"
#include "Symbol.h"
#include "OpTab.h"
#include "LitTab.h"
#include <fstream>
#include <string.h>
#include <string>
#include <sstream>
#include <iostream>

using namespace std;

unsigned int Pass1::COMMENT_LENGTH = 31;
unsigned int Pass1::OPERAND_LENGTH = 18;
unsigned int Pass1::MNEMONIC_LENGTH = 6;
unsigned int Pass1::LABEL_LENGTH = 8;
unsigned int Pass1::ALL_LENGTH = 66;
string Pass1::operators = "*/+-";
int Pass1::ABSOLUTE = 0;
int Pass1::RELATIVE = 1;
int Pass1::NEUTRAL = -1;

Pass1::Pass1(string s) {
	fileName = s;
	startFlag = 0;
	endFlag = 0;
	LOCCTR = 0;
	lineCounter = 1;
	twoTokensCase = 0;
	errorDetected = 0;
	startLocation = 0;
	lengthOfProgram = 0;
	ExprCounter = 0;
	operatorMap = new map<string, int> ();
	operatorMap->insert(make_pair("*", 1));
	operatorMap->insert(make_pair("/", 1));
	operatorMap->insert(make_pair("+", 0));
	operatorMap->insert(make_pair("-", 0));

	initFiles();
	symTable = new SymTab();
	litTable = new LitTab();
	opTable = new OpTab();
	MakeDirectives();
	MakeRegisters();
}

Pass1::~Pass1() {
}

void Pass1::initFiles() {
	// open file to read instructions
	inputFile.open(fileName.c_str());

	if (!inputFile.is_open()) {//file not found
		std::cerr << "Error opening file";
		exit(EXIT_FAILURE);
	}
	// open intermediate file to write generated source program
	outFile.open("assemble.c");
	listFile.open("listingFile.txt");
}
void Pass1::closeFile() {
	//closing input file
	inputFile.close();

	//closing intermediate file
	outFile.close();

	//closing listing file
	listFile.close();

}

string Pass1::getlineStr(int lineCnt) {
	stringstream ss;
	ss << lineCnt;
	string out;
	ss >> out;
	return string(3, ' ').append(out);
}
string Pass1::getComment(string in) {
	int dotIndex = in.find_first_of('.');
	if (dotIndex == -1) {// line is without comment
		return "";
	} else {
		return in.substr(dotIndex, in.length());
	}
}

string Pass1::getOutFormat(string inputLine) {
	int originalLOCCTR = LOCCTR;

	string outputLine = getParser(inputLine);
	string original = outputLine;//save original string in case of a warning

	string label, opcode, operand, comment;

	outputLine = trim(original);
	if (outputLine[0] == '*') {
		errorDetected = 1;
		return original + "\n" + inputLine;
	}
	if (outputLine[0] == '.')
		return original;

	string beforeComment = getBeforeComment(outputLine);
	comment = getComment(outputLine);

	if (comment.length() > COMMENT_LENGTH)
		return "     ****Error: Comment length exceeded\n" + getlineStr(
				lineCounter++) + "\n" + original;

	list.clear();

	tokenize(beforeComment);
	int nTokens = countTokens(beforeComment);

	if (nTokens == 1) {
		if (list[0].length() > MNEMONIC_LENGTH) {
			return "     ****Error: Mnemonic length exceeded\n" + getlineStr(
					lineCounter++) + "\n" + original;
		}
		opcode = list[0];
	} else if (nTokens == 2) {
		if (twoTokensCase == 1) {
			label = list[0];
			opcode = list[1];

			if (opcode == "START")//assign starting address
				originalLOCCTR = LOCCTR;

			if (label.length() > LABEL_LENGTH)
				return "     ****Error: Label length exceeded\n" + getlineStr(
						lineCounter++) + "\n" + original;

			if (opcode.length() > MNEMONIC_LENGTH)
				return "     ****Error: Mnemonic length exceeded\n"
						+ getlineStr(lineCounter++) + "\n" + original;
		} else if (twoTokensCase == 2) {
			opcode = list[0];
			operand = list[1];

			if (opcode.length() > MNEMONIC_LENGTH)
				return "     ****Error: Mnemonic length exceeded\n"
						+ getlineStr(lineCounter++) + "\n" + original;

			if (operand.length() > OPERAND_LENGTH)
				return "     ****Error: Operand length exceeded\n"
						+ getlineStr(lineCounter++) + "\n" + original;
		}
	} else {//tokens ==3
		label = list[0];
		opcode = list[1];
		operand = list[2];

		if (opcode == "START")//assign starting address
			originalLOCCTR = LOCCTR;

		if (label.length() > LABEL_LENGTH)
			return "      ****Error: Label length exceeded\n" + getlineStr(
					lineCounter++) + "\n" + original;

		if (opcode.length() > MNEMONIC_LENGTH)
			return "     ****Error: Mnemonic length exceeded\n" + getlineStr(
					lineCounter++) + "\n" + original;

		if (operand.length() > OPERAND_LENGTH)
			return "     ****Error: Operand length exceeded\n" + getlineStr(
					lineCounter++) + "\n" + original;
	}

	string lineCnt;
	std::stringstream out;
	out << (lineCounter++);
	lineCnt = out.str();

	lineCnt = string(5 - lineCnt.length() + 1, ' ').append(lineCnt) + " ";
	if (opcode == "EQU")
		originalLOCCTR = symTable->get(label)->getAddress();

	string outFormat = lineCnt + " " + getHexaValue(originalLOCCTR, 3) + " ";

	outFormat += string(LABEL_LENGTH - label.length(), ' ').append(label) + " ";
	outFormat += string(MNEMONIC_LENGTH - opcode.length(), ' ').append(opcode)
			+ "  ";
	outFormat += string(OPERAND_LENGTH - operand.length(), ' ').append(operand)
			+ " ";
	outFormat += string(COMMENT_LENGTH - comment.length(), ' ').append(comment)
			+ " ";

	return outFormat;
}

void Pass1::readInstructions() {
	string inputLine, outputLine;

	while (getline(inputFile, inputLine) && !endFlag) {
		outputLine = getOutFormat(inputLine);
		outFile << outputLine << endl;
	}
	lengthOfProgram = LOCCTR - startLocation;

	symTable->show(listFile);
	litTable->print(listFile);
	closeFile();
}

string Pass1::getParser(string input) {

	// Transform string to upper case
	std::transform(input.begin(), input.end(), input.begin(), ::toupper);
	input = trim(input);

	list.clear();// removes all elements in the vector
	string output;
	unsigned int len = input.length();
	if (len == 0) {//empty string
		output = "     ****Error: Empty string";
	} else if (input[0] == '.') {//comment line
		if (len > ALL_LENGTH) {
			output = "     ****Error: Comment line length exceeded.";
		} else {
			output = input;
		}
	} else {
		input = getBeforeComment(input);// trimming comments
		int nTokens = countTokens(input);
		tokenize(input);// splitting line into tokens

		if (nTokens > 3) {// invalid extra fields are used
			output = "     ****Error: Invalid extra fields";
		} else if (nTokens == 1) {
			// Case : OpCode only
			string currToken = list[0];

			if (currToken == "END") {
				LOCCTR = litTable->setAddresses(LOCCTR);
				endFlag = 1;
				return input;
			}

			if (opTable->contains(currToken)) {// valid mnemonic
				string empty("");
				string check = checkOperand("", currToken, empty, 1);

				if (check.length() == 0)
					output = input;
				else
					output = check;

			} else if ((*directives).find(currToken) != (*directives).end()) {// directive
				string check = checkDirectives("", currToken, "");
				if (check.length() == 0)
					output = currToken;
				else
					output = check;

			} else {
				output = "     ****Error: Mnemonic '" + currToken
						+ "' not found";
			}
		} else {
			if (nTokens == 2) {
				string firstToken = list[0];
				string secondToken = list[1];

				/* Case1: Label - opcode
				 * Case2 :OpCode - Operand
				 */

				if (opTable->contains(firstToken)) {
					if (opTable->contains(secondToken) || (*directives).find(
							secondToken) != (*directives).end()) {
						output = "     ****Error: Invalid label '" + firstToken
								+ "', reserved word";
					} else {
						//Case2:
						//Validating operand
						int instFormat = 3;
                        string oldOperand = secondToken;

						if (firstToken[0] == '+') {
							firstToken = firstToken.substr(1,
									firstToken.length());
							instFormat = 4;
						}

                        string check = checkOperand("", firstToken,	secondToken, instFormat);

                        if (secondToken != oldOperand){
                            int index = input.find(oldOperand);
                            input.replace(index,oldOperand.length(),secondToken);
                        }
						if (check.length() == 0)//no errors
							output = input;
						else
							output = check;//error occured
						twoTokensCase = 2;
					}
				} else if ((*directives).find(firstToken)
						!= (*directives).end()) {
					if ((*directives).find(secondToken) != (*directives).end()
							|| opTable->contains(secondToken)) {
						output = "     ****Error: Invalid label '" + firstToken
								+ "', reserved word";
					} else {
						//Case2:
						//Validating operand
						string check = checkDirectives("", firstToken,
								secondToken);

						if (check.length() == 0)//no errors
							output = input;
						else
							output = check;//error occured
						twoTokensCase = 2;
					}
				} else {
					//Case 1
					if (opTable->contains(secondToken)) {
						//then firstToken is a label

						if (secondToken[0] == '+') {
							secondToken = secondToken.substr(1,
									secondToken.length());
						}
						string empty("");
						string check = checkOperand(firstToken, secondToken,
								empty, 1);

						if (check.length() == 0)
							output = input;
						else
							output = check;
						twoTokensCase = 1;
					} else {
						output
								= "     ****Error: Invalid instruction, Operation '"
										+ secondToken
										+ "' not found in OpTable";
					}
				}
			} else {
				// Case: label/opcode/operand
				string firstToken = list[0];
				string secondToken = list[1];
				string thirdToken = list[2];

				if (opTable->contains(firstToken) || (*directives).find(
						firstToken) != (*directives).end()) {
					output = "     ****Error: Invalid label '" + firstToken
							+ "', reserved word";
				} else {
					if (opTable->contains(secondToken)) {

						int instFormat = 3;
						if (secondToken[0] == '+') {
							secondToken = secondToken.substr(1,
									secondToken.length());
							instFormat = 4;
						}

                        string oldOperand = thirdToken ;
						//Validating operand
						string check = checkOperand(firstToken, secondToken,
								thirdToken, instFormat);

                        if (thirdToken != oldOperand){
                            int index = input.find(oldOperand);
                            input.replace(index,oldOperand.length(),thirdToken);
                        }

						if (check.length() == 0) {//no errors
							output = input;
						} else
							output = check;//error occured

					} else if ((*directives).find(secondToken)
							!= (*directives).end()) {
						if ((*directives)[secondToken]->format > 1) {
							//Validating operand
							string check = checkDirectives(firstToken,
									secondToken, thirdToken);

							if (check.length() == 0) {//no errors
								output = input;
							} else
								output = check;//error occured
						} else {
							output
									= "     ****Error: Mnemonic error, NO operand here";
						}
					} else {
						output
								= "     ****Error: Invalid instruction, standard format not matched";
					}
				}
			}
		}
		startFlag = 1;
	}
	return output;
}

int Pass1::hexaParser(string s) {
	int x = 0;
	char c;
	std::stringstream ss(s);
	ss << std::hex;
	ss >> x;
	if (ss.fail() || ss.get(c))
		return -1;
	return x;
}

int Pass1::intParser(string s) {
	int x = 0;
	char c;
	std::stringstream ss(s);
	ss >> x;
	if (ss.fail() || ss.get(c))
		return -100000;
	return x;
}
string Pass1::insertSymTab(string token, char type) {
	if (!symTable->contains(token)) {
		symTable->insert(token, LOCCTR, type);
		return "";
	} else {
//		std::cerr << "     ****Error: Duplicate label'" + token + "'" << endl;
        return "     ****Error: Duplicate label ' " + token + " '";
	}
}

void Pass1::MakeDirectives() {
	directives = new map<string, Opcode*> ;
	directives->insert(make_pair("BASE", new Opcode("BASE", 0, 2)));
	directives->insert(make_pair("NOBASE", new Opcode("NOBASE", 0, 1)));
	directives->insert(make_pair("START", new Opcode("START", 0, 2)));
	directives->insert(make_pair("END", new Opcode("END", 0, 2)));
	directives->insert(make_pair("LTORG", new Opcode("LTORG", 0, 1)));
	directives->insert(make_pair("ORG", new Opcode("ORG", 0, 2)));
	directives->insert(make_pair("BYTE", new Opcode("BYTE", 0, 2)));
	directives->insert(make_pair("WORD", new Opcode("WORD", 0, 2)));
	directives->insert(make_pair("RESB", new Opcode("RESB", 0, 2)));
	directives->insert(make_pair("RESW", new Opcode("RESW", 0, 2)));
	directives->insert(make_pair("EQU", new Opcode("EQU", 0, 2)));
}
void Pass1::MakeRegisters() {
	registerTab = new map<char, char> ;
	registerTab->insert(make_pair('A', 0x00));
	registerTab->insert(make_pair('X', 0x01));
	registerTab->insert(make_pair('L', 0x02));
	registerTab->insert(make_pair('B', 0x03));
	registerTab->insert(make_pair('S', 0x04));
	registerTab->insert(make_pair('T', 0x05));
	registerTab->insert(make_pair('F', 0x06));

}

string Pass1::checkDirectives(string label, string directive, string operand) {
	int length = (*directives)[directive]->format;
	if (length == 1) {
		if (label.length() != 0 || operand.length() != 0) {
			std::cerr << "     ****Error: Extra operand detected" << endl;
			return "     ****Error: Extra operand detected '" + operand + "'";
		} else {
			if (directive == "LTORG") {
				LOCCTR = litTable->setAddresses(LOCCTR);
			} else if (directive == "BASE") {
				if (operand[0] == '#' || operand[0] == '@')
					return "     ****Error: BASE operand can't be set using indirect or immediate addressing '"
							+ operand + "'";
				else
					return "";
			} else if (directive == "NOBASE") {
				if (operand != "")
					return "     ****Error: NOBASE can't have an operand '"
							+ operand + "'";
				return "";
			} else {
				return "";
			}
		}
	} else {
		if (directive == "RESW") {
			int value = intParser(operand);
			if (value <= 0) {
                std::cerr << "     ****Error: Invalid operand " << endl;
				return "     ****Error: Invalid operand '" + operand + "'";
			} else {
				if (label.size() != 0) {
					string tmp = insertSymTab(label, 'R');
					if (tmp.size() != 0) {
						return tmp;
					}
				}
				LOCCTR += (value * 3);
			}
		} else if (directive == "RESB") {
			int value = intParser(operand);
			if (value <= 0) {
				std::cerr << "     ****Error: Invalid operand" << endl;
				return "     ****Error: Invalid operand '" + operand + "'";
			} else {
				if (label.size() != 0) {
					string tmp = insertSymTab(label, 'R');
					if (tmp.size() != 0) {
						return tmp;
					}
				}
				LOCCTR += value;
			}
		} else if (directive == "WORD") {
			int value = intParser(operand);
			if (value > 65534 || value < -65535) {
				std::cerr << "     ****Error: Invalid operand" << endl;
				return "     ****Error: Invalid operand '" + operand + "'";
			} else {
				if (label.size() != 0) {
					string tmp = insertSymTab(label, 'R');
					if (tmp.size() != 0) {
						return tmp;
					}
				}
				LOCCTR += 3;
			}
		} else if (directive == "BYTE") {
			//I must convert the operand to an integer values
			//the operand is in hex value
			//to know the length of this operand then add this to lOCCTR
			if (operand[0] == 'C' && operand[1] == 39
					&& operand[operand.length() - 1] == 39 && operand.length()
					< 19 && operand.length() > 3) {
				if (label.size() != 0) {
					string tmp = insertSymTab(label, 'R');
					if (tmp.size() != 0) {
						return tmp;
					}
				}
				LOCCTR += operand.length() - 3;

			} else if (operand[0] == 'X' && operand[1] == 39
					&& operand[operand.length() - 1] == 39 && operand.length()
					< 18 && operand.length() > 3) {
				if (label.size() != 0) {
					string tmp = insertSymTab(label, 'R');
					if (tmp.size() != 0) {
						return tmp;
					}
				}
				LOCCTR += (operand.length() - 3) / 2;
				if ((operand.length() & 1) == 0)
					LOCCTR++;
			} else {
				std::cerr << "invalid operand" << endl;
				return "     ****Error: Invalid operand '" + operand
						+ "' for BYTE directive";
			}
		} else if (directive == "EQU") {
			Symbol* testExp = validateExpressions(operand);
			if (testExp->getType() != '-') {
				if (symTable->contains(label)) {
					std::cerr << "     ****Error: Duplicate Symbol defined"
							<< endl;
					return "     ****Error: Duplicate Symbol defined '" + label
							+ "'";
				} else {
					symTable->insert(label, testExp->getAddress(),
							testExp->getType());
				}
			} else {
				std::cerr << testExp->getLabel() << endl;
				return testExp->getLabel();
			}
		} else if (directive == "START" || directive == "END") {
			return checkStartEnd(label, directive, operand);
		} else if (directive == "ORG") {
			if (operand[0] == '#') {
				int value = intParser(operand.substr(1, operand.size()));
				if (value < -65535 || value > 65534) {
					std::cerr << "     ****Error: Invalid operand";
					return "     ****Error: Invalid operand '" + operand
							+ "' for ORG directive";
				} else
					LOCCTR = value;
			} else {
				Symbol* testExp = validateExpressions(operand);
				cout << testExp->getType() << endl;
				if (testExp->getType() != '-') {
					LOCCTR = testExp->getAddress();
				} else {
					std::cerr << "     ****Error: Operand expression '"
							+ operand + "' not valid" << endl;
					return "     ****Error: Operand expression '" + operand
							+ "' not valid";
				}
			}
		} else {
			// do nothing
			return "";
		}
	}
	return "";
}

void Pass1::tokenize(string input) {
	istringstream iss(input);

	while (iss) {
		string sub;
		iss >> sub;
		list.push_back(sub);
	}
	list.pop_back();//removing EOF character
}

unsigned int Pass1::countTokens(string s) {
	stringstream stream(s);
	string word;
	unsigned int count = 0;

	while (stream >> word)
		++count;

	return count;
}

string Pass1::trim(string in) {
	if (in[0] == ' ')
		in = in.erase(0, in.find_first_not_of(" "));
	if (in[in.length() - 1] == ' ')
		in = in.erase(in.find_last_not_of(" ") + 1, in.length());
	return in;
}

string Pass1::getBeforeComment(string in) {
	int dotIndex = in.find_first_of('.');
	if (dotIndex == -1) {// line is without comment
		return in;
	} else {
		return in.substr(0, dotIndex);
	}
}

string Pass1::getHexaValue(int num, int width) {
	stringstream s;
	s << hex << num;
	string temp = s.str();
	std::transform(temp.begin(), temp.end(), temp.begin(), ::toupper);
	return fitString(temp, width - temp.length() + 1, width);
}

string Pass1::fitString(string in, int num, int width) {
	if (num < 0)
		return in.substr(in.length() - width - 1, width + 1);

	return string(num, '0').append(in);
}

Symbol* Pass1::validateExpressions(string expression) {

	vector<TERM> tokensVector = splitExpression(expression);// splitting expression into tokens

	replaceLocationAsterisk(tokensVector);// replace * by current location counter

	if (tokensVector.size() == 1)// one token only
	{
		TERM token = tokensVector[0];
		if (token.type == ABSOLUTE)
			return new Symbol("", atoi(token.str.c_str()), 'A');
		else if (token.type == RELATIVE)
			return new Symbol("", atoi(token.str.c_str()), 'R');
		else
			return new Symbol("     ****Error: EQU Operand format error", 0,
					'-');

	} else {// expression found

		//converting to postfix notation
		vector<TERM> postfix;
		string test = infixToPostfix(postfix, tokensVector);

		if (test.length() != 0) // error detected
			return new Symbol(test, 0, '-');

		return getExpressionValue(postfix);
	}
}

vector<TERM> Pass1::splitExpression(string exp) {
	string tempStr;
	vector<TERM> vc;

	for (unsigned int i = 0; i < exp.length(); i++) {
		char current = exp[i];
		if (operators.find(current) != string::npos || current == '('
				|| current == ')') { //operator or paren
			if (tempStr.length() > 0) {
				TERM tempTr;
				tempTr.str = tempStr;
				tempTr.type = getType(tempStr);

				if (tempTr.type == RELATIVE) {
					if (symTable->contains(tempTr.str)) {
						char test = symTable->get(tempTr.str)->getType();
						if (test == 'R')
							tempTr.type = RELATIVE;
						else
							tempTr.type = ABSOLUTE;
					}
				}
				vc.push_back(tempTr);
				tempStr = "";
			}
			TERM tempTr;
			tempTr.str = string(&current, 1);
			tempTr.type = NEUTRAL;
			vc.push_back(tempTr);
		} else {//operand
			tempStr += string(&current, 1);
		}
	}
	if (tempStr.length() > 0) {
		TERM tempTr;
		tempTr.str = tempStr;
		tempTr.type = getType(tempStr);
		vc.push_back(tempTr);
		tempStr = "";
	}
	return vc;
}

int Pass1::getType(string s) {
	unsigned int i = 0;
	if (s.length() > 0 && s[0] == '-')
		i++;

	for (; i < s.length(); i++)
		if (!(s[i] >= '0' && s[i] <= '9'))
			return RELATIVE;

	return ABSOLUTE;
}

void Pass1::replaceLocationAsterisk(vector<TERM> &tokensVector) {
	for (unsigned int i = 0; i < tokensVector.size(); i++) {
		TERM current = tokensVector[i];
		string currStr = current.str;

		if (currStr == "*") {
			if (i == 0 || i == tokensVector.size() - 1 || tokensVector.size()
					== 1 || (operators.find(tokensVector[i - 1].str)
					!= string::npos && operators.find(tokensVector[i + 1].str)
					!= string::npos)
					|| (operators.find(tokensVector[i - 1].str) != string::npos
							&& tokensVector[i + 1].str == ")")
					|| (operators.find(tokensVector[i + 1].str) != string::npos
							&& tokensVector[i - 1].str == "(")
					|| (tokensVector[i - 1].str == "("
							&& tokensVector[i + 1].str == ")")) {
				stringstream ss;
				ss << LOCCTR;
				tokensVector[i].str = ss.str();
				tokensVector[i].type = RELATIVE;
			}
		}
	}
}

string Pass1::infixToPostfix(vector<TERM> &postfix, vector<TERM> vc) {
	stack<TERM> st;

	/*  INFIX --> POSTFIX
	 Go through each character in the string
	 If it is between 0 to 9, append it to output string.
	 If it is left brace push to stack
	 If it is operator *+-/ then
	 If the stack is empty push it to the stack
	 If the stack is not empty then start a loop:
	 If the top of the stack has higher precedence
	 Then pop and append to output string
	 Else break
	 Push to the stack

	 If it is right brace then
	 While stack not empty and top not equal to left brace
	 Pop from stack and append to output string
	 Finally pop out the left brace.

	 If there is any input in the stack pop and append to the output string.
	 */
	for (unsigned int i = 0; i < vc.size(); i++) {
		TERM currTerm = vc[i];
		if (currTerm.type != NEUTRAL) {
			postfix.push_back(currTerm);
		} else if (currTerm.str == "(") {
			st.push(currTerm);
		} else if (currTerm.str == ")") {
			bool testParen = false;
			while (!st.empty()) {
				TERM tempTERM = st.top();
				st.pop();
				if (tempTERM.str == "(") {
					testParen = true;
					break;
				}
				postfix.push_back(tempTERM);
			}
			if (!testParen) {
				std::cerr << "Missing opening paren";
				return "     ****Error: EQU Operand format error (Illegal operation: missing opening paren)";
			}
		} else if (currTerm.type == NEUTRAL) {//operator
			if (st.empty()) {
				st.push(currTerm);
			} else {
				while (!st.empty()) {
					TERM top = st.top();
					st.pop();
					string topElement = top.str;
					string currElement = currTerm.str;
					int first = (*operatorMap)[topElement];
					int second = (*operatorMap)[currElement];

					if (topElement != "(" && (first >= second)) {
						postfix.push_back(top);
					} else {
						st.push(top);
						break;
					}
				}
				st.push(currTerm);
			}
		}
	}
	while (!st.empty()) {
		TERM top = st.top();
		if (top.str == "(") {
			std::cerr << "Missing closing paren";
			return "     ****Error: EQU Operand format error (Illegal operation: missing closing paren)";
		}
		postfix.push_back(top);
		st.pop();
	}
	return "";
}

Symbol* Pass1::getExpressionValue(vector<TERM> postfix) {
	stack<TERM> stk;

	for (unsigned int i = 0; i < postfix.size(); i++) {
		TERM currTerm = postfix[i];
		string currStr = currTerm.str;
		int currType = currTerm.type;

		if (currType != NEUTRAL) {
			stk.push(currTerm);
		} else {
			if (stk.size() < 2) {
				return new Symbol("     ****Error: EQU Operand format error '"
						+ currStr + "'", 0, '-');
			} else {
				TERM second = stk.top();
				stk.pop();
				TERM first = stk.top();
				stk.pop();

				if (currStr == "*") {
					if (first.type && second.type) {// multiplying two relative terms is invalid
						return new Symbol(
								"     ****Error: EQU Operand format error (Illegal operation: multiplying two relative terms ("
										+ first.str + "-" + second.str + "))",
								0, '-');
					}
					TERM combTerm;
					string test = getTermsValue(first, second, "*", combTerm);

					if (test.length() != 0)
						return new Symbol(test, 0, '-');

					stk.push(combTerm);
				} else if (currStr == "/") {
					if (first.type && second.type) {// dividing two relative terms is invalid
						return new Symbol(
								"     ****Error: EQU Operand format error (Illegal operation: dividing two relative terms ("
										+ first.str + "-" + second.str + "))",
								0, '-');
					}
					TERM combTerm;
					string test = getTermsValue(first, second, "/", combTerm);

					if (test.length() != 0)
						return new Symbol(test, 0, '-');

					stk.push(combTerm);

				} else if (currStr == "+") {
					TERM combTerm;
					string test = getTermsValue(first, second, "+", combTerm);

					if (test.length() != 0)
						return new Symbol(test, 0, '-');

					stk.push(combTerm);

				} else { //  - operator
					TERM combTerm;
					string test = getTermsValue(first, second, "-", combTerm);

					if (test.length() != 0)
						return new Symbol(test, 0, '-');

					stk.push(combTerm);
				}
			}
		}
	}
	if (stk.size() != 1)
		return new Symbol("     ****Error: Expression format error", 0, '-');

	TERM finalTerm = stk.top();
	stk.pop();

	if (finalTerm.type != 0 && finalTerm.type != 1)
		return new Symbol(
				"     ****Error: EQU Operand format error (Illegal: invalid relative expression)",
				0, '-');
	char type = finalTerm.type == ABSOLUTE ? 'A' : 'R';
	return new Symbol("", atoi(finalTerm.str.c_str()), type);
	// if an error occured sysmbol's label is the error message
	// if no error occured sysmbol's label is Empty, symbol's address the the value of the Expression
	// ***** finalTerm.str is the integer value as a string, finalTerm.type = ABSOLUTE aw RELATIVE
}

string Pass1::getTermsValue(TERM first, TERM second, string op, TERM &combTerm) {
	int one = 0, two = 0;
	if (getType(first.str) == ABSOLUTE) {
		one = atoi(first.str.c_str());
	} else {//relative
		if (symTable->contains(first.str)) {
			one = symTable->get(first.str)->getAddress();
		} else {
			return "     ****Error: EQU Operand format error (label '"
					+ first.str + "' not found in Symbol table";
		}
	}
	if (getType(second.str) == ABSOLUTE) {
		two = atoi(second.str.c_str());
	} else {//relative
		if (symTable->contains(second.str)) {
			two = symTable->get(second.str)->getAddress();
		} else {
			return "     ****Error: EQU Operand format error (label '"
					+ second.str + "' not found in Symbol table";
		}
	}
	stringstream ss;

	if (op == "*") {
		if (second.type == ABSOLUTE)
			combTerm.type = first.type * two;
		else
			combTerm.type = second.type * one;

		ss << (one * two);
	} else if (op == "/") {
		if (second.type == ABSOLUTE)
			combTerm.type = first.type / two;
		else
			combTerm.type = one / second.type;
		ss << (one / two);
	} else if (op == "+") {
		combTerm.type = first.type + second.type;
		ss << (one + two);
	} else {
		combTerm.type = first.type - second.type;
		ss << (one - two);
	}
	combTerm.str = ss.str();
	return "";
}

string Pass1::checkStartEnd(string label, string opcode, string operand) {
	if (opcode == "START") {
		if (startFlag) {
			std::cerr
					<< "     ****Error: Invalid instruction, START must be in the first line"
					<< endl;
			return "     ****Error: Invalid instruction, START must be in the first line";
		} else {
			startLocation = hexaParser(operand);
			if (startLocation == -1) {
				std::cerr
						<< "     ****Error: Start address not valid, setting location counter to 0"
						<< endl;
				return "     ****Error: Start address not valid, setting location counter to 0";
			} else {
				if (label.length() != 0)// program has a specific name
					progName = label;

				LOCCTR = startLocation;
				if (label.size() != 0) {
					insertSymTab(label, 'R');
				}
				return "";
			}
		}
	}

	if (opcode == "END") {
		if (label.length() != 0) {// end label must be blank
			std::cerr << "     ****Error: END label field must be blank"
					<< endl;
			return "     ****Error: END label field must be blank";
		}

		if (operand != progName) {
			std::cerr
					<< "     ****Error: END operand doesn't match program name"
					<< endl;
			return "     ****Error: END operand doesn't match program name";
		}

		// tell litTable to assign addresses and then set the LOCCTR
		LOCCTR = litTable->setAddresses(LOCCTR);
		endFlag = 1;
		return "";
	} else
		return "";
}

string Pass1::checkOperand(string label, string opcode, string &operand,
		int format) {

	if (!label.empty()) {
		string s = insertSymTab(label, 'R');
		if (s.length() != 0) {
            std::cerr << "     ****Error: Duplicate Label Detected " + label
					<< endl;
			return "     ****Error: Duplicate Label Detected '" + label + "'";
		}
	}
	int advance = 0;
	Opcode* menemonic = opTable->get(opcode);
	int length = menemonic->format;

	if (length == 1) {
		if (!operand.empty()) {
			std::cerr
					<< "     ****Error: Extra argument detected (given format 1)"
					<< endl;
			return "     ****Error: Extra argument detected (given format 1)";
		} else {
			LOCCTR++;
			return "";
		}

	} else if (length == 2) {
		if (operand.length() == 1 && (*registerTab).find(operand[0])
				!= (*registerTab).end()) {
			if (regNum(opcode) != 1)
				return "     ****Error: Opcode '" + opcode
						+ "' must take two registers";

			LOCCTR += 2;
			return "";
		} else if (operand.length() == 3 && (*registerTab).find(operand[0])
				!= (*registerTab).end() && (*registerTab).find(operand[2])
				!= (*registerTab).end() && operand[1] == ',') {// referenced registers are in the regTable
			if (regNum(opcode) != 2)
				return "     ****Error: Opcode '" + opcode
						+ "' must take one register";
			LOCCTR += 2;
			return "";
		} else {
			std::cerr << "     ****Error: Not supported register '" << operand
					<< "'" << endl;
			return "     ****Error: Not supported register '" + operand + "'";
		}

	} else if (length == 3 || length == 4) {

		bool isExpression = false;
		for (unsigned int i = 0; i < operand.length() && !isExpression; i++) {
			if (operand[i] == '+' || operand[i] == '-' || operand[i] == '/'
					|| operand[i] == '*') {
				//                cerr<<operand<<"  "<<operand[i] << "  "<<i<<endl;
				isExpression = true;
			}
		}

		if (isExpression) {
			Symbol* exp = validateExpressions(operand);
			if (exp->getType() == '-') {
				//error detected
				return exp->getLabel();
			}

			stringstream ss;
			ss << ExprCounter++;
			operand = string("EX^" + ss.str());
			symTable->insert(operand, exp->getAddress(), exp->getType());

		} else {
			if (operand[0] == '#') {
				if (operand[1] >= '0' && operand[1] <= '9') {
					//#int eg #543
					for (unsigned int i = 2; i < operand.length() - 1; i++) {
						if (operand[i] < '0' || operand[i] > '9') {
							std::cerr
									<< "     ****Error: Operand format error (can't parse integer)"
									<< endl;
							return "     ****Error: Operand format error (can't parse integer) '"
									+ operand + "'";
						}
						string str = operand.substr(1, operand.length() - 1);
						int value = atoi(str.c_str());
						if (value > 4096) {
							std::cerr
									<< "     ****Error: Operand out of supported length"
									<< endl;
							return "     ****Error: Operand out of supported length '"
									+ operand + "'";
						}
					}
				}

			} else if (operand[0] == '@') {
				if (operand[1] < 'A' || operand[1] > 'Z') {
					std::cerr << "     ****Error: Operand format error "
							<< endl;
					return "     ****Error: Operand format error '" + operand
							+ "'";
				}

			} else if (operand[0] == '=') {
				int tempInt = litTable->addLit(operand);
				if (tempInt == -1) {
					std::cerr << "     ****Error: Invalid Literal '" + operand
							+ "'" << endl;
					return "      ****Error: Invalid Literal '" + operand + "'";
				}
			}
			// if not one of the above then direct addressing is used;


			if (opcode == "RSUB") {
				if (operand != "") {
					std::cerr << "     ****Error: Extra operand detected '"
							+ operand + "'" << endl;
					return "     ****Error: Extra operand detected '" + operand
							+ "'";
				}
                advance = 3;
			} else if (operand.empty()) {
				std::cerr << "     ****Error: Operand not found " << endl;
				return "     ****Error: Operand not found ";
			}
		}
        if (opcode != "RSUB")
            advance = (format == 3) ? 3 : 4;
        //or u can use advance = length == 3 ? 3:4;
	}
	LOCCTR += advance;
	return "";
}

int Pass1::regNum(string opcode) {
	if (opcode == "TIXR" || opcode == "CLEAR")
		return 1;
	return 2;
}

int main(int argc, char* argv[]) {
	cout << "SIC/XE Assembler" << endl;
	//check command line for input file
	string fileName;
	if (argc > 1)
		fileName = argv[1];
	else
        fileName = "testy.txt";

	Pass1* pass1 = new Pass1(fileName);
	pass1->readInstructions();

	if (!pass1->errorDetected) {
		Pass2* pass2 = new Pass2("assemble.c", pass1);
		pass2->readInstructions();
	}
	return 0;
}
