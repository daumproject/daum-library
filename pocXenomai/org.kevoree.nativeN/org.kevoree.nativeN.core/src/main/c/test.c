
/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 03/10/12
 * Time: 11:47
 */

#include "events.h"


int main(void){

     EventBroker eventbroker;


  eventbroker.port = 8084;

  createEventBroker (&eventbroker);
}
