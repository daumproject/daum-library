#include "../../../../../org.kevoree.nativeNode/org.kevoree.nativeNode.native/src/main/c/component.h"
#include <stdio.h>


int start()
{
	fprintf(stderr,"Component starting \n");

	while(1)
	{

         // do someting !
		sleep(1);
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

void output_port(char *input)
{
    process_output("output_port",input);
}


void input_port(char *input)
{
    output_port(input);
}


void dispatch()
{
   int i=0,j=0;
   if(ctx != NULL)
   {
       for(i=0;i<  ctx->inputs_count;i++)
       {
          for(j=0;j<getQnum(ctx->inputs[i].id);j++)
          {
             kmessage *msg = dequeue(ctx->inputs[i].id);
             if(msg !=NULL)
                     {
                        switch(i)
                        {
                        case 0:
                           input_port(msg->value);
                        break;
                        }
                     }
          }
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
         register_trigger_port(dispatch);
	    ctx->start();
     }
}
