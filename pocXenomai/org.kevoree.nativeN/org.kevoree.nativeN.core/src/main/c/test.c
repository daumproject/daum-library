#include "events.h"





int main(void){

     EventBroker eventbroker;


  eventbroker.port = 8084;

  createEventBroker (&eventbroker);
}
