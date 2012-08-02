#include <jni.h>
#include "NativeHandler.h"


#define START 0
#define STOP 1


typedef struct _context
{
 int state; 
  pid_t pid;
} Context;

static Context *ctx;


Context * getContext(int key)
{
    char *addr;
   // create memory shared
   shmid = shmget(key,sizeof(Context), 0666 | IPC_CREAT );
   if(shmid < 0)
   {
       perror("shmid");
       return NULL;
   }
   addr = shmat(shmid, 0, 0);
   if(addr < 0)
   {
      perror("shmat");
      return NULL;
   }
    // bind to memory shared
  return  (Context *) addr;
}



JNIEXPORT void JNICALL Java_Test_start  (JNIEnv *env, jobject obj)
{
	puts("Start");
	ctx = getContext(24011987);
	execlp("/home/jed/hello", "/home/jedhello", 24011987);

}


JNIEXPORT void JNICALL Java_NativeHandler_stop  (JNIEnv *, jobject)
{
	puts("stop");
	ctx = getContext(24011987);

}
