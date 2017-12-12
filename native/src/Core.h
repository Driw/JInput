
#ifndef _KEYBOARD_HOOK_H_
#define _KEYBOARD_HOOK_H_

#include <jni.h>

#ifdef __cplusplus
extern "C"
{
#endif

/*
 * Class:     org_diverproject_jni_input_KeyboardHook
 * Method:    register
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_diverproject_jni_input_KeyboardPicker_register(JNIEnv *, jobject);

/*
 * Class:     org_diverproject_jni_input_KeyboardHook
 * Method:    unregister
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_diverproject_jni_input_KeyboardPicker_unregister(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif

#endif