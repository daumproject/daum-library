#include "HelloWorld.h"

void output_port(void *input);
void output_port2(void *input);

/* @Port(name = "input_port") */
void input_port(void *input) {
// USE INPUT
output_port(input);
}

/* @Port(name = "input_port2") */
void input_port2(void *input) {
// USE INPUT
output_port2(input);
}

/*@Start*/
int start()
{
	fprintf(stderr,"Component starting \n");


}

/*@Stop */
int stop()
{
    fprintf(stderr,"Component stoping \n");

}

/*@Update */
int update()
{
    fprintf(stderr,"Component updating \n");
 
}
