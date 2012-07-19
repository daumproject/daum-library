#include <jni.h>
#include <nativelib.h>
#include "fota.h"

 /**
  * Created by jed
  * User: jedartois@gmail.com
  * Date: 18/07/12
  * Time: 8:49
  */



void fota_event(int evt)
{
puts(evt);
}

JNIEXPORT void JNICALL Java_eu_powet_fota_Nativelib_close_1flash(JNIEnv * env, jobject obj)
{
   close_flash();
}


JNIEXPORT jint JNICALL Java_eu_powet_fota_Nativelib_write_1on_1the_1air_1program  (JNIEnv *env, jobject obj, jstring device, jint target, jstring path)
{

    // convert
   char *n_device = (*env)->GetStringUTFChars(env, device, 0);
   char *n_hex_path = (*env)->GetStringUTFChars(env, path, 0);
 // todo malloc in open_file
  unsigned char file_intel_hex_array[1200000];
  register_FlashEvent(fota_event);
  int taille =  open_file(n_hex_path,&file_intel_hex_array);

   return write_on_the_air_program(n_device,target,taille,&file_intel_hex_array[0]);
}



