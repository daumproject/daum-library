#ifndef COMPONENT_H
#define COMPONENT_H
#include <sys/ipc.h>
#include <sys/shm.h>
#include <stdlib.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <pthread.h>
#include "kqueue.h"
#include "events.h"


typedef struct _port
{
  char name[32];
  int id;
} Ports;

typedef struct _context
{
  int pid;
  int pid_jvm;
  int inputs_count;
  Ports inputs[100];
  int outputs_count;
  Ports outputs[100];
  void (*start) (void);
  void (*stop) (void);
  void (*update) (void);
  void (*dispatch) (int id_port,int id_queue);
} Context;



int start ();
int stop ();
int update ();
int alive_component = 0;
int register_start (void *fn);
int register_stop (void *fn);
int register_update (void *fn);
int register_trigger_port (void *fn);

Context *ctx;
EventBroker component_event_broker;
//Publisher component_event_publisher;


void notify(Events ev)
{
    int i;
    switch(ev.ev_type){

        case EV_PORT_INPUT:
                 ctx->dispatch (ev.id_port,ctx->inputs[ev.id_port].id);
        break;

        case EV_PORT_OUTPUT:

        break;

        case EV_UPDATE:
              ctx->update ();
        break;

        case EV_STOP:

        for (i = 0; i < ctx->inputs_count; i++)
            {
              if (destroy_queue (ctx->inputs[i].id) != 0)
            {
              //error
            }
            }
          for (i = 0; i < ctx->outputs_count; i++)
            {
              if (destroy_queue (ctx->outputs[i].id) != 0)
            {
              //error
            }
            }

          ctx->stop ();
          close(component_event_broker.sckServer);
          exit (0);
        break;

    }
}


void *   t_broker (void *p)
{
  createEventBroker (&component_event_broker);
  pthread_exit (NULL);
}


int bootstrap (key_t key,int port_event_broker)
{
  int shmid;
  void *ptr_mem_partagee;
  pthread_t t_event_broker;

  /* create memory shared   */
  shmid = shmget (key, sizeof (Context), S_IRUSR | S_IWUSR);
  if (shmid < 0)
    {
      perror ("shmid");
      return -1;
    }

  if ((ptr_mem_partagee = shmat (shmid, NULL, 0)) == (void *) -1)
    {
      perror ("shmat");
      exit (1);
    }
    ctx = (Context *) ptr_mem_partagee;
    ctx->pid = getpid ();

    component_event_broker.port = port_event_broker;
    component_event_broker.dispatch = &notify;
  /*
    component_event_publisher.port = 8086;
    component_event_publisher.socket = -1;
    strcpy (component_event_publisher.hostname, "127.0.0.1");  */

  if (pthread_create (&t_event_broker, NULL, &t_broker, NULL) != 0)
    {
      return -1;
    }

}



void  process_output (int id_output, void *n_value)
{
	  kmessage kmsg;
	  kmsg.type = 1;
	  strcpy (kmsg.value, n_value);
	  enqueue (ctx->outputs[id_output].id, kmsg);
     /*
	  Events      ev;
      ev.ev_type = EV_PORT_OUTPUT;
      ev.id_queue =   ctx->outputs[id_output].id;
      send_event(component_event_publisher,ev);   */
}

int
register_start (void *fn)
{
  ctx->start = fn;
  return 0;
};


int
register_stop (void *fn)
{
  ctx->stop = fn;
  return 0;
};


int
register_update (void *fn)
{
  ctx->update = fn;
  return 0;
};


int
register_dispatch (void *fn)
{
  ctx->dispatch = fn;
  return 0;
};

#endif
