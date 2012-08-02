#include <jni.h>
#include <nativelib.h>
#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <pthread.h>
#include <unistd.h>
#include "component.h"



static void* ptr_mem_partagee;

JavaVM * g_vm;
jobject g_obj;
jmethodID g_mid;


void process(char *port,void *msg)
{
    JNIEnv * g_env;
    int getEnvStat = (*g_vm)->GetEnv(g_vm,(void **)&g_env, JNI_VERSION_1_6);

    if (getEnvStat == JNI_EDETACHED)
    {
        if ((*g_vm)->AttachCurrentThread(g_vm,(void **) &g_env, NULL) != 0)
        {
                   printf("Failed to attach\n");
        }
    } else if (getEnvStat == JNI_OK)
    {
        //
    } else if (getEnvStat == JNI_EVERSION)
    {
        printf("GetEnv: version not supported\n");
    }

    (*g_env)->CallVoidMethod(g_env,g_obj, g_mid, msg);

    if ((*g_env)->ExceptionCheck(g_env)) {
        (*g_env)->ExceptionDescribe(g_env);
    }
    (*g_vm)->DetachCurrentThread(g_vm);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
    // register events_fota
    g_vm = jvm;
    return JNI_VERSION_1_6;
}


JNIEXPORT jboolean JNICALL Java_org_kevoree_nativeNode_NativeHandler_register  (JNIEnv *env, jobject obj){

                   // convert local to global reference
                g_obj = (*env)->NewGlobalRef(env,obj);
                // save refs for callback
                jclass g_clazz = (*env)->GetObjectClass(env,g_obj);

                if (g_clazz == NULL)
                {
                    printf( "Failed to find class \n");
                }

                g_mid = (*env)->GetMethodID(env,g_clazz, "dispatchEvent", "(I)V");
                if (g_mid == NULL)
                 {
                  printf( "Unable to get method ref");

                 }
                 return (jboolean)0;
  }

JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeHandler_start    (JNIEnv *env, jobject obj, jint key,jstring path_binary)
{

  const char *n_path_binary = (*env)->GetStringUTFChars(env, path_binary, 0);
               char cipckey[25];
      int pid;
     // create memory shared
     int shmid = shmget(key,sizeof(Context), 0666 | IPC_CREAT );
     if(shmid < 0)
     {
         perror("shmid");
         return -1;
     }

     if ((ptr_mem_partagee = shmat(shmid, NULL, 0)) == (void*) -1)	//	J'attache le segment de mémoire partagée identifié par mem_ID au segment de données du processus A dans une zone libre déterminée par le Système d'exploitation
     {
     	perror("shmat");											//	et je m'assure que le segment de mémoire a été correctement attaché à mon processus
     	exit(1);
     }

        // bind to memory shared
      ctx =   (Context *) ptr_mem_partagee;
      ctx->pid = -1;
      ctx->countPorts = 0;

    switch(pid = fork())
    {
    case -1:

      return -1;
    case 0:
            //fprintf(stderr, "shmid %d key %d\n",shmid,key);

             sprintf(cipckey,"%d",key);
             if(execl(n_path_binary,n_path_binary,cipckey,NULL) != 0)
             {
                     perror("execlp");
                     return -1;
             }
      break;

  }

        return shmid;

}



JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeHandler_stop  (JNIEnv *env, jobject obj, jint key)
{
          init_sig();
           void* ptr_mem_partagee;
           int shmid;
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

             if(ctx->pid != 0 && ctx->pid !=-1)
               {
                       kill(ctx->pid, SIG_STOP);
                 }

     	  // destroy shmget
    return 0;
 }


JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeHandler_update   (JNIEnv *env, jobject obj, jint key)
{
                void* ptr_mem_partagee;
                int shmid;
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
                init_sig();
                if(ctx->pid != 0 && ctx->pid !=-1)
                {
                      kill(ctx->pid, SIG_UPDATE);
                }
  }


JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeHandler_create_1queue (JNIEnv *env, jobject obj,jint key,jstring queue)
{
    int id =init_queue();

      void* ptr_mem_partagee;
                    int shmid;
                    /* create memory shared   */
                     shmid= shmget(key,sizeof(Context), 0666| IPC_CREAT );
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
    const char *n_port_name = (*env)->GetStringUTFChars(env, queue, 0);
    strcpy(ctx->ports[ctx->countPorts].name,n_port_name);
    ctx->ports[ctx->countPorts].id = id;
    ctx->countPorts =   1;
 return id;
}

JNIEXPORT jint JNICALL Java_org_kevoree_nativeNode_NativeHandler_enqueue (JNIEnv *env, jobject obj, jint key, jint id, jstring msg)
{

     const char *n_value = (*env)->GetStringUTFChars(env, msg, 0);
     kmessage kmsg;
     kmsg.type = 1;
     sprintf(kmsg.value,"%s",n_value);
     enqueue(id,kmsg);


       void* ptr_mem_partagee;
                int shmid;
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
     init_sig();
         fprintf(stderr,"count port %d \n",    ctx->countPorts);
     if(ctx->pid != 0 && ctx->pid !=-1)
     {
        kill(ctx->pid, SIG_TRIGGER_PORT);
     }

}
