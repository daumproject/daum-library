#include "../../../../../org.kevoree.nativeNode/org.kevoree.nativeNode.native/src/main/c/component.h"
#include <stdio.h>


int start()
{
	fprintf(stderr,"Component starting \n");



	while(1)
	{
        sleep(1000000);
	}
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

void output_port(void *input)
{
    process_output(0,input);
}


void input_port(void *input)
{
     fprintf(stderr,"IN %s \n",input);
}


void dispatch(int id_queue)
{
   int method=0;
   kmessage *msg = dequeue(id_queue);

  if(msg !=NULL)
  {
    switch(method)
    {
        case 0:
               input_port(msg->value);
        break;
    }
   }
}


int main (int argc,char *argv[])
{
   	if(argc >1)
    {
	    key_t key =   atoi(argv[1]);

	     bootstrap(key);
         register_start(start);
         register_stop(stop);
         register_update(update);
         register_dispatch(dispatch);
	     ctx->start();
     }
}
