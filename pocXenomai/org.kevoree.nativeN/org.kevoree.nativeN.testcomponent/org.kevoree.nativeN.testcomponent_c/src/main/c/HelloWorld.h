#include "thirdparty/component.h" 
void output_port(void *input) {
 process_output(0,input);
}
void dispatch(int port,int id_queue)
{
    kmessage *msg = NULL;
    do
    {
          msg = dequeue(id_queue);
          if(msg !=NULL)
          {
             switch(port)
             {			 case 0:
					 input_port(msg->value);
			 break;
                     }
                     }

    } while(msg != NULL);
}int main (int argc,char *argv[])
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