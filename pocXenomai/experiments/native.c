#include <jni.h>
#include "Test.h"
#include "testjni.h"


JNIEXPORT void JNICALL Java_Test_start
  (JNIEnv *env, jobject obj){

start();

}
