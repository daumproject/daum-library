#include "/home/jed/pocXenomai/org.kevoree.nativeNode/org.kevoree.nativeNode.native/src/main/c/component.h"
#include <stdio.h>


int start()
{
	fprintf(stderr,"Component starting \n");

	while(1)
	{
		fprintf(stderr,"ALIVE \n");
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



void input_port()
{



}

void trig_port()
{
int i=0;

   if(ctx == NULL)
   {
        fprintf(stderr,"ctx is null \n");
   }else
    {
         fprintf(stderr,"port trigger %d \n",ctx->countPorts);
       for(i=0;i<  ctx->countPorts;i++)
       {
         kmessage *msg = dequeue(ctx->ports[i].id);
         if(msg !=NULL)
         {
            fprintf(stderr,"MSG !!");
         } else
         {
                    fprintf(stderr,"NULL !!");
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

         register_trigger_port(trig_port);


	    ctx->start();
     }
}
