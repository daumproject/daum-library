 #ifndef EVENTS_H_UDP
 #define EVENTS_H_UDP

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdio.h>
#include <strings.h>



#include "events_common.h"

 int createEventBroker_udp(EventBroker *eventbroker)
 {
       int sockfd;
       struct sockaddr_in servaddr,cliaddr;
       socklen_t len;


       sockfd=socket(AF_INET,SOCK_DGRAM,0);
       if(sockfd < 0){
           return -1;
       }
       bzero(&servaddr,sizeof(servaddr));
       servaddr.sin_family = AF_INET;
       servaddr.sin_addr.s_addr=htonl(INADDR_ANY);
       servaddr.sin_port=htons(eventbroker->port);
       bind(sockfd,(struct sockaddr *)&servaddr,sizeof(servaddr));
       Events ev;
       for (;;)
       {
          len = sizeof(cliaddr);
          recvfrom(sockfd,&ev,sizeof(Events),0,(struct sockaddr *)&cliaddr,&len);
          eventbroker->dispatch(ev);
          usleep(1000);
       }
       return 0;
   }

   int send_event_udp(Publisher publisher,Events ev)
   {

      int sockfd;
      struct sockaddr_in servaddr,cliaddr;

      sockfd=socket(AF_INET,SOCK_DGRAM,0);

      bzero(&servaddr,sizeof(servaddr));
      servaddr.sin_family = AF_INET;
      servaddr.sin_addr.s_addr=inet_addr(publisher.hostname);
      servaddr.sin_port=htons(publisher.port);
      sendto(sockfd,&ev,sizeof (Events),0,  (struct sockaddr *)&servaddr,sizeof(servaddr));
      close(sockfd);

      return 0;

   }
   
   #endif