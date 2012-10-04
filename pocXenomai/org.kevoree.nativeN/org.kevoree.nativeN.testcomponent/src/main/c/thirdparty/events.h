/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 03/10/12
 * Time: 11:47
 */
 #ifndef EVENTS_H
 #define EVENTS_H

#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>
#include <stdio.h>
#include <errno.h>
#include <netdb.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>


#define FILE_ATTENTE 50

 typedef enum
   {
 	EV_STOP,
     EV_UPDATE,
 	EV_PORT_INPUT,
   } Type;

 typedef struct Events
 {
     Type ev_type;
     int id_port;
 } Events;

/**
* Permet de creer un Socket.
* @param port Port à utiliser pour le socket
* @param type TCP ou UDP
* @return -1 erreur la socket si succes
*/
int createSocket(unsigned short *port, int type) {

  int    la_socket, autorisation , ok;

  struct sockaddr_in adresse;
  socklen_t          lgr;

  la_socket=socket(PF_INET, type, 0);
  if (la_socket==-1) { perror("socket"); return -1; }

   /* Pour pouvoir relancer immediatement un serveur TCP  */
   /* ou lancer, sur une meme machine,                    */
   /*    plusieurs recepteurs UDP sur                     */
   /*    le meme port de diffusion (broadcast, multicast) */
  autorisation=1;
  ok = setsockopt(la_socket, SOL_SOCKET, SO_REUSEADDR,&autorisation, sizeof(int));
  if (ok==-1) { perror("setsockopt"); return -1; }

  lgr = sizeof(struct sockaddr_in);
  memset(&adresse, 0, lgr) ;
  adresse.sin_family      = AF_INET ;
  adresse.sin_port        = htons(*port);
  adresse.sin_addr.s_addr = htonl(INADDR_ANY);

  ok = bind (la_socket, (struct  sockaddr*)&adresse, lgr);
  if (ok==-1) { perror("bind"); return -1; }

  ok = getsockname (la_socket, (struct sockaddr*)&adresse, &lgr);
  if (ok==-1) { perror("getsockname"); return -1; }

  *port = ntohs (adresse.sin_port);
  return (la_socket) ;
}


typedef struct _EventBroker {
	unsigned short  port; /* Port */
	int sckServer; /* descripteur socket  Serveur*/
	int sckClient; /* Descripteur du socket du client */
	void (*dispatch)(Events ev);
	struct sockaddr_in client;
} EventBroker;


typedef struct _Publisher {
	int socket; /* point rdv */
	unsigned short 	     port; /* Port */
	struct hostent*      hp ;
	struct sockaddr_in   adr ;
	char hostname[512];
  	socklen_t            lgradr ;
} Publisher;


int createEventBroker(EventBroker *eventbroker)
{
   //   pid_t  pid;
      struct sockaddr_in adr ;
      socklen_t          lgradr ;
       Events ev;
      eventbroker->sckServer = createSocket(&(eventbroker->port), SOCK_STREAM);
      if(eventbroker->sckServer == -1){ return -1; }

     if (listen(eventbroker->sckServer,FILE_ATTENTE)==-1)
     {
     		perror("listen");
      		return -1;
     }
       while (1)
       {

	   //fprintf(stderr,"EventBroker is listenning... %d\n",eventbroker->sckServer);

     lgradr=sizeof(struct sockaddr_in);
     eventbroker->sckClient = accept(eventbroker->sckServer, (struct sockaddr*)&adr, &lgradr);
     if (eventbroker->sckClient==-1)
      	 	    {
        		                    if (errno!=EINTR) perror("accept");
        		                    continue;
        	  	  }


                                    if (eventbroker->sckClient!=-1)
                                 	 {
                                            if(read(eventbroker->sckClient,&ev,sizeof(Events)) < 0)
                                            {

                                            }else
                                            {
                                               eventbroker->dispatch(ev);

                                            }
                                    	 } else {
                                    	      fprintf(stderr,"ERROR CLIENT %d",  	eventbroker->sckClient );
                                    	 }

                                    	 close(eventbroker->sckClient);
      



      }


}



