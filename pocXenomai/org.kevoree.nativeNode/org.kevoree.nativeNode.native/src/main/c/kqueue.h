#include<string.h>
#include<time.h>
#include<sys/ipc.h>
#include<sys/msg.h>
#include<sys/wait.h>
#include <stdio.h>
#include<sys/errno.h>


extern int errno;       // error NO.
#define MSGPERM 0666    // msg queue permission

typedef struct _kmsg
{
         long    type;
         char value[512];
 } kmessage;

 int init_queue();

int init_queue()
{
    fprintf(stderr,"Queue creating \n");
    // create a message queue. If here you get a invalid msgid and use it in msgsnd() or msgrcg(), an Invalid Argument error will be returned.
    int msgqid = msgget(IPC_PRIVATE, MSGPERM|IPC_CREAT|IPC_EXCL);
     if (msgqid < 0)
     {
       perror(strerror(errno));
       printf("failed to create message queue with msgqid = %d\n", msgqid);
       return 1;
     }
     printf("message queue %d created\n",msgqid);
  return msgqid;
}

int destroy_queue(int msgqid){
    int rc;
  rc=msgctl(msgqid,IPC_RMID,NULL);
  if (rc < 0) {
    perror( strerror(errno) );
    printf("msgctl (return queue) failed, rc=%d\n", rc);
    return 1;
  }
  printf("message queue %d is gone\n",msgqid);


}

int enqueue(int msgqid,kmessage msg)
{
    int rc;
    rc = msgsnd(msgqid, &msg, sizeof(msg.value), 0); // the last param can be: 0, IPC_NOWAIT, MSG_NOERROR, or IPC_NOWAIT|MSG_NOERROR.
    if (rc < 0) {
      perror( "enqueue" );
      printf("msgsnd failed, rc = %d\n", rc);
      return -1;
    }
    return 0;
}

kmessage* dequeue(int msgqid)
{
   kmessage *msg = (kmessage*)malloc(sizeof(kmessage));

  int rc = msgrcv(msgqid, msg, sizeof(msg->value), 0,0);
    if (rc < 0) {
      perror( strerror(errno) );
      printf("msgrcv failed, rc=%d\n", rc);
      free(msg);
      return NULL;
    }
    return msg;
}
