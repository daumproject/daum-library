#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <pthread.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include "/home/jed/pocXenomai/org.kevoree.nativeNode/org.kevoree.nativeNode.native/src/main/c/native.h"


int start()
{
	printf("Component starting \n");

	while(1)
	{

		printf("ALIVE \n");
		sleep(1);
	}
}


int stop()
{
	printf("Component stoping \n");

}




void sig_handler(int sig) 
{
   ctx->stop(); 
  ctx->pid  = -1;
   exit(0);
}





int main (int argc,char *argv[])
{
	if(argc >1)
	{
		 signal(SIGINT, sig_handler);
		ctx = getContext(atoi(argv[1]));
		ctx->pid =getpid();  
		printf("shared memory key %d and pid %d\n",atoi(argv[1]),ctx->pid);
		register_start(&start);
		register_stop(&stop);
		ctx->start();
	
	 }
	
}

