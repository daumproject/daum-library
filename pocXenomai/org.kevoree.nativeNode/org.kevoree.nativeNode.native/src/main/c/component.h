#include <sys/ipc.h>
#include <sys/shm.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include "kqueue.h"


typedef struct _port {
    char name[32];
    int id;
}  Ports;

typedef struct _context
{
    int pid;
    int countPorts;
    Ports ports[100];
    void (*start) (void);
    void (*stop) (void);
    void (*update) (void);
    void (*trigger_port) (void);

} Context;


Context *ctx;

int  bootstrap(key_t key);
int start();
int stop();
int update();

int register_start( void* fn);
int register_stop( void* fn);
int register_update( void* fn);
int register_trigger_port( void* fn);

#define SIG_STOP 14
#define SIG_UPDATE 15
#define SIG_TRIGGER_PORT 18


struct sigaction sig_stop;
struct sigaction sig_update;
struct sigaction sig_port;

void sig_handler_stop(int sig)
{
   ctx->stop();

   exit(sig);
}

void sig_handler_update(int sig)
{
   ctx->update();
}

void sig_handler_port(int sig)
{
   ctx->trigger_port();
}


int init_sig()
{
       sig_stop.sa_handler = sig_handler_stop;
       sigemptyset(&sig_stop.sa_mask);
       sigaction(SIG_STOP, &sig_stop, NULL);

       sig_update.sa_handler = sig_handler_update;
       sigemptyset(&sig_update.sa_mask);
       sigaction(SIG_UPDATE, &sig_update, NULL);

      sig_port.sa_handler = sig_handler_port;
      sigemptyset(&sig_port.sa_mask);
      sigaction(SIG_TRIGGER_PORT, &sig_port, NULL);

}

int  bootstrap(key_t key){
        int shmid;
         void* ptr_mem_partagee;

        init_sig();
        /* create memory shared   */
         shmid= shmget(key,sizeof(Context), 0666 | IPC_CREAT );
          if(shmid < 0)
              {
                  perror("shmid");
                  return -1;
              }

              if ((ptr_mem_partagee = shmat(shmid, NULL, 0)) == (void*) -1)
              {
              	perror("shmat");
              	exit(1);
              }
        ctx = (Context*)ptr_mem_partagee;
        ctx->pid=getpid();
}
int register_start( void* fn){
	ctx->start=fn;
	return 0;
};


int register_stop( void* fn)
{
	ctx->stop=fn;
	return 0;
};


int register_update( void* fn)
{
	ctx->update=fn;
	return 0;
};


int register_trigger_port( void* fn)
{
	ctx->trigger_port=fn;
	return 0;
};