/**
* Crée un client
* @param encours Structure client contenant les sockets, adresses, qui sera initialisée
* @return 0 aucun problème, != 0 : erreur
*/
int createPublisher(Publisher *publisher)
{
	int ok;
 	((publisher)->socket) = socket(AF_INET, SOCK_STREAM, 0);
   	if(((publisher)->socket) == -1){ 	  return -1; }

   	publisher->hp = gethostbyname (publisher->hostname);
  	if ((publisher)->hp == NULL) { perror("gethostbyname");  	  return -1; }

  	(publisher)->lgradr = sizeof(struct sockaddr_in);
  	memset(&(publisher)->adr, 0, (publisher)->lgradr);
  	(publisher)->adr.sin_family = AF_INET;
 	(publisher)->adr.sin_port = htons ((publisher)->port);

  	(publisher)->adr.sin_addr = *((struct in_addr *) (((publisher)->hp) -> h_addr_list[0]));

 	 ok = connect((publisher)->socket, (struct sockaddr*)&(publisher)->adr, (publisher)->lgradr);
 	 if (ok == -1) {
 	 perror("connect");

 	  return -1;

 	  }
return 0;
}



/**
* Ferme le socket passé en paramètre
* @param sck Le socket
*/
void fermerSocket(int sck)
{
	int rt;
	rt = close(sck);
	if (rt==-1) {    perror("close");      }
}


/**
* Connexion TCP/IP d'un client à un serveur
* @param adrServeur Adresse du serveur
* @param port Port à utiliser (voir constantes)
* @return -1 en cas d'erreur, 0 sinon
*/
int connexionServeur(char *adrServeur,int port)
{
  struct hostent*    hp ;
  struct sockaddr_in adr ;
  socklen_t lgradr ;
  int sock=-1, ok=-1;
  /***************************************/
  /* Preparation de l'adresse du serveur */
  /***************************************/
  hp = gethostbyname (adrServeur);

  if (hp == NULL) {
	perror("gethostbyname");
	return -1;
  }

  lgradr = sizeof(struct sockaddr_in);
  memset(&adr, 0, lgradr);
  adr.sin_family = AF_INET;
  adr.sin_port = htons (port);

  adr.sin_addr = *((struct in_addr *) (hp -> h_addr_list[0]));

   printf("On écoute à l'adresse %s avec le port %d\n",inet_ntoa( adr.sin_addr),port);

  /****************************************/
  /*        Demande  de connexion         */
  /****************************************/

  ok = connect(sock, (struct sockaddr*)&adr, lgradr);

  if (ok == -1) {
	perror("connect");
		return -1;
  }

  return 0;
}

/**
* Ecriture complète de donnée sur un socket
* @param fd Le descripteur de socket
* @param buf Les données à écrire
* @param count La taille des données à écrire
* @return Nombre réel d'octets envoyés
*/
ssize_t writeFully(int fd, const void *buf, size_t count)
{
	unsigned char* bufc = (unsigned char*)buf;

	ssize_t returnValue   = 0;
	size_t  nbByteToWrite = count;
	size_t  nbByteSent    = 0;

	while (nbByteToWrite != nbByteSent)
	{
		returnValue = write(fd, bufc + nbByteSent, nbByteToWrite - nbByteSent);
		if (returnValue <= 0)
		{
			return returnValue;
		}
		nbByteSent += returnValue;
	}

	return nbByteSent;
}

/**
* Lecture complète de donnée sur un socket
* @param fd Le descripteur de socket
* @param buf Le tampon de réception
* @param count La taille des données à lire
* @return Nombre réel d'octets lus
*/
ssize_t readFully(int fd, const void *buf, size_t count)
{
	unsigned char* bufc = (unsigned char*)buf;

	ssize_t returnValue   = 0;
	size_t  nbByteToRead  = count;
	size_t  nbByteRead    = 0;

	while (nbByteToRead != nbByteRead)
	{
		returnValue = read(fd, bufc + nbByteRead, nbByteToRead - nbByteRead);
		if (returnValue <= 0)
		{
			return returnValue;
		}
		nbByteRead += returnValue;
	}

	return nbByteRead;
}



int send_event(Publisher publisher,Events ev)
{

    if(publisher.socket == -1)
    {
      if(createPublisher (&publisher) == -1)
       {
         publisher.socket = -1;
         return -1;
        }
    }

    if (publisher.socket != -1)
    {
     if(send (publisher.socket, &ev, sizeof (Events),0)< 0)
         {
                         	fprintf(stderr,"send_event");
                         	return -1;
           }
    }
    return 0;
}

 #endif