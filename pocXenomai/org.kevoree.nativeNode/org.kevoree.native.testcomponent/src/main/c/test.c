#include "../../../../../org.kevoree.nativeNode/org.kevoree.nativeNode.native/src/main/c/component.h"
#include <stdio.h>


int start()
{
	fprintf(stderr,"Component starting \n");


}


int stop()
{
    fprintf(stderr,"Component stoping \n");
}


int update()
{
    fprintf(stderr,"Component updating \n");
}


 /********************** TODO GENERATE STUB  *******************/

void input_port(void *input) {
output_port(input);
}

void output_port(void *input) {
 process_output(0,input);
}




void dispatch(int port,int id_queue) {
 kmessage *msg = dequeue(id_queue);
  if(msg !=NULL)  {

    switch(port)

    {
			 case 0:
					 input_port(msg->value);
			 break;
   }
   }
}



int main (int argc,char *argv[])
{
   	if(argc >2)
    {
	    key_t key =   atoi(argv[1]);
	    int port=   atoi(argv[2]);

	     bootstrap(key,port);
         register_start(start);
         register_stop(stop);
         register_update(update);
         register_dispatch(dispatch);
	     ctx->start();

	while(1)
	{
        sleep(1000000);
	}
     }
}
