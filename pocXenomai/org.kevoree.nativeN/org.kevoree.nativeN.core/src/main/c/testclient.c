#include "events.h"





int main(void){

  Publisher publisher;
  publisher.port = 8084;
  strcpy (publisher.hostname, "127.0.0.1");


  Events      t;

      t.ev_type = EV_UPDATE;
  if(createPublisher (&publisher) != -1)
  {

      if(send (publisher.socket, &t, sizeof (Events),0)< 0)
      {

      }

  } else {
    fprintf(stderr,"Create Publisher");
  }
  close(publisher.socket);
}