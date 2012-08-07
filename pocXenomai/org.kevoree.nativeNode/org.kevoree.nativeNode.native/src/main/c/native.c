#include <jni.h>
#include <nativelib.h>
#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include "component.h"


void *ptr_mem_partagee;
int shmid;


JavaVM *g_vm;
jobject g_obj;
jmethodID g_mid;

Publisher     native_event_publisher;
EventBroker   native_event_broker;


void   process (char *port, void *msg)
{
  JNIEnv *g_env;
  int getEnvStat = (*g_vm)->GetEnv (g_vm, (void **) &g_env, JNI_VERSION_1_6);

  if (getEnvStat == JNI_EDETACHED)
    {
      if ((*g_vm)->AttachCurrentThread (g_vm, (void **) &g_env, NULL) != 0)
	{
	  printf ("Failed to attach\n");
	}
    }
  else if (getEnvStat == JNI_OK)
    {
      //
    }
  else if (getEnvStat == JNI_EVERSION)
    {
      printf ("GetEnv: version not supported\n");
    }

  (*g_env)->CallVoidMethod (g_env, g_obj, g_mid,
			    (*g_env)->NewStringUTF (g_env, msg));
  if ((*g_env)->ExceptionCheck (g_env))
    {
      (*g_env)->ExceptionDescribe (g_env);
    }
  (*g_vm)->DetachCurrentThread (g_vm);
}


JNIEXPORT jint JNICALL
JNI_OnLoad (JavaVM * jvm, void *reserved)
{
  g_vm = jvm;
  return JNI_VERSION_1_6;
}


JNIEXPORT jboolean JNICALL
Java_org_kevoree_nativeNode_NativeJNI_register (JNIEnv * env, jobject obj)
{

  // convert local to global reference
  g_obj = (*env)->NewGlobalRef (env, obj);
  // save refs for callback
  jclass g_clazz = (*env)->GetObjectClass (env, g_obj);

  if (g_clazz == NULL)
    {
      printf ("Failed to find class \n");
    }

  g_mid =
    (*env)->GetMethodID (env, g_clazz, "dispatchEvent",
			 "(Ljava/lang/String;)V");
  if (g_mid == NULL)
    {
      printf ("Unable to get method ref");

    }
  return (jboolean) 0;
}



void native_notify(Events ev)
{
    int j;
    kmessage *msg ;

    switch(ev.ev_type)
    {
        case EV_PORT_OUTPUT:

         for (j = 0; j < getQnum (ev.id_queue); j++)
		{
		         msg = dequeue (ev.id_queue);
             	 if (msg != NULL)
                 {
             		    process (ctx->outputs[ev.id_queue].name, msg->value);
             	  }
		}
		break;

    }
}

void *   t_native_broker (void *p)
{
  createEventBroker (&native_event_broker);
  pthread_exit (NULL);
}


JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeJNI_init (JNIEnv * env, jobject obj, jint key)
{

   native_event_broker.port = 8086;
   native_event_broker.dispatch = &native_notify;

   native_event_publisher.port = 8085;
   native_event_publisher.socket = -1;
   strcpy (native_event_publisher.hostname, "127.0.0.1");

  pthread_t t_event_broker;

  if (pthread_create (&t_event_broker, NULL, &t_native_broker, NULL) != 0)
    {
      return -1;
    }

  // create memory shared
  shmid = shmget (key, sizeof (Context), IPC_CREAT | S_IRUSR | S_IWUSR);
  if (shmid < 0)
    {
      perror ("shmid");
      return -1;
    }

  if ((ptr_mem_partagee = shmat (shmid, NULL, 0)) == (void *) -1)
    {
      perror ("shmat");
      return -1;
    }

  ctx = (Context *) ptr_mem_partagee;
  ctx->pid = -1;
  ctx->outputs_count = 0;
  ctx->inputs_count = 0;


  ctx->pid_jvm = getpid ();

  //    fprintf (stderr,"shared memory attached at address %p\n", ptr_mem_partagee);
  return shmid;
}

JNIEXPORT jint JNICALL
Java_org_kevoree_nativeNode_NativeJNI_start (JNIEnv * env, jobject obj,
					     jint key, jstring path_binary)
{

  const char *n_path_binary = (*env)->GetStringUTFChars (env, path_binary, 0);
  char cipckey[25];
  int pid;
  switch (pid = fork ())
    {
    case -1:
      return -1;
    case 0:
      sprintf (cipckey, "%d", key);
      if (execl (n_path_binary, n_path_binary, cipckey, NULL) != 0)
	{
	  perror ("execlp");
	  return -1;
	}
      break;

    }
  return shmid;
}



JNIEXPORT jint JNICALL
Java_org_kevoree_nativeNode_NativeJNI_stop (JNIEnv * env, jobject obj,
					    jint key)
{

  ctx = (Context *) ptr_mem_partagee;

  Events      ev;
  ev.ev_type = EV_STOP;

   send_event(native_event_publisher,ev);


  shmdt (ptr_mem_partagee);	/* detach segment */
  /* Deallocate the shared memory segment.  */
  shmctl (shmid, IPC_RMID, 0);
  return 0;
}


JNIEXPORT jint JNICALL
Java_org_kevoree_nativeNode_NativeJNI_update (JNIEnv * env, jobject obj,
					      jint key)
{
  ctx = (Context *) ptr_mem_partagee;

  Events      ev;
  ev.ev_type = EV_UPDATE;

    send_event(native_event_publisher,ev);
}


JNIEXPORT jint JNICALL
Java_org_kevoree_nativeNode_NativeJNI_create_1input (JNIEnv * env,  						     jobject obj, jint key,
						     jstring queue)
{
  const char *n_port_name = (*env)->GetStringUTFChars (env, queue, 0);
  int id_queue = init_queue ();
  ctx = (Context *) ptr_mem_partagee;
  strcpy (ctx->inputs[ctx->inputs_count].name, n_port_name);
  ctx->inputs[ctx->inputs_count].id = id_queue;
  ctx->inputs_count = ctx->inputs_count + 1;
  return id_queue;
}

JNIEXPORT jint JNICALL
Java_org_kevoree_nativeNode_NativeJNI_create_1output (JNIEnv * env,
						      jobject obj, jint key,
						      jstring queue)
{
  const char *n_port_name = (*env)->GetStringUTFChars (env, queue, 0);
  int id_queue = init_queue ();
  ctx = (Context *) ptr_mem_partagee;
  strcpy (ctx->outputs[ctx->outputs_count].name, n_port_name);
  ctx->outputs[ctx->outputs_count].id = id_queue;
  ctx->outputs_count = ctx->outputs_count + 1;
  return id_queue;
}

JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeJNI_enqueue (JNIEnv * env, jobject obj,  jint key, jint id_queue,    jstring msg)
{
  const char *n_value = (*env)->GetStringUTFChars (env, msg, 0);
  kmessage kmsg;
  kmsg.type = 1;
  strcpy (kmsg.value, n_value);
  ctx = (Context *) ptr_mem_partagee;
  enqueue (id_queue, kmsg);

  Events      ev;
  ev.ev_type = EV_PORT_INPUT;
  ev.id_queue =   id_queue;

    send_event(native_event_publisher,ev);
  return 0;

}
