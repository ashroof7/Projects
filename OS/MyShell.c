/*
 ============================================================================
 Name        : SimpleShell.c
 Author      : Ashraf Saleh
 Version     : 1.0
 Copyright   : leonardo_aly7 on github or 
 Description : Simple Shell on linux environment, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <unistd.h>
#include <errno.h>
#include <sys/param.h>

#define MAX_BUFR 300
#define MAX_ARGS 100
char lineBuffer[MAX_BUFR] ;
char* cur_arg;
char* args[MAX_ARGS]; // arguments of a command
int  argc ;// number of arguments
FILE *log_file;
// related to directory handling
char workingDirectory[PATH_MAX];
char oldWorkingDirectory[PATH_MAX];
char default_path[100];
char host_name[HOST_NAME_MAX] = "host" ;
char usr_name[LOGIN_NAME_MAX] = "user";


void parentHandle(pid_t child, int _wait){
	int status;
	if (_wait){
		waitpid(child, &status,0);
		fprintf(log_file,"%d\t terminated \t SIGCHILD \t %d\n",child,status);
		printf("%d\t terminated \t SIGCHILD \t %d\n",child,status);
		fflush(log_file);
	}
}

void childHandle (){
	execvp(args[0],args);
	// if the program arrives here, an error must be happened
	perror("");
	exit(1);
}

void childSignalHandler(int signum){
	pid_t pid;
	// using the while loop to ensure that all terminted process are killed (no zombie)
	int status;
	while ((pid = waitpid(-1, &status, WNOHANG )) > 0){
		fprintf(log_file,"%d\t terminated \t SIGCHILD \t %d\n",pid,status);
		fprintf(stderr,"%d\t terminated \t SIGCHILD \t %d\n",pid,status);
	}
	fflush(log_file);
}

void HandleDirectoryCommands(){
	if (argc == 1)// "cd"
		strcpy(workingDirectory, default_path);

	else //"cd ..", "cd {Directory}" or "cd {absolute path}"
		strcpy(workingDirectory,args[1]);

	int status = chdir(workingDirectory);
	if (status != 0)
		perror("");
		
	getcwd(workingDirectory,PATH_MAX);
}

int main() {
	// setting the default path to the path where the executable file exists
	getcwd(default_path,100);//setting the default path
	log_file = fopen("log.txt", "w");
	const	char* delimeter = " \t\n";
	const char* exitCommand = "exit";
	int wait_request;

	//register new signal handler for SIGCHILD
	signal(SIGCHLD, childSignalHandler);
	//retrieving the host name and username 
	gethostname (host_name,HOST_NAME_MAX);
	getlogin_r(usr_name, LOGIN_NAME_MAX);

	//set working directory
	chdir("/home");
	getcwd(workingDirectory,PATH_MAX);

	while (1){
		printf("%s@%s~%s$ ",usr_name,host_name,workingDirectory);
		fgets(lineBuffer,MAX_BUFR,stdin);

		//tokenizing the lineBuffer
		argc = 0 ;
		args[argc++] = strtok(lineBuffer,delimeter);
		while ((args[argc++] = strtok(NULL,delimeter) ) != NULL && argc<MAX_ARGS);
		argc--;

		//checking if command == "exit"
		if(strcmp(args[0], exitCommand) == 0){
			printf("closing\n");
			fclose(log_file);
			exit(0);
		}
		// changing directory
		else if (strcmp(args[0], "cd") == 0){
			HandleDirectoryCommands();
			continue ;
		}

		// checking the '&'
		wait_request = 1;
		if (strcmp(args[argc-1],"&") == 0){
			args[argc-1] = NULL;
			wait_request = 0;
		}


		// forking new process
		pid_t pid = fork();
		if ( pid > 0 )
			parentHandle(pid, wait_request); //parent process
		else if (pid == 0)
			childHandle(); //child process
		else {
			//failed to fork
			perror("failed to fork a new child\n");
		}
	}

	fclose(log_file);
	return 0;
}

