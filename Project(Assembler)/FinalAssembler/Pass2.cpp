#include "Pass2.h"
#include "SymTab.h"
#include "Symbol.h"
#include "OpTab.h"
#include "Opcode.h"
#include "Reader.h"
#include "Writer.h"
#include "OpCodeParser.h"
#include "Pass1.h"

Pass2::Pass2(string assembFile, Pass1* pass1) {
	pass_1 = pass1;
	symTable = pass1->symTable;
	opTable = pass1->opTable;
	litTable = pass1->litTable;
	regTab = pass1->registerTab;
	directives = pass1->directives;
	pass_1 = pass1;
	reader = new Reader(assembFile);
	writer = new Writer("object.asm");
	opParser = new OpCodeParser(symTable, regTab, litTable);
	listFile.open("listingFile.txt", ios::app);
}

void Pass2::readInstructions() {
	cout << endl << "           Program Listing" << endl;
	cout << "-------------------------------------------------" << endl;
	listFile << endl << "           Program Listing" << endl;
	listFile << "-------------------------------------------------" << endl;

	reader-> advance();
	stringstream stream;
	stream << hex << pass_1->startLocation;
	string strtLoc = stream.str();
	transform(strtLoc.begin(), strtLoc.end(), strtLoc.begin(), ::toupper);
	stream.str("");//==.clear
	stream << hex << pass_1->lengthOfProgram;
	string len = stream.str();
	transform(len.begin(), len.end(), len.begin(), ::toupper);

	if (reader->getLabel().length() != 0) {
		writer->start(reader->getLabel() + "", strtLoc, len);
		cout << reader->getAddress() << "\t" << reader->getLabel() << "\t"
				<< "START" << "\t" << pass_1->startLocation << endl;
		listFile << reader->getAddress() << "\t" << reader->getLabel() << "\t"
				<< "START" << "\t" << pass_1->startLocation << endl;

	} else {
		writer->start("      ", strtLoc, len);
		cout << "     " << "\t" << "START" << "\t" << pass_1->startLocation
				<< "\t\t" << endl;
		listFile << "     " << "\t" << "START" << "\t" << pass_1->startLocation
				<< "\t\t" << endl;
	}

	// handles the flow of line parsing
	while (reader->advance()) {
		string opcode = reader->getOpcode();
		string operand = reader->getOperand();
		string currAddress = reader->getAddress();
		string nextAddress;
		if (opcode != "END")
			nextAddress = reader->getNextAddress();

		cout << currAddress << "\t" << reader->getLabel() << "\t" << opcode
				<< "\t" << operand << "\t\t";
		listFile << currAddress << "\t" << reader->getLabel() << "\t" << opcode
				<< "\t" << operand << "\t\t";

		if (opTable->contains(opcode)) {//valid opcode not directive
			bool needModRec = false;
			string objectCode = opParser->parse(
					pass_1->hexaParser(nextAddress), opcode, operand,
					needModRec);
			if (objectCode.substr(0, 5) == "error") {
				listFile << objectCode << endl;
				cout << "*** Error in operand: '" << operand << "' "
						<< objectCode.substr(5, objectCode.length() - 4)
						<< endl;
				continue;
			}
			if (needModRec) {
				writer->writeModRec(currAddress, 5);
			}

			cout << objectCode;
			listFile << objectCode;

			writer->writeTextRec(objectCode, currAddress, 0);
		} else {//directive
			if (opcode == "BYTE") {
				writer->writeTextRec(getByteValue(operand), currAddress, 0);

				cout << getByteValue(operand);
			} else if (opcode == "WORD") {
				writer->writeTextRec(operand, currAddress, 0);
			} else if (opcode == "BASE") {

				int i = 0;
				if (operand[0] >= '0' && operand[0] <= '9')//numeric value
					i = atoi(operand.c_str());
				else if (pass_1->symTable->contains(operand))
					i = pass_1->symTable->get(operand)->getAddress();
				else {
					cerr << "BASE operand is not found";
					listFile << "     ****Error: BASE operand is not found";
				}
				opParser->setBaseFlag(true, i);

			} else if (opcode == "NOBASE") {
				opParser->setBaseFlag(false, 0);
			} else if (opcode == "END") {
				setLiterals(currAddress);
				writer->end(strtLoc);

			} else if (opcode == "LTORG") {
				setLiterals(currAddress);
			} else if (opcode == "RESB" || opcode == "RESW" || opcode == "ORG") {
				// RESB, RESW, ORG
				writer->closeRecord();
			}
		}
		cout << "" << endl;
		listFile << "" << endl;
	}
}

void Pass2::setLiterals(string currAddress) {

	if (pass_1->litTable->ltorgQ->empty())
		return;
	vector<string>* literals = pass_1->litTable->ltorgQ->front();
	pass_1->litTable->ltorgQ->pop();

	vector<string>::iterator it;

	for (it = literals->begin(); it != literals->end(); it++) {
		string operand = *it;
		transform(operand.begin(), operand.end(), operand.begin(), ::toupper);
		writer->writeTextRec(operand, currAddress, 0);
	}
}

string Pass2::getByteValue(string in) {
	string res;
	if (in[0] == 'C') {//C'...'
		stringstream ss;
		for (unsigned int i = 2; i < in.length() - 1; i++)
			ss << hex << int(in[i]);
		res = ss.str();
	} else {//X'...'
		res = in.substr(2, in.length() - 3);
	}
	std::transform(res.begin(), res.end(), res.begin(), ::toupper);
	return res;
}
